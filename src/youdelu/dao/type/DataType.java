/**  
 *	   @company  澳亚卫视
 *	   @author  游德禄
 *     @Email youdelu@sina.cn
 *     @date  2015年10月28日 下午5:49:00 
 *     @version 1.0 
 *     @parameter  
 *     @return  
 *     
 */
package youdelu.dao.type;

import java.util.HashMap;
import java.util.Map;


/**
 * @author 游德禄
 *
 */
public class DataType {
	private Config config;
	public class Config{
		private String driver;
		private String url ;
		private String username;
		private String password;
		private boolean showlog;
		private String dateformat;
		public String getDriver() {
			return driver;
		}
		public void setDriver(String driver) {
			this.driver = driver;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public boolean isShowlog() {
			return showlog;
		}
		public void setShowlog(boolean showlog) {
			this.showlog = showlog;
		}
		public String getDateformat() {
			return dateformat;
		}
		public void setDateformat(String dateformat) {
			this.dateformat = dateformat;
		}
	}
	private  Map<String,Bean> table = new HashMap<String,Bean>();
	public static	class Bean{
			private String[] columns;
			private  Map<String,Object> sql = new HashMap<String,Object>();
			public String[] getColumns() {
				return columns;
			}
			public void setColumns(String[] columns) {
				this.columns = columns;
			}
			public Map<String,Object> getSql() {
				return sql;
			}
			public void setSql(Map<String,Object> sql) {
				this.sql = sql;
			}
		}
	public Config getConfig() {
		return config;
	}
	public void setConfig(Config config) {
		this.config = config;
	}
	public Map<String,Bean> getTable() {
		return table;
	}
	public void setTable(Map<String,Bean> table) {
		this.table = table;
	}
}
