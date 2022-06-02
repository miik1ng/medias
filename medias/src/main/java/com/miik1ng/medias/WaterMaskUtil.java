package com.miik1ng.medias;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Mi on 2022/6/2
 */
public class WaterMaskUtil {
    /**
     * 设置水印图片在左上角
     * @param context
     * @param src
     * @param waterMark
     * @param paddingLeft
     * @param paddingTop
     * @return
     */
    public static Bitmap createWaterMaskLeftTop(Context context, Bitmap src, Bitmap waterMark, int paddingLeft, int paddingTop) {
        return createWaterMaskBitmap(src, waterMark, dp2px(context, paddingLeft), dp2px(context, paddingTop));
    }

    /**
     * 设置水印图片在右下角
     * @param context
     * @param src
     * @param waterMark
     * @param paddingRight
     * @param paddingBottom
     * @return
     */
    public static Bitmap createWaterMaskRightBottom(Context context, Bitmap src, Bitmap waterMark, int paddingRight, int paddingBottom) {
        return createWaterMaskBitmap(src, waterMark, src.getWidth() - waterMark.getWidth() - dp2px(context, paddingRight), src.getHeight() - waterMark.getHeight() - dp2px(context, paddingBottom));
    }

    /**
     * 设置水印图片到右上角
     * @param context
     * @param src
     * @param waterMark
     * @param paddingRight
     * @param paddingTop
     * @return
     */
    public static Bitmap createWaterMaskRightTop(Context context, Bitmap src, Bitmap waterMark, int paddingRight, int paddingTop) {
        return createWaterMaskBitmap(src, waterMark, src.getWidth() - waterMark.getWidth() - dp2px(context, paddingRight), dp2px(context, paddingTop));
    }

    /**
     * 设置水印图片到左下角
     * @param context
     * @param src
     * @param waterMark
     * @param paddingLeft
     * @param paddingBottom
     * @return
     */
    public static Bitmap createWaterMaskLeftBottom(Context context, Bitmap src, Bitmap waterMark, int paddingLeft, int paddingBottom) {
        return createWaterMaskBitmap(src, waterMark, dp2px(context, paddingLeft), src.getHeight() - waterMark.getHeight() - dp2px(context, paddingBottom));
    }

    /**
     * 设置水印图片到中间
     * @param src
     * @param waterMark
     * @return
     */
    public static Bitmap createWaterMaskCenter(Bitmap src, Bitmap waterMark) {
        return createWaterMaskBitmap(src, waterMark, (src.getWidth() - waterMark.getWidth()) / 2, (src.getHeight() - waterMark.getHeight()) / 2);
    }

    /**
     * 添加文字水印到左上角
     * @param context
     * @param bitmap
     * @param text
     * @param size
     * @param color
     * @param paddingLeft
     * @param paddingTop
     * @return
     */
    public static Bitmap drawTextToLeftTop(Context context, Bitmap bitmap, String text, int size, int color, int paddingLeft, int paddingTop) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds, dp2px(context, paddingLeft), dp2px(context, paddingTop) + bounds.height());
    }

    /**
     * 添加文字水印到右下角
     * @param context
     * @param bitmap
     * @param text
     * @param size
     * @param color
     * @param paddingRight
     * @param paddingBottom
     * @return
     */
    public static Bitmap drawTextToRightBottom(Context context, Bitmap bitmap, String text, int size, int color, int paddingRight, int paddingBottom) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds, bitmap.getWidth() - bounds.width() - dp2px(context, paddingRight), bitmap.getHeight() - dp2px(context, paddingBottom));
    }

    /**
     * 添加文字水印到右上角
     * @param context
     * @param bitmap
     * @param text
     * @param size
     * @param color
     * @param paddingRight
     * @param paddingTop
     * @return
     */
    public static Bitmap drawTextToRightTop(Context context, Bitmap bitmap, String text, int size, int color, int paddingRight, int paddingTop) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds, bitmap.getWidth() - bounds.width() - dp2px(context, paddingRight), dp2px(context, paddingTop) + bounds.height());
    }

    /**
     * 添加文字水印到左下角
     * @param context
     * @param bitmap
     * @param text
     * @param size
     * @param color
     * @param paddingLeft
     * @param paddingBottom
     * @return
     */
    public static Bitmap drawTextToLeftBottom(Context context, Bitmap bitmap, String text, int size, int color, int paddingLeft, int paddingBottom) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds, dp2px(context, paddingLeft), bitmap.getHeight() - dp2px(context, paddingBottom + 2));
    }

    /**
     * 添加文字水印到中间
     * @param context
     * @param bitmap
     * @param text
     * @param size
     * @param color
     * @return
     */
    public static Bitmap drawTextToCenter(Context context, Bitmap bitmap, String text, int size, int color) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds, (bitmap.getWidth() - bounds.width()) / 2, (bitmap.getHeight() + bounds.height()) / 2);
    }

    /**
     * 添加水印图片
     * @param src
     * @param waterMark
     * @param paddingLeft
     * @param paddingTop
     * @return
     */
    private static Bitmap createWaterMaskBitmap(Bitmap src, Bitmap waterMark, int paddingLeft, int paddingTop) {
        if (src == null) {
            return null;
        }
        int width = src.getWidth();
        int height = src.getHeight();
        //创建一个bitmap
        Bitmap newb = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);//创建一个新的和src长度宽度一样的位图
        //将该图片作为画布
        Canvas canvas = new Canvas(newb);
        //在画布0，0坐标上开始绘制原始图片
        canvas.drawBitmap(src, 0, 0, null);
        //在画布上绘制水印图片
        canvas.drawBitmap(waterMark, paddingLeft, paddingTop, null);
        //保存
        canvas.save();
        //存储
        canvas.restore();
        return newb;
    }

    /**
     * 图片上绘制文字
     * @param context
     * @param bitmap
     * @param text
     * @param paint
     * @param bounds
     * @param paddingLeft
     * @param paddingTop
     * @return
     */
    private static Bitmap drawTextToBitmap(Context context, Bitmap bitmap, String text, Paint paint, Rect bounds, int paddingLeft, int paddingTop) {
        Bitmap.Config bitmapConfig = bitmap.getConfig();

        paint.setDither(false);
        paint.setFilterBitmap(false);
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);
        drawMultiLineText(text, paddingLeft, paddingTop, paint, canvas);
        return bitmap;
    }

    private static void drawMultiLineText(String str, float x, float y, Paint paint, Canvas canvas) {
        String[] lines = str.split("\n");
        float txtSize = -paint.ascent() + paint.descent();

        if (paint.getStyle() == Paint.Style.FILL_AND_STROKE || paint.getStyle() == Paint.Style.STROKE) {
            txtSize += paint.getStrokeWidth();
        }
        float lineSpace = txtSize * 0.1f;
        for (int i = 0; i < lines.length; ++i) {
            canvas.drawText(lines[i], x, y - (txtSize + lineSpace) * (lines.length - 1 - i), paint);
        }
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored", "TryWithIdenticalCatches"})
    public static void saveBitmap(Context context, Bitmap bitmap, String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
