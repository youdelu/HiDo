package youdelu.dao;

import java.util.regex.Pattern;

import youdelu.bean.ArrayList;
import youdelu.bean.ForEntity;
import youdelu.bean.HashMap;
import youdelu.bean.List;
import youdelu.bean.Map;
import youdelu.dao.type.DataType.Bean;
import youdelu.util.Log;
import youdelu.util.SQLFmt;
import youdelu.util.StringUtil;

/**
 * 实现各种增删改查  用法： HiDo hido = HiDo.open(tableName);  
 * 
 * @author 游德禄
 *
 */
public class HiDo {
	private static class ConfigInstance {
		private volatile static HiDo hido;
		private volatile static String name;
		private volatile static String[] fullcolumns;
		private volatile static String[] columns;
		private volatile static int size;
		private volatile static Map<String, Object> sql = new HashMap<String, Object>();
	}

	/**
	 * 载入一张表的数据进行相关操作
	 * 
	 * @param tableName
	 * @return
	 */
	public static HiDo open(Object tableName) {
		ConfigInstance.hido = new HiDo();
		Bean b = new Bean();
		b = Data.getInstance().getTable(tableName);
		if (tableName instanceof Integer) {
			ConfigInstance.name = Data.getInstance().getTableName(
					(int) tableName);
		} else {
			ConfigInstance.name = (String) tableName;

		}
		if (b != null) {
			ConfigInstance.fullcolumns = b.getColumns();
			java.util.Map<String, Object> m = new java.util.HashMap<String, Object>();
			m = b.getSql();
			Object[] it = m.keySet().toArray();
			for (Object object : it) {
				ConfigInstance.sql.put((String) object, m.get(object));
			}
			ConfigInstance.size = ConfigInstance.fullcolumns.length;
			ConfigInstance.columns = new String[ConfigInstance.size];
			for (int j = 0; j < ConfigInstance.size; j++) {
				String s = ConfigInstance.fullcolumns[j].trim();
				if (s != null)
					ConfigInstance.columns[j] = s.contains(" ") ? s.split(" +")[0]
							: s;
			}
			Log.p("已载入表  " + ConfigInstance.name);
		} else {
			Log.p("没有找到该表 " + tableName, true);
		}
		return ConfigInstance.hido;
	}

	/**
	 * 建立所有表
	 * 
	 * @return
	 */
	public static int createAll() {
		return Data.getInstance().createAll();
	}

	/**
	 * 建表 成功返回true 失败返回false
	 * 
	 * @return
	 */
	public boolean create() {
		DBHelper.executeSql2("drop table if exists " + ConfigInstance.name);
		String sql = "create table " + ConfigInstance.name + " (";
		for (int i = 0; i < ConfigInstance.size; i++) {
			String s = ConfigInstance.fullcolumns[i];
			sql += s.contains(" ") ? s : s + " text ";
			if (ConfigInstance.size > 0 && i != ConfigInstance.size - 1) {
				sql += " , ";
			}
		}
		sql += " ) ";
		return Log.e(DBHelper.executeSql2(sql) == 0);
	}

	/**
	 * 添加数据
	 * 
	 * @param obj
	 *            实体
	 * @return 成功返回 true 失败返回 false
	 */
	public boolean add(Object obj) {
		ForEntity f = SQLFmt.getEntity(obj);
		String sql = "insert into " + ConfigInstance.name + "("
				+ SQLFmt.getRow(f.left, false) + ") ";
		sql += "values(" + SQLFmt.getS(f.right.length) + ")";
		return Log.e(DBHelper.executeSql(sql, f.right) > 0);
	}

	/**
	 * 添加数据
	 * 
	 * @param values
	 *            要插入的数据
	 * @return
	 */
	public boolean add_(Object[] values) {
		String sql = "insert into " + ConfigInstance.name + "("
				+ SQLFmt.getRow(ConfigInstance.columns, false, values.length)
				+ ") ";
		sql += "values(" + SQLFmt.getS(values.length, ConfigInstance.columns)
				+ ")";
		return DBHelper.executeSql(sql, values) > 0;
	}

	/**
	 * 添加可变长数据
	 * 
	 * @param obj
	 * @return
	 */
	public boolean add(Object... obj) {
		return add_(SQLFmt.getObject(obj));
	}

