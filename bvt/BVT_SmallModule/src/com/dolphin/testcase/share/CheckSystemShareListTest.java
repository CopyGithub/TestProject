package com.dolphin.testcase.share;

import com.adolphin.common.BaseTest;
import com.test.annotation.Reinstall;

/**
 * 脚本编号: DOLINT-490
 * <p>
 * 脚本描述: 验证首次安装dolphin, 系统服务排序正确
 * 
 * @author sjguo
 * 
 */
// @TestClass("验证首次安装dolphin, 系统服务排序正确")
@Reinstall
public class CheckSystemShareListTest extends BaseTest {
    // @UiAutomatorAfter("com.dante.SystemShareListTest")
    // 使用UiAutomator无法点击Menu的Share (TODO)
    public void testCheckSystemShareList() {
    }

    @Override
    public void setUp() {
    }

    @Override
    public void tearDown() {
    }

}
