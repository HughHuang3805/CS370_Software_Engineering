package Phase3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main {

	public static void main(String[] args){

		try {
			Scanner myScanner1;
			File infile1;

			int argumentLength = args.length;
			if(argumentLength == 2){
				infile1 = new File(args[0]);  //command line argument 1
				FileReader myReader1 = new FileReader(infile1);//reader of file 1
				myScanner1 = new Scanner(myReader1);  //scanner 1 of input file

				GUI myGui = new GUI();
				@SuppressWarnings("unused")
				Controller x = new Controller(myGui, myScanner1, args[1]);//creates everything

			} else{
				GUI myGui = new GUI();
				@SuppressWarnings("unused")
				Controller x = new Controller(myGui);//creates everything
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

}
