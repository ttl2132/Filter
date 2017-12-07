/**
 * Once built by its inner Builder class, determines which filters will be used,
 * if any, along with the fields needed for the filters. Contains a toString
 * method that prints the computed information to the console.
 * 
 * @author Tian Low ttl2132
 *
 */
public class TSVFilter {
	private final String field;
	private final String field2;
	private final long longValue;
	private final String stringValue;
	private final String fileName;
	private final String terminalType;

	/**
	 * Constructs an instance of TSVFilter
	 * 
	 * @param myBuilder
	 *            This is the inner class that helps to make TSVFilter.
	 */
	private TSVFilter(Builder myBuilder) {
		fileName = myBuilder.fileName;
		field = myBuilder.field;
		field2 = myBuilder.field2;
		longValue = myBuilder.longValue;
		stringValue = myBuilder.stringValue;
		terminalType = myBuilder.terminalType;
	}

	/**
	 * Allows other classes to get its file name.
	 * 
	 * @return the file name
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Allows other classes to get its field for a select filter.
	 * 
	 * @return the field for a select filter
	 */
	public String getField() {
		return field;
	}

	/**
	 * Allows other classes to get its field for a compute filter.
	 * 
	 * @return the field for a compute filter
	 */
	public String getField2() {
		return field2;
	}

	/**
	 * Allows other classes to get a numerical value for a select filter.
	 * 
	 * @return the numerical value for a select filter
	 */
	public long getLongValue() {
		return longValue;
	}

	/**
	 * Allows other classes to get a String value for a select filter.
	 * 
	 * @return a numerical value for a select filter
	 */
	public String getStringValue() {
		return stringValue;
	}

	/**
	 * Allows other classes to get the type of value in the terminal for a
	 * compute filter.
	 * 
	 * @return the type of value in the terminal for a compute filter
	 */
	public String getTerminalType() {
		return terminalType;
	}

	/**
	 * The inner class for TSVFilter. Allows the information in TSVFilter to
	 * "change" until completely finalized.
	 * 
	 * @author Tian Low ttl2132
	 *
	 */
	public static class Builder {
		private String terminalType;
		private String field;
		private String field2;
		private long longValue;
		private String stringValue;
		private String fileName;

		/**
		 * Constructs a Builder. Give the default values of "None" to field and
		 * field2 so it is easy to check if a filter is needed.
		 * 
		 * @param myFileName
		 *            the file name
		 */
		public Builder(String myFileName) {
			fileName = myFileName;
			field = "None";
			field2 = "None";
		}

		/**
		 * Determines the file name. Not really necessary, since the user inputs
		 * it through the Scanner.
		 * 
		 * @param myFileName
		 *            the file name
		 * @return the Builder variable with the file name value
		 */

		public Builder whichFile(String myFileName) {
			this.fileName = myFileName;
			return this;
		}

		/**
		 * Determines what field is being filtered and for what numerical value.
		 * 
		 * @param myField
		 *            the field being filtered
		 * @param myValue
		 *            the numerical value that's being filtered for
		 * @return the Builder variable with the field and numerical value
		 */
		public Builder select(String myField, long myValue) {
			field = myField;
			longValue = myValue;
			return this;
		}

		/**
		 * Determines what field is being filtered and for what String value.
		 * 
		 * @param myField
		 *            the field being filtered
		 * @param myValue
		 *            the String value that's being filtered for
		 * @return the Builder variable with the field and String value
		 */
		public Builder select(String myField, String myValue) {
			field = myField;
			stringValue = myValue;
			return this;
		}

		/**
		 * Determines which type of statistic will be passed onto TSVPipeline.
		 * 
		 * @param myField2
		 *            the field that's being computed
		 * @param myTerminal
		 *            an instance of Terminal
		 * @return the type of terminal statistic
		 */
		public Builder compute(String myField2, Terminal myTerminal) {
			field2 = myField2;
			switch (myTerminal) {
			case ALLSAME:
				terminalType = "ALLSAME";
				break;
			case COUNT:
				terminalType = "COUNT";
				break;
			case MIN:
				terminalType = "MIN";
				break;
			case MAX:
				terminalType = "MAX";
				break;
			case SUM:
				terminalType = "SUM";
				break;
			case STATS:
				terminalType = "STATS";
				break;
			case AVERAGE:
				terminalType = "AVERAGE";
				break;
			case STANDARD_DEVIATION:
				terminalType = "STANDARD_DEVIATION";
				break;
			}
			return this;
		}

		/**
		 * Builds the instance of TSVFilter.
		 * 
		 * @return the finalized TSVFilter
		 */
		public TSVFilter done() {
			return new TSVFilter(this);
		}
	}

	/**
	 * Converts the computed information into a String that prints to the
	 * console.
	 * 
	 * @param computed
	 *            the result of the computation
	 */
	public void toString(String computed) {
		System.out.println("The value of " + terminalType + " is " + computed + ".");
	}
}
