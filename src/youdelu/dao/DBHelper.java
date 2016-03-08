package youdelu.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import youdelu.bean.ArrayList;
import youdelu.bean.HashMap;
import youdelu.bean.List;
import youdelu.bean.Map;
import youdelu.dao.type.CommandType;
import youdelu.dao.type.DataColumn;
import youdelu.dao.type.DataRow;
import youdelu.dao.type.DataSet;
import youdelu.dao.type.DataTable;
import youdelu.dao.type.Parameter;
import youdelu.dao.type.ParameterDirection;
import youdelu.dao.type.SQLConnection;
import youdelu.dao.type.SQLTransaction;
import youdelu.util.Log;
import youdelu.util.SQLFmt;
import youdelu.util.SQLFormat;


/**
 * 
 * @author 游德禄
 *
 */
public class DBHelper {
	// 连接字符串
	
	public static final int CURSOR = -10;
	
	/**
	 * 根据sql与参数获得List<Map<String, Object>>集合
	 * 
	 * @param sql
	 *            要执行的sql语句
	 * @param sqlParameter
	 *            参数
	 * @return
	 */
	public static List<Map<String, Object>> getListMap(String sql,
			Object[] sqlParameter) {
		long l = Calendar.getInstance().getTimeInMillis();
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		try {
			connection = new SQLConnection().GetConnection();
			ps = connection.prepareStatement(sql);
			if (sqlParameter != null) {
				for (int i = 0; i < sqlParameter.length; i++) {
					ps.setObject(i + 1, sqlParameter[i]);
				}
			}
			resultSet = ps.executeQuery();
			list = convertrResultSetToList(resultSet);
		} catch (Exception e) {
			Log.p("该语句执行失败  "+sql ,true);
			String m = e.getMessage() ;
			if(m!=null) Log.p(m,true);
			return null;
		} finally {
			close(connection, ps, resultSet);
		}
		String s = "执行SQL语句  \n"+SQLFormat.format(sql)+"\n{耗时："+(Calendar.getInstance().getTimeInMillis()-l)+"毫秒}" ;
		Log.p(s);
		return list;
	}
	public static List<Map<String, Object>> getListMap2(String sql,
			Object... sqlParameter) {
		return getListMap(sql,SQLFmt.getObject(sqlParameter));
	}
	/**
	 * 执行sql,主要完成，增加，修改，删除，DDL
	 * 
	 * @param sql
	 *            要执行的sql
	 * @param sqlParameter
	 *            sql参数
	 * @return 影响行数
	 */
	public static int executeSql(String sql, Object[] sqlParameter) {
		long l = Calendar.getInstance().getTimeInMillis();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		int count = -1;
		try {
			connection = new SQLConnection().GetConnection();
			ps = connection.prepareStatement(sql);
			cNull2BlkArray(sqlParameter);
			if (sqlParameter != null) {
				for (int i = 0; i < sqlParameter.length; i++) {
					if (sqlParameter[i] instanceof Date) {
						ps.setObject(i + 1, sqlParameter[i],
								java.sql.Types.DATE);
					} else {
						ps.setObject(i + 1, sqlParameter[i]);
					}
				}
			}
			count = ps.executeUpdate();
		} catch (Exception e) {
			Log.p("该语句执行失败  "+sql ,true);
			String m = e.getMessage() ;
			if(m!=null) Log.p(m,true);
			return -1;
		} finally {
			close(connection, ps, resultSet);
		}
		String s = "执行SQL语句  \n"+SQLFormat.format(sql)+"\n{耗时："+(Calendar.getInstance().getTimeInMillis()-l)+"毫秒}" ;
		Log.p(s);
		return count;
	}

	/**
	 * 执行sql,主要完成，增加，修改，删除，DDL
	 * 
	 * @param sql
	 *            要执行的sql
	 * @param sqlParameter
	 *            sql参数
	 * @return 影响行数
	 */
	public static int executeSql2(String sql, Object... sqlParameter) {
		return executeSql(sql,SQLFmt.getObject(sqlParameter));
	}

	/**
	 * 将null的字符转换成空,sql参数问题，当参数为null时异常
	 */
	public static Object cNull2Blk(Object str) {
		return str == null ? "" : str;
	}

	public static void cNull2BlkArray(Object[] sqlParameter) {
		for (int i = 0; i < sqlParameter.length; i++) {
			if (sqlParameter[i] instanceof String) {
				sqlParameter[i] = cNull2Blk(sqlParameter[i]);
			}
			else if (sqlParameter[i] instanceof Date) {
				DateFormat dateFormat = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				sqlParameter[i] = dateFormat.format(sqlParameter[i]);
			}
			
			Object obj = false;
			if(obj instanceof Boolean){
				if((boolean)obj){
					
				}
			}

		}
	}
 
