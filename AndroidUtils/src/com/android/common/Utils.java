package com.android.common;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import junit.framework.Assert;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings.Secure;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

public class Utils {
    private Context context;
    private com.java.common.Utils utils = new com.java.common.Utils();
    public final int SDK = android.os.Build.VERSION.SDK_INT;
    public final DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
    public final String externalStorageDirectory = Environment.getExternalStorageDirectory()
            .getAbsolutePath();

    /**
     * 部分功能需要{@link Context}, 使用时如果不用到context可以传入{@code null}
     * 
     * @param context
     */
    public Utils(Context context) {
        this.context = context;
    }

    /**
     * 返回手机的AndroidID的MD5值
     * <p>
     * 需要在初始化时传入{@link Context}
     * 
     * @return
     */
    public String getAndroidIDForMD5() {
        return utils.MD5(Secure.getString(context.getContentResolver(), Secure.ANDROID_ID));
    }

    /**
     * 清除剪切板内容
     * <p>
     * 需要在初始化时传入{@link Context}
     */
    public void clearClipboard() {
        ClipboardManager cm = (ClipboardManager) context
                .getSystemService(Service.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(ClipDescription.MIMETYPE_TEXT_PLAIN, "");
        cm.setPrimaryClip(clip);
    }

    /**
     * 将dp转化为pixel
     * 
     * @param dip
     * @return
     */
    public int dipToPixel(float dip) {
        if (dip < 0) {
            return -(int) (-dip * displayMetrics.density + 0.5f);
        } else {
            return (int) (dip * displayMetrics.density + 0.5f);
        }
    }

    /**
     * 将pixel转化为dp
     * 
     * @param pixel
     * @return
     */
    public float pixelToDip(int pixel) {
        if (pixel < 0) {
            return -(-pixel - 0.5f) / displayMetrics.density;
        } else {
            return (pixel - 0.5f) / displayMetrics.density;
        }
    }

    /**
     * 将sp转化为pixel
     * 
     * @param value
     * @return
     */
    public int spToPixel(float value) {
        return (int) (value * displayMetrics.scaledDensity);
    }

    /**
     * 获取父{@link View}的所有子控件
     * 
     * @param parent
     * @param isReversal
     *            是否反转存放结果,{@code true} 表示反转存放
     * @return
     */
    public ArrayList<View> getChildren(View parent, boolean isReversal) {
        if (parent == null) {
            Assert.assertTrue("父目录不能为空", false);
        }
        ArrayList<View> views = new ArrayList<View>();
        if (parent instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) parent;
            int num = viewGroup.getChildCount();
            for (int i = 0; i < num; i++) {
                if (isReversal) {
                    views.add(viewGroup.getChildAt(num - i - 1));
                } else {
                    views.add(viewGroup.getChildAt(i));
                }
            }
        }
        return views;
    }

    /**
     * 得到指定{@link View}的父{@link View}
     * 
     * @param view
     * @param num
     *            寻找的父目录数
     * @return 如果获取不到则返回Null
     */
    public View getParent(View view, int num) {
        if (num < 1) {
            return view;
        }
        while (num-- > 0) {
            if (view.getParent() instanceof View) {
                view = (View) view.getParent();
            } else {
                view = null;
                break;
            }
        }
        return view;
    }

    /**
     * 获取View的中心点坐标
     * 
     * @param view
     * @return
     */
    public int[] getViewCenter(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        location[0] += view.getWidth() / 2;
        location[1] += view.getHeight() / 2;
        return location;
    }

    /**
     * 比较两个View的位置关系
     * 
     * @param one
     *            第一个{@link View}
     * @param two
     *            第二个{@link View}
     * @param landscape
     *            {@code true}:表示横向的左右关系,{@code false}:表示竖向的上下关系
     * @param aligning
     *            {@code true}:表示需要验证左对齐和上对齐关系
     * @param matrix
     *            {@code true}:表示需要验证长宽一致
     * @return {@code -1}:表示位置关系不正确, 其它值都表示位置关系正确,数值表示两个{@link View}之间的空隙长度
     */
    public int ubietyOfView(View one, View two, boolean landscape, boolean aligning, boolean matrix) {
        if (one == null || two == null) {
            Assert.assertTrue("传入的View不能为空", false);
        }
        int[] locationOne = new int[2];
        int[] locationTwo = new int[2];
        one.getLocationOnScreen(locationOne);
        two.getLocationOnScreen(locationTwo);
        int distance = 0;
        if (matrix) {
            if (one.getWidth() != two.getWidth() || one.getHeight() != two.getHeight()) {
                return -1;// 两个View的宽高不等
            }
        }
        if (landscape) {
            if (locationOne[0] > locationTwo[0]) {
                return -1;
            }
            if (aligning) {
                if (locationOne[1] != locationTwo[1]) {
                    return -1;// Y轴不等
                }
                distance = locationTwo[0] - locationOne[0] - one.getWidth();
                if (distance < 0) {
                    return -1;// 有重叠的部分
                }
            }
        } else {
            if (locationOne[1] > locationTwo[1]) {
                return -1;
            }
            if (aligning) {
                if (locationOne[0] != locationTwo[0]) {
                    return -1;// X轴不等
                }
                distance = locationTwo[1] - locationOne[1] - one.getHeight();
                if (distance < 0) {
                    return -1;// 有重叠的部分
                }
            }
        }
        return distance;
    }

