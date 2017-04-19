/**
 * Records information about a specific game, indexed by year, month, day, and
 * home team.
 * 
 * @author Noah Haselow, Bailey Russo
 */
public class Game {
	
	/** Game Classification */
	private int year;
	private int month;
	private int day;
	private Team home_team;
	private Team away_team;
	
	/** Game Statistics */
	private Stats home_stats;
	private Stats away_stats;
	
	/**
	 * Constructs Game object given information needed to classify the game
	 * 
	 * @param year calendar year of game
	 * @param month month of game
	 * @param day day of game
	 * @param home_team home team in game
	 */
	public Game(int year, int month, int day, Team home_team) {
		this.year = year;
		this.month = month;
		this.day = day;
		this.home_team = home_team;
	}
	
	/*********************
	 * ACCESSOR FUNCTIONS *
	  *********************/
	
	public int getYear() {
		return year;
	}
	
	public int getMonth() {
		return month;
	}
	
	public int getDay() {
		return day;
	}
	
	public Team getHomeTeam() {
		return home_team;
	}
	
	public Team getAwayTeam() {
		return away_team;
	}
	
	public Stats getHomeStats() {
		return home_stats;
	}
	
	public Stats getAwayStats() {
		return away_stats;
	}
	
	/********************
	 * MUTATOR FUNCTIONS *
	  ********************/
	
	public void setHomeStats(Stats home_stats) {
		this.home_stats = home_stats;
	}
	
	public void setAwayStats(Stats away_stats) {
		this.away_stats = away_stats;
	}
	
	public void setAwayTeam(Team away_team) {
		this.away_team = away_team;
	}
	
	/**
	 * Returns string version of game - includes information needed only
	 * to classify the game.
	 * @return string version of game object
	 */
	public String toString() {
		return Integer.toString(year) + ", " + Integer.toString(month) + ", "
			+ Integer.toString(day) + ", " + home_team.toString();
	}
}
