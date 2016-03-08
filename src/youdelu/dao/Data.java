/**  
 *	   @company  澳亚卫视
 *	   @author  游德禄
 *     @Email youdelu@sina.cn
 *     @date  2015年10月28日 下午5:20:06 
 *     @version 1.0 
 *     @parameter  
 *     @return  
 *     
 */
package youdelu.dao;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Iterator;

import youdelu.dao.type.DataType;
import youdelu.dao.type.DataType.Bean;
import youdelu.util.ClearComment;
import youdelu.util.JSON;
import youdelu.util.Log;

/**
 * @author 游德禄
 *
 */
public class Data {
	/** 根目录 */  
    private static String rootDir =  Thread.currentThread().getContextClassLoader().getResource("").getPath()+"HiDoConfig.json";   
	public volatile DataType data = new DataType();
	private volatile static Data instance;
	public volatile static boolean isRun = false ;
	private boolean success = false ;
	public static Data getInstance() {
		if (instance == null) {
			synchronized (Data.class) {
				if (instance == null) {
					instance = new Data();
				}
			}
		}
		return instance;
	}
	public Data(){
		long l = Calendar.getInstance().getTimeInMillis();
		Log.p("=====   欢迎使用 HiDo   =====");
		Log.p("作者：游德禄，交流Q群346143484");
		try {
			String s = ClearComment.clearComment(rootDir);
			if(s!=null){
				Log.p("已成功载入配置 HiDoConfig.json");
				try{
					data = JSON.getObjectFromJson(s);
					if(data!=null){
						Log.p("已成功解析配置文件");
						success = true ;
					}
				}catch(Exception e){
					Log.p("配置文件 HiDoConfig.json 解析出错，请确定是json格式",true);
				}
			}
		} catch (FileNotFoundException e) {
			Log.p("没有找到项目根目录下的配置文件 HiDoConfig.json",true);
		} catch (UnsupportedEncodingException e) {
			Log.p("配置文件 HiDoConfig.json  的编码不正确，无法解析，建议使用UTF-8",true);
		}
		if(success){
			Log.p("已成功启动，用时 "+(Calendar.getInstance().getTimeInMillis()-l)+" 毫秒");
		}else{
			Log.p("很抱歉 HiDo 启动失败，请检查配置是否正确",true);
			instance = null ;
		}
		isRun = true ;
	}
	public boolean showLog(){
		if(data==null)
			return true;
		return !data.getConfig().isShowlog();
	}
	public String getDateformat(){
		return data.getConfig().getDateformat();
	}
	public String getConfig() {
		String con = data.getConfig().getDriver()+","+ data.getConfig().getUrl()+","+data.getConfig().getUsername()+","+data.getConfig().getPassword();
		return con ;
	}
	public  Bean getTable(Object key){
		if(key instanceof Integer){
			int i = (int)key ;
			i--;
			try{
				Object[] it =data.getTable().keySet().toArray();
				for (int j = 0; j < it.length; j++) {
					if(i==j){
						Object k=it[j];
						Bean s = data.getTable().get(k);
						if(s!=null)
							return s ;
					}
				}
			}catch(Exception e){
				Log.p("没有找到配置中索引位置为 "+key+" 的表"+e.getMessage(), true);
			}
		}else{
			Bean b = data.getTable().get(key);
			if(b!=null){
				return b;
			}
		}
		return null;
	}
	public String getTableName(int i){
		i--;
		try{
			Object[] it =data.getTable().keySet().toArray();
			for (int j = 0; j < it.length; j++) {
				if(i==j){
					Object k=it[j];
					if(k!=null)
						return (String) k;
				}
			}
		}catch(Exception e){
			 
		}
		return null;
	}
	public int createAll(){
		int size = 0 ;
			Iterator<String> it = data.getTable().keySet().iterator();
			while(it.hasNext()){
				String key=it.next();
				DBHelper.executeSql2("drop table if exists "+key);
				String[] clo = data.getTable().get(key).getColumns();
				if(clo.length>0){
					 String sql = "create table "+key+" (";
					 for (int i = 0 ; i< clo.length  ;i++) {
						 String s = clo[i] ;
						 sql+=s.contains(" ")?s:s+" text ";
						 if ( clo.length  > 0 && i !=  clo.length  - 1) {
							 sql+=" , ";
						 }
					}
					 sql+=" ) ";
					 size += (DBHelper.executeSql2(sql)==0)?1:0;
				}
		}
		Log.p("已成功创建 "+size+" 张表");
		return size;
	}
}
