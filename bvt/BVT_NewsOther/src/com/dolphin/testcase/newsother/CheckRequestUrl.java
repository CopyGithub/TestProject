package com.dolphin.testcase.newsother;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import android.view.View;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.robotium.solo.By;

/**
 * 脚本编号: <br>
 * DolphinInt.Func.NewsAPI_001#002 <br>
 * DolphinInt.Func.NewsAPI_001#003 <br>
 * DolphinInt.Func.NewsAPI_001#004 <br>
 * DolphinInt.Func.NewsAPI_001#005 <br>
 * DolphinInt.Func.NewsAPI_001#006 <br>
 * DolphinInt.Func.NewsAPI_001#007 <br>
 * DolphinInt.Func.NewsAPI_001#008 <br>
 * DolphinInt.Func.NewsAPI_001#009 <br>
 * DolphinInt.Func.NewsAPI_001#010 <br>
 * DolphinInt.Func.NewsAPI_001#011 <br>
 * DolphinInt.Func.NewsAPI_001#012 <br>
 * DolphinInt.Func.NewsAPI_001#013 <br>
 * DolphinInt.Func.NewsAPI_001#014 <br>
 * DolphinInt.Func.NewsAPI_001#015 <br>
 * DolphinInt.Func.NewsAPI_001#016 <br>
 * DolphinInt.Func.NewsAPI_001#017 <br>
 * DolphinInt.Func.NewsAPI_001#018
 * <p>
 * 脚本描述: <br>
 * Tabs <br>
 * Top News--焦点新闻 <br>
 * Top News--图文新闻 <br>
 * Top News--焦点新闻guide页面 <br>
 * Top News--焦点新闻guide页面，相关新闻 <br>
 * Top News--焦点新闻guide页面，相关新闻，详情页 <br>
 * Top News--焦点新闻guide页面，更多新闻list <br>
 * Top News--焦点新闻guide页面，更多新闻list，详情页 <br>
 * Top News--图文新闻详情页 <br>
 * Top News--图文新闻详情页，相关新闻 <br>
 * Top News--图文新闻详情页，相关新闻，详情页 <br>
 * Top News--图文新闻详情页，更多新闻 <br>
 * Top News--图文新闻详情页，更多新闻，详情页 <br>
 * 新闻list <br>
 * 新闻list---新闻详情页 <br>
 * 新闻list---新闻详情页，相关新闻 <br>
 * 新闻list---新闻详情页，更多新闻
 * 
 * @author chchen
 * 
 */
@SuppressWarnings("unused")
public class CheckRequestUrl extends BaseTest {
    private final String local = "ru-ru";
    private final String host = "http://now.dolphin.com/";
    private final String key_locales = "[NewHomeManager]request newhome locales, url:";
    private final String key_detail_guide = "[WebViewCallbackHandler]onPageStarted:";
    private final String key_tab = "[NewsRequestWrapper]Request news tabs:";
    private final String key_top_news = "[NewsRequestWrapper]Visit top news:";
    private final String key_photo_news = "[NewsRequestWrapper]Visit photo news:";
    private final String key_latest = "obj=http://now.dolphin.com/api/infostream/latest.json";
    private final String key_weibo = "obj=http://now.dolphin.com/api/infostream/weibo.json";
    private final String key_infos = "[NewsRequestWrapper]request news tab info, url:";
    private final String key_top_json = "obj=http://now.dolphin.com/api/infostream/top.json";
    private final String url_locales = host + "api/infostream/locales.json";
    private final String url_tabs = host + "api/infostream/tabs.json";
    private final String url_top = host + "api/infostream/top.json";
    private final String url_detail = host + local + "/detail.html";
    private final String url_guide = host + local + "/guide.html";
    private final String url_latest = host + "api/infostream/latest.json";
    private final String url_weibo = host + "api/infostream/weibo.json";
    private final String url_infos = host + "api/infostream/tab/infos.json";
    private final String from_top = "top";
    private final String from_home = "home";
    private final String chn_ofw = "ofw";

    private Process process = null;
    private String filePath = "";

