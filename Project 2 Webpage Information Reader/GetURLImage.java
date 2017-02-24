
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.net.URL;

public class GetURLImage {

	File outputImageFile = null;
	public static BufferedImage image = null;

	public static void fetchImageFromURL (URL url) {
		try {
			// Read from a URL
			image = ImageIO.read(url);
		} catch (IOException e) {
			
		} // catch
	} // fetchImageFromURL

	// Create a URL from the specified address, open a connection to it,
	// and then display information about the URL.

} // GetURLImage