    /**
     * 比较一组{@link View}的位置关系
     * 
     * @param views
     *            {@link View}列表
     * @param column
     *            表示列数<br>
     *            {@code 0}:表示验证数组里所有元素的横向左右位置关系<br>
     *            {@code 1}:表示验证数组里所有元素的纵向上下位置关系<br>
     *            {@code >1}多用于验证矩阵是否对齐,此时参数{@code aligning, matrix} 需要为
     *            {@code true}
     * @param aligning
     *            验证数组的左对齐和上对齐关系
     * @param matrix
     *            验证数组中元素的长宽
     * @param distance
     *            验证数组中元素横竖向的间距关系(横向和竖向的间距不需要相等)
     *            <p>
     *            例如: 要验证矩阵,请将所有{@code boolean}设为{@code true}<br>
     *            只要验证一组{@link View}的上下关系,设置{@code column} 为{@code 1},左右关系设置为
     *            {@code 0},其它设为{@code false}
     */
    public void ubietyOfViews(ArrayList<View> views, int column, boolean aligning, boolean matrix,
            boolean distance) {
        Assert.assertTrue("传入的数组为空或大小小于2", views != null && views.size() > 1);
        int num = views.size();
        // 获取数组大小
        int row = column < 1 ? 1 : (num / column + ((num % column) == 0 ? 0 : 1));
        column = column < 1 ? 1 : column;
        View[][] matrixViews = new View[row][column];
        // 将所有元素赋值给数组
        int point = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (point < num) {
                    matrixViews[i][j] = views.get(point);
                    point++;
                }
            }
        }
        // 判断是否对齐
        String assertString = "第%s行%s列的元素和第%s行%s列的元素位置关系不正确";
        // 验证元素横向对齐
        int originDistance = -1;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column - 1; j++) {
                if (matrixViews[i][j + 1] == null) {
                    break;// View为空时不在验证
                }
                int currentDistance = ubietyOfView(matrixViews[i][j], matrixViews[i][j + 1], true,
                        aligning, matrix);
                Assert.assertTrue(String.format(assertString, i + 1, j + 1, i + 1, j + 2),
                        currentDistance != -1);
                if (distance) {
                    if (originDistance == -1) {// 第一次求得间距时赋值
                        originDistance = currentDistance;
                    } else {// 验证View之间的间距
                        Assert.assertTrue(String.format(assertString, i + 1, j + 1, i + 1, j + 2),
                                currentDistance == originDistance);
                    }
                }
            }
        }
        // 验证元素竖向对齐
        originDistance = -1;
        for (int i = 0; i < row - 1; i++) {
            for (int j = 0; j < column; j++) {
                if (matrixViews[i + 1][j] == null) {
                    break;// View为空时不在验证
                }
                int currentDistance = ubietyOfView(matrixViews[i][j], matrixViews[i + 1][j], false,
                        aligning, matrix);
                Assert.assertTrue(String.format(assertString, i + 1, j + 1, i + 2, j + 1),
                        currentDistance != -1);
                if (distance) {
                    if (originDistance == -1) {// 第一次求得间距时赋值
                        originDistance = currentDistance;
                    } else {// 验证View之间的间距
                        Assert.assertTrue(String.format(assertString, i + 1, j + 1, i + 2, j + 1),
                                currentDistance == originDistance);
                    }
                }
            }
        }
    }

    /**
     * 返回活动的块大小和块数量
     * 
     * @return
     */
    @SuppressWarnings("deprecation")
    public long[] getSDAvailableSize() {
        StatFs stat = new StatFs(externalStorageDirectory);
        long blockSize = 0;
        long availableBlocks = 0;
        if (SDK > 18) {
            blockSize = stat.getBlockSizeLong();
            availableBlocks = stat.getAvailableBlocksLong();
        } else {
            blockSize = stat.getBlockSize();
            availableBlocks = stat.getAvailableBlocks();
        }
        return new long[] { blockSize, availableBlocks };
    }

    /**
     * 根据sdcard要求的剩余空间,创建文件占满sdcard
     * 
     * @param residualSpace
     *            剩余空间,表示要求剩余的空间需要小于该值,大于该值的1/10,目前只支持以MB为单位
     * @param filePath
     *            堆积文件的目录
     */
    public void cramSD(int residualSpace, String filePath) {
        utils.createFileOrDir(filePath, true, false);
        long[] sdAvailableSize = getSDAvailableSize();
        byte[] buffer = new byte[(int) sdAvailableSize[0]];
        long space = 64 * 1024 * 100 * 100;
        while (true) {
            sdAvailableSize = getSDAvailableSize();
            long size = sdAvailableSize[0] * sdAvailableSize[1];
            if (size < residualSpace * 1024 * 1024) {
                break;
            }
            if (size < 1.2f * space) {
                space = space / 100;
            }
            if (space < 64 * 1024) {
                break;
            }
            utils.writeFile(buffer, filePath + File.separator + new Random().nextInt(), space
                    / sdAvailableSize[0], false);
        }
    }
}