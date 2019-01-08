package com.example.zy.myanimation.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * Created on 2017/12/1.
 *
 * @author zhaoy
 */
public class MessengerService extends Service {
    private static final int MSG_FROM_CLIENT = 0x10001;
    private static final int MSG_TO_CLIENT = 0x10002;
    private static final String IS_LOGIN = "isLogin";
    private static final String NICK_NAME = "nickName";
    private static final String USER_ID = "userId";

    @SuppressLint("HandlerLeak")
    private Messenger mMessenger = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msgfromClient) {
            Message msgToClient = Message.obtain(msgfromClient);
            switch (msgfromClient.what) {
                //msg 客户端传来的消息
                case MSG_FROM_CLIENT:
                    try {
                        //模拟耗时
                        Thread.sleep(1000);

                        //传递数据
                        Bundle toClicentDate = new Bundle();
                        toClicentDate.putString(NICK_NAME, "赵宇");
                        toClicentDate.putBoolean(IS_LOGIN, true);
                        toClicentDate.putInt(USER_ID, 10086);
                        msgToClient.setData(toClicentDate);
                        msgToClient.what = MSG_TO_CLIENT;

                        //传回Client
                        msgfromClient.replyTo.send(msgToClient);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }

            super.handleMessage(msgfromClient);
        }
    });

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
