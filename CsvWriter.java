import java.io.*;

/**
 * Handles writing games to a .csv file
 * 
 * @author Noah Haselow
 */
public class CsvWriter {
	
	FileWriter writer;
	
	public CsvWriter() throws IOException {
		String csv = "src/season_data";
		writer = new FileWriter(csv, false);
		
		writer.append(HEADER);
		writer.append(NEW_LINE);
	}
	
	public void write(Game game) throws IOException {
		// Skip NULL games (should be none?). We don't throw an error
		// since missed games are in general arbitrary and shouldn't
		// affect how we fit the model.
		if(game == null) return;
		
		// Write Game Classification
		writer.append(game.toString());
		writer.append(DELIMITER);
		
		/** Home Stats - traditional */
		for(Double stat : game.getHomeStats().trad_stat) {
			writer.append(stat.toString());
			writer.append(DELIMITER);
		}
		/** Home Stats - advanced */
		for(Double stat : game.getHomeStats().adv_stat) {
			writer.append(stat.toString());
			writer.append(DELIMITER);
		}
		
		// Write Away Team's Abbreviation, or null if error in reading html.
		if(game.getAwayTeam() == null)
			writer.append("NULL, ");
		else
			writer.append(game.getAwayTeam().toString() + DELIMITER + " ");
		
		/** Away Stats - traditional */
		for(Double stat : game.getAwayStats().trad_stat) {
			writer.append(stat.toString());
			writer.append(DELIMITER);
		}
		/** Away Stats - advanced */
		for(Double stat : game.getAwayStats().adv_stat) {
			writer.append(stat.toString());
			writer.append(DELIMITER);
		}
		
		writer.append(NEW_LINE);
		writer.flush();
	}
	
	public void close() throws IOException {
		writer.close();
	}
	
	/** Delimiter is comma because .csv */
	private final static char DELIMITER = ',';
	private final static char NEW_LINE = '\n';
	private final static String HEADER = "year, month, day, HOME TEAM,"
			+ "total minutes, fg, fg attempts, fg percentage, 3p, 3p attempts, 3p percentage, ft,"
			+ "ft attempts, ft percentage, orb, drb, trb, ast, stl, blk, tov, pf, pts, "
			+ "total minutes (2), ts, efg, three_ar, ft_ar, orb_p, drb_p, trb_p, ast_p, stl_p, "
			+ "blk_p, tov_p, usg_p, or, dr, AWAY TEAM, a.total minutes, a.fg, a.fg attempts, "
			+ "a.fg percentage, a.3p, a.3p attempts, a.3p percentage, a.ft,"
			+ "a.ft attempts, a.ft percentage, a.orb, a.drb, a.trb, a.ast, a.stl, a.blk, a.tov, a.pf, a.pts, "
			+ "a.total minutes (2), a.ts, a.efg, a.three_ar, a.ft_ar, a.orb_p, a.drb_p, a.trb_p, "
			+ "a.ast_p, a.stl_p, a.blk_p, a.tov_p, a.usg_p, a.or, a.dr ";
}
