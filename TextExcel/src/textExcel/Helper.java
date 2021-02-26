package textExcel;

public class Helper {
	public static String abbreviateString(String input,int length) { //TODO wtf does static mean
		String returnString = input;
		if(input.length()>=length) {
			return input.substring(0, length);
		}else {
			for(int i = 0; i<length-input.length();i++) {
				returnString+=" ";
			}
		}
		return returnString;
	}
}
