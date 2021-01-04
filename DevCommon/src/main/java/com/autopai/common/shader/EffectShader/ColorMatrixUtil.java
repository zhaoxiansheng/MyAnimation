package com.autopai.common.shader.EffectShader;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.widget.ImageView;

/**
 * 收集的颜色滤镜
 */
public class ColorMatrixUtil {

    /**
     * 为imageView设置颜色滤镜
     *
     * @param imageView
     * @param colormatrix
     */
    public static void imageViewColorFilter(ImageView imageView, float[] colormatrix) {
        setColorMatrixColorFilter(imageView, new ColorMatrixColorFilter(new ColorMatrix(colormatrix)));
    }

    /**
     * 为imageView设置颜色偏向滤镜
     *
     * @param imageView
     * @param color
     */
    public static void imageViewColorFilter(ImageView imageView, int color) {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setScale(Color.alpha(color), Color.red(color), Color.green(color), Color.blue(color));
        setColorMatrixColorFilter(imageView, new ColorMatrixColorFilter(colorMatrix));
    }


    /**
     * 生成对应颜色偏向滤镜的图片，并回收原图
     *
     * @param bitmap
     * @param color
     * @return
     */
    public static Bitmap bitmapColorFilter(Bitmap bitmap, int color) {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setScale(Color.alpha(color), Color.red(color), Color.green(color), Color.blue(color));
        return setColorMatrixColorFilter(bitmap, new ColorMatrixColorFilter(colorMatrix), true);
    }

    /**
     * 生成对应颜色滤镜的图片，并回收原图
     *
     * @param bitmap
     * @param colormatrix
     * @return
     */
    public static Bitmap bitmapColorFilter(Bitmap bitmap, float[] colormatrix) {
        return setColorMatrix(bitmap, colormatrix, true);
    }

    /**
     * 生成对应颜色滤镜的图片
     *
     * @param bitmap
     * @param colormatrix
     * @param isRecycle
     * @return
     */
    public static Bitmap setColorMatrix(Bitmap bitmap, float[] colormatrix, boolean isRecycle) {
        return setColorMatrixColorFilter(bitmap, new ColorMatrixColorFilter(new ColorMatrix(colormatrix)), isRecycle);
    }


    public static void setColorMatrixColorFilter(ImageView imageView, ColorMatrixColorFilter matrixColorFilter) {
        imageView.setColorFilter(matrixColorFilter);
    }

    public static Bitmap setColorMatrixColorFilter(Bitmap bitmap, ColorMatrixColorFilter matrixColorFilter, boolean isRecycle) {
        Bitmap resource = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColorFilter(matrixColorFilter);
        Canvas canvas = new Canvas(resource);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return resource;
    }

