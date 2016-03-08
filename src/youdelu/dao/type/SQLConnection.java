package youdelu.dao.type;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import youdelu.dao.Data;
import youdelu.util.Log;

/**
 * 
 * @author 游德禄
 *
 */
public class SQLConnection {

	/**
	 * 连接数据库驱动
	 */
	protected String[] _sa ; 
	/**
	 * SQLConnection构造函数
	 * 
	 * @param String
	 * @throws Exception
	 */
	public SQLConnection() throws Exception {
		String[] _sa = Data.getInstance().getConfig().split(",");
		if(_sa!=null){
			Class.forName(_sa[0]);
			try{
				this.connection = DriverManager.getConnection(_sa[1], _sa[2], _sa[3]);
			}catch(Exception e){
				Log.p("数据库连接失败，请检查 HiDoCongfig.json 文件中的配置是否正确", true);
				return;
			}
			
		}else{
			Log.p("数据库配置错误，未尝试连接",true);
			return;
		}
	}

	/**
	 * 关闭数据库连接
	 * 
	 * @throws Exception
	 */
	public void Close() {
		if (this.connection != null) {
			try {
				this.connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 从OracleConnection类创建事务处理的OracleTransaction类实例
	 * 
	 * @return
	 * @throws Exception
	 */
	public SQLTransaction BeginTransaction() throws Exception {
		SQLTransaction tran = new SQLTransaction(this);
		return tran;
	}

	/**
	 * 获取java.sql.Connection对象
	 * 
	 * @return java.sql.Connection对象
	 */
	public Connection GetConnection() {
		return this.connection;
	}

	private Connection connection = null;

}
