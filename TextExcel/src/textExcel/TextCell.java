package textExcel;

public class TextCell implements Cell {
	String value;
	TextCell(String value){
		this.value=value;
	}
	
	@Override
	public String abbreviatedCellText() {
		return Helper.abbreviateString(value,10);
	}

	@Override
	public String fullCellText() {
		String returnValue="";
		returnValue+="\"";
		returnValue+=value;
		returnValue+="\"";
		return returnValue;
	}

}
