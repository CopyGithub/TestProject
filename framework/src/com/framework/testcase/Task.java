package com.framework.testcase;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.framework.common.ApkInfo;
import com.framework.common.ExcelUtil;
import com.framework.common.Report;
import com.framework.common.Res;
import com.framework.common.Utils;
import com.framework.common.Report.Status;

class Task {
    private Utils utils = new Utils();
    private ExcelUtil excelUtil = new ExcelUtil();
    private ArrayList<ApkInfo> apkInfos = new ArrayList<ApkInfo>();

    class TaskInfo {
        public int taskId = Math.abs(new Random().nextInt());// 随机生成,用户唯一标识任务
        public String taskName;// 任务名,通常由targetApk+updateApk+type组成
        public String targetApk;// 任务目标apk名,升级测试表示老apk
        public String[] targetApkPlus;// 任务目标附加安装的apk列表
        public String updateApk;// 升级测试目标apk, 表示新apk
        public String caseApk;// 执行的Case apk
        public String[] testClasses;// 指定执行的测试任务类, 这里是简写的测试类名
        public String excuteType; // 执行测试的类型,如:update,normal
    }

    private ArrayList<TaskInfo> readTasksFromExcel(String filePath) {
        ArrayList<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
        ArrayList<HSSFRow> rows = excelUtil.readExcelHssfRows(filePath);
        if (rows.size() == 0) {
            return taskInfos;
        }
        rows.remove(0);
        for (HSSFRow row : rows) {
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.targetApk = excelUtil.getCellStringValue(row, 0);
            String string = excelUtil.getCellStringValue(row, 1);
            taskInfo.targetApkPlus = string == null ? null : string.split(",");
            taskInfo.updateApk = excelUtil.getCellStringValue(row, 2);
            taskInfo.caseApk = excelUtil.getCellStringValue(row, 3);
            string = excelUtil.getCellStringValue(row, 4);
            taskInfo.testClasses = string == null ? null : string.split(",");
            taskInfo.excuteType = excelUtil.getCellStringValue(row, 5);
            taskInfo.taskName = String.format("%s%s+%s+%s", taskInfo.targetApk,
                    taskInfo.updateApk == null ? "" : "+" + taskInfo.updateApk, taskInfo.caseApk,
                    taskInfo.excuteType);
            if (verifyTaskValidity(taskInfo)) {
                taskInfos.add(taskInfo);
            } else {
                continue;
            }
        }
        return taskInfos;
    }

    private boolean verifyTaskValidity(TaskInfo taskInfo) {
        if (taskInfo.excuteType == null || taskInfo.targetApk == null || taskInfo.caseApk == null) {
            return false;
        }
        if (taskInfo.excuteType.equals(Res.UPDATE) && taskInfo.updateApk == null) {
            return false;
        }
        return true;
    }

    void executeExcel(String filePath, String devices) throws Exception {
        ArrayList<TaskInfo> taskInfos = readTasksFromExcel(filePath);
        String[] adbs = utils.getAdbs(devices);
        for (TaskInfo taskInfo : taskInfos) {
            System.out.println(taskInfo.taskName);
            ArrayList<Report> reports = new ArrayList<Report>();
            ApkInfo targetApk = getApkInfo(Res.DIR_TARGET_APK + taskInfo.targetApk);
            ApkInfo updateApk = getApkInfo(Res.DIR_TARGET_APK + taskInfo.updateApk);
            ApkInfo caseApk = getApkInfo(Res.DIR_CASE_APK + taskInfo.caseApk);
            // 任务目标附加安装的apk列表处理
            ArrayList<ApkInfo> targetApkPlus = new ArrayList<ApkInfo>();
            if (taskInfo.targetApkPlus != null) {
                for (int i = 0; i < taskInfo.targetApkPlus.length; i++) {
                    ApkInfo apkplus = getApkInfo(Res.DIR_OTHER_APK + taskInfo.targetApkPlus[i]);
                    targetApkPlus.add(apkplus);
                }
            }
            caseApk.classes.clear();
            caseApk.parseApkAdvancedInfo(taskInfo.testClasses, taskInfo.excuteType);
            TestCaseSet testCaseSet = new TestCaseSet(targetApk, updateApk, caseApk, targetApkPlus);
            reports = testCaseSet.execute(adbs, taskInfo);
            recordResultToExcel(taskInfo, reports, targetApk.versionCode);
        }
        summarizedExcel();
    }

