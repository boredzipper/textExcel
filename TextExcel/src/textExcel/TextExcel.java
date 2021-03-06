package textExcel;

import java.io.FileNotFoundException;
import java.util.Scanner;


public class TextExcel
{
	public final static int COLWIDTH = 10;
	public static void main(String[] args)
	{
		//tests
		double testDouble = 123456;
		//System.out.println((int)Math.floor(testDouble));
		
		/*
		String test = "a1 world";
		Location loc=new SpreadsheetLocation(test.substring(0,test.indexOf(" ")));
		System.out.println(loc);
		*/
		
		boolean running = true;
		Scanner userInput=new Scanner(System.in);
	    Spreadsheet sheet = new Spreadsheet();
	    
	    //--------main loop -------------
	    System.out.println(sheet.getGridText());
	    while(running) {
	    	System.out.print("> ");
	    	String command = userInput.nextLine();
	    	if(command.equals("quit")) {//Handle a quit command here, because spreadsheet.processCommand returns a string
	    		System.out.println("Goodbye!");
	    		break;//TODO is breaking bad?
	    	}
	    	System.out.println(sheet.processCommand(command));//print the output of processCommand()
	    	
	    }
	    
	}
}
