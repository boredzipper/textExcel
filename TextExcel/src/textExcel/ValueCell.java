package textExcel;

public class ValueCell extends RealCell {
	boolean isInt;

	ValueCell(double value) {
		this.isInt = false;
		this.doubleValue = value;
	}

	ValueCell(double value, boolean isInt) {
		this.isInt = isInt;
		this.doubleValue = value;
	}

	@Override
	public String abbreviatedCellText() {
		// TODO test
		return Helper.abbreviateString(Double.toString(doubleValue));
	}

	@Override
	public String fullCellText() {
		// TODO test
		if (isInt) {
			return Double.toString(Math.floor(doubleValue)).replace(".0", "");//TODO This is going to break for large integers in scientific notation
		} else {
			return Double.toString(doubleValue); // we should return full precision when asked for it
		}
	}

}
