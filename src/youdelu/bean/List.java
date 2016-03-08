/**  
 *	   @company  澳亚卫视
 *	   @author  游德禄
 *     @Email youdelu@sina.cn
 *     @date  2015年10月29日 下午2:35:01 
 *     @version 1.0 
 *     @parameter  
 *     @return  
 *     
 */
package youdelu.bean;


/**
 * @author 游德禄
 * @param <E>
 *
 */
public  interface List<E> extends java.util.List<E>{
	/**  
	 * 将List对象转化为json格式字符串
	 */
	public String toJson();
}
