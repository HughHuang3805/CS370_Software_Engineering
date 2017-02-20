package Phase3;

import java.net.*;
import java.io.*;
import java.util.*;

public class GetURLInfoFromFile {

	static String fileName;
	static String authorFirstname = "", authorLastname = "", title, publisher, price, yearPublished;

	public static void printURLinfo(URLConnection uc, PrintWriter myWriter) throws IOException {
		// Display the URL address, and information about it.
		myWriter.println("Name of URL: " + uc.getURL().toExternalForm() + ":");
		myWriter.println("Content Type: " + uc.getContentType());
		myWriter.println("Content Length: " + uc.getContentLength());
		myWriter.println("Last Modified: " + new Date(uc.getLastModified()));
		myWriter.println("Expiration: " + uc.getExpiration());
		myWriter.println("Content Encoding: " + uc.getContentEncoding());
	}

	//controller for getting all the info
	public static void getAllInfo(String isbn) throws FileNotFoundException{
		fileName = WebpageReaderWithAgent.htmlReader(isbn);
		getPrice();
		getTitleAndAuthor();
		getPublisherAndYearPublished();
	}
	
	//finds the price tag and save it to price
	public static void getPrice() throws FileNotFoundException{
		@SuppressWarnings("resource")
		Scanner myScanner = new Scanner(new File(fileName));
		String subString;
		while(myScanner.hasNext()){
			String line = myScanner.nextLine();
			if(line.contains("<span class=\"a-color-secondary\">List Price:</span> ")){
				try{
					subString = line.substring(line.indexOf("$"));
					subString = subString.substring(0, subString.indexOf("<"));
					price = subString;
					break;
				} catch(Exception e){

				}
			} 
		}
	}

	//finds the title and author tag and saves them to title, authorFirstname, authorLastname
	public static void getTitleAndAuthor() throws FileNotFoundException{
		@SuppressWarnings("resource")
		Scanner myScanner = new Scanner(new File(fileName));
		String subString;
		while(myScanner.hasNext()){
			String line = myScanner.nextLine();
			if(line.contains("<meta name=\"title\"")){
				try{
					subString = line.substring(line.indexOf(":") + 1);//look for what is important in a string
					subString = subString.substring(0, subString.indexOf("("));
					title = subString;
					//System.out.println(title); 

					subString = line.substring(line.indexOf("):") + 2);
					subString = subString.substring(0, subString.indexOf(","));
					String[] authorFullName = subString.split("\\s+");
					authorLastname = authorFullName[authorFullName.length - 1];

					for(int i = 0; i < authorFullName.length - 1; i++){
						authorFirstname = authorFirstname  + " " + authorFullName[i];
					}
					
					//System.out.println(authorFirstname);
					break;
				} catch(Exception e){

				}

			} 
		}
	}

	//finds the publisher and year published tag and saves them to publisher and year published
	public static void getPublisherAndYearPublished() throws FileNotFoundException{
		@SuppressWarnings("resource")
		Scanner myScanner = new Scanner(new File(fileName));
		String subString;
		while(myScanner.hasNext()){
			String line = myScanner.nextLine();
			if(line.contains("<li><b>Publisher:</b>")){
				try{
					line = line.replace("<li><b>Publisher:</b>", "");//look for what is important
					subString = line.substring(0);
					subString = subString.substring(0, subString.indexOf(";"));
					publisher = subString;

					subString = line.substring(line.indexOf(",") + 2);
					subString = subString.substring(0, subString.indexOf(")"));
					yearPublished = subString;
					//System.out.println(publisher);
					//System.out.println(yearPublished);

					break;
				} catch(Exception e){

				}
			} 
		}
	}
	
} 
