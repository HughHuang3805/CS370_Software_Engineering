package Phase3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Scanner;
import java.util.Calendar;
import javax.swing.JOptionPane;

public class FileProcessing {

	public static int numberOfLines = 0;
	public static int numberOfISBN = 0;
	DataBase myDatabase;
	static File transactionalLog = new File("Transactional Log.txt");
	static File outputFile;
	static PrintWriter logWriter;
	static PrintWriter fileWriter;
	static String urlString = "https://www.amazon.com/gp/offer-listing/";

	public static int checkNumberOfISBN(Scanner myScanner1){//checks for number of ISBN in an input file
		while(myScanner1.hasNext()){
			numberOfISBN++;
			myScanner1.nextLine();
		}
		return numberOfISBN;
	}

	//insert list of ISBN to database
	public static void insertFile(Scanner fileScanner, DataBase myDatabase, GUI myGui) throws FileNotFoundException, SQLException, ParseException{
		while(fileScanner.hasNext()){
			String isbn = fileScanner.nextLine();
			myDatabase.insert(isbn, myGui);
			JOptionPane.showMessageDialog(myGui, "Please wait while retrieving information from file.");
			updateISBNWithInfo(myDatabase, isbn, myGui);
		}
	}

	//update an ISBN with info retrieved from html file
	public static void updateISBNWithInfo(DataBase myDatabase, String isbn, GUI myGui) throws FileNotFoundException, SQLException, ParseException{
		GetURLInfoFromFile.getAllInfo(isbn);
		myDatabase.update2(isbn, GetURLInfoFromFile.title, 1, GetURLInfoFromFile.authorFirstname, GetURLInfoFromFile.authorLastname, GetURLInfoFromFile.publisher, 
				GetURLInfoFromFile.yearPublished, null, null, null, myGui);
		GetURLInfoFromFile.authorFirstname = "";
		GetURLInfoFromFile.authorLastname = "";
		GetURLInfoFromFile.price = "";
		GetURLInfoFromFile.publisher = "";
		GetURLInfoFromFile.yearPublished = "";
		GetURLInfoFromFile.title = "";
	}

	//writes to a log file
	public static void writeToLog(java.sql.PreparedStatement stmt) throws IOException{
		logWriter = new PrintWriter(new BufferedWriter(new FileWriter(transactionalLog, true)));
		logWriter.println(stmt.toString() + " " + Calendar.getInstance().getTime());
		logWriter.flush();
	}

	//writes the database information to an output file
	public static void writeToInfoFile(DataBase myDatabase, String outputFileName) throws IOException, SQLException{

		String[] columnNames = {"ISBN_10", "Title", "Quantity", "Author_Firstname", "Author_Lastname", "Publisher" , "Year_Published",
				"Date_Purchased", "Condition_of_Book", "Collectible_Feature"};

		outputFile  = new File(outputFileName);
		fileWriter = new PrintWriter(new BufferedWriter(new FileWriter(outputFile, true)));
		ResultSet myRs = myDatabase.selection();
		/*fileWriter.printf("%11s|%12s|%8s|%10s|%10s|%15s|%5s|%20s|%20s|%20s|%10s|%10s", "  ISBN-10  ", " Title Name ", "Quantity", 
				"Author Firstname", "Author Lastname", "   Publisher   " , "Year Published", "    Date Entered    ", "   Date Purchased   ", 
				" Date Last Modified ", "Condition of Book", "Collectible Feature");*/
		fileWriter.println( "ISBN-10|Title Name|Quantity|Author Firstname|Author Lastname|Publisher|Year Published|Date Entered|Date Purchased|Date Last Modified|Condition of Book|Collectible Feature"
				+ " " + Calendar.getInstance().getTime());

		try{
			while (myRs.next()) {
				for (int i = 0; i < 12; i++) {
					if(i == 2){
						try{
							int columnValue =  myRs.getInt("QUANTITY");
							String newColumnValue = String.valueOf(columnValue);
							fileWriter.print(newColumnValue + "|");
						} catch(Exception e){
							
						}
						//System.out.println(newColumnValue);
					} else{
						String columnValue = myRs.getString(columnNames[i]);
						//System.out.println(columnValue);
						fileWriter.print(columnValue + "|");
					}
				}
				fileWriter.println();
			}
		} catch(Exception e){
			//System.out.println("Exception");
		}
		fileWriter.flush();;
	}

}
