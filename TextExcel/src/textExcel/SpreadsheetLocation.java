package textExcel;

//Update this file with your own code.

public class SpreadsheetLocation implements Location
{
	int row;
	int col;
	char columnChar;
	int rowDisplayedInt;
	
	/*
	 * @Precondition cellName consists of a letter character followed by a number greater than 0
	 */
	public SpreadsheetLocation(String cellName)
    {
    	char columnChar = cellName.charAt(0);
    	int rowInt = Integer.parseInt(cellName.substring(1)); //TODO error handling
    	
    	
    	row=rowInt-1;
    	col = Character.toLowerCase(columnChar)-97;
    	this.columnChar = columnChar;
    	this.rowDisplayedInt = rowInt;
    	
    }
	
    @Override
    public int getRow()
    {
        return row;
    }

    @Override
    public int getCol()
    {
        return col;
    }
    
	public char getColumnChar() {
		return columnChar;
	}
	public int getRowDisplayedInt() {
		return rowDisplayedInt;
	}

}
