package textExcel;

import java.util.ArrayList;
import java.util.Arrays;

public class FormulaCell extends RealCell {
	FormulaCell(String stringValue) {
		this.stringValue = stringValue;
	}

	public double getDoubleValue() {// calculate on the spot. Don't worry about circular references or order of
									// operations
		// B5 = 5
		// B6 = (1+B5/6)
		// B6 == 1.0
		// an arithmetic formula will consist of x operators and x+1 numbers

		String[] fullArr = stringValue.replaceAll("\\( ", "").replaceAll(" \\)", "").split(" ");
		ArrayList<Character> opArr = new ArrayList<Character>();// create an array of the operators {+,+,*} etc
		ArrayList<Double> numArr = new ArrayList<Double>();// create an array of the numbers, {1,2,3,4}, in double form.
		boolean cellReferenceError = false; // set to true if any of the cells referenced aren't realcells
		if ((isFunction(fullArr, "SUM")) || (isFunction(fullArr, "AVG"))) {
			String[] splitArray = fullArr[1].split("\\-");
			String cellReference1 = splitArray[0];
			String cellReference2 = splitArray[1];
			Location loc1 = new SpreadsheetLocation(cellReference1);
			Location loc2 = new SpreadsheetLocation(cellReference2);
			int height = loc2.getRow() - loc1.getRow()+1;// col of ref2-col of ref1
			int width = loc2.getCol() - loc1.getCol()+1;// row of ref2-row of ref1
			if (!(height >= 0 && width >= 0)) {
				System.out.println("--Invalid range entered--");
				return 0.0;
			}
			int count = width * height;
			Double sum = 0.0;// increment by each cell
			for (int row = 0; row < height; row++) {// loop through the cells, adding the doubleValue to sum each time
				for (int col = 0; col < width; col++) {
					Cell doubleCell = Spreadsheet.getSheet()[row][col];
					if (doubleCell.getClass().getSuperclass().equals(RealCell.class)) {// if a realCell
						sum += ((RealCell) doubleCell).getDoubleValue();
					} else {
						// TODO throw error if a circular reference
						System.out.println("---There is an error in the range of an existing function---");
						cellReferenceError = true;

					}
				}
			}
			// if average, divide by count
			if (isFunction(fullArr, "AVG")) {
				return (sum / (double) count);

			} else {
				return sum;
			}

		} else {// if not sum or avg
			for (String string : fullArr) {
				if (!string.isEmpty()) {
					if (string.matches("\\+|-|\\*|/")) {// should match +,-,*,/
						opArr.add(string.charAt(0));
					} else {
						if (Helper.isCellReference(string)) {
							Cell doubleCell = Spreadsheet.getSheet()[new SpreadsheetLocation(string)
									.getRow()][new SpreadsheetLocation(string).getCol()];
							if (doubleCell.getClass().getSuperclass().equals(RealCell.class)) {
								numArr.add(((RealCell) doubleCell).getDoubleValue());
							} else {
								// TODO throw error if a circular reference
								System.out.println("---There is an error in an existing FormulaCell---");
								numArr.add(0.0);// shouldn't be necessary
								cellReferenceError = true;

							}
						} else {
							numArr.add(Double.valueOf(string));
						}
					}
				}
			}
			if (!cellReferenceError) {// if there wasn't an error
				while (!opArr.isEmpty()) {// while there are still operations left to do
					// check which operator
					// do that operation on the first two numbers in numArr, adding the result to
					// the first and deleting the second
					char currentOp = opArr.get(0);
					if (currentOp == '+') {
						numArr.set(0, numArr.get(0) + numArr.get(1));
					}
					if (currentOp == '-') {
						numArr.set(0, numArr.get(0) - numArr.get(1));
					}
					if (currentOp == '*') {
						numArr.set(0, numArr.get(0) * numArr.get(1));
					}
					if (currentOp == '/') {
						numArr.set(0, numArr.get(0) / numArr.get(1));
					}
					opArr.remove(0);// remove the operation we just did
					numArr.remove(1);
				}
				return numArr.get(0);
			} else {
				return 0.0;// TODO what should be returned with an erroneous reference?
			}
		}
	}

	public static boolean isFunction(String[] formulaArray, String function) {
		function = function.toUpperCase();

		if (formulaArray[0].toUpperCase().equals(function)) {
			try {
				String[] splitArray = formulaArray[1].split("\\-");
				String cellReference1 = splitArray[0];
				String cellReference2 = splitArray[1];
				Location loc1 = new SpreadsheetLocation(cellReference1);
				Location loc2 = new SpreadsheetLocation(cellReference2);
				return (Helper.isCellReference(cellReference1) && Helper.isCellReference(cellReference2));//

			} catch (Exception e) {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public String abbreviatedCellText() {
		return Helper.abbreviateString(String.valueOf(getDoubleValue())); // call getDoubleValue() and abbreviate it
	}

	@Override
	public String fullCellText() {
		return stringValue; // return the actual formula when cell is inspected
	}
}
