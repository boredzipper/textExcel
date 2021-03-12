package textExcel;

import java.util.ArrayList;

public class FormulaCell extends RealCell {
	FormulaCell(String stringValue) {
		this.stringValue = stringValue;
	}

	public double getDoubleValue() {// calculate on the spot. Don't worry about circular references or order of
									// operations
		// B5 = 5
		// B6 = (1+B5/6)
		// B6 == 1
		// an arithmetic formula will consist of x operators and x+1 numbers

		String[] fullArr = stringValue.replaceAll("(", "").replaceAll(")", "").split(" ");// TODO test. remove
																							// parentheses, split at
																							// spaces
		ArrayList<Character> opArr = new ArrayList<Character>();// create an array of the operators {+,+,*} etc
		ArrayList<Double> numArr = new ArrayList<Double>();// create an array of the numbers, {1,2,3,4}, in double form.
															// TODO return an error if any cell references are
															// StringCells

		for (String string : fullArr) {
			if (string.matches("\\+|-|\\*|/")) {// should match +,-,*,/
				opArr.add(string.charAt(0));
			} else {
				if (Helper.isCellReference(string)) {
					Cell doubleCell = Spreadsheet.getSheet()[new SpreadsheetLocation(string)
							.getRow()][new SpreadsheetLocation(string).getCol()];
					if (doubleCell.getClass().equals(RealCell.class)) {
						numArr.add(((RealCell) doubleCell).getDoubleValue());
					} else {
						// TODO throw error if not a realCell, or if a formula cell that references this
						// one
					}
				} else {
					numArr.add(Double.valueOf(string));
				}
			}
		}
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
		}
		return numArr.get(0);

		// TODO figure out how to check for circular references?
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