    public void testCheckRequestUrl() {
        filePath = utils.externalStorageDirectory + "/Robotium-Screenshots/log.txt";
        uiUtil.skipWelcome();

        // 第一次设置会出现重启Crash的问题,盒子第二次运行时会正常运行
        uiUtil.setSmartLocale("ru_RU");
        javaUtils.createFileOrDir(filePath, false, true);
        recordLog(true);
        // 验证:显示New Home后的页面
        solo.sleep(5 * 1000);// 等待news 加载
        // 002:Tabs
        checkAPI(key_tab, url_tabs, local, from_home, null, null);
        // 003:Top News--焦点新闻
        checkAPI(key_top_news, url_top, local, from_top, "3", null);
        // 004:Top News--图文新闻
        checkAPI(key_photo_news, url_top, local, from_top, "1", null);

        // 验证:点击焦点新闻后的api验证
        clickTopNews();
        // 005:Top News--焦点新闻guide页面
        checkAPI(key_detail_guide, url_detail, null, from_top, null, null);

        // 验证:滑动后显示的相关新闻和新闻列表
        // scrollNews();
        // 006:Top News--焦点新闻guide页面，相关新闻
        // TODO 有时会出现没有相关新闻的情况
        // checkAPI(key_top_json, url_top, null, from_top, null, chn_ofw);
        // 007:Top News--焦点新闻guide页面，相关新闻，详情页
        checkAPI(key_detail_guide, url_guide, null, from_top, null, chn_ofw);
        // TODO 没有相关log
        // 008:Top News--焦点新闻guide页面，更多新闻list
        // checkAPI(key_latest, url_latest, null, from_top, null, chn_ofw);

        // 验证:点击更多新闻后的相关API验证
        clickMoreNews();
        // 009:Top News--焦点新闻guide页面，更多新闻list，详情页
        checkAPI(key_detail_guide, url_detail, null, from_top, null, chn_ofw);

        // 验证:点击图形新闻后的api验证
        clickBackAndClickPhotoNews();
        // 010:Top News--图文新闻详情页
        checkAPI(key_detail_guide, url_detail, null, from_top, null, chn_ofw);
        // TODO 没有相关Log
        // checkAPI(key_weibo, url_weibo, null, from_top, null, chn_ofw);

        // 验证:滑动后显示的相关新闻和新闻列表
        // scrollNews();
        // 011:Top News--图文新闻详情页，相关新闻 TODO 缺少log
        // /api/infostream/relevance.json?lc=ru-ru&id=1430904085024053&from=top&feature=3&cid=51&c=4&did=67dc28c6b69d5e8a7d69cd1ad10c7bbb&pn=com.dolphin.browser.express.web&appvc=490&chn=ofw
        // 013:Top News--图文新闻详情页，更多新闻 TODO 缺少log
        // checkAPI(key_latest, url_latest, null, from_top, null, chn_ofw);

        // 验证:点击相关新闻后的相关API验证
        clickRelevanceNews();
        // 012:Top News--图文新闻详情页，相关新闻，详情页
        checkAPI(key_detail_guide, url_detail, null, from_top, null, chn_ofw);
        // TODO 没有相关Log
        // checkAPI(url_weibo, url_weibo, null, from_top, null, chn_ofw);

        // 验证:点击更多新闻后的相关API验证
        clickMoreNews();
        // 014:Top News--图文新闻详情页，更多新闻，详情页
        checkAPI(key_detail_guide, url_detail, null, from_top, null, chn_ofw);
        // TODO 没有相关Log
        // checkAPI(key_weibo, url_weibo, null, from_top, null, chn_ofw);

        // 验证:右屏界面中的api
        clickBackAndEnterSide();
        // 015:新闻list
        checkAPI(key_infos, url_infos, null, from_home, null, chn_ofw);

        // 验证:点击右屏新闻详情页
        clickSingleNews();
        // 016:新闻list---新闻详情页
        checkAPI(key_detail_guide, url_detail, null, from_home, null, chn_ofw);
        // TODO 没有相关Log
        // checkAPI(key_weibo, url_weibo, null, from_home, null, chn_ofw);

        // 验证:相关新闻和更多新闻
        // scrollNews();
        // 017:新闻list---新闻详情页，相关新闻 TODO 相关api没有
        // 018:新闻list---新闻详情页，更多新闻
        checkAPI(key_infos, url_infos, null, from_home, null, chn_ofw);

        recordLog(false);
    }

