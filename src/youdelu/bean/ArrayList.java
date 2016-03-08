/**  
 *	   @company  澳亚卫视
 *	   @author  游德禄
 *     @Email youdelu@sina.cn
 *     @date  2015年10月29日 下午3:00:56 
 *     @version 1.0 
 *     @parameter  
 *     @return  
 *     
 */
package youdelu.bean;

import youdelu.bean.List;
import youdelu.util.JSON;

import java.util.RandomAccess;

/**
 * @author 游德禄
 * @param <E>
 *
 */
public class ArrayList<E> extends java.util.ArrayList<E>  implements List<E>, RandomAccess, Cloneable, java.io.Serializable{
 
	private static final long serialVersionUID = -8541928285817009568L;

	@Override
	public String toJson() {
		return JSON.getJson(this);
	}
}