    // 红色通道 RED
    public static final float colormatrix_red[] = {
            1,0,0,0,0,   //red
            0,0,0,0,0,   //green
            0,0,0,0,0,   //blue
            0,0,0,1,0    //alpha
    };
    // 红色通道 GREEN
    public static final float colormatrix_green[] = {
            0,0,0,0,0,   //red
            0,1,0,0,0,   //green
            0,0,0,0,0,   //blue
            0,0,0,1,0    //alpha
    };
    // 红色通道 BLUE
    public static final float colormatrix_blue[] = {
            0,0,0,0,0,   //red
            0,0,0,0,0,   //green
            0,0,1,0,0,   //blue
            0,0,0,1,0    //alpha
    };
    // 黑白 GREY
    public static final float colormatrix_grey[] = {
            0.8f, 1.6f, 0.2f, 0, -163.9f,
            0.8f, 1.6f, 0.2f, 0, -163.9f,
            0.8f, 1.6f, 0.2f, 0, -163.9f,
            0, 0, 0, 1.0f, 0};
    // 怀旧 CLASSICAL
    public static final float colormatrix_classical[] = {
            0.2f, 0.5f, 0.1f, 0, 40.8f,
            0.2f, 0.5f, 0.1f, 0, 40.8f,
            0.2f, 0.5f, 0.1f, 0, 40.8f,
            0, 0, 0, 1, 0};
    // 哥特 GONTHIC
    public static final float colormatrix_gonthic[] = {
            1.9f, -0.3f, -0.2f, 0, -87.0f,
            -0.2f, 1.7f, -0.1f, 0, -87.0f,
            -0.1f, -0.6f, 2.0f, 0, -87.0f,
            0, 0, 0, 1.0f, 0};
    // 淡雅 ELEGANT
    public static final float colormatrix_elegant[] = {
            0.6f, 0.3f, 0.1f, 0, 73.3f,
            0.2f, 0.7f, 0.1f, 0, 73.3f,
            0.2f, 0.3f, 0.4f, 0, 73.3f,
            0, 0, 0, 1.0f, 0};
    // 蓝调 BLUES
    public static final float colormatrix_blues[] = {
            2.1f, -1.4f, 0.6f, 0.0f, -71.0f,
            -0.3f, 2.0f, -0.3f, 0.0f, -71.0f,
            -1.1f, -0.2f, 2.6f, 0.0f, -71.0f,
            0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
    // 光晕 HALO
    public static final float colormatrix_halo[] = {
            0.9f, 0, 0, 0, 64.9f,
            0, 0.9f, 0, 0, 64.9f,
            0, 0, 0.9f, 0, 64.9f,
            0, 0, 0, 1.0f, 0};
    // 梦幻 FANTASY
    public static final float colormatrix_fantasy[] = {
            0.8f, 0.3f, 0.1f, 0.0f, 46.5f,
            0.1f, 0.9f, 0.0f, 0.0f, 46.5f,
            0.1f, 0.3f, 0.7f, 0.0f, 46.5f,
            0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
    // 酒红 CLARET
    public static final float colormatrix_claret[] = {
            1.2f, 0.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 0.9f, 0.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.8f, 0.0f, 0.0f,
            0, 0, 0, 1.0f, 0};
    // 胶片 FILM
    public static final float colormatrix_film[] = {
            -1.0f, 0.0f, 0.0f, 0.0f, 255.0f,
            0.0f, -1.0f, 0.0f, 0.0f, 255.0f,
            0.0f, 0.0f, -1.0f, 0.0f, 255.0f,
            0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
    // 湖光掠影 RIPPLES_SPARKLES
    public static final float colormatrix_ripples[] = {
            0.8f, 0.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.9f, 0.0f, 0.0f,
            0, 0, 0, 1.0f, 0};
    // 褐片 BROWN
    public static final float colormatrix_brown[] = {
            1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 0.8f, 0.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.8f, 0.0f, 0.0f,
            0, 0, 0, 1.0f, 0};
    // 复古 ANCIENT
    public static final float colormatrix_ancient[] = {
            0.9f, 0.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 0.8f, 0.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.5f, 0.0f, 0.0f,
            0, 0, 0, 1.0f, 0};
    // 泛黄 YELLOWING
    public static final float colormatrix_yellowing[] = {
            1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.5f, 0.0f, 0.0f,
            0, 0, 0, 1.0f, 0};
    // 传统 TRADITION
    public static final float colormatrix_tradition[] = {
            1.0f, 0.0f, 0.0f, 0, -10f,
            0.0f, 1.0f, 0.0f, 0, -10f,
            0.0f, 0.0f, 1.0f, 0, -10f,
            0, 0, 0, 1, 0};
    // 胶片2 FILM2
    public static final float colormatrix_film2[] = {
            0.71f, 0.2f, 0.0f, 0.0f, 60.0f,
            0.0f, 0.94f, 0.0f, 0.0f, 60.0f,
            0.0f, 0.0f, 0.62f, 0.0f, 60.0f,
            0, 0, 0, 1.0f, 0};

    // 锐色 SHARP
    public static final float colormatrix_sharp[] = {
            4.8f, -1.0f, -0.1f, 0, -388.4f,
            -0.5f, 4.4f, -0.1f, 0, -388.4f,
            -0.5f, -1.0f, 5.2f, 0, -388.4f,
            0, 0, 0, 1.0f, 0};
    // 清宁 PEACE
    public static final float colormatrix_peace[] = {
            0.9f, 0, 0, 0, 0,
            0, 1.1f, 0, 0, 0,
            0, 0, 0.9f, 0, 0,
            0, 0, 0, 1.0f, 0};
    // 浪漫 ROMANTIC
    public static final float colormatrix_romantic[] = {
            0.9f, 0, 0, 0, 63.0f,
            0, 0.9f, 0, 0, 63.0f,
            0, 0, 0.9f, 0, 63.0f,
            0, 0, 0, 1.0f, 0};
    // 夜色 DIM
    public static final float colormatrix_dim[] = {
            1.0f, 0.0f, 0.0f, 0.0f, -66.6f,
            0.0f, 1.1f, 0.0f, 0.0f, -66.6f,
            0.0f, 0.0f, 1.0f, 0.0f, -66.6f,
            0.0f, 0.0f, 0.0f, 1.0f, 0.0f};


}
