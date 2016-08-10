package com.adolphin.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import com.android.common.Utils;
import com.robotium.solo.Solo;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageUtil {
    private Solo solo;
    private Bitmap result = null;
    private View screenShot;

    public ImageUtil(Solo solo) {
        this.solo = solo;
    }

    /**
     * 从bitmap中获取pixels
     * 
     * @param bitmap
     * @param isDiagonal
     *            是否获取对角线的pixels
     * @return
     */
    private int[] getPixelsFromBitmap(Bitmap bitmap, boolean isDiagonal) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels;
        if (isDiagonal) {
            int max = width < height ? width : height;
            pixels = new int[max];
            for (int i = 0; i < max; i++) {
                pixels[i] = bitmap.getPixel(i, i);
            }
        } else {
            pixels = new int[width * height];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        }
        return pixels;
    }

    /**
     * 获取{@link View} 的{@link Bitmap}
     * 
     * @param view
     * @return
     */
    public Bitmap getBitmapFromView(final View view) {
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                if (view == null) {
                    screenShot = solo.getViews().get(0);
                } else {
                    screenShot = view;
                }
                screenShot.setDrawingCacheEnabled(true);
                Bitmap bitmap = screenShot.getDrawingCache();
                if (bitmap == null) {
                    return;
                }
                Bitmap.Config config = bitmap.getConfig();
                if (config == null) {
                    config = Bitmap.Config.ARGB_8888;
                }
                result = bitmap.copy(bitmap.getConfig(), false);
                screenShot.setDrawingCacheEnabled(false);
            }
        };
        solo.getInstrumentation().runOnMainSync(runnable);
        return result;
    }

    /**
     * 比较两个Bitmap是否一样
     * 
     * @param bitmap1
     * @param bitmap2
     * @param isDiagonal
     *            是否采用比较对角线
     * @return
     */
    public boolean compareBitmap(Bitmap bitmap1, Bitmap bitmap2, boolean isDiagonal) {
        int[] pixels1 = getPixelsFromBitmap(bitmap1, isDiagonal);
        int[] pixels2 = getPixelsFromBitmap(bitmap2, isDiagonal);
        return Arrays.equals(pixels1, pixels2);
    }

    /**
     * 获取设置图像的{@link Bitmap}
     * 
     * @param view
     *            设置图像的{@link View}
     * @param runnable
     *            运行设置的{@link Runnable}方法
     * @return
     */
    private Bitmap getBitmapBySetImage(View view, Runnable runnable) {
        solo.getInstrumentation().runOnMainSync(runnable);
        // TODO 等待Runnable运行完成, 该方式不是太好, 需要寻找更好的方式
        solo.sleep(5 * 1000);
        return getBitmapFromView(view);
    }

    /**
     * 得到设置{@link TextView} 的{@link Runnable}
     * 
     * @param textView
     * @param direction
     *            {@link TextView}中图像设置的方向, {@code 1}表示左边,{@code 2}表示上边,{@code 3}
     *            表示右边,{@code 4}表示下边
     * @param rId
     *            图像的资源ID, 来自于Dolphin的R文件
     * @return
     */
    private Runnable getRunnableForSetTextView(final TextView textView, final int direction,
            final int rId) {
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                Drawable drawable = solo.getInstrumentation().getTargetContext().getResources()
                        .getDrawable(rId);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                switch (direction) {
                case 1:
                    textView.setCompoundDrawables(drawable, null, null, null);
                    break;
                case 2:
                    textView.setCompoundDrawables(null, drawable, null, null);
                    break;
                case 3:
                    textView.setCompoundDrawables(null, null, drawable, null);
                    break;
                case 4:
                    textView.setCompoundDrawables(null, null, null, drawable);
                    break;
                }
            }
        };
        return runnable;
    }

    /**
     * 设置一个imageview中的图像,这里有两种方式 <br>
     * 1.图片不在resource中,而是在Assert文件夹中<br>
     * 2.图片在Resource中:<br>
     * a.获取rId的Drawable,之后设置到ImageView,使用setImageDrawable <br>
     * b.直接使用rId来设置一个ImageView,使用setImageResource<br>
     * <p>
     * 三种方式,2中两种方式的图片缩放方式不同,使用isDrawable来控制
     * 
     * @param imageView
     * @param isDrawable
     *            设置方式是否是上述的第二种中的a方法
     * @param rId
     *            资源ID,这里需要将被测对象的最新R文件复制到测试工程中
     * @param assertPath
     *            如果不是Assert中,使用{@code null},否则将使用Assert指定的路径
     * @return
     */
    private Runnable getRunnableForSetImageView(final ImageView imageView,
            final boolean isDrawable, final int rId, final String assertPath) {
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                Resources resources = solo.getInstrumentation().getTargetContext().getResources();
                if (assertPath != null) {
                    try {
                        InputStream is = resources.getAssets().open(assertPath);
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        imageView.setImageBitmap(bitmap);
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (isDrawable) {
                        Drawable drawable = resources.getDrawable(rId);
                        imageView.setImageDrawable(drawable);
                    } else {
                        imageView.setImageResource(rId);
                    }
                }
            }
        };
        return runnable;
    }

    /**
     * 判断{@link ImageView}中的图像是否和指定的图像完全一样
     * 
     * @param imageView
     * @param isDrawable
     *            设置方式是否是{@link Drawable}
     * @param rId
     *            资源ID,这里需要将被测对象的最新R文件复制到测试工程中
     * @param assertPath
     *            是否是使用的Assert目录中的图像, 如果不使用,请使用{@code null}
     * @return
     */
    public boolean compareImageView(ImageView imageView, boolean isDrawable, int rId,
            String assertPath) {
        Bitmap bitmap = getBitmapFromView(imageView);
        int[] pixels1 = getPixelsFromBitmap(bitmap, false);
        Bitmap bitmap2 = getBitmapBySetImage(imageView,
                getRunnableForSetImageView(imageView, isDrawable, rId, assertPath));
        int[] pixels2 = getPixelsFromBitmap(bitmap2, false);
        return Arrays.equals(pixels1, pixels2);
    }

    /**
     * 判断{@link TextView}中的图像是否和指定的资源图像完全一样
     * 
     * @param textView
     * @param direction
     *            {@link TextView}中图像设置的方向, {@code 1}表示左边,{@code 2}表示上边,{@code 3}
     *            表示右边,{@code 4}表示下边
     * @param rId
     *            图像对应的资源ID
     * @return
     */
    public boolean compareTextView(TextView textView, int direction, int rId) {
        Bitmap bitmap1 = getBitmapFromView(textView);
        int[] pixels1 = getPixelsFromBitmap(bitmap1, false);
        Bitmap bitmap2 = getBitmapBySetImage(textView,
                getRunnableForSetTextView(textView, direction, rId));
        int[] pixels2 = getPixelsFromBitmap(bitmap2, false);
        return Arrays.equals(pixels1, pixels2);
    }

    /**
     * 以PNG格式保存一个Bitmap图片到外置存储器上
     * 
     * @param bitmapName
     *            保存的图片名
     * @param bitmap
     *            传入的Bitmap
     * @return 返回保存是否成功
     */
    public boolean saveBitmap(String bitmapName, Bitmap bitmap) {
        boolean flag = false;
        final String sdcardPath = new Utils(null).externalStorageDirectory + "/";
        File file = new File(sdcardPath + bitmapName + ".png");
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            flag = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }
}