	/**
	 * 更新 默认主键为 第一列
	 * 
	 * @param obj
	 * @return
	 */
	public boolean update(Object obj) {
		return update(obj, 1);
	}

	/**
	 * 更新 默认主键为 第一列
	 * 
	 * @param obj
	 * @return
	 */
	public boolean update_(Object[] obj) {
		return update(obj, 1);
	}

	/**
	 * 更新
	 * 
	 * @param obj
	 *            不定长的值
	 * @return
	 */
	public boolean update(Object... obj) {
		return update(SQLFmt.getObject(obj));
	}

	/**
	 * 更新
	 * 
	 * @param index
	 *            指定主键位置
	 * @param obj
	 *            不定长的值
	 * @return
	 */
	public boolean update_i(int index, Object... obj) {
		return update(SQLFmt.getObject(obj), index);
	}

	/**
	 * 更新
	 * 
	 * @param obj
	 * @param index
	 *            指定表中的主键位置 从 1 开始
	 * @return
	 */
	public boolean update(Object obj, int index) {
		index--;
		if (index < 0) {
			Log.p("主键位置必须在当前表 1 ~ " + (ConfigInstance.columns.length) + " 的范围",
					true);
			return false;
		}
		if (index > ConfigInstance.columns.length - 1) {
			Log.p("主键位置 index = " + index + " 大于当前表中的列，请输入1 ~ "
					+ (ConfigInstance.columns.length) + "的范围", true);
			return false;
		}
		ForEntity s = SQLFmt.getEntity(obj);
		if (s.left.length > 1) {
			String sql = "update " + ConfigInstance.name + " set "
					+ SQLFmt.getRow(s.left, true) + " where "
					+ ConfigInstance.columns[index] + " = ?";
			int size = s.right.length + 1;
			String[] d = new String[size];
			for (int i = 0; i < size - 1; i++) {
				d[i] = s.right[i];
			}
			d[size - 1] = s.right[index];
			return Log.e(DBHelper.executeSql(sql, d) > 0);
		}
		Log.p("您只传递了一个参数和值，无法完成更新操作", true);
		return false;
	}

	/**
	 * 更新
	 * 
	 * @param obj
	 * @param index
	 *            指定主键位置
	 * @return
	 */
	public boolean update(Object[] obj, int index) {
		return update_point(obj, null, index);
	}

	/**
	 * 更新 默认主键位置为 1
	 * 
	 * @param column
	 *            指定的列或位置索引，多个用半角逗号隔开 如： username,password 或 2,3,6
	 * @param obj
	 * @return
	 */
	public boolean update_point(String column, Object... obj) {
		return update_point(SQLFmt.getObject(obj), column, 1);
	}

	/**
	 * 指定列更新
	 * 
	 * @param index
	 *            指定主键
	 * @param column
	 *            指定的列或位置索引，多个用半角逗号隔开 如： username,password 或 2,3,6 或 username,2
	 * @param obj
	 * @return
	 */
	public boolean update_point_i(int index, String column, Object... obj) {
		return update_point(SQLFmt.getObject(obj), column, index);
	}

	/**
	 * 指定列更新
	 * 
	 * @param obj
	 * @param column
	 *            指定的列或位置索引，多个用半角逗号隔开 如： username,password 或 2,3,6
	 * @param index
	 *            指定主键
	 * @return
	 */
	public boolean update_point(Object[] obj, String column, int index) {
		index--;
		if (index < 0) {
			Log.p("主键位置必须在当前表 1 ~ " + (ConfigInstance.columns.length) + " 的范围",
					true);
			return false;
		}
		if (index > ConfigInstance.columns.length - 1) {
			Log.p("主键位置 index = " + index + " 大于当前表中的列，请输入1 ~ "
					+ (ConfigInstance.columns.length) + "的范围", true);
			return false;
		}
		if (obj.length > 1) {
			String sql = "update "
					+ ConfigInstance.name
					+ " set "
					+ (column == null ? SQLFmt.getRow(ConfigInstance.columns,
							true, obj.length) : SQLFmt.getRow(
							ConfigInstance.columns, column)) + " where "
					+ ConfigInstance.columns[index] + "=?";
			int size = obj.length + 1;
			Object[] d = new Object[size];
			for (int i = 0; i < size - 1; i++) {
				d[i] = obj[i];
			}
			d[size - 1] = obj[index];
			return Log.e(DBHelper.executeSql(sql, d) > 0);
		}
		Log.p("您只传递了一个参数和值，无法完成更新操作", true);
		return false;
	}

