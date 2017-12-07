import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.NumberFormatException;
import java.util.ArrayList;

public class TSVPipeline {
	private String myFileName;
	private Formatter myFormatter;
	private String entireFile;
	private String firstLine = "";

	public void doIt(TSVFilter myTSVFilter) {
		myFileName = myTSVFilter.getFileName() + ".tsv";
		myFormatter = new Formatter(myFileName);
		entireFile = myFormatter.readFile();
		firstLine = myFormatter.firstLine;
		if (entireFile.equals("File Not Found!"))
			;
		else if (entireFile.isEmpty() || firstLine == null)
			System.out.println("The file is missing its first line!");
		else {
			String wantedField = myTSVFilter.getField();
			String wantedField2 = myTSVFilter.getField2();
			if (wantedField.equals("None"))
				myFormatter.writeFile(entireFile);
			else {
				if (myTSVFilter.getStringValue() == (null))
					filterFile(wantedField, myTSVFilter.getLongValue());
				else
					filterFile(wantedField, myTSVFilter.getStringValue());
				myFormatter.writeFile(entireFile);
			}
			if (!(wantedField2.equals("None")))
				chooseCompute(myTSVFilter, new Computer(myFormatter.firstLine, entireFile));
		}
	}

	public void filterFile(String onlyField, String data) {
		int recordIndex = -1;
		String[] fields = firstLine.split("\t");
		for (int i = 0; i < fields.length; i++) {
			if (onlyField.equals(fields[i]))
				recordIndex = i;
		}
		if (recordIndex == -1) {
			System.out.println("Accurate field not specified.");
		} else {
			String newEntireFile = "";
			for (String each : entireFile.split("\r\n")) {
				String[] words = each.split("\t");
				if (words[recordIndex].equals(data))
					newEntireFile += (each + "\r\n");
			}
			entireFile = newEntireFile;
		}

	}

	public void filterFile(String onlyField, long data) {
		int recordIndex = -1;
		String[] fields = firstLine.split("\t");
		for (int i = 0; i < fields.length; i++) {
			if (onlyField.equals(fields[i]))
				recordIndex = i;
		}
		if (recordIndex == -1)
			System.out.println("Accurate field not specified.");
		else {
			String newEntireFile = "";
			for (String each : entireFile.split("\r\n")) {
				String[] words = each.split("\t");
				long wantedNum = Long.parseLong(words[recordIndex]);
				if (wantedNum == data)
					newEntireFile += (each + "\r\n");
			}
			entireFile = newEntireFile;
		}
	}

	public void chooseCompute(TSVFilter myTSVF, Computer myComputer) {
		switch (myTSVF.getTerminalType()) {
		case "ALLSAME":
			myTSVF.toString("" + myComputer.computeALLSAME(myTSVF.getField2()));
			break;
		case "COUNT":
			myTSVF.toString("" + myComputer.computeCOUNT());
			break;
		case "MIN":
			if (myComputer.computeMIN(myTSVF.getField2()).equals("" + myComputer.MINDEFAULT))
				myTSVF.toString("unavailable");
			else
				myTSVF.toString("" + myComputer.computeMIN(myTSVF.getField2()));
			break;
		case "MAX":
			if (myComputer.computeMAX(myTSVF.getField2()).equals("" + myComputer.MAXDEFAULT))
				myTSVF.toString("unavailable");
			else
				myTSVF.toString("" + myComputer.computeMAX(myTSVF.getField2()));
			break;
		case "SUM":
			if (myComputer.computeSUM(myTSVF.getField2()) == myComputer.SUMDEFAULT)
				myTSVF.toString("unavailable");
			else
				myTSVF.toString("" + myComputer.computeSUM(myTSVF.getField2()));
			break;
		case "AVERAGE":
			myTSVF.toString("" + myComputer.computeAVERAGE(myTSVF.getField2()));
			break;
		case "STANDARD_DEVIATION":
			if(myComputer.computeSTANDARD_DEVIATION(myTSVF.getField2()) == 0)
				myTSVF.toString("either unavailable or the field only has the same number in it");
			else
				myTSVF.toString("" + myComputer.computeSTANDARD_DEVIATION(myTSVF.getField2()));
		case "STATS":
			if (myComputer.computeAVERAGE(myTSVF.getField2()) == 0 && myComputer.computeSUM(myTSVF.getField2()) == 0
					&& myComputer.computeSTANDARD_DEVIATION(myTSVF.getField2()) == 0)
				myTSVF.toString("either unavailable or the field only has zeros in it");
			else{
				myTSVF.toString(
						"COUNT: " + myComputer.computeCOUNT() + " SUM: " + myComputer.computeSUM(myTSVF.getField2())
								+ " AVERAGE: " + myComputer.computeAVERAGE(myTSVF.getField2()) + " STANDARD_DEVIATION: "
								+ myComputer.computeSTANDARD_DEVIATION(myTSVF.getField2()));
			}
			break;
		}
	}

}
