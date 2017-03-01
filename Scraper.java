import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.*;
import org.jsoup.nodes.*;

/**
 * Scrapes through basketball-reference.com to record data from 2014-2015, 2015-2016
 * seasons into a .csv file. Takes ~45min to run over 1200+ games/season
 * @author Noah Haselow, Bailey Russo
 */
public class Scraper {
	
	public Scraper() {}
	
	/**
	 * Runs scraper. Iterates through 2014-2015, 2015-2016 seasons and records
	 * data from each game into a .csv file named "season_data".
	 * 
	 * TODO: Make function to iterate over games and place into season. Too
	 * much copied code in "2014 Season", "2015 Season" sections, and if we
	 * do it on other seasons, we'll have to copy/paste again (ew).
	 * 
	 * @throws IOException on url reading error
	 */
	public void run() throws IOException {
		// List of all seasons
		ArrayList<Season> seasons = new ArrayList<Season>();
		// Single game to iterate over
		Game game;
				
		/**************
		 * 2014 Season *
		  **************/
		// List of all URL extensions from games in 2014
		ArrayList<String> url_extensions_2014 = getUrlExtensions("2014");
		
		int counter = 0; //TODO: Test variable
		Season s_2014 = new Season();
		for(String extension : url_extensions_2014) {
			counter++; //TODO: for testing
			game = new Game(Integer.parseInt(extension.substring(0, 4)),
					Integer.parseInt(extension.substring(4, 6)),
					Integer.parseInt(extension.substring(6, 8)), 
					Team.valueOf(extension.substring(9,12))
					);
			game = scrapeGame(url_base_box_score + extension, game);
			
			// Handling Away Team Errors
			if(game.getAwayTeam() == null) {
				System.out.println("NULL - " + game.toString());
				throw new NullPointerException("Null Away Team - " + game.toString());
			}
			
			s_2014.add(game);
			System.out.println("Extension " + counter + " Done"); //TODO: for testing
		}
		
		/**************
		 * 2015 Season *
		  **************/
		// List of all URL extensions from games in 2015
		ArrayList<String> url_extensions_2015 = getUrlExtensions("2015");
		
		counter = 0; //TODO: for testing
		Season s_2015 = new Season();
		for(String extension : url_extensions_2015) {
			counter++; //TODO: testing
			game = new Game(Integer.parseInt(extension.substring(0, 4)),
					Integer.parseInt(extension.substring(4, 6)),
					Integer.parseInt(extension.substring(6, 8)), 
					Team.valueOf(extension.substring(9,12))
					);
			game = scrapeGame(url_base_box_score + extension, game);
			
			// Handling Away Team Errors
			if(game.getAwayTeam() == null) {
				System.out.println("NULL - " + game.toString());
				throw new NullPointerException("Null Away Team - " + game.toString());
			}
			
			s_2015.add(game);
			System.out.println("Extension " + counter + " Done"); //TODO: testing
		}
		
		seasons.add(s_2014);
		seasons.add(s_2015);
		
		//Convert scraped Data to .csv file.
		SeasonToCSV converter = new SeasonToCSV(seasons);
		converter.convert();
		
	}
	
	/**
	 * Returns a list of all url extensions from a given season. Scrapes links
	 * to all box score pages during season and returns the extensions in an
	 * ArrayList.
	 * 
	 * @param year starting year in given season. For example, if you want the 
	 * url extensions in the 2011-2012 season, then year = 2011.
	 * @return list of url extensions in a given season
	 * @throws IOException on url reading error
	 */
	private ArrayList<String> getUrlExtensions(String year) throws IOException {
		// List of all url extensions from year
		ArrayList<String> url_extensions = new ArrayList<String>();
		// base url on schedule page
		String schedule_base = "http://www.basketball-reference.com/leagues/NBA_" + year + "_games-";
		// extensions for schedule pages
		String[] extensions = {"october", "november", "december", "january", "february", "march", "april"};
		
		// document to iterate over - html file for each schedule page
		Document doc;
		// String version of doc
		String schedule;
		// starting index of the (i+1)th extension
		int start_idx;
		
		/** Iterate through each scheduling webpage */
		for(String extension : extensions) {
			doc = Jsoup.connect(schedule_base + extension + ".html").get();
			schedule = doc.select("tbody").get(0).toString();
			
			/** Iterate through selected webpage and extract extensions */
			while(schedule.contains("box_score_text")) {
				// each extension is prefaced by tag data-stat="box_score_text"
				start_idx = schedule.indexOf("box_score_text");
				// extension starts 36 characters after tag
				schedule = schedule.substring(start_idx + 36);
				// extension is 17 characters long
				url_extensions.add(schedule.substring(0, 17));
			}
		}
		
		return url_extensions;
	}
	
