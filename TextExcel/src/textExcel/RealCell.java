package textExcel;

public class RealCell implements Cell {

	String stringValue;
	
	public double getDoubleValue() {//override in ValueCell, PercentCell, and FormulaCell.
		return 0.0;
	}
	@Override
	public String abbreviatedCellText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String fullCellText() {
		// TODO Auto-generated method stub
		return null;
	}

}
