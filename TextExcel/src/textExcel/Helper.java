package textExcel;

public class Helper {
	//Contains several useful methods
	
	/**
	 * 
	 * @param input
	 * @param length
	 * @return the input string shortened or lengthened to length characters
	 */
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
	/**
	 * 
	 * @param input
	 * @return The input string shortened or lengthened to the constant TextExcel.COLWIDTH, which should be 10
	 */
	public static String abbreviateString(String input) { //TODO wtf does static mean
		return abbreviateString(input,TextExcel.COLWIDTH);
	}
	
	
	/**
	 * @param command A cell assignment command
	 * @return the cell reference that the command begins with
	 */
	public static String assignmentCellReference(String command) {
		return command.substring(0, command.indexOf("=") - 1);//TODO test
	}
	
	public static String assignmentTextValue(String command) {
		return command.substring(command.indexOf("=") + 2);// everything after the =. Plus 2 because it's inclusive and needs to skip a space
	}
}
