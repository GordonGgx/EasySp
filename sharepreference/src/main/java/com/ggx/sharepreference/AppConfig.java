package com.ggx.sharepreference;

import android.annotation.SuppressLint;
import android.content.Context;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author jerry.Guan
 *         created by 2017/9/2
 */

public class AppConfig {


    private static Context mContext;
    private static final Map<String, Inject> binderMap = new LinkedHashMap<>();//管理保持管理者Map集合

    public static void init(Context context){
        mContext=context;
    }
    @SuppressWarnings("unchecked")
    public static <T>T create(Class<T> clazz) {
        String fullName=clazz.getName();
        Inject<T> inject=binderMap.get(fullName);
        if(inject==null){
            try {
                SpManager manager=SpManager.getManager(mContext,clazz.getCanonicalName());
                Class<?> cla=Class.forName(clazz.getName()+"$$InjectSp");
                Constructor<Inject> con= (Constructor<Inject>) cla.getConstructor(SpManager.class);
                inject=con.newInstance(manager);
            } catch (ClassNotFoundException | NoSuchMethodException
                    | IllegalAccessException | InstantiationException
                    | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        try {
            return inject!=null? inject.readObj(clazz.newInstance()) :null;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean save(Class<?> o) {
        String fullName = o.getClass().getName();
        Inject<?> inject = binderMap.get(fullName);
        return inject != null && inject.save();
    }
}
