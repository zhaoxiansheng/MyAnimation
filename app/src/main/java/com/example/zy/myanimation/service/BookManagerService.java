package com.example.zy.myanimation.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

import com.example.zy.myanimation.aidl.Book;
import com.example.zy.myanimation.aidl.IBookManager;
import com.example.zy.myanimation.aidl.IOnNewBookArrivedListener;
import com.orhanobut.logger.Logger;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import androidx.annotation.Nullable;

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
}
