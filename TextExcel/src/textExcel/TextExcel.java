package textExcel;

import java.util.Scanner;
import java.io.*;


public class TextExcel
{
	public final static int COLWIDTH = 10;
	public static void main(String[] args) throws IOException
	{
		//tests

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
