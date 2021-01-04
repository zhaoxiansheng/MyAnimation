package com.autopai.common.utils.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * Toast工具集
 */
public class ToastUtils {
    private static final boolean SHOW_BUG_TOAST = true;
    private static final boolean ENABLE_LOG = true;

	private static Handler handler	= new Handler(Looper.getMainLooper());

	private static Toast toast	= null;

	private static Object synObj = new Object();

	public static void showShortMessage(Context context, String TAG, CharSequence text) {
        if(ENABLE_LOG) {
            Log.e(TAG, text.toString());
        }
	    if(SHOW_BUG_TOAST) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }
	}

	public static void showLongMessage(final Context context, final String TAG, final CharSequence text) {
	    if(ENABLE_LOG) {
            Log.e(TAG, text.toString());
        }
        if(SHOW_BUG_TOAST) {
        	if(Looper.myLooper() == Looper.getMainLooper()){
				Toast.makeText(context, text, Toast.LENGTH_LONG).show();
			}else{
				handler.post(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(context, text, Toast.LENGTH_LONG).show();
					}
				});
			}
        }
	}

	public static void showMessage(Context context, int resId) {
		showMessage(context, resId, Toast.LENGTH_SHORT);
	}

	public static void showMessage(final Context context, final CharSequence text, final int len) {
		if (text == null || text.equals("")) {
			return;
		}
		handler.post(new Runnable() {
			@Override
			public void run() {
				synchronized (synObj) { // 加上同步是为了每个toast只要有机会显示出来
					if (toast != null) {
						toast.setText(text);
						toast.setDuration(len);
					} else {
						toast = Toast.makeText(context, text, len);
					}
					toast.show();
				}
			}
		});
	}

	public static void showMessage(final Context context, final int resId, final int len) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				synchronized (synObj) {
					if (toast != null) {
						// toast.cancel();
						toast.setText(resId);
						toast.setDuration(len);
					} else {
						toast = Toast.makeText(context, resId, len);
					}
					toast.show();
				}
			}
		});
	}

	public static void cancel() {
		if (toast != null) {
			toast.cancel();
		}
	}
}
