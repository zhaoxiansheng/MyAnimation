package com.example.zy.myanimation.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.PixelCopy;
import android.view.Surface;

import com.example.zy.myanimation.utils.shitu.AdvancedGeneral;
import com.example.zy.myanimation.view.recycler.utils.ScreenUtils;
import com.example.zy.myanimation.view.recycler.utils.Utils;

import java.nio.ByteBuffer;

public class ScreenShotUtils {

    private static final String TAG = "MediaProjectionUtils";

    private static final String ROOT_PATH = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/Shot_";

    public static int mResultCode;
    public static Intent mResultData;
    private static MediaProjectionManager mMediaProjectionManager;
    private static MediaProjection mMediaProjection;
    private static VirtualDisplay mVirtualDisplay;

    private static ShituCallBack mShituCallBack;

    private static CopyFinishListener mScreenShotListener;

    private static Handler mHandler = new Handler();

    private static int IMAGES_PRODUCED = 0;

    public static void requestMediaProjection(Context context, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (context instanceof Activity) {
                mMediaProjectionManager = (MediaProjectionManager) context.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
                ((Activity) context).startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(), requestCode);
            }
        }
    }

    public static void setUpMediaProjection() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mMediaProjection = mMediaProjectionManager.getMediaProjection(mResultCode, mResultData);
        }
    }

    private static void setUpVirtualDisplay(int width, int height, int density, Surface surface) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mVirtualDisplay = mMediaProjection.createVirtualDisplay("ScreenCapture", width, height, density,
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, surface, null, null);
        }
    }

    public static void screenSnapShot(int width, int height, String path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ImageReader imageReader = ImageReader.newInstance(width, height, 0x1, 2);
            ScreenShotUtils.setUpVirtualDisplay(width, height, (int) ScreenUtils.getScreenDensity(), imageReader.getSurface());
            //如果不sleep获取到是null
            SystemClock.sleep(1000);
            Image image = imageReader.acquireLatestImage();
            if (image == null) {
                Log.d(TAG, "screenSnapShot: image is null");
                return;
            } else {
                saveImage(image, path);
            }
//            imageReader.setOnImageAvailableListener(new ImageAvailableListener(), null);
        }
    }

    static void saveImage(Image image, String path) {
        if (image != null) {
            int width = image.getWidth();
            int height = image.getHeight();
            final Image.Plane[] planes = image.getPlanes();
            final ByteBuffer buffer = planes[0].getBuffer();
            int pixelStride = planes[0].getPixelStride();
            int rowStride = planes[0].getRowStride();
            int rowPadding = rowStride - pixelStride * width;
            Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
            bitmap.copyPixelsFromBuffer(buffer);
            image.close();

            ImageUtil.saveBitmap(bitmap, path);
        }
    }

    static class ImageAvailableListener implements ImageReader.OnImageAvailableListener {
        @Override
        public void onImageAvailable(ImageReader reader) {
            try (Image image = reader.acquireLatestImage()) {
                if (image != null) {
                    IMAGES_PRODUCED++;
                    Log.e("captured image: ", String.valueOf(IMAGES_PRODUCED));

                    long time = System.currentTimeMillis();
                    final String path = ROOT_PATH + time + ".png";

                    saveImage(image, path);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @SuppressLint("NewApi")
    private static class CopyFinishListener implements PixelCopy.OnPixelCopyFinishedListener {
        Bitmap mBitmap = null;

        @Override
        public void onPixelCopyFinished(int copyResult) {
            if (copyResult == PixelCopy.SUCCESS) {
                save();
            }
        }

        void save() {
            long time = System.currentTimeMillis();
            final String path = +time + ".png";
            ImageUtil.saveBitmap(mBitmap, path);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String result = AdvancedGeneral.advancedGeneral(path);
                    mShituCallBack.onSuccess(result);
                }
            }).start();
        }

        Bitmap createBitmap(int width, int height) {
            if (mBitmap == null || mBitmap.getWidth() != width || mBitmap.getHeight() != height) {
                mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            }
            return mBitmap;
        }
    }

    public static void playback(ShituCallBack callBack, int width, int height, Surface surface) {
        mShituCallBack = callBack;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (mScreenShotListener == null) {
                mScreenShotListener = new CopyFinishListener();
            }
            Bitmap bitmap = mScreenShotListener.createBitmap(width, height);
            PixelCopy.request(surface, bitmap, mScreenShotListener, mHandler);
        } else {
            long time = System.currentTimeMillis();
            final String path = ROOT_PATH + time + ".png";
            screenSnapShot(width, height, path);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String result = AdvancedGeneral.advancedGeneral(path);
                    mShituCallBack.onSuccess(result);
                }
            }).start();
        }
    }

    public interface ShituCallBack {
        void onSuccess(String result);
    }
}
