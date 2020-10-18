//Andrew Kappert 40063638


import java.util.*;
import java.io.*;


interface iAdministrator {    
	void Start() throws IOException;
}
class Administrator implements iAdministrator {
	static File srcFile = null;
    
    ArrayList<String> srcFilenames = new ArrayList<String>();
    ArrayList<String> options = new ArrayList<String>();
	String commandName = "";
	static boolean verboseActive = false;
	
	public static void displayUsage() {
	    	System.out.println("Usage:	command [options] <src> {<src>}" );
			System.out.println("\nDo some stuff like count characters, lines, and words");
			System.out.println("\nOptions:\n	-h, -?, -help	Display this help" );
			System.out.println("	-b, -banner	Display the banner" );
			System.out.println("	-v, -verbose	Display ...s " );
	    }
	void checkArgsLength(String[] args) {
		if (args.length < 2) {
			System.out.println("Invalid number of arguments");
			displayUsage();
			return;
		}
	}
	void getCommandName(String[] args) {
		commandName = args[0];
	}
	void getOptions(String[] args) {
		for (int n = 1; n < (args.length); ++n) {
	        if (args[n] != null) {
	        	if (args[n].startsWith("-")) {
	        		options.add(args[n]);
	        	}
	        }
		}
	}
	void getFileNames(String[] args) {
		for (int n = 1; n < (args.length); ++n) {
	        if (args[n] != null) {
	        	if (!args[n].startsWith("-")) {
	        		srcFilenames.add(args[n]);
	        	}
	        }
		}
	}
	
	Administrator(String[] args) {
		checkArgsLength(args);
		getCommandName(args);
		getOptions(args);
		getFileNames(args);
	}
	
	
	public void Start() throws IOException {
		optionFactory OF = new optionFactory();
		OF.getOptions(options);
		
		for (int i = 0; i < srcFilenames.size(); i++) {
			srcFile = new File(srcFilenames.get(i));
			if (!srcFile.canRead()) {
				System.out.println("wc: Cannot open srcFile '" + srcFilenames.get(i) + "'");
				System.exit(0);
            }
		
			FileInputStream srcStream = new FileInputStream(srcFile);
			counterFactory CF = new counterFactory();
			CF.getCounter(commandName, srcFile);
		}
	}
	
}	
	

class optionFactory {
	iOptions createHelp() {
		return new Help();
	}
	iOptions createBanner() {
		return new Banner();
	}
	iOptions createVerbose() {
		return new Verbose();
	}
	iOptions getOptions(ArrayList<String> Options) {
		for (int i = 0; i < Options.size(); i++) {
			switch(Options.get(i)) {
				case "-h": {createHelp(); break;}
				case "-?": {createHelp(); break;}
				case "-help": {createHelp(); break;}
				case "-b": {createBanner(); break;}
				case "-banner": {createBanner(); break;}
				case "-v": {createVerbose(); break;}
				case "-verbose": {createVerbose(); break;}
			}
		}
		return null;
	}
}

interface iOptions extends iAdministrator {	
	void Start();
}

class Help implements iOptions {
	Help() {
		Start();
	}

	@Override
	public void Start() {
		System.out.println("Usage:	command [options] <src> {<src>}" );
		System.out.println("\nDo some stuff like count characters, lines, and words");
		System.out.println("\nOptions:\n	-h, -?, -help	Display this help" );
		System.out.println("	-b, -banner	Display the banner" );
		System.out.println("	-v, -verbose	Display ...s " );
		
	}
	
}

class Banner implements iOptions {
	Banner() {
		Start();
	}

	@Override
	public void Start() {
		System.out.println("********************************************" );
		System.out.println("This is a program designed by Andrew Kappert" );
		System.out.println("          My student ID is 40063638");
		System.out.println("     This was made in 2020 for SOEN 341" );
		System.out.println("********************************************" );
		
	}
}

class Verbose implements iOptions {
	Verbose(){
		Start();
	}

	@Override
	public void Start() {
		Administrator.verboseActive = true;
		
	}
}


class counterFactory {
	iCounter[] createCounters(File srcFile) throws IOException {
		iCounter[] c = new iCounter[3];
		c[0] = new charCounter(srcFile);
		c[1] = new lineCounter(srcFile);
		c[2] = new wordCounter(srcFile);
		return c;
	}
	iCounter createCharCounter(File srcFile) throws IOException {
		return new charCounter(srcFile);
	}
	iCounter createLineCounter(File srcFile) throws IOException {
		return new lineCounter(srcFile);
	}
	iCounter createWordCounter(File srcFile) throws IOException {
		return new wordCounter(srcFile);
	}
	iCounter getCounter(String counterName, File srcFile) throws IOException {
		counterName.toLowerCase(Locale.ROOT);
		
		switch(counterName) {
			case "wc": {createCounters(srcFile); break;}
			case "charcounter": {createCharCounter(srcFile); break;}
			case "linecounter": {createLineCounter(srcFile); break;}
			case "wordcounter": {createWordCounter(srcFile); break;}
		
		}
		return null;
	}
	
	
}

interface iCounter {
	public void Count(File srcFile) throws IOException;
}

class charCounter implements iCounter{
	charCounter(File srcFile) throws IOException {
		Count(srcFile);
	}
	
	@Override
	public void Count(File srcFile) throws IOException {
		FileInputStream srcStream = new FileInputStream(srcFile);
		int c;
		int nChars = 0;
		while ((c = srcStream.read()) != -1) {
            ++nChars;
            if (Administrator.verboseActive) System.out.print('.');
		}
		System.out.println("\ncharcount: " + nChars + " characters in sourceFile " + srcFile.getName());
		srcStream.close();
	}
	
}

class lineCounter implements iCounter{
	lineCounter(File srcFile) throws IOException {
		Count(srcFile);
	}
	@Override
	public void Count(File srcFile) throws IOException {
		FileInputStream srcStream = new FileInputStream(srcFile);
		int c;
	    int nLines = 1;

	    while ((c = srcStream.read()) != -1) {
	        if (c == '\n') {
	        	++nLines;
	        	if (Administrator.verboseActive) System.out.print('.');
	        }
	    }
	    System.out.println("\nlinecount: " + nLines + " lines in sourceFile " + srcFile.getName());
	    srcStream.close();
	}
	
}

class wordCounter implements iCounter{
	wordCounter(File srcFile) throws IOException {
		Count(srcFile);
	}
	
	@Override
	public void Count(File srcFile) throws IOException {
		FileInputStream srcStream = new FileInputStream(srcFile);
		int  c;
		int  nWords = 0;
		boolean inWord = false;

		while ((c = srcStream.read()) != -1) {
		    if (!(c == '\t' || c == '\n' || c == '\f' || c == '\r' || c == ' ')) {
		        if (!inWord) {
		            inWord = true;
		            ++nWords;
		            if (Administrator.verboseActive) System.out.print('.');
		        }
		    } else {
		        inWord = false;
		    }
		}
		System.out.println("\nwordcount: " + nWords + " words in sourceFile " + srcFile.getName());
		srcStream.close();
	}
	
}




public class wcOO {

	public static void main(String[] args) throws IOException {
		Administrator admin = new Administrator(args);
		admin.Start();
	}

}
