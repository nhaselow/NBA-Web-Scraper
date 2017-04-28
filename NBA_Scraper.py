from bs4 import BeautifulSoup
import urllib2
import sys

HEADER = "year, month, day, HOME_TEAM, AWAY_TEAM, " \
		+ "total_minutes, fg, fg_attempts, fg_percentage, 3p, 3p_attempts, 3p_percentage, ft, " \
		+ "ft_attempts, ft_percentage, orb, drb, trb, ast, stl, blk, tov, pf, pts, total_minutes(2), " \
		+ "ts, efg, three_ar, ft_ar, orb_p, drb_p, trb_p, ast_p, stl_p, blk_p, tov_p, usg_p, or, dr, " \
		+ "a.total_minutes, a.fg, a.fg_attempts, a.fg_percentage, a.3p, a.3p_attempts, a.3p_percentage, " \
		+ "a.ft, a.ft_attempts, a.ft_percentage, a.orb, a.drb, a.trb, a.ast, a.stl, a.blk, a.tov, a.pf, " \
		+ "a.pts, a.total_minutes(2), a.ts, a.efg, a.three_ar, a.ft_ar, a.orb_p, a.drb_p, a.trb_p, " \
		+ "a.ast_p, a.stl_p, a.blk_p, a.tov_p, a.usg_p, a.or, a.dr"

BASE_URL = "http://www.basketball-reference.com"

YEAR_ONE_MONTHS = ("october", "november", "december")
YEAR_TWO_MONTHS = ("january", "february", "march", "april")

VALID_YEARS = range(1976,2018)



# Returns the schedule URL for a given month/year
def get_schedule_URL(year, month) :
	if month in YEAR_ONE_MONTHS :
		return BASE_URL + "/leagues/NBA_" + str(year) + "_games-" + month + ".html"
	else :
		return BASE_URL + "/leagues/NBA_" + str(year + 1) + "_games-" + month + ".html"

  
  
# Returns a list of all url extensions from a given 
# season. Scrapes links to all box score pages during 
# season and returns the extensions in an array
def get_season_URLs(year) :

	if not year in VALID_YEARS :
		print str(year) + " is an invalid year"
		return
	base_url_schedule_1 = BASE_URL + "/leagues/NBA_" + str(year) + "_games-"
	url_extensions = YEAR_ONE_MONTHS + YEAR_TWO_MONTHS	  
	urls = []

	for extension in url_extensions :
		schedule_url = get_schedule_URL(year, extension)
		doc = BeautifulSoup(urllib2.urlopen(schedule_url).read(), "html.parser")
		schedule_table = doc.find_all("tbody")[0]
		box_score_els = schedule_table.find_all(attrs={"data-stat": "box_score_text"})

		for el in box_score_els :
			urls.append(BASE_URL + el.find("a").get('href'))
	return urls



# Scrapes a box score from a game with the given url. Records in 
# file f. Returns False and doesn't record if it's a Playoff game,
# returns True otherwise
def scrape_game(url, f) :

	html = urllib2.urlopen(url).read()
	doc = BeautifulSoup(html, "html.parser")
	if not len(doc.find_all(attrs={"data-label":"All Games in Series"})) == 0 :
		return False
	line = ""

	#################
	# Find Away Team #
	 #################
	breadcrumbs = doc.find_all("div", class_="breadcrumbs")[0]
	game_head = breadcrumbs.find_all("span", itemprop="name")[1].get_text()
	away_team_name = game_head[:game_head.find(" at ")]

	#################
	# Find Game Date #
	 #################
	year = url[url.find('res/')+4:url.find('res/')+8]
	month = url[url.find(year)+4:url.find(year)+6]
	day = url[url.find(year)+6:url.find(year)+8]

	#################
	# Find Home Team #
	 #################
	
	home_team_abbrev = url[url.find(year)+9:url.find(year)+12]

	###############
	# Record Stats #
	 ###############
	tables = doc.find_all("tfoot")
	line = line + year + ", " + month + ", " + day + ", " + home_team_abbrev \
				+ ", " + abbrev_team(str(away_team_name)) + ", "
	for table in tables :
		stats = table.find_all("td")
		for stat in stats :
			if(stat.get_text() != "") : # Trad Stat table has an empty column
				line = line + stat.get_text() + ", "
	f.write(line[:-2] + "\n")

	return True



# Runs scraper on given years and records in file titled "nba_data.csv"
def run_season(years) :
	f = open("nba_data.csv", "w")
	f.write(HEADER + "\n")
	for year in years :
		game_count = 1
		urls = get_season_URLs(year)
		for url in urls :
			if not scrape_game(url, f) :
				break
			print str(year-1) + "-" + str(year) + " Game no." \
							+ str(game_count) + " done."
			game_count = game_count + 1
	print "Data Collection Complete - nba_data.csv"
	f.close()




# Converts full team name to abbreviated name. Returns error string if
# full name does not exist
def abbrev_team(team_name) :
	return {
		"Atlanta Hawks" : "ATL",
		"Brooklyn Nets" : "BRK",
		"Boston Celtics" :  "BOS",
		"Charlotte Bobcats" : "CHA",
		"Charlotte Hornets" : "CHO",
		"Chicago Bulls" : "CHI",
		"Cleveland Cavaliers" : "CLE",
		"Dallas Mavericks" : "DAL",
		"Denver Nuggets" : "DEN",
		"Detroit Pistons" : "DET",
		"Golden State Warriors" : "GSW",
		"Houston Rockets" : "HOU",
		"Indiana Pacers" : "IND",
		"Los Angeles Clippers" : "LAC",
		"Los Angeles Lakers" : "LAL",
		"Memphis Grizzlies" : "MEM",
		"Miami Heat" : "MIA",
		"Milwaukee Bucks" : "MIL",
		"Minnesota Timberwolves" : "MIN",
		"New Orleans Pelicans" : "NOP",
		"New Orleans Hornets" : "NOH",
		"New York Knicks" : "NYK",
		"Oklahoma City Thunder" : "OKC",
		"Orlando Magic" : "ORL",
		"Philadelphia 76ers" : "PHI",
		"Phoenix Suns" : "PHO",
		"Portland Trail Blazers" : "POR",
		"Sacramento Kings" : "SAC",
		"San Antonio Spurs" : "SAS",
		"Toronto Raptors" : "TOR",
		"Utah Jazz" : "UTA",
		"Washington Wizards" : "WAS"
	}.get(team_name, "ERROR - abbrev_team")



 ########
## Main ##
 ########
if  (
		len(sys.argv) != 3
		or not (sys.argv[1].isdigit() and sys.argv[2].isdigit())
	) :
	sys.exit("Usage: " + sys.argv[0] + " start_year end_year")

start_year = int(sys.argv[1])
end_year = int(sys.argv[2])

if  (start_year > end_year) :
	sys.exit("Error: start_year " + sys.argv[1] + " is after end_year " + sys.argv[2])

if start_year == end_year :
	print "Running season " + str(start_year)
else :
	print("Running seasons " + str(start_year) + " to " + str(end_year))

try :
	run_season(range(start_year, end_year + 1))
except KeyboardInterrupt :
	print "\nScraping Halted.\nWarning: nba_data.csv is incomplete."
	
sys.exit(0)

