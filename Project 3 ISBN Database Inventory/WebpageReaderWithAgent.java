package Phase3;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class WebpageReaderWithAgent {

	public static PrintWriter writer;
	private static String webpage = null;
	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36";
	static String urlString = "https://www.amazon.com/dp/";
	static String fileName;
	
	public static InputStream getURLInputStream(String sURL) throws Exception {
		URLConnection oConnection = (new URL(sURL)).openConnection();
		oConnection.setRequestProperty("User-Agent", USER_AGENT);
		return oConnection.getInputStream();
	} // getURLInputStream

	public static BufferedReader read(String url) throws Exception {
		InputStream content = getURLInputStream(url);
		return new BufferedReader (new InputStreamReader(content));
	} // read

	public static String htmlReader(String isbn){//reads html types and print to output, output file printwriter
		try {
			fileName = isbn + ".txt";
			writer = new PrintWriter(fileName); //this writer is used to print to html file for recreation of url
			webpage = urlString + isbn;
			//System.out.println("Contents of the following URL: "+ webpage);
			BufferedReader reader = read(webpage);
			String line = reader.readLine();
			
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
		return fileName;
	}

} // WebpageReaderWithAgent
