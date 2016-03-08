/**  
 *	   @company  澳亚卫视
 *	   @author  游德禄
 *     @Email youdelu@sina.cn
 *     @date  2015年10月28日 下午3:51:18 
 *     @version 1.0 
 *     @parameter  
 *     @return  
 *     
 */
package youdelu.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import youdelu.bean.HashMap;

/**
 * 
 * @author 游德禄
 *
 */
public class StringUtil {
	/**
	 * 判断字符串为空
	 *
	 * @param str
	 *            字符串信息
	 * @return true or false
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}
 
	/**
	 * 
	 * @param map
	 * @param index
	 * @return
	 */
	public static Object getMapKey(Map<String,Object> map,int index){
		Object[] it = map.keySet().toArray();
		for (int j = 0; j < it.length; j++) {
			if(j==index){
				Object s =  map.get(it[j]);
				if(s!=null)
				return s ;
			}
		}
		return null;
	}
	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param t
	 * @param hashMap
	 * @return
	 * @throws Exception
	 */
	public static <T, K, V> T map2Bean(T t,HashMap<K, V> hashMap) throws Exception{
	    Class<? extends Object> clazz = t.getClass();
	    //实例化类
		@SuppressWarnings("unchecked")
		T entity = (T)clazz.newInstance();
		@SuppressWarnings("unchecked")
		Set<String> keys = (Set<String>) hashMap.keySet();
		//变量map 赋值
		for(String key:keys){
			String fieldName = key;
			//判断是sql 还是hql返回的结果
			if(key.equals(key.toUpperCase())){
				//获取所有域变量
				Field[] fields = clazz.getDeclaredFields();
				for(Field field: fields){
					if(field.getName().toUpperCase().equals(key)) fieldName=field.getName();
					break;
				}
			}
			//设置赋值
			try {
				//参数的类型  clazz.getField(fieldName)
				Class<?> paramClass = clazz.getDeclaredField(fieldName).getType();
				//拼装set方法名称
				String methodName = "set"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
				//根据名称获取方法
				Method method = clazz.getMethod(methodName, paramClass);
				//调用invoke执行赋值
				method.invoke(entity, hashMap.get(key));
			} catch (Exception e) {
				//NoSuchMethod
			}
		}
		return entity;
	}
}
