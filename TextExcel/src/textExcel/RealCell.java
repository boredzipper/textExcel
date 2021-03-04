package textExcel;

public class RealCell implements Cell {

	String stringValue;
	double doubleValue;
	public String getStringValue() {
		return stringValue;
	}
	public double getDoubleValue() {//override in ValueCell, PercentCell, and FormulaCell.
		return doubleValue;
	}
	@Override
	public String abbreviatedCellText() {
		// TODO override in subclasses
		return Helper.abbreviateString(stringValue);
	}

	@Override
	public String fullCellText() {
		// TODO override in subclasses
		return stringValue;
	}

}
