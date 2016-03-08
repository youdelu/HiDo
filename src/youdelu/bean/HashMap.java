package youdelu.bean;

import java.util.RandomAccess;

import youdelu.util.JSON;
import youdelu.util.Log;
import youdelu.util.StringUtil;

/**
 * 
 * @author 游德禄
 *
 * @param <K>
 * @param <V>
 */
public class HashMap<K, V> extends java.util.HashMap<K, V> implements Map<K, V>, RandomAccess, Cloneable, java.io.Serializable{
 
	private static final long serialVersionUID = 1L;

	@Override
	public <T> T toEntity(T t) {
		try {
			return StringUtil.map2Bean(t, this);
		} catch (Exception e) {
			 Log.p("无法将结果转化为该实体,请确定", true);
			 String d = e.getMessage();
			 if(d!=null)
			 Log.p(e.getMessage(), true);
		}
		return t;
	}

	@Override
	public String toJson() {
		return JSON.getJson(this);
	}

}
