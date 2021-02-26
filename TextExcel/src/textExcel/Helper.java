package textExcel;

public class Helper {
	public String abbreviateString(String input,int length) {
		String returnString = input;
		if(input.length()>=length) {
			return input.substring(0, length-1);
		}else {
			for(int i = 0; i<length-input.length();i++) {
				input+=" ";
			}
		}
		return returnString;
	}
}