	/**
	 * 无条件删除本表的数据
	 * 
	 * @return
	 */
	public boolean delete() {
		return delete(0, null);
	}

	/**
	 * 删除数据 默认主键为第 1 列
	 * 
	 * @param obj
	 *            主键值
	 * @return
	 */
	public boolean delete(Object obj) {
		return delete(1, obj);
	}

	/**
	 * 删除数据
	 * 
	 * @param i
	 *            指定条件位置
	 * @param obj
	 *            条件的值
	 * @return
	 */
	public boolean delete(int i, Object obj) {
		Object[] o = null;
		if (obj != null) {
			o = new Object[1];
			o[0] = obj;
		}
		return delete(String.valueOf(i), o);
	}

	/**
	 * 多条件删除
	 * 
	 * @param column
	 *            指定的列或位置索引，多个用半角逗号隔开 如： username,password 或 2,3,6
	 * @param obj
	 * @return
	 */
	public boolean delete_(String column, Object... obj) {
		return delete(column, SQLFmt.getObject(obj));
	}

	/**
	 * 多条件删除
	 * 
	 * @param column
	 *            指定的列或位置索引，多个用半角逗号隔开 如： username,password 或 2,3,6
	 * @param obj
	 * @return
	 */
	public boolean delete(String column, Object[] obj) {
		String sql = "delete from " + ConfigInstance.name;
		if (obj != null) {
			sql += " where "
					+ SQLFmt.getRow(ConfigInstance.columns, column, true);
		}
		return Log.e(DBHelper.executeSql2(sql, obj) > 0);
	}

	/**
	 * 多记录删除
	 * 
	 * @param obj
	 *            条件值 默认条件列为 1
	 * @return 影响行数
	 */
	public int delete_in_(Object[] obj) {
		return delete_in(1, SQLFmt.getIds(obj));
	}

	/**
	 * 多记录删除
	 * 
	 * @param i
	 *            指定条件位置
	 * @param obj
	 *            条件值
	 * @return 影响行数
	 */
	public int delete_in(int i, Object[] obj) {
		return delete_in(i, SQLFmt.getIds(obj));
	}

	/**
	 * 多记录删除
	 * 
	 * @param obj
	 *            不定长条件值 默认主键列 为 1
	 * @return 影响行数
	 */
	public int delete_in(Object... obj) {
		return delete_in(SQLFmt.getObject(obj));
	}

	/**
	 * 多记录删除
	 * 
	 * @param i
	 *            指定条件位置
	 * @param obj
	 *            不定长条件值
	 * @return 影响行数
	 */
	public int delete_in_i(int i, Object... obj) {
		return delete_in(i, SQLFmt.getObject(obj));
	}

	/**
	 * 多记录删除
	 * 
	 * @param i
	 *            指定条件列 1 开始
	 * @param ids
	 * @return 影响行数
	 */
	public int delete_in(int i, String ids) {
		if (i > 0 && i <= ConfigInstance.size) {
			i--;
			if (!Pattern.matches("^(\\d+\\,?)+$", ids)) {
				ids = SQLFmt.getS(ids);
			}
			String sql = "delete from " + ConfigInstance.name + " where "
					+ ConfigInstance.columns[i] + " in (" + ids + ")";
			return DBHelper.executeSql2(sql);
		} else {
			Log.p("指定列 i = " + i + " 的值不在表列 ( 1~ " + (ConfigInstance.size)
					+ ") 范围内", true);
		}
		return 0;
	}

