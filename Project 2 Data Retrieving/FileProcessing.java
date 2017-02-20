
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class FileProcessing {

	private static int numberOfURLs = 0;
	public static int numberOfLines = 0;
	private static String[] URLArray;
	private static URL url;
	private static URLConnection connection;
	
	public static int checkNumberOfURLs(Scanner myScanner1){//checks for number of urls
		while(myScanner1.hasNext()){
			numberOfURLs++;
			myScanner1.nextLine();
		}
		return numberOfURLs;
	}
	
	public static void createURLArray(Scanner myScanner2){//creates an url array
		URLArray = new String[numberOfURLs];
		int counter = 0;
		while(myScanner2.hasNext()){
			URLArray[counter] = myScanner2.nextLine();
			counter++;
		}
	}
	
	public static void writeURLInfoToFile(PrintWriter myWriter){//prints each url from URLArray info about url to output file using same printwriter object
		try {
			for(int i = 0; i < numberOfURLs; i++){
				url = new URL(URLArray[i]);
				connection = url.openConnection();
				GetURLInfo.printURLinfo(connection, myWriter);
				myWriter.println();
				myWriter.println();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void checkURLType(PrintWriter myWriter){//checks the type of url and do corresponding stuff to it
		/*String pattern = "";
		Pattern urlPattern;
		Matcher urlMatcher;*/
		
		for(int i = 0; i < numberOfURLs; i++){
			if(URLArray[i].contains("html") || URLArray[i].contains("htm")){//html or htm
				WebpageReaderWithAgent.htmlReader(URLArray[i], myWriter);
				myWriter.println("Number of Lines: " + numberOfLines);
				numberOfLines = 0;
				myWriter.println();
			} else if(URLArray[i].contains("jpeg") || URLArray[i].contains("jpg")){//jpeg or jpg
				WebpageReaderWithAgent.jpegReader(URLArray[i], myWriter);
				GetURLImage.image = null;
			} else if(URLArray[i].contains("pdf")){
				WebpageReaderWithAgent.pdfReader(URLArray[i], myWriter);//pdf
			}
		}
	}
	
}
