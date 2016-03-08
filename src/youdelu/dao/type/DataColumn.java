package youdelu.dao.type;

/**
 * 
 * @author 游德禄
 *
 */
public class DataColumn extends Object {
	public DataColumn() {
	}

	public DataColumn(String n, Object v) {
		colName = n;
		colValue = v;
	}

	public Object colValue = null;
	public String colName = null;

}
