package textExcel;

public class PercentCell extends RealCell {
	String stringValue;
	double doubleValue;
	PercentCell(double doubleValue){
		this.doubleValue=doubleValue;
		stringValue=String.valueOf(doubleValue);
	}
	
	@Override
	public String abbreviatedCellText() {
		return Helper.abbreviateString(stringValue);
	}

	@Override
	public String fullCellText() {
		return stringValue + "%";
	}
}
