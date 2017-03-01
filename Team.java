
/**
 * Enumeration of teams. Each enum has the full name of the team as a data field.
 * 
 * @author Bailey Russo, Noah Haselow
 */
public enum Team {
	ATL ("Atlanta Hawks"), BRK ("Brooklyn Nets"), BOS ("Boston Celtics"), CHA ("Charlotte Bobcats"), CHO ("Charlotte Hornets"),
	CHI ("Chicago Bulls"), CLE ("Cleveland Cavaliers"), DAL ("Dallas Mavericks"), DEN ("Denver Nuggets"), 
	DET ("Detroit Pistons"), GSW ("Golden State Warriors"), HOU ("Houston Rockets"), IND ("Indiana Pacers"), 
	LAC ("Los Angeles Clippers"), LAL ("Los Angeles Lakers"), MEM ("Memphis Grizzlies"), MIA ("Miami Heat"), 
	MIL ("Milwaukee Bucks"), MIN ("Minnesota Timberwolves"), NOP ("New Orleans Pelicans"), NYK ("New York Knicks"), 
	OKC ("Oklahoma City Thunder"), ORL ("Orlando Magic"), PHI ("Philadelphia 76ers"), PHO ("Phoenix Suns"), 
	POR ("Portland Trail Blazers"), SAC ("Sacramento Kings"), SAS ("San Antonio Spurs"), TOR ("Toronto Raptors"), 
	UTA ("Utah Jazz"), WAS ("Washington Wizards");
	
	private Team(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * Iterates through enums and returns enum with the passed full team name, or
	 * null if team name doesn't exist. For example, "ORL" would return null but
	 * "Orlando Magic" would return ORL.
	 * 
	 * @param name full team name
	 * @return enum value of name
	 */
	public static Team fromString(String name) {
		for(Team t : Team.values()) {
			if(name.equalsIgnoreCase(t.getName())) return t;
		}
		return null;
	}

	private final String name;
}
