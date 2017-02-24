
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
	
	public static void main(String[] args){
		
		try {
			File infile = new File(args[0]);  //command line argument 1
			File outfile1 = new File(args[1]);//command line argument 2
			
			FileReader myReader1 = new FileReader(infile);//reader of file 1
			Scanner myScanner1 = new Scanner(myReader1);  //scanner 1 of input file
			
			FileReader myReader2 = new FileReader(infile);//reader of file 2
			Scanner myScanner2 = new Scanner(myReader2);  //scanner 2 of input file
			
			FileProcessing.checkNumberOfURLs(myScanner1); //checks number of urls in input file
			FileProcessing.createURLArray(myScanner2);    //creates an url array
			PrintWriter urlInfoWriter = new PrintWriter(outfile1); //printwriter for output file
			FileProcessing.writeURLInfoToFile(urlInfoWriter); //print to output file
			FileProcessing.checkURLType(urlInfoWriter);       //check the type of url and do corresponding stuff to it 
			
			myScanner1.close();
			urlInfoWriter.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}
	
}
