package com.autopai.common.utils.utils.file;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;

import com.autopai.common.utils.utils.Utils;
import com.autopai.common.utils.utils.thread.BasicThreadFactory;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileLock;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public final class FileUtils {

    private static final String TAG = "FileUtils";

    public static final int MAX_FILE_COUNT = 200000;
    public static final int MAX_DIR_MEMORY = 10485760;

    public static int CURRENT_CRASH_FILE_COUNT;
    public static long CURRENT_CRASH_FILE_MEMORY;

    /**
     * 文件分割符
     */
    public static final String FILE_SEP = System.getProperty("file.separator");
    public static final String LINE_SEP = System.getProperty("line.separator");

    private FileUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Clean the internal cache.
     * <p>directory: /data/data/package/cache</p>
     *
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean cleanInternalCache() {
        return deleteFilesInDir(Utils.getApp().getCacheDir());
    }

    /**
     * Clean the internal files.
     * <p>directory: /data/data/package/files</p>
     *
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean cleanInternalFiles() {
        return deleteFilesInDir(Utils.getApp().getFilesDir());
    }

    /**
     * Clean the internal databases.
     * <p>directory: /data/data/package/databases</p>
     *
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean cleanInternalDbs() {
        return deleteFilesInDir(new File(Utils.getApp().getFilesDir().getParent(), "databases"));
    }

    /**
     * Clean the internal database by name.
     * <p>directory: /data/data/package/databases/dbName</p>
     *
     * @param dbName The name of database.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean cleanInternalDbByName(final String dbName) {
        return Utils.getApp().deleteDatabase(dbName);
    }

    /**
     * Clean the internal shared preferences.
     * <p>directory: /data/data/package/shared_prefs</p>
     *
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean cleanInternalSp() {
        return deleteFilesInDir(new File(Utils.getApp().getFilesDir().getParent(), "shared_prefs"));
    }

    /**
     * Clean the external cache.
     * <p>directory: /storage/emulated/0/android/data/package/cache</p>
     *
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean cleanExternalCache() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                && deleteFilesInDir(Utils.getApp().getExternalCacheDir());
    }

    /**
     * Clean the custom directory.
     *
     * @param dirPath The path of directory.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean cleanCustomDir(final String dirPath) {
        return deleteFilesInDir(dirPath);
    }

    /**
     * Clean the custom directory.
     *
     * @param dir The directory.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean cleanCustomDir(final File dir) {
        return deleteFilesInDir(dir);
    }

    public static boolean deleteFilesInDir(final String dirPath) {
        return deleteFilesInDir(getFileByPath(dirPath));
    }

    public static int[] getFileCountAndMemory(final String dirPath) {
        return getFileCountAndMemory(getFileByPath(dirPath));
    }

    public static void deleteOldFile(final String dirPath) {
        new AsyncTask<String, Integer, Void>() {
            @Override
            protected Void doInBackground(String... strings) {
                deleteOldFile(getFileByPath(dirPath));
                return null;
            }
        }.execute();
    }

    public static long getFileMemory(final String filePath) {
        return getFileLength(getFileByPath(filePath));
    }

    ///////////////////////////////////////////////////////////////////////////
    // other utils methods
    ///////////////////////////////////////////////////////////////////////////

    private static boolean deleteFilesInDir(final File dir) {
        if (dir == null) {
            return false;
        }
        // dir doesn't exist then return true
        if (!dir.exists()) {
            return true;
        }
        // dir isn't a directory then return false
        if (!dir.isDirectory()) {
            return false;
        }
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!file.delete()) {
                        return false;
                    }
                } else if (file.isDirectory()) {
                    if (!deleteDir(file)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static boolean deleteDir(final File dir) {
        if (dir == null) {
            return false;
        }
        // dir doesn't exist then return true
        if (!dir.exists()) {
            return true;
        }
        // dir isn't a directory then return false
        if (!dir.isDirectory()) {
            return false;
        }
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!file.delete()) {
                        return false;
                    }
                } else if (file.isDirectory()) {
                    if (!deleteDir(file)) {
                        return false;
                    }
                }
            }
        }
        return dir.delete();
    }

    public static File getFileByPath(final String filePath) {
        return isSpace(filePath) ? null : new File(filePath);
    }

    private static boolean deleteOldFile(final File dir) {
        if (dir == null) {
            return false;
        }
        // dir doesn't exist then return true
        if (!dir.exists()) {
            return true;
        }
        // dir isn't a directory then return false
        if (!dir.isDirectory()) {
            return false;
        }

        File[] files = dir.listFiles();
        File file;
        if (files.length > 0) {
            file = files[0];
        } else {
            Log.e(TAG, "deleteOldFile: the directory count is 0 ");
            return false;
        }
        long lastModified = dir.lastModified();
        for (File f : files) {
            long time = f.lastModified();
            if (time < lastModified) {
                lastModified = time;
                file = f;
            }
        }
        return file.isFile() && file.delete();
    }

    /**
     * 获取文件当前数量和容量.
     *
     * @param dir The directory .
     * @return {@code int[0]}: memory of file <br>{@code int[1]}: count of file
     */
    private static int[] getFileCountAndMemory(final File dir) {
        FileLock lock = null;
        int[] fileProperty = new int[2];
        if (dir == null) {
            return fileProperty;
        }
        // dir doesn't exist then return true
        if (!dir.exists()) {
            return fileProperty;
        }
        // dir isn't a directory then return false
        if (!dir.isDirectory()) {
            return fileProperty;
        }

        File[] files = dir.listFiles();
        fileProperty[0] = (int) getFileLength(dir);
        fileProperty[1] = files.length;
        return fileProperty;
    }

    /**
     * 获取文件当前容量.
     *
     * @param file The file .
     * @return {@code length}: memory of file
     */
    private static long getFileLength(final File file) {
        if (file == null) {
            return 0;
        }
        // dir doesn't exist then return true
        if (!file.exists()) {
            return 0;
        }
        // dir isn't a directory then return false
        if (!file.isDirectory()) {
            return file.length();
        }

        File[] files = file.listFiles();
        long length = 0;
        for (File f : files) {
            if (f.isFile()) {
                length += f.length();
            }
        }
        return length;
    }

    private static String showLongFileSzie(long length) {
        if (length >= 1048576) {
            return (length / 1048576) + "MB";
        } else if (length >= 1024) {
            return (length / 1024) + "KB";
        } else {
            return length + "B";
        }
    }

    /**
     * 创建文件
     *
     * @param filePath
     * @return
     */
    public static boolean createOrExistsFile(final String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.isFile();
        }
        if (!createOrExistsDir(file.getParentFile())) {
            return false;
        }
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 创建目录
     *
     * @param file
     * @return
     */
    public static boolean createOrExistsDir(final File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    /**
     * 写文件
     *
     * @param input
     * @param filePath
     */
    public static boolean input2File(final String input, final String filePath, final boolean append) {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("crash-schedule-pool-%d").daemon(true).priority(Thread.MIN_PRIORITY).build());
        Future<Boolean> submit = executorService.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                BufferedWriter bw = null;
                try {
                    bw = new BufferedWriter(new FileWriter(filePath, append));
                    bw.write(input);
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    try {
                        if (bw != null) {
                            bw.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        try {
            if (submit.get()) {
                return true;
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "write info to " + filePath + " failed!");
        return false;
    }

    private static boolean isSpace(final String s) {
        if (s == null) {
            return true;
        }
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 计算Sdcard的剩余大小
     *
     * @return MB
     */
    public static long getAvailableSize() {
        //sd卡大小相关变量
        StatFs statFs;
        File file = Environment.getExternalStorageDirectory();
        statFs = new StatFs(file.getPath());
        //获得Sdcard上每个block的size
        long blockSize = statFs.getBlockSizeLong();
        //获取可供程序使用的Block数量
        long blockavailable = statFs.getAvailableBlocksLong();
        //计算标准大小使用：1024，当然使用1000也可以
        long blockavailableTotal = blockSize * blockavailable / 1024 / 1024;
        return blockavailableTotal;
    }

    /**
     * SDCard 总容量大小
     *
     * @return MB
     */
    public static long getTotalSize() {
        StatFs statFs;
        File file = Environment.getExternalStorageDirectory();
        statFs = new StatFs(file.getPath());
        //获得sdcard上 block的总数
        long blockCount = statFs.getBlockCountLong();
        //获得sdcard上每个block 的大小
        long blockSize = statFs.getBlockSizeLong();
        //计算标准大小使用：1024，当然使用1000也可以
        long bookTotalSize = blockCount * blockSize / 1024 / 1024;
        return bookTotalSize;
    }

    /**
     * 保存bitmap到本地
     *
     * @param bitmap
     * @return
     */
    public static File saveBitmap(Bitmap bitmap, int quality) {
        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            savePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    .toString()
                    + File.separator;
        } else {
            Log.e(TAG, "saveBitmap: null");
            return null;
        }
        try {
            filePic = new File(savePath + "Pic_" + System.currentTimeMillis() + ".jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "saveBitmap: IOException");
            return null;
        }
        Log.i(TAG,"saveBitmap: " + filePic.getAbsolutePath());
        return filePic;
    }

    /**
     * 压缩图片
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        /** 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中*/
        image.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        /** 把压缩后的数据baos存放到ByteArrayInputStream中*/
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        /** 把ByteArrayInputStream数据生成图片*/
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            isBm.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 文件夹删除
     * */
    public static void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteFile(f);
            }
            file.delete();//如要保留文件夹，只删除文件，请注释这行
        } else if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 压缩图片文件
     * */
    public static void compressPic(Context context, final File picFile, OnCompressListener listener){
        String savePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString()
                + File.separator;
        Luban.with(context)
                .load(picFile.getPath())
                .ignoreBy(100)
                .setTargetDir(savePath)
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                })
                .setCompressListener(listener).launch();
    }

    /**
     * 读取文件内容，作为字符串返回
     */
    public static String readFileAsString(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath);
        }

        if (file.length() > 1024 * 1024 * 1024) {
            throw new IOException("File is too large");
        }

        StringBuilder sb = new StringBuilder((int) (file.length()));
        // 创建字节输入流
        FileInputStream fis = new FileInputStream(filePath);
        // 创建一个长度为10240的Buffer
        byte[] bbuf = new byte[10240];
        // 用于保存实际读取的字节数
        int hasRead = 0;
        while ((hasRead = fis.read(bbuf)) > 0) {
            sb.append(new String(bbuf, 0, hasRead));
        }
        fis.close();
        return sb.toString();
    }

    /**
     * 根据文件路径读取byte[] 数组
     */
    public static byte[] readFileByBytes(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath);
        } else {

            try (ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length())) {
                BufferedInputStream in = null;
                in = new BufferedInputStream(new FileInputStream(file));
                short bufSize = 1024;
                byte[] buffer = new byte[bufSize];
                int len1;
                while (-1 != (len1 = in.read(buffer, 0, bufSize))) {
                    bos.write(buffer, 0, len1);
                }

                return bos.toByteArray();
            }
        }
    }

    public static File getFile(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        long time = System.currentTimeMillis();
        final String path = "/sdcard/DCIM/Camera/Shot_" + time + ".png";
        File file = new File(path);
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            InputStream is = new ByteArrayInputStream(baos.toByteArray());
            int x = 0;
            byte[] b = new byte[1024 * 100];
            while ((x = is.read(b)) != -1) {
                fos.write(b, 0, x);
            }
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
}