	/**
	 * 获取单条数据  默认索引为第一列
	 * @param value 列索引值  
	 * @return
	 */
	public Map<String, Object> getOne(Object value){
		return getOne(1, value) ;
	}
	/**
	 * 指定索引列 获取单条数据
	 * @param index 列索引位置 可指定位置  position 或直接传列名
	 * @param value 列索引值
	 * @return
	 */
	public Map<String, Object> getOne(Object index, Object value) {
		List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
		String sql = "select " + SQLFmt.getRow(ConfigInstance.columns, false)
				+ " from " + ConfigInstance.name + " where " ;
		if(index instanceof Integer){
			sql+=ConfigInstance.columns[(int)index-1] + " = ? " ;
		}else{
			sql +=  index + " = ? " ; 
		}
		map = DBHelper.getListMap2(sql,value);
		if (map.size() == 1) 
			return map.get(0);
		if(map.size()>1)
		Log.p("该条件已获得多条记录，请使用  query() 函数来获得结果集", true);
		return null;
	}

 
	/**
	 * 获取当前表记录数
	 * @return
	 */
	public long getCount(){
		String sql = "select count(*) from "+ConfigInstance.name;
		return (long)DBHelper.getListMap2(sql).get(0).get("count(*)");
	}
	/**
	 * 获取当前表记录数 带条件
	 * @return
	 */
	public long getCount(String condition){
		String sql = "select count(*) from "+ConfigInstance.name + " where "+condition;
		return (long)DBHelper.getListMap2(sql).get(0).get("count(*)");
	}
	/**
	 * 查询当前表中的全部数据
	 * 
	 * @return
	 */
	public List<Map<String, Object>> query() {
		return query(null);
	}

	/**
	 * 执行各种查询操作
	 * 
	 * @param key
	 *            可以是key或position (从1开始)
	 * @param value
	 *            该项的值对应的是SQL语句里的 ?
	 * @return
	 */
	public List<Map<String, Object>> query(Object key, Object... value) {
		return query_(key, SQLFmt.getObject(value));
	}

	/**
	 * 简单的无参查询，条件需在SQL语句中写死
	 * 
	 * @param key
	 *            可以是key或position (从1开始)
	 * @return
	 */
	public List<Map<String, Object>> query(Object key) {
		return query_(key, null);
	}

	/**
	 * 执行各种查询操作
	 * 
	 * @param key
	 *            可以是key或position (从1开始)
	 * @param value
	 *            该项的值对应的是SQL语句里的 ?
	 * @return
	 */
	public List<Map<String, Object>> query_(Object key, Object[] value) {
		String sql = null;
		if (key == null) {
			sql = "select " + SQLFmt.getRow(ConfigInstance.columns, false)
					+ " from " + ConfigInstance.name;
		} else {
			sql = getSql(key);
		}
		if (sql != null) {
			return DBHelper.getListMap(sql, value);
		}
		return null;
	}
	
	

	/**
	 * 
	 * @param key
	 * @return
	 */
	public int exec(Object key) {
		return exec_(key, null);
	}

	/**
	 * 执行增、删、改等操作，返回影响行数，执行错误返回 -1
	 * 
	 * @param key
	 *            可以是key或position (从1开始)
	 * @param value
	 *            该项的值对应的是SQL语句里的 ?
	 * @return
	 */
	public int exec(Object key, Object... value) {
		return exec_(key, SQLFmt.getObject(value));
	}

	/**
	 * 执行增、删、改等操作，返回影响行数，执行错误返回 -1
	 * 
	 * @param key
	 *            可以是key或position (从1开始)
	 * @param value
	 *            该项的值对应的是SQL语句里的 ?
	 * @return
	 */
	public int exec_(Object key, Object[] value) {
		String sql = null;
		sql = getSql(key);
		if (sql != null)
			return DBHelper.executeSql(sql, value);
		return -1;
	}

	/**
	 * 获得配置文件中的SQL语句
	 * 
	 * @param key
	 *            可以是key或position (从1开始)
	 * @return
	 */
	public String getSql(Object key) {
		if (key instanceof Integer) {
			int i = (int) key;
			i--;
			try {
				String s = (String) StringUtil.getMapKey(ConfigInstance.sql, i);
				if (s != null)
					return s;
			} catch (Exception e) {
				Log.p("没有找到  " + ConfigInstance.name + " 表中索引位置为 " + key
						+ " 的SQL语句", true);
			}
		} else {
			String s = (String) ConfigInstance.sql.get(key);
			if (s != null)
				return s;
			Log.p("没有找到关键字为" + key + "的SQL语句", true);
		}
		return null;
	}
}
