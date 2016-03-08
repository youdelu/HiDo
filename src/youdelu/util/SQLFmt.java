/**  
 *	   @company  澳亚卫视
 *	   @author  游德禄
 *     @Email youdelu@sina.cn
 *     @date  2015年10月15日 下午2:26:03 
 *     @version 1.0 
 *     @parameter  
 *     @return  
 *     
 */
package youdelu.util;

import youdelu.bean.ForEntity;


/**
 * 格式化SQL语句
 * @author 游德禄
 *
 */
public class SQLFmt {
	public static String getIds(Object[] obj){
		String str = "";
		for(int i = 0 ; i< obj.length  ;i++){
			str += obj[i] ;
			if (obj.length > 0 && i != obj.length - 1) {
				str += ",";
			}
		}
		 return str;
	}
	/**
	 * 
	 * @param table
	 * @param is
	 * @return
	 */
	public static String getRow(String[] table,boolean is){
		return getRow(table,is,0);
	}
	/**
	 * 将列表名转化为sql语句格式
	 * @param table
	 * @param is
	 * @return
	 */
	public static String getRow(String[] table,boolean is,int s){
		int size = s ==0?table.length:s;
		if(size> table.length){
			size = table.length;
		}
		String str = "";
		for(int i = 0 ; i< size  ;i++){
			str += table[i] +(is?" = ? ":"");
			if (size > 0 && i != size - 1) {
				str += ",";
			}
		}
		return str ;
	}
	public static String getRow(String[] table,String clumns){
		return getRow(table,clumns,false);
	}
	/**
	 *  将列表名转化为sql语句格式
	 * @param table
	 * @param clumns
	 * @return
	 */
	public static String getRow(String[] table,String clumns,boolean and){
		String str = "";
		String[] clu = clumns.split(",");
		System.out.println(clu.length);
		for(int i = 0 ; i<  clu.length;i++){
			String ss = clu[i];
			if(ss.matches("\\d+")){
				int d = Integer.parseInt(ss)-1 ;
				if(d>=0&&d<table.length){
					ss = table[d] ;
				}else{
					Log.p("索引超出范围 "+ ss,true);
					break;
				}
			}
			str += ss+" = ? ";
			if (clu.length > 0 && i != clu.length - 1) {
				str += and?" and ":" , ";
			}
		}
		return str ;
	}
	public static String getS(int len){
		return getS(len,null);
	}
	public static String getS(int len,String[] table){
		if(table!=null){
			if(len> table.length){
				len = table.length;
			}
		}
		String str = "";
		for(int i = 0 ; i<  len;i++){
			str += "?";
			if (len > 0 && i != len - 1) {
				str += ",";
			}
		}
		return str ;
	}
	public static String getEntity(Object obj,int i){
		String d = null ;
		if(i==0){
			d = JSON.getJson(obj).replace("\"", "");
		}else if(i==1){
			d = JSON.getJson(obj).replace("\"", "'").replace(":", "=");
		}
		return d.substring(1, d.length()-1);
	}
	public static ForEntity getEntity(Object obj){
		String d = SQLFmt.getEntity(obj,0);
		ForEntity fe = new ForEntity();
		if(d.contains(",")){
			String[] a = d.split(",");
			fe.left = new String[a.length];
			fe.right = new String[a.length];
			for (int i = 0 ; i < a.length ; i++) {
				String[] s = a[i].split(":");
				fe.left[i] = s[0];
				fe.right[i] = s[1];
			}
		}else{
			String[] s = d.split(":");
			fe.left = new String[1];
			fe.right = new String[1];
			fe.left[0] = s[0];
			fe.right[0] = s[1];
		}
		return fe;
	}
	public static Object[] getObject(Object... obj){
		Object[] o = null;
		if(obj!=null){
			o = new Object[obj.length];
			for(int i = 0 ; i < obj.length ; i ++){
				o[i] = obj[i];
			}
		}
		return o ;
	}
	public static String getS(String s){
		String[] str = s.split(",");
		String ss = "" ;
		for (int i = 0; i < str.length; i++) {
			ss+= "'"+str[i]+"'";
			if (str.length > 0 && i != str.length - 1) {
				ss += ",";
			}
		}
		return ss;
	}
}
