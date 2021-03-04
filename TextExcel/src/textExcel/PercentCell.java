package textExcel;

public class PercentCell extends RealCell {
	String stringValue;
	double doubleValue;
	PercentCell(double doubleValue){
		//currently accepts the double that is precedes the percent sign. 
		//divide this by 100 to get the decimal form of the percentage
		//will need to multiply by 100 and truncate to revert to a percent in the 
		this.doubleValue=doubleValue/100.0;
		stringValue=String.valueOf(this.doubleValue);
	}
	
	@Override
	public String abbreviatedCellText() {
		//return Helper.abbreviateString(stringValue);
		return Helper.abbreviateString(String.valueOf(((int)(doubleValue*100.0)))+"%");//this is horrible
	}

	@Override
	public String fullCellText() {
		return stringValue;
	}
}
