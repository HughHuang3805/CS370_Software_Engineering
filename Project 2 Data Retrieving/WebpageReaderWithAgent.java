
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;

public class WebpageReaderWithAgent {

	public static PrintWriter writer;
	private static String webpage = null;
	private static int htmlFileCount = 1;
	private static int imageFileCount = 1;
	private static int pdfFileCount = 1;
	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36";

	public static InputStream getURLInputStream(String sURL) throws Exception {
		URLConnection oConnection = (new URL(sURL)).openConnection();
		oConnection.setRequestProperty("User-Agent", USER_AGENT);
		return oConnection.getInputStream();
	} // getURLInputStream

	public static BufferedReader read(String url) throws Exception {
		InputStream content = getURLInputStream(url);
		return new BufferedReader (new InputStreamReader(content));
	} // read

	public static void htmlReader(String url, PrintWriter myWriter){//reads html types and print to output, output file printwriter
		try {
			writer = new PrintWriter("saved url " + htmlFileCount + ".html"); //this writer is used to print to html file for recreation of url
			myWriter.println("Name of HTML: " + "saved url " + htmlFileCount + ".html");
			webpage = url;
			System.out.println("Contents of the following URL: "+ webpage);
			BufferedReader reader = read(webpage);
			String line = reader.readLine();
			System.out.println(line);
			
			while (line != null) {
				FileProcessing.numberOfLines++;
				writer.println(line);
				line = reader.readLine();
			} // while
			writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		htmlFileCount++;
	}
	
	public static void jpegReader(String url, PrintWriter myWriter){//saves url of jpeg type
		try {
			URL imageURL = new URL(url);
			File outputImageFile = new File("OutputImage" + imageFileCount +".jpeg");
			myWriter.println("Name of JPEG: " + "OutputImage" + imageFileCount +".jpeg");
			myWriter.println();
			imageFileCount++;
			GetURLImage.fetchImageFromURL(imageURL);//calls method to read the image
			ImageIO.write(GetURLImage.image, "jpeg", outputImageFile);//writing image(from GetURLImage.image) to file 
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void pdfReader(String url, PrintWriter myWriter){//prints name of file to output, saves the pdf
		try {
			URL pdfURL = new URL(url);
			File outputPDFFile = new  File("OutputPDF" + pdfFileCount + ".pdf");
			myWriter.println("Name of PDF: " + "OutputPDF" + pdfFileCount + ".pdf");
			myWriter.println();
			pdfFileCount++;
			GetURLPdf.getPDF(pdfURL, outputPDFFile);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
} // WebpageReaderWithAgent