    /**
     * 记录单条任务执行结果到Excel中
     * 
     * @param taskInfo
     *            单条任务的信息
     * @param reports
     *            执行结果报告
     */
    private void recordResultToExcel(TaskInfo taskInfo, ArrayList<Report> reports, int versionCode) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFCellStyle cellStyle = initWorkbook(workbook);
        HSSFSheet sheet = workbook.getSheetAt(0);
        for (int rowIndex = 0; rowIndex < reports.size() + 1; rowIndex++) {
            Report report = rowIndex == 0 ? null : reports.get(rowIndex - 1);
            HSSFRow row = sheet.createRow(rowIndex);
            HSSFCell cell = null;
            for (int i = 0; i < 8; i++) {
                cell = row.createCell(i);
                if (rowIndex == 0) {
                    cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                } else {
                    cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                }
                cell.setCellStyle(cellStyle);
                switch (i) {
                case 0:
                    cell.setCellValue(rowIndex == 0 ? "Report ID" : String.valueOf(report.reportId));
                    break;
                case 1:
                    cell.setCellValue(rowIndex == 0 ? "Case ID" : Arrays.toString(report.caseId));
                    break;
                case 2:
                    cell.setCellValue(rowIndex == 0 ? "期望结果" : report.testContent);
                    break;
                case 3:
                    cell.setCellValue(rowIndex == 0 ? "执行结果" : report.status.toString());
                    break;
                case 4:
                    cell.setCellValue(rowIndex == 0 ? "执行耗时" : report.time);
                    break;
                case 5:
                    cell.setCellValue(rowIndex == 0 ? "执行的类" : report.className);
                    break;
                case 6:
                    cell.setCellValue(rowIndex == 0 ? "执行详情" : report.content);
                    break;
                case 7:
                    if (rowIndex == 0) {
                        cell.setCellValue("截图");
                    } else if (utils.needScreenShot(report.status.toString())) {
                        try {
                            String filename = utils.getScreenShotDir(versionCode, report.className);
                            insertPictureToExcel(workbook, rowIndex, sheet, filename, null);
                        } catch (IOException e) {
                            // TODO: handle exception
                            cell.setCellValue("截图失败");
                        }
                    }
                    break;
                default:
                    break;
                }
            }
        }
        try {
            new File(Res.DIR_RESULT).mkdir();
            workbook.write(new FileOutputStream(Res.DIR_RESULT + taskInfo.taskName + ".xls"));
            workbook.close();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * 汇总excel的所有错误
     */
    private void summarizedExcel() {
        int totalOkNum = 0;
        int totalFailureNum = 0;
        int totalSummaryNum = 0;
        File[] files = new File(Res.DIR_RESULT).listFiles();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFCellStyle cellStyleReport = initWorkbook(workbook);
        HSSFSheet sheet = workbook.getSheetAt(0);
        HSSFCellStyle cellStyleTitle = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cellStyleTitle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyleTitle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        cellStyleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyleTitle.setFont(font);
        int rowIndex = 0;
        for (int i = 0; i < files.length; i++) {
            ArrayList<ArrayList<HSSFRow>> rowLists = sortResult(files[i]);
            // 写入表名
            HSSFRow rowTitle = sheet.createRow(rowIndex++);
            rowTitle.setRowStyle(cellStyleTitle);
            HSSFCell cell = rowTitle.createCell(0);
            cell.setCellStyle(cellStyleTitle);
            int okNum = rowLists.get(2).size();
            int failureNum = rowLists.get(1).size() + rowLists.get(3).size()
                    + rowLists.get(4).size();
            int summaryNum = 0;
            for (int j = 0; j < rowLists.size(); j++) {
                summaryNum += rowLists.get(j).size();
            }
            String summary = String.format("%s, 成功: %d, 失败: %d, 其它: %d, 成功率: %2.2f%%",
                    files[i].getName(), okNum, failureNum, summaryNum - okNum - failureNum, okNum
                            * 1f / summaryNum * 100);
            cell.setCellValue(summary);
            // 读取表中非OK项，并写入
            ArrayList<HSSFRow> rows = excelUtil.readExcelHssfRows(files[i].getAbsolutePath());
            try {
                InputStream inp = new FileInputStream(files[i].getAbsolutePath());
                HSSFWorkbook workbookForPicture = (HSSFWorkbook) WorkbookFactory.create(inp);
                boolean flag = false;
                for (HSSFRow row : rows) {
                    if (!Status.OK.toString().equals(excelUtil.getCellStringValue(row, 3))) {
                        rowTitle = sheet.createRow(rowIndex++);
                        for (int j = 0; j < row.getLastCellNum(); j++) {
                            if (j == 7 && excelUtil.getCellStringValue(row, 7) == null) {
                                flag = true;
                                continue;
                            }
                            cell = rowTitle.createCell(j);
                            cell.setCellStyle(cellStyleReport);
                            cell.setCellValue(excelUtil.getCellStringValue(row, j));
                        }
                        if (flag
                                && utils.needScreenShot(excelUtil.getCellStringValue(row, 3)
                                        .toString())) {
                            HSSFPictureData picData = getPictureFromExcel(row.getRowNum(),
                                    workbookForPicture);
                            if (picData != null) {
                                byte[] data = picData.getData();
                                insertPictureToExcel(workbook, rowIndex - 1, sheet, null, data);
                                flag = false;
                            }
                        }
                    }
                }

            } catch (InvalidFormatException | IOException e) {
                e.printStackTrace();
            }
            totalOkNum += okNum;
            totalFailureNum += failureNum;
            totalSummaryNum += summaryNum;
        }
        HSSFRow summaryRow = sheet.createRow(rowIndex++);
        summaryRow.setRowStyle(cellStyleTitle);
        HSSFCell summaryCell = summaryRow.createCell(0);
        summaryCell.setCellStyle(cellStyleTitle);
        String summary = String.format("总计: 成功: %d, 失败: %d, 其它: %d, 成功率: %2.2f%%", totalOkNum,
                totalFailureNum, totalSummaryNum - totalOkNum - totalFailureNum, totalOkNum * 1f
                        / totalSummaryNum * 100);
        summaryCell.setCellValue(summary);
        try {
            workbook.write(new FileOutputStream(Res.DIR_RESULT + Res.NAME_TOTAL));
            workbook.close();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * 初始化一个结果展示的Excel,并创建一个单元格样式,初始化基本值
     * 
     * @param workbook
     *            初始化的Excel
     * @return 返回创建的单元格样式{@link HSSFCellStyle}
     */
    private HSSFCellStyle initWorkbook(HSSFWorkbook workbook) {
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        HSSFSheet sheet = workbook.createSheet();
        // 初始化纵向对齐方式为居中
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 初始化单元格大小为包含文本
        cellStyle.setWrapText(true);
        // 初始化工作表的列宽
        sheet.setColumnWidth(0, 15 * 256);
        sheet.setColumnWidth(1, 15 * 256);
        sheet.setColumnWidth(2, 40 * 256);
        sheet.setColumnWidth(3, 15 * 256);
        sheet.setColumnWidth(4, 10 * 256);
        sheet.setColumnWidth(5, 25 * 256);
        sheet.setColumnWidth(6, 130 * 256);
        sheet.setColumnWidth(7, 50 * 256);
        return cellStyle;
    }

    /**
     * 获取apk基本信息, 如果之前已经解析过, 将直接使用
     * 
     * @param apkPath
     *            apk路径
     * @return 返回apk的基础信息
     * @throws Exception
     */
    synchronized private ApkInfo getApkInfo(String apkPath) throws Exception {
        for (ApkInfo apkInfo : apkInfos) {
            if (apkPath.equals(apkInfo.filePath)) {
                return apkInfo;
            }
        }
        ApkInfo apkInfo = new ApkInfo(apkPath);
        apkInfos.add(apkInfo);
        return apkInfo;
    }

    private ArrayList<ArrayList<HSSFRow>> sortResult(File file) {
        ArrayList<ArrayList<HSSFRow>> rowLists = new ArrayList<ArrayList<HSSFRow>>();
        ArrayList<HSSFRow> rowNone = new ArrayList<HSSFRow>();
        ArrayList<HSSFRow> rowCrash = new ArrayList<HSSFRow>();
        ArrayList<HSSFRow> rowOk = new ArrayList<HSSFRow>();
        ArrayList<HSSFRow> rowFailures = new ArrayList<HSSFRow>();
        ArrayList<HSSFRow> rowError = new ArrayList<HSSFRow>();
        ArrayList<HSSFRow> rowNotFoundClass = new ArrayList<HSSFRow>();
        ArrayList<HSSFRow> rowNotData = new ArrayList<HSSFRow>();
        ArrayList<HSSFRow> rows = excelUtil.readExcelHssfRows(file.getAbsolutePath());
        for (HSSFRow row : rows) {
            String status = excelUtil.getCellStringValue(row, 3);
            if (status == null) {
                continue;
            }
            if (Status.NONE.toString().equals(status)) {
                rowNone.add(row);
            } else if (Status.CRASH.toString().equals(status)) {
                rowCrash.add(row);
            } else if (Status.OK.toString().equals(status)) {
                rowOk.add(row);
            } else if (Status.FAILURES.toString().equals(status)) {
                rowFailures.add(row);
            } else if (Status.ERROR.toString().equals(status)) {
                rowError.add(row);
            } else if (Status.NOT_FOUND_CLASS.toString().equals(status)) {
                rowNotFoundClass.add(row);
            } else if (Status.NOT_DATA.toString().equals(status)) {
                rowNotData.add(row);
            }
        }
        rowLists.add(rowNone);
        rowLists.add(rowCrash);
        rowLists.add(rowOk);
        rowLists.add(rowFailures);
        rowLists.add(rowError);
        rowLists.add(rowNotFoundClass);
        rowLists.add(rowNotData);
        return rowLists;
    }

    private void insertPictureToExcel(HSSFWorkbook workbook, int rowIndex, HSSFSheet sheet,
            String filename, byte[] data) throws IOException {
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255, (short) 7, rowIndex,
                (short) 7, rowIndex);
        anchor.setAnchorType(3);
        ByteArrayOutputStream byteArrayout = new ByteArrayOutputStream();

        if (filename == null) {
            byteArrayout.write(data);
        } else {
            BufferedImage bufferImg;
            bufferImg = ImageIO.read(new File(filename + File.separator + Res.NAME_SCREENSHOT));
            ImageIO.write(bufferImg, "jpg", byteArrayout);
        }
        int picIndex = workbook.addPicture(byteArrayout.toByteArray(),
                HSSFWorkbook.PICTURE_TYPE_JPEG);
        patriarch.createPicture(anchor, picIndex).resize(0.8, 0.8);
    }

    private HSSFPictureData getPictureFromExcel(int rownum, HSSFWorkbook workbook) {
        HSSFSheet sheet = workbook.getSheetAt(0);
        List<HSSFShape> shapes = sheet.getDrawingPatriarch().getChildren();
        List<HSSFPictureData> pictures = workbook.getAllPictures();
        if (shapes.size() != pictures.size()) {
            System.out.println("Excel解析图片信息失败！\n");
            return null;
        }
        for (int i = 0; i < shapes.size(); i++) {
            if (shapes.get(i) instanceof HSSFPicture) {
                HSSFPicture pic = (HSSFPicture) shapes.get(i);
                ClientAnchor anchor = pic.getClientAnchor();
                if (anchor.getRow1() == rownum) {
                    return pic.getPictureData();
                }
            }
        }
        return null;
    }
}
