package youdelu.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import youdelu.dao.Data;
import youdelu.dao.type.DataType;
 

/**
 * 
 * @author 游德禄
 */
public class JSON {
	public static String getJson(Object object) {
		String f = Data.getInstance().getDateformat();
		if(StringUtil.isEmpty(f)){
			f = "yyyy-MM-dd HH:mm:ss" ;
		}
		Gson gson = new GsonBuilder()  
		  .setDateFormat(f)  
		  .create();
		return gson.toJson(object);
	}
	public static DataType getObjectFromJson(String str){
		Gson gson = new Gson();
		DataType d = gson.fromJson(str, DataType.class); 
		return d;
	}
}
