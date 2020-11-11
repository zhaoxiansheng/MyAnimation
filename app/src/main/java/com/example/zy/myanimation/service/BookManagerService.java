package com.example.zy.myanimation.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;

import com.example.zy.myanimation.R;
import com.example.zy.myanimation.activity.MainActivity;
import com.example.zy.myanimation.aidl.Book;
import com.example.zy.myanimation.aidl.IBookManager;
import com.example.zy.myanimation.aidl.IOnNewBookArrivedListener;
import com.orhanobut.logger.Logger;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

/**
 * Created on 2017/12/4.
 *
 * @author zhaoy
 */
public class BookManagerService extends Service {

    private static final String TAG = "BMS";

    private AtomicBoolean mIsServiceDestroyed = new AtomicBoolean(false);

    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();

    private CopyOnWriteArrayList<IOnNewBookArrivedListener> mListenerList = new CopyOnWriteArrayList<>();

    private Binder mBinder = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }

        @Override

        public void registerListener(IOnNewBookArrivedListener listener) {
            if (!mListenerList.contains(listener)) {
                mListenerList.add(listener);
            } else {
                Logger.d(TAG, "already exists");
            }
            Logger.d(TAG, "registerListener, size:" + mListenerList.size());
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) {
            if (!mListenerList.contains(listener)) {
                mListenerList.remove(listener);
                Logger.d(TAG, "unregister listener succeed");
            } else {
                Logger.d(TAG, "not found");
            }
            Logger.d(TAG, "unregisterListener, current size:" + mListenerList.size());
        }

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1, "Android"));
        mBookList.add(new Book(2, "ios"));
        new Thread(new ServiceWorker()).start();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotification("MyService", "MyName");
        }
    }

    private void onNewBookArrived(Book book) throws RemoteException {
        mBookList.add(book);
        Logger.d(TAG, "onNewBookArrived, notify listeners:" + mListenerList.size());
        for (int i = 0; i < mListenerList.size(); i++) {
            IOnNewBookArrivedListener listener = mListenerList.get(i);
            listener.onNewBookArrived(book);
        }
    }

    private class ServiceWorker implements Runnable {
        @Override
        public void run() {
            while (!mIsServiceDestroyed.get()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int bookId = mBookList.size() + 1;
                Book newBook = new Book(bookId, "new book#" + bookId);
                try {
                    onNewBookArrived(newBook);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotification(String channelID, String channelName) {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_NONE);
        channel.setLightColor(Color.BLUE);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        // 设置渠道描述
        channel.setDescription("测试通知组");
        // 设置绕过请勿打扰模式
        channel.setBypassDnd(true);
        // 桌面Launcher的消息角标
        channel.canShowBadge();
        // 设置显示桌面Launcher的消息角标
        channel.setShowBadge(true);
        // 设置通知出现时声音，默认通知是有声音的
        channel.setSound(null, null);
        // 设置通知出现时的闪灯（如果 android 设备支持的话）
        channel.enableLights(true);
        // 设置通知出现时的震动（如果 android 设备支持的话）
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400,
                300, 200, 400});

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 11,
                new Intent(getApplicationContext(), MainActivity.class), 0);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);
        Notification notification = new NotificationCompat.Builder(this, channelID)
                .setContentTitle("12312312312313")
                .setContentText("322323223223")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(pendingIntent)
//                .setChannelId(CALENDAR_ID)
                .build();

        startForeground(1, notification);
    }
}
