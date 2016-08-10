package com.adolphin.common;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.android.common.Utils;
import com.robotium.solo.By;
import com.robotium.solo.Solo;
import com.robotium.solo.WebElement;

import junit.framework.Assert;

public class CaseUtil {
    private final Utils utils;
    private final com.java.common.Utils javaUtils = new com.java.common.Utils();
    private final Solo solo;
    private final Instrumentation inst;
    private final Context context;
    private final Resources resources;
    private final float speed = 2.5f;// 自定义值,移动速度

    public final String packageName;
    public final int appvc;
    public final String appvn;

    public CaseUtil(Solo solo) {
        this.solo = solo;
        inst = solo.getInstrumentation();
        context = inst.getTargetContext();
        utils = new Utils(context);
        resources = context.getResources();
        packageName = context.getPackageName();
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (NameNotFoundException e) {
            Assert.assertTrue("无法正确获取包信息", false);
        }
        appvc = packageInfo.versionCode;
        appvn = packageInfo.versionName;
    }

    /**
     * 返回当前界面的旋转状态
     * 
     * @return {@code 0} 旋转0度,{@code 1} 旋转90度,{@code 2} 旋转180度,{@code 3} 旋转270度,
     */
    public int getDisplayRotation() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getRotation();
    }

    /**
     * 获取当前状态下的x和y,如480*800
     * <p>
     * 旋转后,就是旋转后的xy,如800*480
     * 
     * @param real
     *            是否为真实屏幕大小, 主要指有虚拟按键的手机
     * @return
     */
    @SuppressWarnings("deprecation")
    public int[] getDisplaySize(boolean real) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        if (real && utils.SDK >= 17) {
            wm.getDefaultDisplay().getRealSize(point);
        } else if (utils.SDK >= 13) {
            wm.getDefaultDisplay().getSize(point);
        } else {
            point.x = wm.getDefaultDisplay().getWidth();
            point.y = wm.getDefaultDisplay().getHeight();
        }
        return new int[] { point.x, point.y };
    }

    /**
     * 得到指定{@link View}的物理尺寸
     * 
     * @param view
     *            表示控件，当{@link View}为{@code null}的时候表示手机可使用屏幕
     * @return 返回x,y的物理尺寸
     */
    private double[] getDisplayPhysicalSize(View view, boolean isReal) {
        double[] physicalSize = new double[2];
        int rotation = getDisplayRotation();
        float[] dpi;
        if (rotation == 0 || rotation == 2) {
            dpi = new float[] { utils.displayMetrics.xdpi, utils.displayMetrics.ydpi };
        } else {
            dpi = new float[] { utils.displayMetrics.ydpi, utils.displayMetrics.xdpi };
        }
        if (view == null) {
            int[] xy = new int[2];
            xy = getDisplaySize(isReal);
            physicalSize[0] = xy[0] / dpi[0];
            physicalSize[1] = xy[1] / dpi[1];
        } else {
            physicalSize[0] = view.getWidth() / dpi[0];
            physicalSize[1] = view.getHeight() / dpi[1];
        }
        return physicalSize;
    }

    /**
     * 返回屏幕的大小范围
     * <p>
     * 目前界定: 3.9寸一下为小屏幕机型,6.5寸一下为中屏幕机型,其它为大屏幕机型
     * 
     * @return {@code 0} 表示小屏幕机型, {@code 1} 表示中屏幕机型, {@code 2} 表示大屏幕机型
     */
    public int getDisplayRange() {
        double[] xy = getDisplayPhysicalSize(null, true);
        double inch = Math.sqrt(Math.pow(xy[0], 2) + Math.pow(xy[1], 2));
        if (inch < 3.9) {
            return 0;
        } else if (inch < 6.5) {
            return 1;
        } else {
            return 2;
        }
    }

    /**
     * 判断指定{@link View}是否在视图内
     * 
     * @param view
     * @param fullscreen
     *            {@code true} 表示全屏, {@code false} 表示非全屏
     * @return 在视图内返回{@code true}, 不在视图内则返回{@code false}
     */
    public boolean isInsideDisplay(Object object, boolean fullscreen) {
        int[] location = new int[2];
        if (object instanceof View) {
            ((View) object).getLocationOnScreen(location);
        } else if (object instanceof WebElement) {
            ((WebElement) object).getLocationOnScreen(location);
        } else {
            Assert.assertTrue("对象不是一个View或WebElement", false);
        }
        int[] display = getDisplaySize(fullscreen);
        if (location[0] >= 0 && location[1] >= 0 && location[0] < display[0] - 1
                && location[1] < display[1] - 1) {
            return true;
        }
        return false;
    }

    /**
     * 指定List中是否有一个{@link View}是在界面上可见
     * 
     * @param views
     * @param fullscreen
     *            {@code true} 表示全屏, {@code false} 表示非全屏
     * @return
     */
    public boolean isViewShow(ArrayList<View> views, boolean fullscreen) {
        boolean flag = false;
        for (View view : views) {
            flag = view.isShown() && isInsideDisplay(view, fullscreen);
            if (flag) {
                return true;
            }
        }
        return false;
    }

    /**
     * 按照物理速度计算移动一段距离需要的step
     * 
     * @param location
     *            两个点的坐标组, 大小不能小于4
     * @param speedPer
     *            默认速度的倍数,{code 1.0f} 表示使用默认速度
     * @return
     */
    private int calculateStep(int[] location, float speedPer) {
        if (location == null || location.length < 4) {
            Assert.assertTrue("location值不能为空或小于4,需要指定移动的下一个点", false);
        }
        float[] physical = new float[2];
        float[] dpi = new float[2];
        int rotation = getDisplayRotation();
        if (rotation == 0 || rotation == 2) {
            dpi = new float[] { utils.displayMetrics.xdpi, utils.displayMetrics.ydpi };
        } else {
            dpi = new float[] { utils.displayMetrics.ydpi, utils.displayMetrics.xdpi };
        }
        physical[0] = (location[2] - location[0]) / dpi[0];
        physical[1] = (location[3] - location[1]) / dpi[1];
        float speed = this.speed * speedPer;
        return (int) (Math.sqrt(Math.pow(physical[0], 2) + Math.pow(physical[1], 2)) / speed * 100);
    }

    /**
     * 执行指定的手势操作
     * 
     * @param location
     *            操作的坐标点集合,大于2个点(即4个值)时,表示滑动或拖拽
     * @param actions
     *            操作行为,值为{@link MotionEvent}类下的Action行为,如:
     *            {@code MotionEvent.ACTION_DOWN}
     * @param speedPer
     *            默认速度的倍数,{code 1f} 表示使用默认速度, 只有滑动或拖拽时才有意义
     * @param longPress
     *            是否在执行按下和弹起时进行等待, 通常叫长按或拖拽时设置为{@code true}
     */
    private void motion(int[] location, int[] actions, float speedPer, boolean longPress) {
        if (location == null || location.length < 2) {
            Assert.assertTrue("location值不能为空或小于2,需要至少一组数表示一个点坐标", false);
        }
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();
        float[] xy = new float[] { location[0], location[1] };
        if (actions != null) {
            for (int action : actions) {
                int retry = 0;
                boolean successfull = false;
                MotionEvent event = MotionEvent
                        .obtain(downTime, eventTime, action, xy[0], xy[1], 0);
                do {
                    try {
                        inst.sendPointerSync(event);
                        successfull = true;
                    } catch (SecurityException e) {
                        retry++;
                    }
                } while (!successfull && retry < 10);
                Assert.assertTrue("MotionEvent不能完成", successfull);
            }
        } else {
            int time = ViewConfiguration.getLongPressTimeout() * 3;
            float[] dxy = null;
            int step = 1;
            motion(location, new int[] { MotionEvent.ACTION_DOWN }, 1f, false);
            if (longPress) {
                javaUtils.sleep(time);
            }
            while (location.length >= 4) {
                step = calculateStep(location, speedPer);
                dxy = new float[] { (location[2] - location[0]) / (step * 1f),
                        (location[3] - location[1]) / (step * 1f) };
                while (--step > 0) {
                    boolean successfull = false;
                    int retry = 0;
                    xy[0] += dxy[0];
                    xy[1] += dxy[1];
                    downTime = SystemClock.uptimeMillis();
                    eventTime = SystemClock.uptimeMillis();
                    MotionEvent event = MotionEvent.obtain(downTime, eventTime,
                            MotionEvent.ACTION_MOVE, xy[0], xy[1], 0);
                    do {
                        try {
                            inst.sendPointerSync(event);
                            successfull = true;
                        } catch (SecurityException e) {
                            retry++;
                        }
                    } while (!successfull && retry < 10);
                    Assert.assertTrue("ACTION_MOVE不能完成", successfull);
                }
                location = javaUtils.removeElement(javaUtils.removeElement(location, 0), 0);
            }
            if (longPress) {
                javaUtils.sleep(time);
            }
            motion(location, new int[] { MotionEvent.ACTION_UP }, 1f, false);
        }
    }

    /**
     * 画一个指定的图案,或拖拽指定位置的元素到另一个指定位置
     * 
     * @param location
     *            移动轨迹的数组,每两组数据表示一个点
     * @param speedPer
     *            默认速度的倍数,{@code 1f} 表示使用默认速度
     * @param longPress
     *            是否在执行按下和弹起时进行等待, 需要拖拽时设置为{@code true}
     */
    public void drag(int[] location, float speedPer, boolean longPress) {
        if (location == null || location.length < 4) {
            Assert.assertTrue("location值不能为空或小于4,需要指定两个点", false);
        }
        motion(location, null, speedPer, longPress);
    }

    /**
     * 对一个{@link View} 进行按下/点击/长按操作
     * 
     * @param view
     *            操作的{@link View}
     * @param action
     *            行为,0表示只执行ACTION_DOWN,1表示点击行为,2表示长按
     */
    public void motion(View view, int action) {
        int[] location = utils.getViewCenter(view);
        switch (action) {
        case 0:
            motion(location, new int[] { MotionEvent.ACTION_DOWN }, 1f, false);
            break;
        case 1:
            motion(location, new int[] { MotionEvent.ACTION_DOWN, MotionEvent.ACTION_UP }, 1f,
                    false);
            break;
        case 2:
            motion(location, null, 1f, true);
            break;
        default:
            Assert.assertTrue("请输入0-2之间的值", false);
            break;
        }
    }

    /**
     * 在指定{@link View}内画一个图案 <br>
     * 当{@link View}为[@code null}时,状态栏和导航栏也是计算到比例里的,需要考虑这点
     * 
     * @param view
     *            指定的View, 当为{@code null} 会以全屏计算,请传参时优先进行判断
     * @param location
     *            坐标组, 参数为相对整个{@link View} 的比例
     * @param speedPer
     *            默认速度的倍数,{code 1.0f} 表示使用默认速度
     */
    public void draw(View view, float[] location, float speedPer) {
        int[] viewLocation = new int[2];
        int[] xy = new int[location.length];
        if (view == null) {
            viewLocation = getDisplaySize(true);
            for (int i = 0; i < location.length; i++) {
                if (i % 2 == 0) {
                    xy[i] = (int) (viewLocation[0] * location[i]);
                } else {
                    xy[i] = (int) ((viewLocation[1]) * location[i]);
                }
                if (xy[i] > 1) {
                    xy[i]--;
                }
            }
        } else {
            view.getLocationOnScreen(viewLocation);
            for (int i = 0; i < location.length; i++) {
                if (i % 2 == 0) {
                    xy[i] = (int) (location[i] * (view.getWidth()) + viewLocation[0]);
                } else {
                    xy[i] = (int) (location[i] * view.getHeight() + viewLocation[1]);
                }
            }
        }
        drag(xy, speedPer, false);
    }

    /**
     * 滑动界面
     * 
     * @param view
     *            指定的View, 当为{@code null} 会以全屏计算,请传参时优先进行判断
     * @param fromX
     *            参数为相对整个{@link View} 的比例
     * @param fromY
     *            参数为相对整个{@link View} 的比例
     * @param toX
     *            参数为相对整个{@link View} 的比例
     * @param toY
     *            参数为相对整个{@link View} 的比例
     * @param speedPer
     *            默认速度的倍数,{code 1.0f} 表示使用默认速度
     */
    public void slide(View view, float fromX, float fromY, float toX, float toY, float speedPer) {
        draw(view, new float[] { fromX, fromY, toX, toY }, speedPer);
    }

    /**
     * 按照默认的滑动方面和比例进行滑动
     * 
     * @param view
     *            指定的View, 当为{@code null} 会以全屏计算,请传参时优先进行判断
     * @param isLeftRight
     *            滑动方向,{@code true} 表示左右滑动,{@code false}表示上下滑动,
     * @param per
     *            滑动范围的百分比, 如0.3f,表示左右或上下缩进30%,即0.3f-0.7f,>0.5f时表示反向
     * @param speedPer
     *            默认速度的倍数,{code 1.0f} 表示使用默认速度
     */
    public void slideDireciton(View view, boolean isLeftRight, float per, float speedPer) {
        if (per < 0f || per > 1f) {
            Assert.assertTrue("百分比per需要在0-1f之间", false);
        }
        float[] location = new float[] { 1 / 2f, 1 / 2f, 1 / 2f, 1 / 2f };
        if (isLeftRight) {
            location[0] = per;
            location[2] = 1 - per;
        } else {
            location[1] = per;
            location[3] = 1 - per;
        }
        slide(view, location[0], location[1], location[2], location[3], speedPer);
    }

    /**
     * 拖拽一个{@link View} 到另一个{@link View} 中, 也可以表示长按{@link View},然后滑动到另一个
     * {@link View}所在的位置
     * 
     * @param fromView
     *            要拖拽的{@link View}
     * @param toView
     *            拖拽的目标{@link View}
     */
    public void dragViewToView(View fromView, View toView) {
        if (fromView == null || toView == null) {
            Assert.assertTrue("拖拽的两个View不能为空", false);
        }
        int[] fromLocation = utils.getViewCenter(fromView);
        int[] toLocation = utils.getViewCenter(toView);
        int[] location = new int[] { fromLocation[0], fromLocation[1], toLocation[0], toLocation[1] };
        drag(location, 1f, true);
    }

    /**
     * 点击弹出框中的列表的第index个
     * 
     * @param index
     *            要选择弹出框里面的第几行，0表示第一条
     */
    public void clickOnSelections(int index) {
        try {
            inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);
        } catch (SecurityException e) {
            Assert.assertTrue("Can not press the context menu!", false);
        }
        for (int i = 0; i < index; i++) {
            javaUtils.sleep(300);
            inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);
        }
        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_ENTER);
    }

    /**
     * 长按{@link View}等待弹出框，并选择弹出框的第几个条选项
     * 
     * @param view
     *            需要进行点击的{@link View}
     * @param index
     *            要选择弹出框里面的第几行，0表示第一条
     */
    public void longClickAndClickPopIndex(View view, int index) {
        motion(view, 2);
        javaUtils.sleep(Res.integer.time_wait);
        clickOnSelections(index);
    }

    /**
     * 判断指定的{@link View} 是否已经无法滑动了
     * 
     * @param view
     *            {@code null}
     * @param direction
     * @return
     * @throws Throwable
     */
    public boolean isScroll(View view, int direction) {
        ImageUtil imageUtil = new ImageUtil(solo);
        Bitmap bitmap1 = imageUtil.getBitmapFromView(view);
        javaUtils.sleep(Res.integer.time_wait);
        // TODO 目前只支持向下滑动,默认direction=1
        slideDireciton(view, false, 2 / 3f, 1f);
        javaUtils.sleep(Res.integer.time_wait);
        Bitmap bitmap2 = imageUtil.getBitmapFromView(view);
        return !imageUtil.compareBitmap(bitmap1, bitmap2, true);
    }

    /**
     * 启动一个特定的{@link Activity}
     * 
     * @param className
     *            {@link Activity} 的类名
     * @param action
     *            启动的Action参数,没有设置为{@code null}
     * @param url
     *            启动uri参数,没有设置为{@code null}
     */
    public void startActivity(String className, String action, String url) {
        Intent intent = new Intent();
        intent.setClassName(packageName, className);
        if (action != null) {
            intent.setAction(action);
        }
        if (url != null) {
            intent.setData(Uri.parse(url));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        solo.sleep(Res.integer.time_change_activity);
    }

    /**
     * 使用文本的Resource ID来获取对应当前国家的文本
     * 
     * @param rId
     *            文本的Resource ID
     * @param index
     *            如果是array,则使用index
     * @return
     */
    public String getTextByRId(String rId, int index) {
        if (index < 0) {
            return getTextByRId(rId);
        } else {
            try {
                Class<?> clz = Class.forName(packageName + ".R$array");
                Field field = clz.getDeclaredField(rId);
                field.setAccessible(true);
                int value = (Integer) field.get(clz);
                return resources.getStringArray(value)[index];
            } catch (ClassNotFoundException e) {
                Assert.assertTrue(e.getMessage(), false);
            } catch (NoSuchFieldException e) {
                return getTextByRId(rId);
            } catch (IllegalArgumentException e) {
                Assert.assertTrue(e.getMessage(), false);
            } catch (IllegalAccessException e) {
                Assert.assertTrue(e.getMessage(), false);
            }
        }
        return "";
    }

    /**
     * 使用文本的Resource ID来获取对应当前国家的文本
     * 
     * @param rId
     *            文本的Resource ID
     * @return
     */
    public String getTextByRId(String rId) {
        try {
            Class<?> clz = Class.forName(packageName + ".R$string");
            Field field = clz.getDeclaredField(rId);
            field.setAccessible(true);
            int value = (Integer) field.get(clz);
            return resources.getString(value);
        } catch (ClassNotFoundException e) {
            Assert.assertTrue(e.getMessage(), false);
        } catch (NoSuchFieldException e) {
            Assert.assertTrue("没有找到指定RID:" + rId + "所对应的字段", false);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage(), false);
        } catch (IllegalAccessException e) {
            Assert.assertTrue(e.getMessage(), false);
        }
        return "";
    }

    /**
     * 根据{@link View} 的ID获取特定{@link View}下的的第一个{@link View}
     * 
     * @param parent
     *            {@code null} 表示当前根目录下查找, 否则表示在指定{@link View}下查找
     * @param id
     *            指定{@link View}的可见ID
     * @return 如果没有找到,则返回{@code null}
     */
    public View getView(View parent, String id) {
        ArrayList<View> views = solo.getViews(parent);
        int viewId = resources.getIdentifier(id, "id", packageName);
        for (View view : views) {
            if (viewId == view.getId()) {
                return view;
            }
        }
        return null;
    }

    /**
     * 根据{@link View} 的ID获取特定{@link View}下的的所有{@link View}
     * 
     * @param parent
     *            {@code null} 表示当前根目录下查找, 否则表示在指定{@link View}下查找
     * @param id
     *            指定{@link View}的可见ID
     * @return 如果没有找到,则返回{@code null}
     */
    public ArrayList<View> getViews(View parent, String id) {
        ArrayList<View> views = solo.getViews(parent);
        ArrayList<View> result = new ArrayList<View>();
        int viewId = resources.getIdentifier(id, "id", packageName);
        for (View view : views) {
            if (viewId == view.getId()) {
                result.add(view);
            }
        }
        return result;
    }

    /**
     * 根据类名获取所有的{@link View}
     * 
     * @param className
     *            查找的类名
     * @param fuzzy
     *            是否模糊查询{@code true} 表示模糊查询
     * @return
     */
    public ArrayList<View> getViewsByClassName(String className, boolean fuzzy) {
        ArrayList<View> result = new ArrayList<View>();
        ArrayList<View> views = solo.getViews();
        for (View view : views) {
            String name = view.getClass().getSimpleName();
            if (fuzzy ? name.contains(className) : name.equals(className)) {
                result.add(view);
            }
        }
        return result;
    }

    /**
     * 根据类名找到匹配的第一个{@link View}
     * 
     * @param className
     *            需要查找的类名
     * @param index
     *            查找第几个,从0开始进行索引,小于0时使用0
     * @param fuzzy
     *            是否模糊查询
     * @return {@code null} 当没有找到匹配的类名时返回
     */
    public View getViewByClassName(String className, int index, boolean fuzzy) {
        index = index < 0 ? 0 : index;
        ArrayList<View> views = getViewsByClassName(className, fuzzy);
        if (index < views.size()) {
            return views.get(index);
        } else {
            return null;
        }
    }

    /**
     * 等待通过类名找到的View不为空
     * 
     * @param className
     *            需要查找的类名
     * @param fuzzy
     *            是否模糊查询
     * @param timeout
     *            超时时间
     * @return
     */
    public boolean waitForViewByClassName(String className, boolean fuzzy, int timeout) {
        timeout = timeout < 1000 ? 1000 : timeout;
        long endTime = SystemClock.uptimeMillis() + timeout;
        while (endTime > SystemClock.uptimeMillis()) {
            if (getViewByClassName(className, 0, fuzzy) != null) {
                return true;
            }
            solo.sleep(1000);
        }
        return false;
    }

    /**
     * 通过文本获取对应的View
     * 
     * @param text
     *            文本,RID或原始文本
     * @param index
     *            rId的索引,小于0表示为原始文本
     * @param equal
     *            是否完全相等
     * @param visible
     *            是否可见
     * @return
     */
    private View getViewByText(String text, int index, boolean equal, boolean visible) {
        solo.sleep(500);
        ArrayList<View> views = solo.getViews();
        String realText = "";
        for (View view : views) {
            if (visible && !view.isShown()) {
                continue;
            }
            if (!(view instanceof TextView)) {
                continue;
            }
            String textView = ((TextView) view).getText().toString();
            if (index < 0) {
                realText = text;
            } else {
                realText = getTextByRId(text, index);
            }
            if (equal) {
                if (textView.equals(realText)) {
                    return view;
                }
            } else {
                if (textView.contains(realText)) {
                    return view;
                }
            }
        }
        return null;
    }

    /**
     * 通过文本获取第一个对应的View
     * 
     * @param text
     *            文本,RID或原始文本
     * @param index
     *            rId的索引,小于0表示为原始文本
     * @param equal
     *            是否需要完全匹配
     * @param visible
     *            是否需要可见
     * @param scroll
     *            是否需要滑动
     * @return
     */
    public View getViewByText(String text, int index, boolean equal, boolean visible, boolean scroll) {
        do {
            View view = getViewByText(text, index, equal, visible);
            if (view != null) {
                return view;
            }
        } while (scroll && isScroll(null, 1));
        return null;
    }

    /**
     * 搜索指定文本是否存在
     * 
     * @param text
     *            文本,RID或原始文本
     * @param index
     *            rId的索引,小于0表示为原始文本
     * @param equal
     *            是否需要完全匹配
     * @param visible
     *            是否需要可见
     * @param scroll
     *            是否需要滑动
     * @return
     */
    public boolean searchText(String text, int index, boolean equal, boolean visible, boolean scroll) {
        View view = getViewByText(text, index, equal, visible, scroll);
        return view == null ? false : true;
    }

    /**
     * 等待指定的文本出现
     * 
     * @param text
     *            文本,RID或原始文本
     * @param index
     *            rId的索引,小于0表示为原始文本
     * @param equal
     *            是否需要完全匹配
     * @param visible
     *            是否需要可见
     * @param timeout
     *            超时时间
     * @param scroll
     *            是否需要滑动
     * @return
     */
    public boolean waitForText(String text, int index, boolean equal, boolean visible,
            long timeout, boolean scroll) {
        timeout = timeout < 1000 ? 1000 : timeout;
        long endTime = SystemClock.uptimeMillis() + timeout;
        while (endTime > SystemClock.uptimeMillis()) {
            boolean flag = searchText(text, index, equal, visible, scroll);
            if (flag) {
                return true;
            }
            javaUtils.sleep(1000);
        }
        return false;
    }

    /**
     * 等待指定文本消失
     * 
     * @param text
     *            文本,RID或原始文本
     * @param index
     *            rId的索引,小于0表示为原始文本
     * @param equal
     *            是否需要完全匹配
     * @param visible
     *            是否需要可见
     * @param timeout
     *            超时时间
     * @return
     */
    public boolean waitForTextDismiss(String text, int index, boolean equal, boolean visible,
            long timeout) {
        timeout = timeout < 1000 ? 1000 : timeout;
        long endTime = SystemClock.uptimeMillis() + timeout;
        while (endTime > SystemClock.uptimeMillis()) {
            boolean flag = searchText(text, index, equal, visible, false);
            if (!flag) {
                return true;
            }
            javaUtils.sleep(1000);
        }
        return false;
    }

    /**
     * 通过ID来搜索{@link View}
     * 
     * @param _id
     * @param visible
     * @return
     */
    public boolean searchViewById(String _id, boolean visible) {
        solo.sleep(500);
        ArrayList<View> views = solo.getViews();
        int id = resources.getIdentifier(_id, "id", packageName);
        for (View view : views) {
            if (view instanceof View) {
                if (visible) {
                    if (!view.isShown()) {
                        continue;
                    }
                }
                if (view.getId() == id) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取指定{@link View}索引列表下的{@link View}
     * 
     * @param parent
     *            如果为{@code null}则返回{@code null}
     * @param indexlist
     *            索引列表
     * @return 如果索引没有找到则返回为{@code null}
     */
    public View getViewByIndex(View parent, int[] indexlist) {
        View view = parent;
        if (view == null) {
            return null;
        }
        for (int i = 0; i < indexlist.length; i++) {
            int index = indexlist[i];
            if (!(view instanceof ViewGroup)) {
                return null;
            }
            ViewGroup viewGroup = (ViewGroup) view;
            if (viewGroup.getChildCount() <= index) {
                return null;
            }
            view = viewGroup.getChildAt(index);
        }
        return view;
    }

    /**
     * 获取指定View的ID下的某个子控件
     * 
     * @param id
     * @param index
     * @return
     */
    public View getViewByIndex(String id, int index) {
        return getViewByIndex(getView(null, id), new int[] { index });
    }

    /**
     * 点击匹配到的第一个指定id的{@link View}
     * 
     * @param viewId
     *            {@link View} 的id
     */
    public void clickOnView(String viewId) {
        View view = solo.getView(viewId);
        solo.clickOnView(view);
    }

    /**
     * 点击特定界面上的特定文本
     * <p>
     * 默认点击可见的文本,必要时会滑动
     * 
     * @param text
     *            文本,RID或原始文本
     * @param index
     *            rId的索引,小于0表示为原始文本
     * @param equal
     *            是否需要完全匹配
     */
    public void clickOnText(String text, int index, boolean equal) {
        javaUtils.sleep(500);
        View view = getViewByText(text, index, equal, true, true);
        solo.clickOnView(view);
        javaUtils.sleep(500);
    }

    /**
     * 点击Web元素
     * 
     * @param by
     *            如何查找web元素
     * @param scroll
     *            是否需要滚动查找(滚动很耗时和性能)
     */
    public void clickOnWebElement(By by, boolean scroll) {
        long endTime = SystemClock.uptimeMillis() + 5 * 60 * 1000;
        WebElement webElement;
        while (endTime > SystemClock.uptimeMillis()) {
            webElement = solo.getWebElement(by, 0);
            if (webElement != null) {
                int[] location = new int[2];
                webElement.getLocationOnScreen(location);
            }
            if (webElement != null && isInsideDisplay(webElement, false)) {
                solo.clickOnWebElement(by);
                return;
            }
            View webView = getViewByClassName("WebView", 0, true);
            if (scroll && !isScroll(webView, 1)) {
                return;
            }
        }
        return;
    }

    /**
     * 判断软键盘是否弹出，并且隐藏软键盘
     * 
     * @param editTextViewId
     *            文本框的id
     * @return
     */
    public boolean hideSoftKeyboard(final TextView textView) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        return imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
    }

    /**
     * 设置文本框中的内容
     * 
     * @param textView
     *            需要是一个{@link TextView}的实例,否则将报错
     * @param text
     *            设置的内容,{@code ""} 表示设置空值
     */
    public void setText(final TextView textView, final String text) {
        Assert.assertTrue("不能为空", textView != null);
        inst.runOnMainSync(new Runnable() {

            @Override
            public void run() {
                hideSoftKeyboard(textView);
                ((TextView) textView).setText(text);
            }
        });
    }
}