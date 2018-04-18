package com.ggx.sharepreference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author jerry.Guan
 *         created by 2017/9/2
 */

public abstract class Inject<T> {

    private SpManager manager;

    private T t;

    public Inject (SpManager manager){
        this.manager=manager;
    }

    public boolean save(Object obj){
        SharedPreferences.Editor editor=manager.getPreferences().edit();
        this.t= (T) obj;
        editor(t,editor);
        return editor.commit();
    }

    public void saveAsyn(Object obj){
        SharedPreferences.Editor editor=manager.getPreferences().edit();
        this.t= (T) obj;
        editor(t,editor);
        editor.apply();
    }

    public SpManager getManager() {
        return manager;
    }
    public T readObj(T t){
        this.t=read(t,manager);
        return this.t;
    }

     public abstract T read(T t, SpManager manager);
     public abstract void editor(T t,SharedPreferences.Editor editor);

     public T get(){
         return t;
     }
}