	/**
	 * 关闭，释放资源
	 * 
	 * @param statement
	 * @param resultset
	 */
	public static void close(Connection connection, Statement statement,
			ResultSet resultSet) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 执行查询，获得数据集
	 * 
	 * @param 
	 *            连接字符串
	 * @param commandType
	 *            sql类型,可以是存储过程
	 * @param commandText
	 *            sql
	 * @return 数据集
	 * @throws Exception
	 */
	public static DataSet GetDataSet(CommandType commandType, String commandText)
			throws Exception {
		return GetDataSet(new SQLConnection(), commandType,
				commandText);
	}

	/**
	 * 执行查询，获得数据集 带参数
	 * 
	 * @param 
	 * @param commandType
	 * @param commandText
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public static DataSet GetDataSet(CommandType commandType,
			String commandText, Parameter... parameters) throws Exception {
		return GetDataSet(new SQLConnection(), commandType,
				commandText, parameters);
	}

	/**
	 * 执行sql返回一个数据集
	 * 
	 * @param connection
	 * @param commandType
	 * @param commandText
	 * @return
	 * @throws Exception
	 */
	public static DataSet GetDataSet(SQLConnection connection,
			CommandType commandType, String commandText) throws Exception {
		return GetDataSet(connection, commandType, commandText,
				new Parameter[0]);
	}

	/**
	 * 
	 * @param connection
	 * @param commandType
	 * @param commandText
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public static DataSet GetDataSet(SQLConnection connection,
			CommandType commandType, String commandText,
			Parameter... parameters) throws Exception {

		DataSet ds = new DataSet();
		if (commandType.equals(CommandType.Text)) {
			Statement s = connection.GetConnection().createStatement();
			ResultSet rs = s.executeQuery(commandText);
			ds.AddTable(ConvertResultSetToDataTable(rs));
		} else if (commandType.equals(CommandType.StoreProcedure)) {

			Parameter[] paras = parameters;

			String sql = "";
			for (int i = 0; i < paras.length; i++) {
				sql = sql + "?,";
			}
			if (sql.length() > 0) {
				sql = "(" + sql.substring(0, sql.length() - 1) + ")";
			}

			sql = "{ call " + commandText + sql + " }";
			CallableStatement proc = null;
			proc = connection.GetConnection().prepareCall(sql);
			for (int i = 0; i < paras.length; i++) {
				Parameter p = paras[i];
				if (p.parameterDirection == ParameterDirection.IN) {
					proc.setObject(i + 1, p.Value, p.parameterType);
				} else if (p.parameterDirection == ParameterDirection.OUT) {
					proc.registerOutParameter(i + 1, p.parameterType);
				}
			}
			try {
				proc.execute();

				for (int i = 0; i < paras.length; i++) {
					Parameter p = paras[i];
					if (p.parameterDirection == ParameterDirection.OUT) {
						p.Value = proc.getObject(i + 1);
						if (p.parameterType == CURSOR) {
							ResultSet rs = (ResultSet) p.Value;
							DataTable dt = ConvertResultSetToDataTable(rs);
							DataSet _lds = new DataSet();
							_lds.AddTable(dt);
							p.Value = _lds;
							ds.AddTable(dt);
						}
					}
				}
			} catch (Exception e) {
				throw e;
			} finally {
				connection.Close();
			}

		} else {
			throw new Exception("命令类型不正确");
		}

		return ds;
	}

	/**
	 * 
	 * @param transaction
	 * @param commandType
	 * @param commandText
	 * @return
	 * @throws Exception
	 */
	public static DataSet GetDataSet(SQLTransaction transaction,
			CommandType commandType, String commandText) throws Exception {
		return GetDataSet(transaction.Connection, commandType, commandText);
	}

	/**
	 * 
	 * @param transaction
	 * @param commandType
	 * @param commandText
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public static DataSet GetDataSet(SQLTransaction transaction,
			CommandType commandType, String commandText,
			Parameter... parameters) throws Exception {
		return GetDataSet(transaction.Connection, commandType, commandText,
				parameters);
	}

	/**
	 * 
	 * @param 
	 * @param commandType
	 * @param commandText
	 * @throws Exception
	 */
	public static void ExecuteNonQuery(CommandType commandType,
			String commandText) throws Exception {
		ExecuteNonQuery(new SQLConnection(), commandType,
				commandText);
	}

	/**
	 * 
	 * @param 
	 * @param commandType
	 * @param commandText
	 * @param parameters
	 * @throws Exception
	 */
	public static void ExecuteNonQuery(CommandType commandType,
			String commandText, Parameter... parameters) throws Exception {
		ExecuteNonQuery(new SQLConnection(), commandType,
				commandText, parameters);
	}

	/**
	 * 
	 * @param connection
	 * @param commandType
	 * @param commandText
	 * @throws Exception
	 */
	public static void ExecuteNonQuery(SQLConnection connection,
			CommandType commandType, String commandText) throws Exception {
		ExecuteNonQuery(connection, commandType, commandText, new Parameter[0]);
	}

