package com.fusion.ajpermission.callback;

import com.fusion.ajpermission.utils.JPermissionHelper;

import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

abstract public class AbstractDispatcher {
    protected ConcurrentHashMap<Long, IPermission> mListeners = new ConcurrentHashMap<>();
    protected static AtomicLong mToken = new AtomicLong(0);

    public AbstractDispatcher(){
    }

    public long enqueueRequest(IPermission listener){
        if(listener != null) {
            Long token = mToken.getAndIncrement();
            mListeners.put(token, listener);
            return token;
        }
        return -1L;
    }

    public void cancelRequest(long token){
        mListeners.remove(token);
    }

    abstract protected void dispatchResule(long token, int requestCode, JPermissionHelper.PermissionResult result);
}
