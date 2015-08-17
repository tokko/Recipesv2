package com.tokko.recipesv2.backend.util;

import com.googlecode.objectify.Ref;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class IterableUtil {

    public static void populate(Object o, Object o1) {
        Field[] entityFields = o1.getClass().getDeclaredFields();
        Field[] thisFields = o.getClass().getDeclaredFields();
        try {

            for (Field field : thisFields) {
                field.setAccessible(true);
                for (Field field1 : entityFields) {
                    field1.setAccessible(true);
                    if (field.getType().equals(field1.getType()) && field.getName().equals(field1.getName()))
                        field.set(o, field1.get(o1));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static <T> T cloneTo(T src, Class<T> clx) {
        try {
            T tnew = clx.newInstance();
            populate(tnew, src);
            return tnew;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> List<T> deRef(Iterable<Ref<T>> iter) {
        List<T> ret = new ArrayList<T>();
        for (Ref<T> t : iter) {
            ret.add(t.get());
        }
        return ret;
    }
}
