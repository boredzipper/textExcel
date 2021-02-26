package textExcel;

// Update this file with your own code.

public class Spreadsheet implements Grid {
	public final int NUMROWS = 20;
	public final int NUMCOLS = 12;
	Cell[][] sheet = new Cell[NUMROWS][NUMCOLS];

	Spreadsheet() {
		clear(); // initialize all cells to be new EmptyCells
	}

	public String clear() {
		for (int row = 0; row < NUMROWS; row++) {
			for (int col = 0; col < NUMCOLS; col++) {
				sheet[row][col] = new EmptyCell();
			}
		}
		return getGridText();
	}

	@Override
	public String processCommand(String command) {
		if (command.equals("")) {// blank command returns nothing
			return "";
		}
		if (command.toLowerCase().equals("clear")) {// clear command calls the clear function to clear entire spreadsheet
			clear();
			return getGridText();
		}
		if (isCellReference(command)) {// if it's a single cell reference
			if (Character.toUpperCase(command.charAt(0)) - 65 < getCols()) {// if the first character is less than the number of columns
				if (Integer.valueOf(command.substring(1)) <= getRows()) {// if the integer portion is less than the
																			// number of rows
					Location loc = new SpreadsheetLocation(command);
					return (sheet[loc.getRow()][loc.getCol()].fullCellText());
				} else {
					return "Error, row out of bounds";
				}

			} else {
				return "Error, column out of bounds";
			}

		}
		if (isStringCellAssignment(command)) {// If a string cell assignment, ie: a1 = "Hello World"
			Location loc = new SpreadsheetLocation(command.substring(0, command.indexOf("=") - 1));// everything before
																									// the equals sign
			String stringValue = command.substring(command.indexOf("=") + 2);// everything after the =. Plus 2 because
																				// its inclusive and needs to skip a
																				// space
			if (stringValue.contains("\"")) {// if the cell should be set to a string
				stringValue = stringValue.replaceAll("\"", "");// remove quotation marks. might need an extra escape to
																// make regex work
				sheet[loc.getRow()][loc.getCol()] = new TextCell(stringValue);
			} else {
				// TODO implement other cell assignments
			}
			return getGridText();// after mutating sheet, return the updated sheet
		}

		if (command.equals("clear")) {// clear entire spreadsheet
			clear();
		}
		if (isClearCell(command)) {
			Location target = new SpreadsheetLocation(command.substring(command.indexOf(" ")+1));
			sheet[target.getRow()][target.getCol()]=new EmptyCell();
			return getGridText();
		}
		return "Sorry, command not recognised";
	}

	private boolean isClearCell(String command) {
		if(command.length()>6&command.toLowerCase().startsWith("clear ")) {
			String cellReference = command.substring(command.indexOf(" ")+1);//everything after the first space
			if(isCellReference(cellReference)) {
				return true;
			}
		}
		return false;
	}
	private boolean isCellReference(String command) {
		if ((!isStringCellAssignment(command)) & command.length() > 1 & command.charAt(0) >= 65 & command.charAt(0) <= 123) {
			// if not a cell assignment, is at least 2 chars long, and the first char is
			// between A and z
			for (int i = 1; i < command.length(); i++) {// looping from the second character to the end
				if (command.charAt(i) >= 48 & command.charAt(i) <= 57) {// is a digit
				} else {
					return false;
				}
			}
			return true;
		}
		return false; // if something above is false, it's not a valid cell reference
	}

	private boolean isStringCellAssignment(String command) {
		if (command.contains("\"")) {// if includes quotation marks
			if (command.substring(0, command.indexOf("\"")).contains("=")) {// don't look for equals sign inside ""
				return true;
			} else {
				return false;
			}
		} else if (command.contains("=")) {// if command contains an equals sign, it's a cell assisgnment
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int getRows() {
		return sheet.length;
	}

	@Override
	public int getCols() {
		return sheet[0].length;// would fail on a spreadsheet with 0 rows
	}

	@Override
	public Cell getCell(Location loc) {// assuming that loc is in sheet
		// TODO error handling
		return sheet[loc.getRow()][loc.getCol()];
	}

	@Override
	public String getGridText() {
		String gridText = "";
		// append "header": 3 spaces, a pipe, letter, 9 spaces, another pipe
		gridText = gridText + "   |";
		for (int col = 0; col < getCols(); col++) {// for each column, add capital letter, spaces and pipe character
			char letter = (char) (col + 65);
			gridText = gridText + letter + "         |";
		}
		gridText += "\n";// new line
		for (int row = 0; row < getRows(); row++) {// for each row, append each column
			String rowText = "";
			rowText += String.valueOf(row + 1) + "  ";
			if (rowText.length() > 3) { // If 2 digit number, remove a space
				rowText = rowText.substring(0, 3);
			}
			for (int col = 0; col < getCols(); col++) {
				rowText += "|" + sheet[row][col].abbreviatedCellText();
			}
			rowText += "|\n";
			gridText += rowText;
		}

		return gridText;
	}

}
