package textExcel;

public class ValueCell extends RealCell {
	ValueCell(double value){
		this.doubleValue=value;
	}
	
	@Override
	public String abbreviatedCellText() {
		// TODO test
		return Helper.abbreviateString(Double.toString(doubleValue));
	}

	@Override
	public String fullCellText() {
		// TODO Auto-generated method stub
		return Double.toString(doubleValue);
	}
	
}
