package com.coolxtc.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.coolxtc.common.App;


/**
 * desc: SP缓存类
 * author：BlueTory
 * date: on 2016/4/7 17:20
 */
public class SPUtils {
    private static SPUtils util;

    public static SPUtils getInstance() {
        if (util == null) {
            util = new SPUtils();
        }
        return util;

    }

    private SPUtils() {
        super();
    }

    private String name = App.instance.getPackageName();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInt(String key) {

        SharedPreferences sp = App.instance.getSharedPreferences(name,
                Context.MODE_PRIVATE);

        return sp.getInt(key, 0);
    }

    public void setInt(String key, int num) {

        SharedPreferences sp = App.instance.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt(key, num);
        editor.commit();

    }

    public String getString(String key) {

        SharedPreferences sp = App.instance.getSharedPreferences(name,
                Context.MODE_PRIVATE);

        return sp.getString(key, "");
    }

    public String getString(String key, String defaultVal) {

        SharedPreferences sp = App.instance.getSharedPreferences(name,
                Context.MODE_PRIVATE);

        return sp.getString(key, defaultVal);
    }

    public void setString(String key, String str) {

        SharedPreferences sp = App.instance.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(key, str);
        editor.commit();
    }

    public void setBoolean(String key, boolean value) {
        SharedPreferences sp = App.instance.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBoolean(String key) {
        SharedPreferences sp = App.instance.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defValue) {
        SharedPreferences sp = App.instance.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }
}