    private void recordLog(boolean isStart) {
        if (isStart) {
            try {
                process = Runtime.getRuntime().exec(
                        "logcat " + "-f " + filePath + " -s TunnyBrowser_" + caseUtil.appvn);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            process.destroy();
        }
    }

    private String readLog(String key, String url) {
        String result = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(
                    filePath), "UTF-8"));
            String s = br.readLine();
            while (s != null) {
                if (s.contains(key) && s.contains(url)) {
                    result = s;
                }
                s = br.readLine();
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue("没有找到需要的日志. 搜索关键字为:" + key + ",搜索的url为:" + url, result != "");
        return result;
    }

    private HashMap<String, String> getHashMap(String key, String url) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        String originText = readLog(key, url);
        originText.replace("obj=http", "obj=");
        String originUrl = originText.substring(originText.indexOf("http:"));
        String apiUrl = originUrl.substring(0, originUrl.indexOf("?"));
        hashMap.put("url", apiUrl);
        String[] parameterList = originUrl.substring(originUrl.indexOf("?") + 1).split("&");
        for (int i = 0; i < parameterList.length; i++) {
            String[] keyValue = parameterList[i].split("=");
            if (!(keyValue.length < 2)) {
                hashMap.put(keyValue[0], keyValue[1]);
            }
        }
        return hashMap;
    }

    private void checkAPI(String key, String urlN, String lcN, String fromN, String cN, String chnN) {
        HashMap<String, String> hasMap = getHashMap(key, urlN);
        String pn = hasMap.get("pn");
        assertTrue("参数pn错误,请求的是:" + pn + ",需求的是:" + caseUtil.packageName,
                pn.equals(caseUtil.packageName));
        String appvc = hasMap.get("appvc");
        assertTrue("参数appvc错误,请求的是:" + appvc + ",需求的是:" + caseUtil.appvc,
                appvc.equals(String.valueOf(caseUtil.appvc)));
        String did = hasMap.get("did");
        String androidID = utils.getAndroidIDForMD5();
        assertTrue("参数did错误,请求的是:" + did + ",需求的是:" + androidID, did.equals(androidID));

        if (cN != null) {
            String c = hasMap.get("c");
            assertTrue("参数c错误,请求的是:" + c + ",需求的是:" + cN, c.equals(cN));
        }
        if (fromN != null) {
            String from = hasMap.get("from");
            assertTrue("参数from错误,请求的是:" + from + ",需求的是:" + fromN, from.equalsIgnoreCase(fromN));
        }
        if (lcN != null) {
            String lc = hasMap.get("lc");
            assertTrue("参数lc错误,请求的是:" + lc + ",需求的是:" + local, lc.equals(local));
        }
        if (chnN != null) {
            String chn = hasMap.get("chn");
            assertTrue("参数chn错误,请求的是:" + chn + ",需求的是:" + chnN, chn.equals(chnN));
        }
    }

    private void clickTopNews() {
        View news = caseUtil.getViewByClassName("TopNewsItemView", 0, false);
        assertTrue("没有正确显示对应的topnews新闻", news.isShown());
        solo.clickOnView(news);
        watiForDetails();
    }

    private void clickRelevanceNews() {
        caseUtil.clickOnWebElement(By.className("related-news-list-item"), true);
        watiForDetails();
    }

    private void clickMoreNews() {
        caseUtil.clickOnWebElement(By.className("list-item"), true);
        watiForDetails();
    }

    private void clickBackAndClickPhotoNews() {
        do {
            solo.goBack();
            solo.sleep(Res.integer.time_wait);
        } while (!caseUtil.waitForViewByClassName("MessageView", false, 3 * 1000));
        solo.clickOnView(caseUtil.getViewByClassName("MessageView", 0, false));
        watiForDetails();
    }

    private void watiForDetails() {
        solo.sleep(3 * 1000);
        assertTrue("没有正确打开图形新闻", uiUtil.waitForWebPageFinished());
        solo.sleep(10 * 1000);// 等待页面内容加载完成
    }

    private void clickBackAndEnterSide() {
        do {
            solo.goBack();
            solo.sleep(Res.integer.time_wait);
        } while (!caseUtil.waitForViewByClassName("MessageView", false, 3 * 1000));
        caseUtil.slideDireciton(null, true, 1f, 1f);// TODO 这里划不动, 必须手动划一次才正常
        solo.sleep(5 * 1000);// 等待内容加载完成
    }

    private void clickSingleNews() {
        View news = caseUtil.getViewByClassName("SingleImageNewsItemView", 0, false);
        solo.clickOnView(news);
        watiForDetails();
    }
}