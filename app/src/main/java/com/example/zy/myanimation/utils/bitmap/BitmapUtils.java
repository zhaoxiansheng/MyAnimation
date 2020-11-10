package com.example.zy.myanimation.utils.bitmap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.TreeMap;

public class BitmapUtils {

    private static final String TAG = BitmapUtils.class.getSimpleName();

    private static final Object object = new Object();

    //缓存基本设置
    private static final int DEFAULT_MEM_CACHE_SIZE = 1024 * 5; // 内存缓存默认大小5MB

    private static final boolean DEFAULT_MEM_CACHE_ENABLED = true;

    private TreeMap<Integer, WeakReference<Bitmap>> mReusableBitmaps;
    private TreeMap<Integer, WeakReference<Bitmap>> mUsableBitmaps;

    private ImageCacheParams mCacheParams;

    private static BitmapUtils mUtils = null;

    public static class ImageCacheParams {

        int memCacheSize = DEFAULT_MEM_CACHE_SIZE;
        boolean memoryCacheEnabled = DEFAULT_MEM_CACHE_ENABLED;

        /***
         * 手动设置内存缓存大小
         * @param percent 缓存大小占最大可用内存的比例
         */
        public void setMemCacheSizePercent(float percent) {
            if (percent < 0.01f || percent > 0.8f) {
                throw new IllegalArgumentException("setMemCacheSizePercent - percent must be "
                        + "between 0.01 and 0.8 (inclusive)");
            }
            memCacheSize = Math.round(percent * Runtime.getRuntime().maxMemory() / 1024);
        }
    }


    public static BitmapUtils getInstance(ImageCacheParams cacheParams) {
        if (mUtils == null) {
            synchronized (object) {
                if (mUtils == null) {
                    mUtils = new BitmapUtils(cacheParams);
                }
            }
        }
        return mUtils;
    }

    public static BitmapUtils getInstance() {
        if (mUtils == null) {
            synchronized (object) {
                if (mUtils == null) {
                    mUtils = new BitmapUtils(new ImageCacheParams());
                }
            }
        }
        return mUtils;
    }

    private BitmapUtils(ImageCacheParams cacheParams) {
        init(cacheParams);
    }

    private void init(ImageCacheParams cacheParams) {
        mCacheParams = cacheParams;
        if (mCacheParams.memoryCacheEnabled) {
            mReusableBitmaps = new TreeMap<>();
            mUsableBitmaps = new TreeMap<>();
        }
    }

    private Bitmap getBitmapFromReusableSet(BitmapFactory.Options options, Bitmap.Config config) {
        Bitmap bitmap = null;

        int size = getByteCount(options, config);
        if (mReusableBitmaps != null && !mReusableBitmaps.isEmpty()) {
            int ceilSize = mReusableBitmaps.ceilingKey(size);
            bitmap = mReusableBitmaps.get(ceilSize).get();
            if (bitmap != null && bitmap.isMutable()) {
                mReusableBitmaps.remove(ceilSize);

                mUsableBitmaps.put(size, new WeakReference<>(bitmap));
            }
        }
        return bitmap;
    }

    private boolean addInBitmapOptions(BitmapFactory.Options options, Bitmap.Config config) {
        options.inMutable = true;
        if (mReusableBitmaps != null && !mReusableBitmaps.isEmpty()) {
            Bitmap inBitmap = getBitmapFromReusableSet(options, config);
            if (inBitmap != null) {
                options.inBitmap = inBitmap;
                return true;
            }
        }
        return false;
    }

    private boolean canUseForInBitmap(Bitmap candidate, BitmapFactory.Options targetOptions) {

        //4.4之前的版本，尺寸必须完全吻合
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return candidate.getWidth() == targetOptions.outWidth
                    && candidate.getHeight() == targetOptions.outHeight
                    && targetOptions.inSampleSize == 1;
        }
        //根据图片格式，计算具体的bitmap大小
        int byteCount = getByteCount(targetOptions, candidate.getConfig());

        return byteCount <= getByteCount(candidate);
    }

    private int getByteCount(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return bitmap.getAllocationByteCount();
        } else {
            return bitmap.getByteCount();
        }
    }

    private int getByteCount(BitmapFactory.Options options, Bitmap.Config config) {
        int width = options.outWidth / options.inSampleSize;
        int height = options.outHeight / options.inSampleSize;

        //根据图片格式，计算具体的bitmap大小
        return width * height * getBytesPerPixel(config);
    }

    private static int getBytesPerPixel(Bitmap.Config config) {
        if (config == Bitmap.Config.ARGB_8888) {
            return 4;
        } else if (config == Bitmap.Config.RGB_565) {
            return 2;
        } else if (config == Bitmap.Config.ARGB_4444) {
            return 2;
        } else if (config == Bitmap.Config.ALPHA_8) {
            return 1;
        }
        return 1;
    }

    private BitmapFactory.Options getSampledBitmapOptionsFromStream(InputStream is, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;

        return options;
    }

    /**
     * @param options   option
     * @param reqWidth  请求宽度
     * @param reqHeight 请求高度
     * @return int 缩小的比例
     * 计算图片缩放比例
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public Bitmap decodeBitmapFromStream(InputStream is, int reqWidth, int reqHeight, Bitmap.Config config) {
        BitmapFactory.Options options = getSampledBitmapOptionsFromStream(is, reqWidth, reqHeight);
        boolean inBitmap = addInBitmapOptions(options, config);
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
        if (!inBitmap) {
            int size = getByteCount(bitmap);
            mReusableBitmaps.put(size, new WeakReference<>(bitmap));
        }
        return bitmap;
    }

    public Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    void removeFormUsable(Bitmap bitmap) {
        if (mUsableBitmaps != null && !mUsableBitmaps.isEmpty()) {
            if (bitmap != null) {
                int size = getByteCount(bitmap);
                Bitmap item = mUsableBitmaps.get(size).get();
                if (item != null && item == bitmap) {
                    mUsableBitmaps.remove(size);
                    mReusableBitmaps.put(size, new WeakReference<>(bitmap));
                }
            }
        }
    }
}
