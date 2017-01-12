package com.basetest;

import com.robotium.solo.Solo;

import android.app.Instrumentation;
import android.content.Context;

public class XmlUtil {
    private com.android.common.XmlUtil xmlUtil;
    // share preferences file name
    private final String preferences;
    private final String theme = "theme";
    private final String long_press_pop_preferences_file = "long_press_pop_preferences_file";
    private final String preload = "preload";
    private final String recordExitCount = "recordExitCount";
    private final String localization = "localization";
    // share preferences key name
    private final String normal_data_track_enabled = "normal_data_track_enabled";
    private final String orientation = "orientation";
    private final String download_dir = "download_dir";
    private final String theme_id = "theme_id";
    private final String show_guide_count = "com.dolphin.browser.gesturesonar.show_guide_count";
    private final String search_categories = "search_categories";
    private final String remote_locale = "remote_locale";
    private final String pref_username = "pref_username";

    public XmlUtil(Instrumentation instrumentation, Solo solo) {
        Context context = instrumentation.getTargetContext();
        xmlUtil = new com.android.common.XmlUtil(context);
        preferences = new CaseUtil(instrumentation, solo).packageName + "_preferences";
    }

    /**
     * 返回normal_data_track_enabled的状态，默认为false
     * 
     * @return
     */
    public boolean getNormalDataTrackEnabled() {
        return xmlUtil.getSharedPreferencesValues(preferences, normal_data_track_enabled);
    }

    /**
     * 设置normal_data_track_enabled的状态
     * 
     * @param flag
     */
    public boolean setNormalDataTrackEnabled(boolean flag) {
        return xmlUtil.setSharedPreferencesValues(preferences, normal_data_track_enabled, flag);
    }

    /**
     * 返回orientation的状态,默认为auto=-1, 竖屏portrait=1,横屏landscape=0
     * 
     * @return
     */
    public int getOrientation() {
        return xmlUtil.getSharedPreferencesValuesInt(preferences, orientation);
    }

    /**
     * 设置orientation的状态,默认为auto=-1, 竖屏portrait=1,横屏landscape=0
     * 
     * @param flag
     */
    public boolean setOrientation(int flag) {
        return xmlUtil.setSharedPreferencesValuesInt(preferences, orientation, flag);
    }

    /**
     * 返回Download Dir的路径
     * 
     * @return
     */
    public String getDownloadDir() {
        return xmlUtil.getSharedPreferencesValuesString(preferences, download_dir);
    }

    /**
     * 设置Download Dir的路径
     * 
     * @param downloadDir
     * @return
     */
    public boolean setDownloadDir(String downloadDir) {
        return xmlUtil.setSharedPreferencesValuesString(preferences, download_dir, downloadDir);
    }

    /**
     * 获取当前Theme ID
     * 
     * @return
     */
    public int getThemeId() {
        return xmlUtil.getSharedPreferencesValuesInt(theme, theme_id);
    }

    /**
     * 获取当前ShowGuide次数
     * 
     * @return
     */
    public int getShowGuideCount() {
        return xmlUtil.getSharedPreferencesValuesInt(long_press_pop_preferences_file,
                show_guide_count);
    }

    /**
     * 设置当前ShowGuide次数
     * 
     * @param num
     * @return
     */
    public boolean setShowGuideCount(int num) {
        return xmlUtil.setSharedPreferencesValuesInt(long_press_pop_preferences_file,
                show_guide_count, num);
    }

    /**
     * 返回SearchEngine的Json字符串
     * 
     * @return
     */
    public String getSearchCategories() {
        return xmlUtil.getSharedPreferencesValuesString(preload, search_categories);
    }

    /**
     * 获取指定类记录的重运行的次数
     * 
     * @param className
     *            这里一般设置为当前类名
     * @return
     */
    public int getExitCount(String className) {
        return xmlUtil.getSharedPreferencesValuesInt(recordExitCount, className);
    }

    /**
     * 设置指定类重运行的次数
     * 
     * @param className
     *            这里一般设置为当前类名
     * @param num
     *            重启的次数
     * @return
     */
    public boolean setExitCount(String className, int num) {
        return xmlUtil.setSharedPreferencesValuesInt(recordExitCount, className, num);
    }

    /**
     * 获取当前的Smart Local值
     * 
     * @return
     */
    public String getSmartLocale() {
        return xmlUtil.getSharedPreferencesValuesString(localization, remote_locale);
    }

    /**
     * 设置当前的Smart Locale值
     * 
     * @param smartLocale
     * @return
     */
    public boolean setSmartLocale(String smartLocale) {
        return xmlUtil.setSharedPreferencesValuesString(localization, remote_locale, smartLocale);
    }

    /**
     * 获取登录的用户名
     * 
     * @return
     */
    public String getConnectUserName() {
        return xmlUtil.getSharedPreferencesValuesString(preferences, pref_username);
    }
}
