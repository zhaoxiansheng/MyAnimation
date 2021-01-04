package com.autopai.common.utils.utils.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.view.Surface;

import com.autopai.common.utils.image.ImageUtil;
import com.autopai.common.utils.view.ScreenUtils;

import java.nio.ByteBuffer;

public class MediaProjectionUtils {

    private static final String TAG = "MediaProjectionUtils";

    public static int mResultCode;
    public static Intent mResultData;
    private static MediaProjectionManager mMediaProjectionManager;
    private static MediaProjection mMediaProjection;
    private static VirtualDisplay mVirtualDisplay;

    private static int IMAGES_PRODUCED = 0;

    public static void requestMediaProjection(Context context, int requestCode) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
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

    public static Bitmap screenSnapShot(Context context, int width, int height, String path, boolean isSave) {
        Bitmap bitmap = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ImageReader imageReader = ImageReader.newInstance(width, height, 0x1, 2);
            MediaProjectionUtils.setUpVirtualDisplay(width, height, (int) ScreenUtils.getScreenDensity(context), imageReader.getSurface());
            //如果不sleep获取到是null
            SystemClock.sleep(1000);
            Image image = imageReader.acquireLatestImage();
            if (image == null) {
                Log.d(TAG, "screenSnapShot: image is null");
            } else {
                bitmap = imageToBitmap(image);
            }
        }
        if (isSave) {
            ImageUtil.saveBitmap(bitmap, path);
        }
        return bitmap;
    }

    static Bitmap imageToBitmap(Image image) {
        Bitmap bitmap = null;
        if (image != null) {
            int width = image.getWidth();
            int height = image.getHeight();
            final Image.Plane[] planes = image.getPlanes();
            final ByteBuffer buffer = planes[0].getBuffer();
            int pixelStride = planes[0].getPixelStride();
            int rowStride = planes[0].getRowStride();
            int rowPadding = rowStride - pixelStride * width;
            bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
            bitmap.copyPixelsFromBuffer(buffer);
            image.close();
        }
        return bitmap;
    }

    static class ImageAvailableListener implements ImageReader.OnImageAvailableListener {
        @Override
        public void onImageAvailable(ImageReader reader) {
            try (Image image = reader.acquireLatestImage()) {
                if (image != null) {
                    IMAGES_PRODUCED++;
                    Log.e("captured image: ", String.valueOf(IMAGES_PRODUCED));

                    long time = System.currentTimeMillis();
                    final String path = "/sdcard/DCIM/Camera/Shot_" + time + ".png";

                    Bitmap bitmap = imageToBitmap(image);
                    ImageUtil.saveBitmap(bitmap, path);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
