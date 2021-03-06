package textExcel;

public class ValueCell extends RealCell {
	boolean isInt;

	ValueCell(Number value) {
		if (value.getClass() == Integer.class) { // if the input number is an Integer, isInt is true
			isInt = true;
			this.doubleValue = Double.valueOf(value.intValue());
		}
		this.doubleValue = value.doubleValue();
	}

	@Override
	public String abbreviatedCellText() {
		// TODO test
		if (isInt) {
			return Helper.abbreviateString(Integer.toString((int) doubleValue)); // convert to int, then to string, pass
																					// to helper
		} else {
			return Helper.abbreviateString(Double.toString(doubleValue));
		}
	}

	@Override
	public String fullCellText() {
		// TODO test
		return Double.toString(doubleValue); // we should return full precision when asked for it
	}

}
