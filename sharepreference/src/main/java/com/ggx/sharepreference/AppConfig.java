package com.ggx.sharepreference;

import android.app.Application;
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
        if(!(context instanceof Application)){
            throw new RuntimeException("the context must be ApplicationContext");
        }
        String fullName=clazz.getName();
        Inject<T> inject=binderMap.get(fullName);
        if(inject==null){
            try {
                Class<?> cla=Class.forName(clazz.getName()+"$$InjectSp");
                Constructor<Inject> con= (Constructor<Inject>) cla.getConstructor(Context.class);
                inject=con.newInstance(context);
                binderMap.put(clazz.getName(),inject);
                return inject.readObj(clazz.newInstance());
            } catch (ClassNotFoundException | NoSuchMethodException
                    | IllegalAccessException | InstantiationException
                    | InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        }
        return inject.get();
    }

    public static boolean save(Object obj) {
        String fullName = obj.getClass().getName();
        if(binderMap.containsKey(fullName)){
            Inject<?> inject = binderMap.get(fullName);
            binderMap.remove(fullName);
            binderMap.put(fullName,inject);
            return inject.save(obj);
        }else {
            throw new RuntimeException("未发现"+obj.getClass().getName());
        }
    }
}
