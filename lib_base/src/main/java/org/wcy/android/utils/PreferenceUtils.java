package org.wcy.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class PreferenceUtils {
    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void put(Context context, String key, Object object) {
        SharedPreferences sp = getSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }

        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 存储Object,支持简单类型
     *
     * @param context
     * @param o
     * @return
     */
    public static boolean setObject(Context context, Object o) {
        Field[] fields = o.getClass().getFields();
        SharedPreferences sp = context.getSharedPreferences(o.getClass().getName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        for (int i = 0; i < fields.length; i++) {
            Class<?> type = fields[i].getType();
            if (isSingle(type)) {
                try {
                    final String name = fields[i].getName();
                    if (type == Character.TYPE || type.equals(String.class)) {
                        Object value = fields[i].get(o);
                        editor.putString(name, null == value ? null : value.toString());
                    } else if (type.equals(int.class) || type.equals(Short.class))
                        editor.putInt(name, fields[i].getInt(o));
                    else if (type.equals(double.class))
                        editor.putFloat(name, (float) fields[i].getDouble(o));
                    else if (type.equals(float.class))
                        editor.putFloat(name, fields[i].getFloat(o));
                    else if (type.equals(long.class))
                        editor.putLong(name, fields[i].getLong(o));
                    else if (type.equals(boolean.class))
                        editor.putBoolean(name, fields[i].getBoolean(o));
                } catch (Exception e) {
                }
            }
        }
        return editor.commit();
    }

    /**
     * 存储Object,支持简单类型
     *
     * @param <T>
     * @param context
     * @return
     */
    public static <T> T getObject(Context context, Class<T> clazz) {

        try {
            T o = clazz.newInstance();
            Field[] fields = clazz.getFields();
            SharedPreferences sp = context.getSharedPreferences(clazz.getName(), Context.MODE_PRIVATE);
            for (int i = 0; i < fields.length; i++) {
                Class<?> type = fields[i].getType();
                if (isSingle(type)) {
                    try {
                        final String name = fields[i].getName();
                        fields[i].setAccessible(true);
                        if (type == Character.TYPE || type.equals(String.class)) {
                            final String value = sp.getString(name, null);
                            if (null != value)
                                fields[i].set(o, value);
                        } else if (type.equals(int.class) || type.equals(Short.class))
                            fields[i].setInt(o, sp.getInt(name, 0));
                        else if (type.equals(double.class))
                            fields[i].setDouble(o, sp.getFloat(name, 0));
                        else if (type.equals(float.class))
                            fields[i].setFloat(o, sp.getFloat(name, 0));
                        else if (type.equals(long.class))
                            fields[i].setLong(o, sp.getLong(name, 0));
                        else if (type.equals(boolean.class))
                            fields[i].setBoolean(o, sp.getBoolean(name, false));

                    } catch (Exception e) {
                    }
                }
            }
            return o;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断是否是值类型
     **/
    private static boolean isSingle(Class<?> clazz) {
        return isBoolean(clazz) || isNumber(clazz) || isString(clazz);
    }

    /**
     * 是否布尔值
     **/
    public static boolean isBoolean(Class<?> clazz) {
        return (clazz != null) && ((Boolean.TYPE.isAssignableFrom(clazz)) || (Boolean.class.isAssignableFrom(clazz)));
    }

    /**
     * 是否数值
     **/
    public static boolean isNumber(Class<?> clazz) {
        return (clazz != null)
                && ((Byte.TYPE.isAssignableFrom(clazz)) || (Short.TYPE.isAssignableFrom(clazz)) || (Integer.TYPE.isAssignableFrom(clazz)) || (Long.TYPE.isAssignableFrom(clazz))
                || (Float.TYPE.isAssignableFrom(clazz)) || (Double.TYPE.isAssignableFrom(clazz)) || (Number.class.isAssignableFrom(clazz)));
    }

    /**
     * 判断是否是字符串
     **/
    public static boolean isString(Class<?> clazz) {
        return (clazz != null) && ((String.class.isAssignableFrom(clazz)) || (Character.TYPE.isAssignableFrom(clazz)) || (Character.class.isAssignableFrom(clazz)));
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object get(Context context, String key, Object defaultObject) {
        SharedPreferences sp = getSharedPreferences(context);
        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        SharedPreferences sp = getSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public static void clear(Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean contains(Context context, String key) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getAll();
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }

    private PreferenceUtils() {
    }

    public static boolean setValue(Context context, String key, String value) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.edit().putString(key, value).commit();
    }

    public static String getValue(Context context, String key, String defValue) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getString(key, defValue);
    }

    public static boolean setValue(Context context, String name, String key, String value) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.edit().putString(key, value).commit();
    }

    public static String getValue(Context context, String name, String key, String defValue) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getString(key, defValue);
    }

    public static boolean setValue(Context context, String key, boolean value) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.edit().putBoolean(key, value).commit();
    }

    public static boolean getValue(Context context, String key, boolean defValue) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getBoolean(key, defValue);
    }

    public static boolean setValue(Context context, String name, String key, boolean value) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.edit().putBoolean(key, value).commit();
    }

    public static boolean getValue(Context context, String name, String key, boolean defValue) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getBoolean(key, defValue);
    }

    public static boolean setValue(Context context, String name, String key, int value) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.edit().putInt(key, value).commit();
    }

    public static int getValue(Context context, String name, String key, int defValue) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getInt(key, defValue);
    }

    public static boolean setValue(Context context, String key, int value) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.edit().putInt(key, value).commit();
    }

    public static int getValue(Context context, String key, int defValue) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getInt(key, defValue);
    }

    public static boolean setValue(Context context, String key, long value) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.edit().putLong(key, value).commit();
    }

    public static long getValue(Context context, String key, long defValue) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getLong(key, defValue);
    }

    public static void clearSettings(Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().clear().commit();
    }

    public static void clearSettings(Context context, Class<?> clazz) {
        SharedPreferences sp = context.getSharedPreferences(clazz.getName(), Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        if (sp == null) {
            sp = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return sp;
    }

    private static SharedPreferences sp = null;
}
