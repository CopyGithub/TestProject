package com.adolphin.common;

public final class Res {

    public static final class integer {
        // 常用时间(毫秒)
        public static final int time_wait = 1500;
        public static final int time_open_url = 3 * 1000;
        public static final int time_change_activity = 4 * 1000;
        public static final int time_launch = 7 * 1000;
        public static final int wait_timeout = 30 * 1000;
        public static final int network_timeout = 2 * 60 * 1000;
    }

    public static final class string {
        // 配置文件
        public static final String launcher_activity_classname = "mobi.mgeek.TunnyBrowser.BrowserActivity";
        // 通用的文本
        public static final String not_enter_activity = "没有正确进入%s界面";
        public static final String take_screen = "testCaseFailed";
        public static final String browser_activity = "BrowserActivity";
        // URL网址
        public static final String server_simple_domain = "autotest.baina.com";
        public static final String server_domain = "http://autotest.baina.com/";
        public static final String url_test = server_domain + "test/";
        public static final String url_aaa = server_domain + "test/aaa/";
        public static final String url_downloadtest = server_domain + "test/downloadtest/";
        public static final String url_kimi = server_domain + "test/downloadtest/img/kimi.jpg";
        public static final String url_img = server_domain + "test/downloadtest/kimi.html";
        public static final String url_performance = server_domain + "test/Performance/";
        public static final String url_textcode = server_domain + "test/textcode/";
        public static final String url_longpage = server_domain + "test/downloadtest/longPage.html";
        public static final String url_one = server_domain + "test/downloadtest/one.html";
        public static final String url_two = server_domain + "test/downloadtest/two.html";
        public static final String url_three = server_domain + "test/downloadtest/three.html";
        public static final String url_four = server_domain + "test/downloadtest/four.html";
        public static final String url_five = server_domain + "test/downloadtest/five.html";
        public static final String url_sixth = server_domain + "test/downloadtest/sixth.html";
        public static final String url_seven = server_domain + "test/downloadtest/seven.html";
        public static final String url_eight = server_domain + "test/downloadtest/eight.html";
        public static final String url_layouttest = server_domain + "test/layouttest/";
        public static final String url_player = server_domain + "/test/downloadtest/player.html";
        public static final String url_jetpacktest = server_domain + "test/jetpacktest/";
        public static final String url_download_long_name = server_domain
                + "/test/downloadtest/longNameTestlongNameTestlongNameTestlongNameTest.tar";
        public static final String down_apk = server_domain + "test/downloadtest/apk/downapk.apk";
        public static final String down_apk1 = server_domain + "/test/downloadtest/bigOne.zip";
        public static final String down_apk2 = server_domain + "/test/downloadtest/bigTwo.zip";
        public static final String down_apk3 = server_domain + "/test/downloadtest/middleOne.zip";
        public static final String down_apk4 = server_domain + "/test/downloadtest/middleTwo.zip";
        public static final String down_apk5 = server_domain + "/test/downloadtest/middleThree.zip";
        public static final String down_pdf = server_domain + "test/downloadtest/downpdf.pdf";
        // 网站名字
        public static final String testurl_name_1 = "one";
        public static final String testurl_name_2 = "two";
        public static final String testurl_name_3 = "three";
        public static final String testurl_name_4 = "four";
        public static final String testurl_name_5 = "five";
        public static final String testurl_name_6 = "sixthPage";
        public static final String testurl_name_7 = "seven";
        public static final String testurl_name_8 = "eight";
        public static final String down_apk_name1 = "bigOne.zip";
        public static final String down_apk_name2 = "bigTwo.zip";
        public static final String down_apk_name3 = "middleOne.zip";
        public static final String down_apk_name4 = "middleTwo.zip";
        public static final String down_apk_name5 = "middleThree.zip";
        public static final String forMostVisitedFolder = "ForMostVisited";
        // View的ID文本
        public static final String second_tab_title = "second_tab_title";
        public static final String ds_email = "ds_email";
        public static final String _continue = "_continue";
        public static final String ds_password = "ds_password";
        public static final String ds_dolphin_login = "ds_dolphin_login";
        public static final String dolphin_icon = "dolphin_icon";
        public static final String button1 = "button1";
        // Dolphin中的文本
        public static final String clear_erase_data = "clear_erase_data";
        public static final String ds_cancel = "ds_cancel";
        public static final String no_account_info_title = "no_account_info_title";
        public static final String action_menu_item_settings = "action_menu_item_settings";
        public static final String synced_tips = "synced_tips";
        public static final String forget_password = "forget_password_underline";
        public static final String forget_password_activityname = "PasswordResetActivity";
        public static final String create_an_account = "sign_up_underline";
        public static final String create_an_account_activityname = "DolphinSignUpActivity";
        public static final String account_info = "account_info";
        public static final String dolphinconnect_signin = "dolphinconnect_signin";
        // Facebook中的文本
        public static final String sign_in_id = "name_txt";
        public static final String login_facebook_id = "tv_facebook_login";
        // Settings及相关界面用到的文本
        public static final String browser_settings_page = "BrowserSettingsPage";
        public static final String login_activity = "LoginActivity";
        public static final String login_wait_dialog = "LoginWaitDialog";
        public static final String dolphin_connect_activity = "DolphinConnectActivity";
        public static final String account_service_manage_activity = "AccountServiceManageActivity";
        public static final String cloud_data_manage_activity = "CloudDataManageActivity";
        public static final String dolphin_user_name = "autotest@gmail.com";
        public static final String dolphin_user_name1 = "autotest1@gmail.com";
        public static final String dolphin_user_name2 = "autotest350@gmail.com";
        public static final String dolphin_user_password = "123456";
        public static final String login_ds_fail = "登录Dolphin connect失败";
        public static final String communication_fail = "两个设备间的通讯超时或跨设备确认的操作失败";
        public static final String cross_login_fail = "从设备登录失败";
        public static final String login_success = "LoginSuccess";
        public static final String login_start = "LoginStart";
        public static final String login_fail = "LoginFail";
        public static final String logout_start = "LogoutStart";
        public static final String logout_success = "LogoutSuccess";
        public static final String logout_fail = "LogoutFail";
        public static final String can_not_null = "成功和失败的参数不能为空";
        public static final String settings_folder = "forSettings";
    }
}