
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class GetURLPdf {

	public static void getPDF(URL url, File pdfFile){
		try {
			InputStream in = url.openStream();//open inputstream
			FileOutputStream fileOutput = new FileOutputStream(pdfFile);
			System.out.println("reading from resource and writing to file...");
			int length = -1;
			byte[] buffer = new byte[1024];// buffer for portion of data from connection
			while ((length = in.read(buffer)) > -1) {
			    fileOutput.write(buffer, 0, length);
			}
			fileOutput.close();
			in.close();
			System.out.println("File downloaded");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
