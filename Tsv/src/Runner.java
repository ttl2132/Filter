import java.util.Scanner;

/**
 * Runs the entire program. Creates a Scanner object where the user can input
 * the name of the file that he/she/they wants to format and/or filter. Allows
 * the user to compute different statistics and "search" in a file based on one
 * aspect of a record.
 * 
 * @author Tian Low ttl2132
 *
 */
public class Runner {
	public static void main(String[] args) {
		Scanner myScanner = new Scanner(System.in);
		String input = myScanner.nextLine();
		TSVPipeline myTSVP = new TSVPipeline();
		TSVFilter myTSVFilter = new TSVFilter.Builder(input).compute("Name", Terminal.STATS).done();
		myTSVP.doIt(myTSVFilter);
	}

}
