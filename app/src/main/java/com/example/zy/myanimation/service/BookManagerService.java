package com.example.zy.myanimation.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.example.zy.myanimation.aidl.Book;
import com.example.zy.myanimation.aidl.IBookManager;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created on 2017/12/4.
 *
 * @author zhaoy
 */
public class BookManagerService extends Service {

    private static final String TAG = "BMS";
    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();

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
    }
}
