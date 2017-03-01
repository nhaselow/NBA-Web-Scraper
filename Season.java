import java.util.ArrayList;

/**
 * Set of games in the same season.
 * 
 * @author Noah Haselow, Bailey Russo
 */
public class Season {
	
	// Set of games in same season
	private ArrayList<Game> season;
	
	public Season() {
		season = new ArrayList<Game>();
	}
	
	/**
	 * Add game to season
	 * 
	 * @param game game to add
	 */
	public void add(Game game) {
		season.add(game);
	}
	
	/**
	 * Returns game with given date and home team, or null if not found.
	 * 
	 * @return game classification matching parameters
	 */
	public Game getGame(int year, int month, int day, Team home_team) {
		for(Game g : season) {
			if(g.getYear() == year && g.getMonth() == month && g.getDay() == day
					&& g.getHomeTeam() == home_team) {
				return g;
			}
		}
		return null;
	}
	
	/**
	 * Returns set of all games
	 * 
	 * @return set of all games
	 */
	public ArrayList<Game> getAllGames() {
		return season;
	}
}
