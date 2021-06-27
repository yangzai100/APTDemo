package com.example.apt_sdk1;

import android.app.Activity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SensorsDataAPI {
    public static void bindView(Activity activity){
        Class<? extends Activity> aClass = activity.getClass();
        try {
            Class<?> bindViewClass = Class.forName(aClass.getName() + "_SensorsDataViewBinding");
            Method method = bindViewClass.getMethod("bindView", activity.getClass());
            method.invoke(bindViewClass.newInstance(),activity);

        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
