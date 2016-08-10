package com.framework.performance;

import java.util.ArrayList;

class Data {
    protected int testNo;// 测试次数
    protected int peak;// 峰值
    protected int average;// 平均值
    protected ArrayList<int[]> originalDatas = new ArrayList<int[]>();// 测试的原始值
}
