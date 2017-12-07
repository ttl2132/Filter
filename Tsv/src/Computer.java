import java.lang.Math;

/**
 * This class helps TSVPipeline compute the different values for whether all
 * fields are the same, average, number of records in a field, the maximum and
 * minimum values, standard deviation, and the sum of all of the numbers in a
 * given field.
 * 
 * @author Tian Low ttl2132
 *
 */
public class Computer {
	private String firstLine;
	private String entireFile;
	// Defaults for certain computations.
	public static final long MAXDEFAULT = Long.MIN_VALUE;
	public static final long MINDEFAULT = Long.MAX_VALUE;
	public static final long SUMDEFAULT = 0;

	/**
	 * Constructs a Computer that computes values.
	 * 
	 * @param myFirstLine
	 *            the first line of the file
	 * @param myEntireFile
	 *            the entire file
	 */
	public Computer(String myFirstLine, String myEntireFile) {
		firstLine = myFirstLine;
		entireFile = myEntireFile;
	}

	/**
	 * Iterates through fields in the first line to find the field entered by
	 * the user.
	 * 
	 * @param myField
	 *            the user input field
	 * @return the index of the field
	 */
	public int fieldIndex(String myField) {
		int fieldIndex = -1;
		String[] fields = firstLine.split("\t");
		for (int i = 0; i < fields.length; i++) {
			if (myField.equals(fields[i]))
				fieldIndex = i;
		}
		return fieldIndex;
	}

	/**
	 * Computes whether all of the values in a certain field column are the same
	 * value.
	 * 
	 * @param myField
	 *            the user input field
	 * @return whether or not the values are the same
	 */
	public boolean computeALLSAME(String myField) {
		int recordIndex = fieldIndex(myField);
		if (recordIndex == -1)
			System.out.println("Accurate field not specified.");
		else {
			String previous = "";
			String[] splitFile = entireFile.split("\r\n");
			for (int i = 0; i < splitFile.length; i++) {
				String[] words = splitFile[i].split("\t");
				if (i > 0) {
					if (!(previous.equals(words[recordIndex])))
						return false;
				}
				previous = words[recordIndex];
			}
		}
		return true;
	}

	/**
	 * Computes the highest value under the given field. Iterates through the
	 * entire file, checking if the value is a String or long along the way.
	 * Note that for Strings the highest value is the last word alphabetically.
	 * 
	 * @param myField
	 *            the user input field
	 * @return the highest value
	 */
	public String computeMAX(String myField) {
		int recordIndex = fieldIndex(myField);
		long previous = MAXDEFAULT;
		if (recordIndex == -1)
			System.out.println("Accurate field not specified.");
		else {
			String[] splitFile = entireFile.split("\r\n");
			if (splitFile.length == 0)
				return "" + previous;
			if (entireFile.contains("Error"))
				return "" + previous;
			String[] words = splitFile[0].split("\t");
			String previous2 = words[recordIndex];
			for (int i = 0; i < splitFile.length; i++) {
				try {
					words = splitFile[i].split("\t");
					if (previous < (Long.parseLong(words[recordIndex])))
						previous = Long.parseLong(words[recordIndex]);
				} catch (NumberFormatException error) {
					if (previous2.compareTo(words[recordIndex]) < 0)
						previous2 = words[recordIndex];
				}
			}
			if (previous == MAXDEFAULT)
				return previous2;
		}
		return "" + previous;
	}

	/**
	 * Computes the lowest value under the given field. Iterates through the
	 * entire file, checking if the value is a String or long along the way.
	 * Note that for Strings the lowest value is the first word alphabetically.
	 * 
	 * @param myField
	 *            the user input field
	 * @return the lowest value
	 */
	public String computeMIN(String myField) {
		int recordIndex = fieldIndex(myField);
		long previous = MINDEFAULT;
		if (recordIndex == -1)
			System.out.println("Accurate field not specified.");
		else {
			String[] splitFile = entireFile.split("\r\n");
			if (splitFile.length == 0)
				return "" + previous;
			if (entireFile.contains("Error"))
				return "" + previous;
			String[] words = splitFile[0].split("\t");
			String previous2 = words[recordIndex];
			for (int i = 0; i < splitFile.length; i++) {
				try {
					words = splitFile[i].split("\t");
					if (previous > (Long.parseLong(words[recordIndex])))
						previous = Long.parseLong(words[recordIndex]);
				} catch (NumberFormatException error) {
					if (previous2.compareTo(words[recordIndex]) > 0)
						previous2 = words[recordIndex];
				}
			}
			if (previous == MINDEFAULT)
				return previous2;
		}
		return "" + previous;
	}

	/**
	 * Computes how many entires there are of a field. Since the fields are
	 * already formatted at this point, it will really just be how many lines
	 * there are.
	 * 
	 * @return the number of records of the field
	 */
	public int computeCOUNT() {
		return entireFile.split("\r\n").length;
	}

	/**
	 * Computes the sum of all of the numbers under a given field.
	 * 
	 * @param myField
	 *            the user input field
	 * @return the sum of all of the numbers under the field
	 */
	public long computeSUM(String myField) {
		long sum = SUMDEFAULT;
		int recordIndex = fieldIndex(myField);
		if (recordIndex == -1)
			System.out.println("Accurate field not specified.");
		else {
			String[] splitFile = entireFile.split("\r\n");
			if (entireFile.contains("Error"))
				return sum;
			for (int i = 0; i < splitFile.length; i++) {
				String[] words = splitFile[i].split("\t");
				try {
					sum += Long.parseLong(words[recordIndex]);
				} catch (NumberFormatException error) {
				}
			}
			if (sum == SUMDEFAULT)
				System.out.println("Record is not a number.");
		}
		return sum;
	}

	/**
	 * Computes the mean of all of the numbers under a given field. Takes the
	 * sum of all of the number and divides it by the number of numbers.
	 * 
	 * @param myField
	 *            the user input field
	 * @return the mean of all of the numbers under the given field
	 */
	public long computeAVERAGE(String myField) {
		return computeSUM(myField) / computeCOUNT();
	}

	/**
	 * Computes the standard deviation of the numbers in a given field.
	 * 
	 * @param myField
	 *            the user input field
	 * @return the standard deviation of all of the numbers under the given
	 *         field
	 */
	public long computeSTANDARD_DEVIATION(String myField) {
		long sum = 0;
		int recordIndex = fieldIndex(myField);
		if (recordIndex == -1)
			System.out.println("Accurate field not specified.");
		else {
			String[] splitFile = entireFile.split("\r\n");
			if (entireFile.contains("Error"))
				return sum;
			for (int i = 0; i < splitFile.length; i++) {
				String[] words = splitFile[i].split("\t");
				try {
					sum += Math.pow(((Long.parseLong(words[recordIndex]) - computeAVERAGE(myField))), 2);
				} catch (NumberFormatException error) {
				}
			}
			if (sum == SUMDEFAULT) {
				System.out.println("Record is not a number.");
				return sum;
			}

		}
		return (long) Math.sqrt(sum / (computeCOUNT() - 1));

	}
}
