import java.util.ArrayList;

/**
 * Records statistics for a team in a game
 * @author Noah Haselow
 */
public class Stats {
	
	/**
	 * Both traditional and advanced stats are individually unlabeled, but each
	 * list's statistics are added in a specific order (see Scraper.java). Order
	 * is noted above each ArrayList.
	 */
	
	// Traditional Stats
		//ORDER:
		//tot_min, fg, fg_attempts, fg_p, threes, three_attempts, three_p, ft, 
		//ft_attempts, fg_p, orb, drb, ast, stl, blk, tov, pf, pts;
	public ArrayList<Double> trad_stat = new ArrayList<Double>();
	
	
	// Advanced Stats
		//ORDER:
		//ts, efg, three_ar, ft_ar, orb_p, drb_p, 
		//trb_p, ast_p, stl_p, blk_p, tov_p, usg_p, or, dr;
	public ArrayList<Double> adv_stat = new ArrayList<Double>();
	
	public Stats() {}
	
}
