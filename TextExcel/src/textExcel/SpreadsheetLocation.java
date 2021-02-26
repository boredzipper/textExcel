package textExcel;

//Update this file with your own code.

public class SpreadsheetLocation implements Location
{
	int row;
	int col;
	
	/*
	 * @Precondition cellName consists of a letter character followed by a number greater than 0
	 */
	public SpreadsheetLocation(String cellName)
    {
    	char columnChar = cellName.charAt(0);
    	int rowInt = Integer.parseInt(cellName.substring(1)); //TODO error handling
    	
    	//check if columnChar is capital or lower case
    	//A is 65, a is 97 
    	//set col to int value of columnChar
    	if(columnChar>=97) {
    		col = columnChar-97;
    	}else {//must be upper case
    		col = columnChar-65;
    	}
    	row=rowInt-1;
    	
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

}
