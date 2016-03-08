package youdelu.bean;

 
/**
 * 
 * @author 游德禄
 *
 * @param <K>
 * @param <V>
 */
public interface Map<K, V> extends java.util.Map<K, V>{
	/**
	 * 将Map转化为实体对象
	 * 用法：Entity e = xx.toEntity(new Entity());
	 */
	public <T> T toEntity(T t);
	/**
	 * 将Map对象转化为json格式字符串
	 */
	public String toJson();
}
