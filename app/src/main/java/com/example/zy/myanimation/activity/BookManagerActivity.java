package com.example.zy.myanimation.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.zy.myanimation.aidl.Book;
import com.example.zy.myanimation.aidl.IBookManager;
import com.example.zy.myanimation.service.BookManagerService;

import com.example.zy.myanimation.R;
import com.example.zy.myanimation.aidl.Book;
import com.example.zy.myanimation.aidl.IBookManager;
import com.example.zy.myanimation.service.BookManagerService;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created on 2017/12/4.
 *
 * @author zhaoy
 */
public class BookManagerActivity extends Activity {

    private static final String TAG = "BookManagerActivity";

    // TODO: 2018/5/28  
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            IBookManager bookManager = IBookManager.Stub.asInterface(iBinder);
            try {
                List<Book> list = bookManager.getBookList();
                Log.i(TAG, "query book list, list type:" + list.getClass().getCanonicalName());
                Log.i(TAG, "query book list:" + list.toString());
                Book newBook = new Book(3, "Android开发艺术探索");
                list.add(newBook);
                List<Book> newList = bookManager.getBookList();
                Logger.d(TAG, "query book list:" + newList.toString());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manager);
        Intent intent = new Intent(this, BookManagerService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }
}
