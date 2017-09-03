package com.ggx.sharepreference;

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

    private static final Map<String, Inject> binderMap = new LinkedHashMap<>();//管理保持管理者Map集合

    @SuppressWarnings("unchecked")
    public static <T>T create(Context context,Class<T> clazz) {
        String fullName=clazz.getName();
        Inject<T> inject=binderMap.get(fullName);
        if(inject==null){
            try {
                Class<?> cla=Class.forName(clazz.getName()+"$$InjectSp");
                Constructor<Inject> con= (Constructor<Inject>) cla.getConstructor(Context.class);
                inject=con.newInstance(context);
                binderMap.put(clazz.getName(),inject);
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
        String fullName = o.getName();
        Inject<?> inject = binderMap.get(fullName);
        return inject != null && inject.save();
    }
}
