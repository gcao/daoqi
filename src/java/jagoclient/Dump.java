package jagoclient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

/**
 * A class to generate debug information in a dump file.
 * It is a class with all static members.
 */

public class Dump {
// ------------------------------ FIELDS ------------------------------

	static PrintWriter out;
	static boolean writeToConsole = false;

// -------------------------- STATIC METHODS --------------------------

	/**
	 * Open a dump file. If this is not called there will be no
	 * file dumps.
	 */
	public static void open(String file) {
		try {
			out = new PrintWriter(new FileOutputStream(file), true);
			out.println("Locale: " + Locale.getDefault() + "\n");
		}
		catch (IOException e) {
			out = null;
		}
	}

	/**
	 * dump a string in a line
	 */
	public static void println(String s) {
		if (out != null) out.println(s);
		if (writeToConsole) System.out.println(s);
	}

	/**
	 * dump a string without linefeed
	 */
	public static void print(String s) {
		if (out != null) out.print(s);
		if (writeToConsole) System.out.print(s);
	}

	/**
	 * close the dump file
	 */
	public static void close() {
		if (out != null) out.close();
	}

	/**
	 * determine terminal dumps or not
	 */
	public static void terminal(boolean flag) {
		writeToConsole = flag;
	}
}
