package textExcel;

public class TextCell implements Cell {
	String value;
	TextCell(String value){
		this.value=value;
	}
	
	@Override
	public String abbreviatedCellText() {
		String returnString=value;
		returnString+="          ";//This feels hacky, but it avoids if statements and loops
		
		return returnString.substring(0,10);
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
