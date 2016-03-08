package youdelu.dao.type;

/**
 * 
 * @author 游德禄
 *
 */
public class DataRow extends Object {

	public DataRow(DataColumn[] cols) {
		Columns = cols.clone();
	}

	public DataRow() {

	}

	public void AddColumn(DataColumn col) {
		DataColumn[] _cols = Columns.clone();
		Columns = null;
		Columns = new DataColumn[_cols.length + 1];
		for (int i = 0; i < _cols.length; i++) {
			Columns[i] = _cols[i];
		}
		Columns[Columns.length - 1] = col;
		_cols = null;
	}

	public DataColumn[] Columns = new DataColumn[0];

}
