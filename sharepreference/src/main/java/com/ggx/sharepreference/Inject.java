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

    public boolean save(){
        SharedPreferences.Editor editor=manager.getPreferences().edit();
        editor(t,editor);
        return editor.commit();
    }

    public SpManager getManager() {
        return manager;
    }
    public T readObj(T t){
        this.t=t;
        return read(t,manager);
    }

     public abstract T read(T t, SpManager manager);
     public abstract void editor(T t,SharedPreferences.Editor editor);
}
