package textExcel;

public class EmptyCell implements Cell {

	@Override
	public String abbreviatedCellText() { //must be exactly 10 characters.
		return "          ";
	}

	@Override
	public String fullCellText() {
		return "";
	}

}
