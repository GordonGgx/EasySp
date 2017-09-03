package com.ggx.sharepreference;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.SmsManager;

/**
 * @author jerry.Guan
 *         created by 2017/9/2
 */

public class SpManager {

    private SharedPreferences preferences;

    private SpManager(Context context,String name){
        preferences=context.getSharedPreferences(name,Context.MODE_PRIVATE);
    }

    public static SpManager getManager(Context context,String name){
        return new SpManager(context,name);
    }

    public int getInt(String key,int defaultValue){
        return preferences.getInt(key,defaultValue);
    }
    public int getInt(String key){
        return getInt(key,0);
    }

    public String getString(String key,String defaultValue){
        return preferences.getString(key,defaultValue);
    }
    public String getString(String key){
        return getString(key,null);
    }

    public long getLong(String key,long defaultValue){
        return preferences.getLong(key,defaultValue);
    }
    public long getLong(String key){
        return getLong(key,0);
    }

    public boolean getBoolean(String key,boolean defvaultValue){
        return preferences.getBoolean(key,defvaultValue);
    }
    public boolean getBoolean(String key){
        return getBoolean(key,false);
    }
    public float getFloat(String key,float defaultValue){
        return preferences.getFloat(key,defaultValue);
    }
    public float getFloat(String key){
        return getFloat(key,0);
    }

    public boolean putInt(String key,int value){
        return preferences.edit().putInt(key,value).commit();
    }
    public boolean putString(String key,String value){
        return preferences.edit().putString(key,value).commit();
    }
    public boolean putLong(String key,long value){
        return preferences.edit().putLong(key,value).commit();
    }
    public boolean putFloat(String key,float value){
        return preferences.edit().putFloat(key,value).commit();
    }
    public boolean putBoolean(String key,boolean value){
        return preferences.edit().putBoolean(key,value).commit();
    }

    public SharedPreferences getPreferences(){
        return preferences;
    }

}
