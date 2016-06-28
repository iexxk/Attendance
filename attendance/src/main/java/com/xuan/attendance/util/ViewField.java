package com.xuan.attendance.util;

import android.app.Activity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ViewField {
    /**
     * view对应的id
     *
     * @return
     */
    int value();
    public static class Processor {
        public static void process(Activity activity) throws Exception {
            Field[] fields = activity.getClass().getDeclaredFields();
            ViewField annot = null;
            for (Field field : fields) {
                annot = field.getAnnotation(ViewField.class);
                if (annot != null) {
                    field.setAccessible(true);
                    field.set(activity, activity.findViewById(annot.value()));
                }
            }
        }
    }
}