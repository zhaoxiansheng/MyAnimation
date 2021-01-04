package com.autopai.common.utils.image;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.ThumbnailUtils;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Image Bitmap tool class
 *
 */
public class ImageUtil {
    private static final String TAG = "ImageUtil";

    /**
     * 渐变颜色背景
     *
     * @param orientation
     * @param color
     * @return
     */
    public static GradientDrawable getGradientDrawable(GradientDrawable.Orientation orientation, int[] color) {
        return new GradientDrawable(orientation, color);
    }

    public static Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);

        return mutableBitmap;
    }

    /**
     * zoom Bitmap to the targetWidth and targetWidth
     *
     * @param targetWidth
     * @param targetHeight
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int targetWidth, int targetHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) targetWidth / width);
        float scaleHeight = ((float) targetHeight / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return newBitmap;
    }

    /**
     * Converts Drawable to Bitmap
     *
     * @param drawable
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;

    }

    /**
     * get a Image bitmap of a rounded Angle
     *
     * @param context
     * @param ResourceId
     * @param cornerRadius
     */
    public static Bitmap getImageRoundBitmap(Context context, int ResourceId, float cornerRadius) {
        Log.i(TAG, "getImageRoundedBitmap: ResourceId == " + ResourceId +
                "\nroundPx == " + cornerRadius);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), ResourceId);
