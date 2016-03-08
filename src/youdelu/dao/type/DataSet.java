package youdelu.dao.type;

/**
 * 
 * @author 游德禄
 *
 */
public class DataSet {

	public DataSet(DataTable[] tables) {
		this.Tables = tables.clone();
	}

	public DataSet() {

	}

	public void AddTable(DataTable table) {
		DataTable[] _tables = this.Tables.clone();
		this.Tables = null;
		this.Tables = new DataTable[_tables.length + 1];
		for (int i = 0; i < _tables.length; i++) {
			this.Tables[i] = _tables[i];
		}
		this.Tables[this.Tables.length - 1] = table;
		_tables = null;
	}

	public DataTable[] Tables = new DataTable[0];
}
