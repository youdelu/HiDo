package youdelu.dao.type;

/**
 * 
 * @author 游德禄
 *
 */
public class Parameter {
	public Parameter(String paraName, int paraType, Object paraValue,
			ParameterDirection paraDirection) {
		this.parameterName = paraName;
		this.parameterType = paraType;
		this.Value = paraValue;
		this.parameterDirection = paraDirection;
	}

	public Parameter(String paraName, int paraType, Object paraValue) {
		this.parameterName = paraName;
		this.parameterType = paraType;
		this.Value = paraValue;
		this.parameterDirection = ParameterDirection.IN;
	}

	public String parameterName = "";
	public Object Value = null;
	public ParameterDirection parameterDirection = null;
	public int parameterType = 0;
}
