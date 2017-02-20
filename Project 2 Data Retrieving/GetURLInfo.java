
//This example based on the code from from the book _Java in a Nutshell_ by David Flanagan.
//Written by David Flanagan.  Copyright (c) 1996 O'Reilly & Associates.

import java.net.*;
import java.io.*;
import java.util.*;

public class GetURLInfo {
	public static void printURLinfo(URLConnection uc, PrintWriter myWriter) throws IOException {
		// Display the URL address, and information about it.
		myWriter.println("Name of URL: " + uc.getURL().toExternalForm() + ":");
		myWriter.println("Content Type: " + uc.getContentType());
		myWriter.println("Content Length: " + uc.getContentLength());
		myWriter.println("Last Modified: " + new Date(uc.getLastModified()));
		myWriter.println("Expiration: " + uc.getExpiration());
		myWriter.println("Content Encoding: " + uc.getContentEncoding());
	}
	// Create a URL from the specified address, open a connection to it,
	// and then display information about the URL.
 // main
} // GetURLInfo
