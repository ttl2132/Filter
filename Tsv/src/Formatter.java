import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Formatter helps TSVPipeline make sure the file chosen is in the right format.
 * If the file isn't in the right format, the records that don't match are
 * removed. Any extra whitespace between fields in a record are removed.
 * Formatter writes the File after the modifications are done.
 * 
 * @author Tian Low ttl2132
 *
 */
public class Formatter {
	public String firstLine = "";
	public String entireFile = "";
	private String secondLine = "";
	private String fileFormat = "";
	private FileReader fReader = null;
	private FileReader fReader2 = null;
	private BufferedReader bReader = null;
	private BufferedReader bReader2 = null;
	private File myFile;
	private String myFileName;
	private int count;

	/**
	 * Constructs an instance of Formatter.
	 * 
	 * @param input
	 *            the name of the file
	 */
	public Formatter(String input) {
		myFileName = input;
	}

	/**
	 * Determines whether the first and second lines are valid. If they are
	 * valid, the file is then cleansed of incorrectly formatted lines.
	 */
	public void defineFormat() {
		if (hasFirstLine() && hasFormattedSecondLine()) {
			fileFormat = findFormat(secondLine);
			cleanseFile();
		} else
			entireFile = "Error: header and fields do not match in file. Make sure to at least have an example value for the tsv file!";
	}

	/**
	 * Reads the file that the user wants to format. Note that there are two
	 * sets of readers, because the first and second line are separately checked
	 * from the rest of the file.
	 * 
	 * @return the entire file as one String
	 */
	public String readFile() {
		myFile = new File(System.getProperty("user.dir"));
		try {
			fReader = new FileReader(myFileName);
			bReader = new BufferedReader(fReader);
			fReader2 = new FileReader(myFileName);
			bReader2 = new BufferedReader(fReader2);
			defineFormat();
		} catch (FileNotFoundException e) {
			entireFile = "File Not Found!";
			System.out.println(entireFile);
		}
		return entireFile;
	}

	/**
	 * Writes the contents of the first line and the entire rest of the file to
	 * a new file. The new file has the word filtered at the front of the
	 * original name.
	 * 
	 * @param filteredFile
	 *            the cleansed and filtered file, or an error message
	 */
	public void writeFile(String filteredFile) {
		BufferedWriter bWriter = null;
		FileWriter fWriter = null;
		try {
			fWriter = new FileWriter("filtered" + myFileName, false);
			bWriter = new BufferedWriter(fWriter);
			if (!(firstLine.isEmpty()))
				bWriter.write(firstLine + "\r\n" + filteredFile);
			else
				bWriter.write(filteredFile);
		} catch (IOException error) {
		} finally {
			try {
				if (bWriter != null)
					bWriter.close();
				if (fWriter != null)
					fWriter.close();
			} catch (IOException error) {
				error.printStackTrace();
			}
		}
	}

	/**
	 * Checks whether the first line exists and has field(s).
	 * 
	 * @return whether the first line exists and has field(s).
	 */
	public boolean hasFirstLine() {
		try {
			firstLine = bReader.readLine();
			if (firstLine == null || firstLine.isEmpty())
				return false;
			String[] temp = removeExtraTabs(firstLine);
			count = temp.length;
			firstLine = toString(temp);
			return true;
		} catch (IOException error) {
			return false;
		}
	}

	/**
	 * Checks whether the second line exists and has field(s) that correspond
	 * with the first line in terms of number of fields.
	 * 
	 * @return whether the second line exists and is correctly formatted.
	 */
	public boolean hasFormattedSecondLine() {
		try {
			secondLine = bReader.readLine();
			String[] fields = removeExtraTabs(secondLine);
			secondLine = toString(fields);
			if (count != fields.length)
				return false;
			if (secondLine == null)
				return false;
			else
				return true;
		} catch (IOException error) {
			return false;
		}
	}

	/**
	 * Cleanses the file by checking each record if it follows the format of the
	 * first and second line. If it doesn't, the line is not put into the new
	 * file.
	 */
	public void cleanseFile() {
		String currentLine;
		try {
			while ((currentLine = bReader2.readLine()) != null) {
				currentLine = toString(removeExtraTabs(currentLine));
				if (hasFormat(currentLine))
					entireFile += currentLine + "\r\n";
			}
		} catch (IOException error) {

		} finally {
			try {
				if (bReader2 != null)
					bReader2.close();
				if (fReader2 != null)
					fReader2.close();
			} catch (IOException error) {

			}
		}
	}

	/**
	 * Removes the extra tabs in a line and returns the individual fields in the
	 * line in an array.
	 * 
	 * @param line
	 *            any line in the file
	 * @return an array of each individual field without extra space
	 */
	public String[] removeExtraTabs(String line) {
		ArrayList<String> fieldHolder = new ArrayList<String>();
		String[] splitLine = line.split("\t");
		for (String each : splitLine) {
			each = each.trim();
			if (!(each.isEmpty()))
				fieldHolder.add(each);
		}
		String[] correctLine = new String[fieldHolder.size()];
		for (int i = 0; i < fieldHolder.size(); i++) {
			correctLine[i] = fieldHolder.get(i);
		}
		return correctLine;
	}

	/**
	 * Converts String arrays to a String.
	 * 
	 * @param stringArray
	 *            an array with fields from a line
	 * @return the line as one String
	 */
	public String toString(String[] stringArray) {
		String temp = "";
		for (String each : stringArray) {
			temp += each + "\t";
		}
		return temp;
	}

	/**
	 * Determines the format of each line based off of the values of the first
	 * and second line. Removes the tabs from the lines in order to align each
	 * column.
	 * 
	 * @param line
	 *            any line from the file
	 * @return the format in terms of a String of 0's and 1's. 1 means the value
	 *         is a String. 0 means it is not a String.
	 */
	public String findFormat(String line) {
		String[] fields = removeExtraTabs(line);
		String format = "";
		for (String each : fields) {
			try {
				Long.parseLong(each);
				format += 0;
			} catch (NumberFormatException error) {
				format += 1;
			}
		}
		return format;
	}

	/**
	 * Determines the format of each line based off of the values of the first
	 * and second line, then checks the current line's format against the second
	 * line's format.
	 * 
	 * @param currentLine the line the iterator is currently on
	 * @return whether or not the current line's format is correct
	 */
	public boolean hasFormat(String currentLine) {
		String currentLineFormat = findFormat(currentLine);
		if (fileFormat.equals(currentLineFormat))
			return true;
		else
			return false;
	}

}
