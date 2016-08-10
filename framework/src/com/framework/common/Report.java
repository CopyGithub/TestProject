package com.framework.common;

import java.util.ArrayList;
import java.util.Random;

public class Report {
    private static final String RESULT = "Test results for InstrumentationTestRunner=";
    private static final String CRASH = "shortMsg=Process crashed";

    public int reportId = Math.abs(new Random().nextInt());// 报告ID,随机生成
    public int taskId;// 任务ID,用于唯一标识任务
    public String taskName;// 任务名
    public String[] caseId;// Case对应的手工用例编号
    public String className;// 执行Case类的完整名
    public String testContent;// 执行的手工用例说明
    public Status status = Status.NONE;// 执行的Case结果
    public String time;// 执行Case的耗时
    public String content = "";// 执行Case后Junit返回的结果

    public enum Status {
        NONE, CRASH, OK, FAILURES, ERROR, NOT_FOUND_CLASS, NOT_DATA
    }

    public Status analysis(ArrayList<String> content) {
        if (content == null) {
            return Status.NONE;
        }
        for (String string : content) {
            if (string.contains(CRASH)) {
                return Status.CRASH;
            }
            if (string.contains(RESULT)) {
                String result = string.replace(RESULT, "").trim();
                if (result.contains(".E")) {
                    return Status.ERROR;
                } else if (result.contains(".F")) {
                    return Status.FAILURES;
                } else {
                    return Status.OK;
                }
            }
        }
        return Status.NONE;
    }

    /**
     * 将一个{@link ArrayList}格式化为一个{@link String},并保留换行
     * 
     * @param reportContent
     *            需要格式化的{@link ArrayList}
     * @return
     */
    public void formatReportContent(ArrayList<String> reportContent) {
        for (String string : reportContent) {
            if (string == null || string.length() == 0) {
                continue;
            }
            char[] chars = new char[1];
            string.getChars(0, 1, chars, 0);
            if (chars[0] == 9) {
                string = "    " + string;
            }
            content += string + "\r\n";
        }
    }

    /**
     * 恢复报告值到默认状态
     */
    public void restoreDefault() {
        status = Status.NONE;
        content = "";
    }
}
