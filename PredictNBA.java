import java.io.IOException;

/**
 * Main class, runs scraper. Pretty boring tbh. Might change the boring-ness.
 * 
 * @author Noah Haselow
 */
public class PredictNBA {
	
	public static void main(String[] args) {
		// create and run scraper. print any errors
		Scraper scr = new Scraper();
		try{
			scr.run();
		} catch(IOException exc) {
			exc.printStackTrace();
		}
	}
	
}