	/**
	 * 
	 * @param connection
	 * @param commandType
	 * @param commandText
	 * @param parameters
	 * @throws Exception
	 */
	public static void ExecuteNonQuery(SQLConnection connection,
			CommandType commandType, String commandText,
			Parameter... parameters) throws Exception {

		if (commandType.equals(CommandType.Text)) {
			Statement s = connection.GetConnection().createStatement();
			s.execute(commandText);

		} else if (commandType.equals(CommandType.StoreProcedure)) {

			Parameter[] paras = parameters;

			String sql = "";
			for (int i = 0; i < paras.length; i++) {
				sql = sql + "?,";
			}
			if (sql.length() > 0) {
				sql = "(" + sql.substring(0, sql.length() - 1) + ")";
			}

			sql = "{ call " + commandText + sql + " }";
			CallableStatement proc = null;
			proc = connection.GetConnection().prepareCall(sql);
			for (int i = 0; i < paras.length; i++) {
				Parameter p = paras[i];
				if (p.parameterDirection == ParameterDirection.IN) {
					proc.setObject(i + 1, p.Value, p.parameterType);
				} else if (p.parameterDirection == ParameterDirection.OUT) {
					proc.registerOutParameter(i + 1, p.parameterType);
				}
			}
			try {
				proc.execute();

				for (int i = 0; i < paras.length; i++) {
					Parameter p = paras[i];
					if (p.parameterDirection == ParameterDirection.OUT) {
						p.Value = proc.getObject(i + 1);
						if (p.parameterType == CURSOR) {
							ResultSet rs = (ResultSet) p.Value;
							DataTable dt = ConvertResultSetToDataTable(rs);
							DataSet _lds = new DataSet();
							_lds.AddTable(dt);
							p.Value = _lds;
						}
					}
				}
			} catch (Exception e) {
				throw e;
			} finally {
				connection.Close();
			}

		} else {
			throw new Exception("commandType is invalid");
		}
	}

	/**
	 * 
	 * @param transaction
	 * @param commandType
	 * @param commandText
	 * @throws Exception
	 */
	public static void ExecuteNonQuery(SQLTransaction transaction,
			CommandType commandType, String commandText) throws Exception {
		ExecuteNonQuery(transaction.Connection, commandType, commandText);
	}

	/**
	 * 
	 * @param transaction
	 * @param commandType
	 * @param commandText
	 * @param parameters
	 * @throws Exception
	 */
	public static void ExecuteNonQuery(SQLTransaction transaction,
			CommandType commandType, String commandText,
			Parameter... parameters) throws Exception {
		ExecuteNonQuery(transaction.Connection, commandType, commandText,
				parameters);
	}

	/**
	 * 将ResultSet装换为DataTable的函数
	 * 
	 * @param rs
	 * @return
	 * @throws Exception
	 */
	private static DataTable ConvertResultSetToDataTable(ResultSet rs)
			throws Exception {
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();

		DataTable dt = new DataTable();
		while (rs.next()) {
			DataRow dr = new DataRow();
			for (int i = 1; i <= columnCount; i++) {
				DataColumn dc = new DataColumn(rsmd.getColumnName(i)
						.toLowerCase(), rs.getObject(i));
				dr.AddColumn(dc);
			}
			dt.AddRow(dr);
		}
		return dt;
	}

	/**
	 * 将resultSet转换成List<Map<String, Object>>
	 * 
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	private static List<Map<String, Object>> convertrResultSetToList(
			ResultSet resultSet) throws SQLException {
		ResultSetMetaData rsmd = resultSet.getMetaData();
		int columnCount = rsmd.getColumnCount();

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		while (resultSet.next()) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 1; i <= columnCount; i++) {
				map.put(rsmd.getColumnName(i).toLowerCase(),
						resultSet.getObject(i));
			}
			list.add(map);
		}
		return list;
	}

	/**
	 * 一个简单的将DataTable转换成json的方法
	 * 
	 * @param dt
	 * @return
	 */
	public static String getJSON(DataTable dt) {
		// [{k:v,k:v},{k:v,k:v}]
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		try {
			if (dt.Rows.length > 0) {
				for (int i = 0; i < dt.Rows.length; i++) {
					sb.append("{");
					for (int j = 0; j < dt.Rows[i].Columns.length; j++) {
						sb.append(String.format("\"%1$s\":\"%2$s\"",
								dt.Rows[i].Columns[j].colName,
								dt.Rows[i].Columns[j].colValue));
						if (j < (dt.Rows[i].Columns.length - 1)) {
							sb.append(",");
						}
					}
					sb.append("}");
					if (i < (dt.Rows.length - 1)) {
						sb.append(",");
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			sb.append("]");
		}
		return sb.toString();
	}

	/**
	 * 获得分页的开始索引
	 * 
	 * @param page
	 *            第几页从1开始
	 * @param rows
	 *            每页分多少
	 * @return 开始索引
	 */
	public static int getStart(int page, int rows) {
		return (page - 1) * rows ;
	}
}