//        Bitmap zoomBitmap = ImageUtil.zoomBitmap(bitmap, 200, 200);
        return getRoundCornerBitmap(bitmap, cornerRadius);

    }


    /**
     * get a Image bitmap of a bitmap
     *
     * @param bitmap
     * @param cornerRadius
     */
    public static Bitmap getRoundCornerBitmap(Bitmap bitmap, float cornerRadius) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * Get a image bitmap with image reflections
     *
     * @param bitmap
     */
    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        final int reflectionGap = 4;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(bitmap,
                0, height / 2, width, height / 2, matrix, false);

        Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 2), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint defaultPaint = new Paint();
        canvas.drawRect(0, height, width, height + reflectionGap,
                defaultPaint);

        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0,
                bitmap.getHeight(), 0, bitmapWithReflection.getHeight()
                + reflectionGap, Color.WHITE, Color.WHITE, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);

        return bitmapWithReflection;
    }

    /**
     * add shadow to bitmap
     *
     * @param originalBitmap
     * @return
     */
    public static Bitmap drawImageDropShadow(Bitmap originalBitmap, int color) {

        BlurMaskFilter blurFilter = new BlurMaskFilter(1,
                BlurMaskFilter.Blur.NORMAL);
        Paint shadowPaint = new Paint();
        shadowPaint.setAlpha(50);
        shadowPaint.setColor(color);
        shadowPaint.setMaskFilter(blurFilter);

        int[] offsetXY = new int[2];
        Bitmap shadowBitmap = originalBitmap
                .extractAlpha(shadowPaint, offsetXY);

        Bitmap shadowImage32 = shadowBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas c = new Canvas(shadowImage32);
        c.drawBitmap(originalBitmap, offsetXY[0], offsetXY[1], null);

        return shadowImage32;
    }

    public static Bitmap addShadow(Bitmap bm) {
        int[] mBackShadowColors = new int[]{0x00000000, 0xB0AAAAAA};
        GradientDrawable mBackShadowDrawableLR = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, mBackShadowColors);
        mBackShadowDrawableLR.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        mBackShadowDrawableLR.setBounds(0, 0, 20, bm.getHeight());
        Canvas canvas = new Canvas(bm);
        mBackShadowDrawableLR.draw(canvas);
        return bm;
    }


    public static Bitmap getTransparentBitmap(Bitmap sourceImg, int number) {

        int[] argb = new int[sourceImg.getWidth() * sourceImg.getHeight()];
        sourceImg.getPixels(argb, 0, sourceImg.getWidth(), 0, 0,
                sourceImg.getWidth(), sourceImg.getHeight());// 获得图片的ARGB值

        number = number * 255 / 100;

        for (int i = 0; i < argb.length; i++) {
            argb[i] = (number << 24) | (argb[i] & 0x00FFFFFF);
        }
        sourceImg = Bitmap.createBitmap(argb, sourceImg.getWidth(),
                sourceImg.getHeight(), Bitmap.Config.ARGB_8888);
        return sourceImg;
    }

    /**
     * get Gray color Drawable
     *
     * @param context
     * @param resId
     * @return  GrayDrawable
     */
    public static Drawable getGrayDrawable(Context context, int resId) {
        Drawable drawable = context.getResources().getDrawable(resId);
        drawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        return drawable;
    }

    /**
     * get Low Saturation Bitmap
     *
     * @param context
     * @param resId
     * @return targetBitmap
     */
    public static Bitmap getLowSaturationBitmap(Context context, int resId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        int width = bitmap.getWidth(); // the bitmap's width
        int height = bitmap.getHeight(); // the bitmap's height
        int[] pixels = new int[width * height]; // Create pixel count groups
        //
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];
                // Separate the primary colors
                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);
                // Convert to grayscale pixels
                grey = (int) (red * 0.3 + green * 0.59 + blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
        }
        // new Bitmap
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        // set bitmap pixels
        newBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        //Use the ThumbnailUtils Scale the bitmap
        Bitmap targetBitmap = ThumbnailUtils.extractThumbnail(newBitmap, 460, 460);
        return targetBitmap;
    }

    /**
     * get Zero Saturation Drawable
     *
     * @param context
     * @param iconResId
     * @return  drawable
     */
    public static Drawable getZeroSaturationDrawable(Context context, int iconResId) {
        Drawable drawable = context.getResources().getDrawable(iconResId);
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        drawable.setColorFilter(new ColorMatrixColorFilter(matrix));
        return drawable;
    }

    // ywq: 2018/11/27 begin
    public static Bitmap getBitmapFromRes(Context context, int resId) {
        Bitmap newBitmap = null;
        try {
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
            opt.inPurgeable = true;
            opt.inInputShareable = true;
            // 获取资源图片
            InputStream is = context.getResources().openRawResource(resId);
            newBitmap = BitmapFactory.decodeStream(is, null, opt);
        }catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            return newBitmap;
        }
    }

    public static Bitmap getBitmapFromFile(Context context, String fileName)
    {
        Bitmap newBitmap = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inPurgeable = true;//允许可清除
            options.inInputShareable = true;// 以上options的两个属性必须联合使用才会有效果
            AssetManager am = context.getAssets();
            InputStream is = am.open(fileName);
            newBitmap = BitmapFactory.decodeStream(is, null, options);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            return newBitmap;
        }
    }

    public Drawable getDrawableFromBitmap(Context context, Bitmap bitmap)
    {
        if(bitmap == null)
            return null;
        return new BitmapDrawable(context.getResources(), bitmap);
    }
    // ywq: 2018/11/27 end

    //ImageUtil.saveBitmap(mLoadingBitmap, "/sdcard/DCIM/Camera/Shot_" + mLoadingTime + ".png");
    public static void saveBitmap(Bitmap bitmap,String fileName)
    {
        File file = new File(fileName);
        if(file.exists()){
            file.delete();
        }
        FileOutputStream out;
        try{
            out = new FileOutputStream(file);
            if(bitmap.compress(Bitmap.CompressFormat.PNG, 90, out))
            {
                out.flush();
                out.close();
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //draw Path to a bitmap, return this bitmap
    public static Bitmap pathToBitmap(final Path path, final int bitmapWidth, final int bitmapHeight){
        Bitmap bm = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);// alpha must 255

        canvas.drawPath(path, paint);
        return bm;
    }

    //corp srcBitmap, use region defined by path. return a corp bitmap
    public static Bitmap getBitmapWinthinPath(final Bitmap srcBitmap, final Path path){
        RectF pathBound = new RectF();
        path.computeBounds(pathBound, false);
        int width = (int)Math.ceil(pathBound.width());
        int height = (int)Math.ceil(pathBound.height());

        Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        //draw back color
        //canvas.drawColor(Color.GRAY);

        int sc = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            sc = canvas.saveLayer(0 , 0, width, height, null);
        }else{
            sc = canvas.saveLayer(new RectF(0 , 0, width, height), null, Canvas.ALL_SAVE_FLAG);
        }

        //drawCrop
        Matrix matrix = new Matrix();
        matrix.setTranslate(-pathBound.left, -pathBound.top);
        canvas.drawBitmap(pathToBitmap(path, srcBitmap.getWidth(), srcBitmap.getHeight()), matrix, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));//只在SRC的区域内绘制

        canvas.drawBitmap(srcBitmap, matrix, paint);
        paint.setXfermode(null);
        canvas.restoreToCount(sc);
        return bm;
    }
}
