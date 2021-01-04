package com.autopai.common.utils.share;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.autopai.common.utils.common.StringUtils;

import java.util.Map;
import java.util.Set;

/**
 * sharedPreference工具集
 *
 */
public class SharedPrefUtils {
    /**
     * sharedPreference默认名字
     */
    public static String SHAREDPREF_BASE_NAME = "sharedPref_base_name";
    // TODO: 2019/4/17 以前launcher的车型配置
    //    public static String SHAREDPREF_BASE_NAME = "LauncherWT_Sharedpref";
    public static SharedPrefUtils sharedPrefUtils;
    public static String sharedPreName = SHAREDPREF_BASE_NAME;
    private SharedPreferences preferences;
    private Editor editor;

    private SharedPrefUtils() {

    }

    private void createSharedPre(Context context, String name) {
        preferences = context.getApplicationContext().getSharedPreferences(name, Context.MODE_PRIVATE); // 私有数据
        editor = preferences.edit();
        sharedPreName = name;
    }

    /**
     * 得到默认名称的sharepreferencce
     *
     * @param context
     * @return
     */
    public static SharedPrefUtils getInstance(Context context) {
        return getInstance(context, SHAREDPREF_BASE_NAME);
    }

    /**
     * 得到指定名称的sharepreferencce
     *
     * @param context
     * @param name    不能为空，为空是默认名称的sharepreferencce
     * @return
     */
    public static SharedPrefUtils getInstance(Context context, String name) {
        if (StringUtils.isEmptyByTrim(name)) {
            name = SHAREDPREF_BASE_NAME;
        }
        if (sharedPrefUtils == null) {
            sharedPrefUtils = new SharedPrefUtils();
            sharedPrefUtils.createSharedPre(context.getApplicationContext(), name);
        }
        if (!sharedPreName.equals(name)) {
            sharedPrefUtils.createSharedPre(context.getApplicationContext(), name);
        }
        return sharedPrefUtils;
    }

    public void save(String key, String value) {
        if (StringUtils.isEmptyByTrim(key)) {
            return;
        }
        if (StringUtils.isEmpty(value)) {
            value = "";
        }
        editor.putString(key, value);
        editor.commit();
    }

    public void save(String key, boolean value) {
        if (StringUtils.isEmptyByTrim(key)) {
            return;
        }
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void save(String key, float value) {
        if (StringUtils.isEmptyByTrim(key)) {
            return;
        }
        editor.putFloat(key, value);
        editor.commit();
    }

    public void save(String key, int value) {
        if (StringUtils.isEmptyByTrim(key)) {
            return;
        }
        editor.putInt(key, value);
        editor.commit();
    }

    public void save(String key, long value) {
        if (StringUtils.isEmptyByTrim(key)) {
            return;
        }
        editor.putLong(key, value);
        editor.commit();
    }

    public void save(String key, Set<String> values) {
        if (StringUtils.isEmptyByTrim(key) || values == null || values.isEmpty()) {
            return;
        }
        editor.putStringSet(key, values);
        editor.commit();
    }

   /* public void saveJsonObject(String key, Object t) {
        if (StringUtils.isEmptyByTrim(key)) {
            return;
        }
        if (null == t) {
            editor.putString(key, "");
        } else {
            editor.putString(key, GsonUtils.toJson(t));
        }
        editor.commit();
    }*/

    public String getString(String key, String defValue) {
        if (StringUtils.isEmptyByTrim(key)) {
            return defValue;
        }
        return preferences.getString(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        if (StringUtils.isEmptyByTrim(key)) {
            return defValue;
        }
        return preferences.getBoolean(key, defValue);
    }

    public float getFloat(String key, float defValue) {
        if (StringUtils.isEmptyByTrim(key)) {
            return defValue;
        }
        return preferences.getFloat(key, defValue);
    }

    public int getInt(String key, int defValue) {
        if (StringUtils.isEmptyByTrim(key)) {
            return defValue;
        }
        return preferences.getInt(key, defValue);
    }

    public long getLong(String key, long defValue) {
        if (StringUtils.isEmptyByTrim(key)) {
            return defValue;
        }
        return preferences.getLong(key, defValue);
    }

    public Set<String> getStringSet(String key, Set<String> defValue) {
        if (StringUtils.isEmptyByTrim(key)) {
            return defValue;
        }
        return preferences.getStringSet(key, defValue);
    }

    /*public <T> T getJsonObject(String key, Class<T> cls) {
        if (StringUtils.isEmptyByTrim(key)) {
            return null;
        }
        String t = preferences.getString(key, "");
        if (StringUtils.isEmptyByTrim(t)) {
            return null;
        } else {
//            return GsonUtils.toData(t, cls);
        }
    }*/

    public Map<String, ?> getAll() {
        return preferences.getAll();
    }

    public void clear() {
        editor.clear().commit();
    }

    public void remove(String key) {
        if (StringUtils.isEmptyByTrim(key)) {
            return;
        }
        editor.remove(key).commit();
    }
}
