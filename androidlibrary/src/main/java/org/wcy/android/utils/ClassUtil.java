package org.wcy.android.utils;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


/**
 * 
* @Title: ClassUtil.java
* @Description:反射工具类
* @author Wangchaoyong   
* @date 下午2:34:57 
* @version V1.0
 */
@SuppressWarnings({"rawtypes"})
public class ClassUtil {

	/**
	 * 通过反射，获得指定类的父类泛型参数的第一个实际类型，即获得UserDao extends BaseDao<User>中的User.
	 * 
	 * @param clazz
	 *            反射的class对象
	 * @return 父类泛型参数对应实际类
	 * @author wangchaoyong
	 */
	public static Class getGenericClassTpye(Class clazz,int i) {
		// 得到泛型父类,包含泛型参数信息,如:Class<User>
		Type genericClassType = clazz.getGenericSuperclass();
		// 所有泛型必须实现ParameterizedType接口，如没实现则不是泛型，直接返回Object.class
		if (!(genericClassType instanceof ParameterizedType)) {
			return Object.class;
		}
		// 得到泛型实际参数中对应的Class,如Class<User>中的User,参数可为多个，所以定义为数组
		Type params[] = ((ParameterizedType) genericClassType)
				.getActualTypeArguments();
		if (!(params.length > 0 && params != null)) {
			return Object.class;
		}
		if (!(params[i] instanceof Class)) {
			return Object.class;
		}
		return (Class) params[i];

	}
	public static Class getGenericClassTpye(Class clazz){
		return getGenericClassTpye(clazz,0);
	}
	public static <T> T getT(Object o, int i) {
		try {
			// 得到泛型父类,包含泛型参数信息,如:Class<User>
			Type genericClassType = o.getClass().getGenericSuperclass();
			// 得到泛型实际参数中对应的Class,如Class<User>中的User,参数可为多个，所以定义为数组
			Type params[] = ((ParameterizedType) genericClassType).getActualTypeArguments();
			if (params!=null && params.length > i) {
				return ((Class<T>) params[i]).newInstance();
			}
		} catch (Exception e) {
		}
		return null;
	}
}