	/**
	 * Scrapes box score from a game with the given url extension. Records in
	 * passed Game object and returns the same object.
	 * 
	 * TODO: Make function for iterating through table, since most of the code
	 * is copied/pasted, as evidenced by comments repeating 4 times. It's pretty gross.
	 * 
	 * @param url_box_score url of game to scrape box score from
	 * @param game Game object where data will be recorded in
	 * @return passed Game object
	 * @throws IOException on url reading error
	 */
	private Game scrapeGame(String url_box_score, Game game) throws IOException {
		
		// html file to scrape
		Document doc = Jsoup.connect(url_box_score).get();
		
		// Find Away Team
		String breadcrumbs = doc.select("div.breadcrumbs").get(0).toString();
		int start_idx = breadcrumbs.lastIndexOf("\"name\">") + 7;
		String temp = breadcrumbs.substring(start_idx);
		int end_idx = temp.indexOf(" at ");
		String away_team = temp.substring(0, end_idx);
		System.out.println(away_team); //TODO: testing
		game.setAwayTeam(Team.fromString(away_team));
		
		/******************
		 * FIND AWAY STATS *
		  ******************/
		Stats away_stats = new Stats();
		String stat_to_parse;
		
		// Table holding away team's traditional statistics
		String away_t_trad = doc.select("tfoot").get(0).toString();
		// Get rid of first data-stat tag, which holds the title of the table
		away_t_trad = away_t_trad.substring(away_t_trad.indexOf("data-stat") + 14);
		
		/** Iterate over 18 traditional statistics */
		while(away_stats.trad_stat.size() <= 18) {
			// data-stat tag is 9 characters long, plus 5 to avoid a_toi'ing over the 3 in 3p tag
			away_t_trad = away_t_trad.substring(away_t_trad.indexOf("data-stat") + 14);
			try{
				// Index until next element in table
				stat_to_parse = away_t_trad.substring(0, away_t_trad.indexOf("data-stat"));
			} catch(StringIndexOutOfBoundsException exc) {
				// Exception occurs at end of table, so just parse over remaining html
				stat_to_parse = away_t_trad;
			}
			away_stats.trad_stat.add(Double.parseDouble(stat_to_parse.replaceAll("[^0-9\\.]","")));
		}
		
		// Table holding away team's advanced statistics
		String away_t_adv = doc.select("tfoot").get(1).toString();
		// Get rid of first data-stat tag, which holds the title of the table
		away_t_adv = away_t_adv.substring(away_t_adv.indexOf("data-stat") + 14);
		
		/** Iterate over 14 advanced statistics */
		while(away_stats.adv_stat.size() <= 14) {
			// data-stat tag is 9 characters long, plus 5 to avoid a_toi'ing over the 3 in 3p tag
			away_t_adv = away_t_adv.substring(away_t_adv.indexOf("data-stat") + 14);
			try{
				// Index until next element in table
				stat_to_parse = away_t_adv.substring(0, away_t_adv.indexOf("data-stat"));
			} catch(StringIndexOutOfBoundsException exc) {
				// Exception occurs at end of table, so just parse over remaining html
				stat_to_parse = away_t_adv;
			}
			away_stats.adv_stat.add(Double.parseDouble(stat_to_parse.replaceAll("[^0-9\\.]","")));
		}
		
		// Record Away Team's statistics
		game.setAwayStats(away_stats);
		
		/******************
		 * FIND HOME STATS *
		  ******************/
		Stats home_stats = new Stats();
		
		// Table holding home team's traditional statistics
		String home_t_trad = doc.select("tfoot").get(2).toString();
		// Get rid of first data-stat tag, which holds the title of the table
		home_t_trad = home_t_trad.substring(home_t_trad.indexOf("data-stat") + 14);
		
		/** Iterate over 18 traditional statistics */
		while(home_stats.trad_stat.size() <= 18) {
			// data-stat tag is 9 characters long, plus 5 to avoid a_toi'ing over the 3 in 3p tag
			home_t_trad = home_t_trad.substring(home_t_trad.indexOf("data-stat") + 14);
			try{
				// Index until next element in table
				stat_to_parse = home_t_trad.substring(0, home_t_trad.indexOf("data-stat"));
			} catch(StringIndexOutOfBoundsException exc) {
				// Exception occurs at end of table, so just parse over remaining html
				stat_to_parse = home_t_trad;
			}
			home_stats.trad_stat.add(Double.parseDouble(stat_to_parse.replaceAll("[^0-9\\.]","")));
		}
		
		// Table holding home team's advanced statistics
		String home_t_adv = doc.select("tfoot").get(3).toString();
		// Get rid of first data-stat tag, which holds the title of the table
		home_t_adv = home_t_adv.substring(home_t_adv.indexOf("data-stat") + 14);
		
		/** Iterate over 14 advanced statistics */
		while(home_stats.adv_stat.size() <= 14) {
			// data-stat tag is 9 characters long, plus 5 to avoid a_toi'ing over the 3 in 3p tag
			home_t_adv = home_t_adv.substring(home_t_adv.indexOf("data-stat") + 14);
			try{
				// Index until next element in table
				stat_to_parse = home_t_adv.substring(0, home_t_adv.indexOf("data-stat"));
			} catch(StringIndexOutOfBoundsException exc) {
				// Exception occurs at end of table, so just parse over remaining html
				stat_to_parse = home_t_adv;
			}
			home_stats.adv_stat.add(Double.parseDouble(stat_to_parse.replaceAll("[^0-9\\.]","")));
		}
		
		// Record Home Team's statistics
		game.setHomeStats(home_stats);
		
		return game;
	}
	
	/** Common URL for all box score websites*/
	private final String url_base_box_score = "http://www.basketball-reference.com/boxscores/";
}
