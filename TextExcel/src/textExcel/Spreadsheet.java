package textExcel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

// Update this file with your own code.

public class Spreadsheet implements Grid {
	public static final int NUMROWS = 20;
	public static final int NUMCOLS = 12;
	static Cell[][] sheet = new Cell[NUMROWS][NUMCOLS];

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
		if (command.toLowerCase().equals("clear")) {// clear command calls the clear function to clear entire
													// spreadsheet
			clear();
			return getGridText();
		}
		if (isCellReference(command)) {// if it's a single cell reference
			if (Character.toUpperCase(command.charAt(0)) - 65 < getCols()) {// if the first character is less than the
																			// number of columns
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
			Location loc = new SpreadsheetLocation(Helper.assignmentCellReference(command));// everything before the
																							// equals sign
			String stringValue = Helper.assignmentTextValue(command); // everything after the equals sign
			stringValue = stringValue.replaceAll("\"", "");// remove quotation marks.
			sheet[loc.getRow()][loc.getCol()] = new TextCell(stringValue);// Assign the location to be a new text cell

			return getGridText();// after mutating sheet, return the updated sheet
		}

		if (isValueCellAssignment(command)) {// if its a value cell assignment, ie: a1 = 19.3
			Location loc = new SpreadsheetLocation(Helper.assignmentCellReference(command));
			String stringValue = Helper.assignmentTextValue(command);
			double value = Double.valueOf(stringValue);
			if (stringValue.contains(".")) {
				sheet[loc.getRow()][loc.getCol()] = new ValueCell(value);
			} else {
				sheet[loc.getRow()][loc.getCol()] = new ValueCell(value, true);// is an integer
			}

			return getGridText();

		}

		if (isPercentCellAssignment(command)) {
			String sanitizedCommand = command.replaceAll("%", "");

			Location loc = new SpreadsheetLocation(Helper.assignmentCellReference(sanitizedCommand));
			String stringValue = Helper.assignmentTextValue(sanitizedCommand);
			double doubleValue = Double.valueOf(stringValue);
			sheet[loc.getRow()][loc.getCol()] = new PercentCell(doubleValue);

			return getGridText();
		}
		if (isFormulaCellAssignment(command)) { // TODO implement
			Location loc = new SpreadsheetLocation(Helper.assignmentCellReference(command));
			String formulaValue = Helper.assignmentTextValue(command);
			sheet[loc.getRow()][loc.getCol()] = new FormulaCell(formulaValue);
			return getGridText();
		}

		if (command.equals("clear")) {// clear entire spreadsheet
			clear();
		}
		if (isClearCell(command)) { // if clearing a specific cell
			Location target = new SpreadsheetLocation(command.substring(command.indexOf(" ") + 1));
			sheet[target.getRow()][target.getCol()] = new EmptyCell();
			return getGridText();
		}
		if (isSaveCommand(command)) {
			// TODO implement
			String fileLocation = command.substring(command.indexOf(" ") + 1);
			String csvString = ""; // save the entire spreadsheet as a String, and write it to a file a single
									// time.
			for (int row = 0; row < NUMROWS; row++) {
				for (int col = 0; col < NUMCOLS; col++) {
					Cell currentCell = sheet[row][col];
					if (currentCell.getClass() != EmptyCell.class) {// if not empty
						// append identifier (ie C20)
						csvString += ((char) (col + 65)) + String.valueOf((row + 1)) + ",";
						// append type (ie ValueCell)
						csvString += currentCell.getClass().getSimpleName() + ",";
						// append fullCellText
						csvString += currentCell.fullCellText() + "\n";
					}
				}
			}
			// write to file
			// TODO test
			File outPutFile = new File(fileLocation);// TODO how to save to arbitrary file
			if (!outPutFile.exists()) {
				try {
					outPutFile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "Failed to create file: " + fileLocation;
				}
			}
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(outPutFile));
				out.write(csvString);
				out.close();
				return "Succesfully saved spreadsheet to: " + fileLocation;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "Failed to save to: " + fileLocation;
			}
		}
		if (isOpenCommand(command)) {
			// determine file name
			String fileLocation = command.substring(command.indexOf(" ") + 1);
			File inFile = new File(fileLocation);
			// check if file exists
			if (!inFile.exists()) {
				return "Error, could not find file: " + fileLocation;
			} else {
				System.out.println("Opening file: " + fileLocation);
			}
			// check if valid file
			if (isValidCSVFile(inFile)) {
				// if it is, clear current sheet.
				clear();
				try {
					// for each line in the input file, set the corresponding location to a new cell
					Scanner in = new Scanner(inFile);
					while (in.hasNextLine()) { // for each line in file
						String[] splitString = in.nextLine().split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
						// [0], [1], [2]
						// cellreference, celltype, cell value
						Location loc = new SpreadsheetLocation(splitString[0]);
						if (splitString[1].equals(ValueCell.class.getSimpleName())) {
							sheet[loc.getRow()][loc.getCol()] = new ValueCell(Double.valueOf(splitString[2]),
									!splitString[2].contains("."));

						}
						if (splitString[1].equals(PercentCell.class.getSimpleName())) {
							sheet[loc.getRow()][loc.getCol()] = new PercentCell(Double.valueOf(splitString[2]) * 100.0);
						}
						if (splitString[1].equals(TextCell.class.getSimpleName())) {
							sheet[loc.getRow()][loc.getCol()] = new TextCell(
									splitString[2].substring(1, splitString[2].length() - 1));// remove stored double
																								// quotes
						}
						if (splitString[1].equals(FormulaCell.class.getSimpleName())) {
							sheet[loc.getRow()][loc.getCol()] = new FormulaCell(splitString[2]);
						}

					}
					in.close();
				} catch (Exception e) {
					// this should never run
					return "Error, something bad happened";
				}
			} else {
				return "Error, " + fileLocation + " is not a valid TextExcel file.";
			}
			return getGridText();

		}
		return "Sorry, command not recognised";
	}

	private boolean isValidCSVFile(File file) {
		try {
			Scanner in = new Scanner(file);
			while (in.hasNextLine()) { // for each line in file, if it is not a valid csv string, return false
				// testing
				String currentLine = in.nextLine();
				System.out.println("testing line: " + currentLine);// test
				if (!isValidCSVString(currentLine)) {
					System.out.println("Failed"); // test
					in.close();
					return false;
				}
			}
			in.close();
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
		return true;
	}

	private boolean isValidCSVString(String csvString) {
		String[] splitString = csvString.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");// TODO should match all commas not in
																					// quotes
		if (splitString.length != 3) {
			System.out.print("too short");// test
			return false;
		}
		// check if first part of line is a cellReference
		if (!isCellReference(splitString[0])) {
			System.out.println("not cell reference");// test
			return false;
		}
		// check if second part is valid cell type
		if (!(splitString[1].equals("ValueCell") || splitString[1].equals("TextCell")
				|| splitString[1].equals("PercentCell") || splitString[1].equals("FormulaCell"))) { // TODO did I forget
																									// any?
			System.out.println("cell type not valid");// test
			return false;
		}
		// check if final part matches the cell type
		// for value cell or percent cell, must be a valid double
		// for text cell, all values are valid
		// for function cell, will have to create a method to check later.
		if (splitString[1] == "ValueCell" || splitString[1] == "PercentCell") {
			try {
				Double.valueOf(splitString[2]); // try to convert final string to a double. If we can't the csv string
												// is not valid
				return true;
			} catch (Exception e) {
				System.out.println("bad value");// test
				return false;
			}
		}
		// TODO check if valid function cell
		return true;
	}

	private boolean isOpenCommand(String command) {
		// TODO implement
		if (command.contains("open") && command.length() >= 5) {
			if (command.substring(0, 5).equals("open ")) {
				return true;
			}
		}
		return false;
	}

	private boolean isSaveCommand(String command) {

		// TODO implement
		if (command.contains("save") && command.length() >= 5) {
			if (command.substring(0, 5).equals("save ")) {
				return true;
			}
		}
		return false;
	}

	private boolean isClearCell(String command) {
		if (command.length() > 6 & command.toLowerCase().startsWith("clear ")) {
			String cellReference = command.substring(command.indexOf(" ") + 1);// everything after the first space
			if (isCellReference(cellReference)) {
				return true;
			}
		}
		return false;
	}

	private boolean isCellReference(String command) {
		if ((!isStringCellAssignment(command)) && command.length() > 1 && command.charAt(0) >= 65
				&& command.charAt(0) <= 123) {
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
		}
		/*
		 * else if (command.contains("=")) {// if command contains an equals sign, it's
		 * a cell assignment return true; }
		 */
		else {
			return false;
		}
	}

	private boolean isValueCellAssignment(String command) {// TODO test
		if (command.contains("=")) {
			try {
				Double.valueOf(Helper.assignmentTextValue(command));
				return true;
			} catch (Exception e) { // if we can't get the value of the string, it must not be a number
				return false;
			}
		}
		return false;
	}

	private boolean isPercentCellAssignment(String command) {// TODO
		if (command.contains("=") & command.contains("%")) {
			try {
				Double.valueOf(Helper.assignmentTextValue(command.replaceAll("%", ""))); // pass all but the percent
																							// sign to the helper method
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}

	private boolean isFormulaCellAssignment(String command) {// TODO implement
		// contains 1 "(" and 1 ")"
		// everything inside is either a double, a cell reference, an operator, or a
		// method(avg, sum)
		if(command.length()<=8) {//a1 = ( )
			return false;
		}
		String commandAssignment = Helper.assignmentTextValue(command);
		if (!(commandAssignment.charAt(0) == '(' && commandAssignment.charAt(commandAssignment.length() - 1) == ')')) {
			return false;
		}
		commandAssignment = commandAssignment.replace("( ", "");
		commandAssignment = commandAssignment.replace(" )", "");
		
		String[] splitString = commandAssignment.split(" ");
		
		if(FormulaCell.isFunction(splitString, "SUM") || FormulaCell.isFunction(splitString, "AVG")) {
			return true;
		}//else
		for (String str : splitString) {
			if (!str.matches("[\\*,+,\\-,/]")) {//if string isn't *, /, +, or -
				try { //check if it is a valid double
					Double.valueOf(str);
				} catch (Exception e) {// if not
					try {
						Location loc = new SpreadsheetLocation(str); // check if it's a valid cell reference
						RealCell valCell = (RealCell) sheet[loc.getRow()][loc.getCol()];// check if that cell is a
																						// RealCell
					} catch (Exception e2) {
						// TODO check if a valid method (sum, avg)
						return false;
					}
				}
			}
		}
		return true;//else return true;
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

	public static Cell[][] getSheet() {
		return sheet;
	}

}
