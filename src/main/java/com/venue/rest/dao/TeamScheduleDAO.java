package com.venue.rest.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.venue.rest.model.ScheduleSeasonData;
import com.venue.rest.model.ScheduleSeasonDataV2;
import com.venue.rest.model.TeamSchedule;
import com.venue.rest.model.TeamScheduleV2;
import com.venue.rest.util.ErrorMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
@Repository
public class TeamScheduleDAO
{

	private static Logger logger = Logger.getLogger(TeamScheduleDAO.class);
	String errorCode = "";
	private JdbcTemplate jdbcTemplateObject = null;

	@Autowired
	@Qualifier("dataSourceVenue")
	DataSource dataSourceVenue;
	
	
	@PostConstruct
    public void init() {
		jdbcTemplateObject = new JdbcTemplate(dataSourceVenue);	
    	
    }


    /**
	 * Method to get team schedule response in JSON format.
	 */
    public Object getTeamSchedule() {
        try
        {
    		logger.info("::in getTeamSchedule::DAO::");
    		Object teamSchedule = getTeamScheduleDetails();
        	if(teamSchedule != null)
            {
            	return teamSchedule;
            }else
            {
	            return ErrorMessage.getInstance().getErrorResponse(errorCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		}
    }

    /**
	 * Method to get current game day URL response in JSON format.
	 */
    public Object getCurrentGameDayURL() {
        try
        {
    		logger.info("::in getCurrentGameDayURL::DAO::");
    		Object gameDayURL = getGameDayURL();
        	if(gameDayURL != null)
            {
            	return gameDayURL;
            }else
            {
	            return ErrorMessage.getInstance().getErrorResponse(errorCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		}
    }

	/**
	 * Method to get team schedule details V2
	 */
    public Object getTeamScheduleDetailsV2(String miaLogo)
	{
    	logger.info("::in getTeamScheduleDetails:");
    	Hashtable<String, Object> scheduleDetails = new Hashtable<String, Object>();
    	Hashtable<String, Object> schedule = new Hashtable<String, Object>();
    	ArrayList<ScheduleSeasonDataV2> seasonType = null;
    	ScheduleSeasonDataV2 scheduleSeasonData = null;
    	ArrayList<TeamScheduleV2> preSeason = null;
    	ArrayList<TeamScheduleV2> nextGame = null;
    	ArrayList<TeamScheduleV2> regularSeason = null;
    	ArrayList<TeamScheduleV2> postSeason = null;
    	TeamScheduleV2 teamSchedule = null;
    	String standins="";
    	SqlRowSet rst = null;
    	SqlRowSet rst1 = null;
    	SqlRowSet rst2 = null;
    	SqlRowSet rst3 = null;

        try {
        	rst = jdbcTemplateObject.queryForRowSet("select * from tbl_team_schedule order by week");
        	preSeason = new ArrayList<TeamScheduleV2>();
        	regularSeason = new ArrayList<TeamScheduleV2>();
        	postSeason = new ArrayList<TeamScheduleV2>();
        	seasonType = new ArrayList<ScheduleSeasonDataV2>();
        	while(rst.next())
        	{
        		standins="";
        		teamSchedule = new TeamScheduleV2();
        		if(rst.getString("week") != null)
        			teamSchedule.setWeek(rst.getString("week"));
        		if(rst.getString("game_date") != null)
        			teamSchedule.setGameDate(rst.getString("game_date"));
        		if(rst.getString("start_time") != null)
        			teamSchedule.setStartTime(rst.getString("start_time"));
        		if(rst.getString("game_date_time") != null)
        			teamSchedule.setGameDateTime(rst.getString("game_date_time"));
        		if(rst.getString("ticket_master_url") != null)
        			teamSchedule.setTicketmasterUrl(rst.getString("ticket_master_url"));
        		else
        			teamSchedule.setTicketmasterUrl(rst.getString("ticket_master_url2"));
        		logger.info("::rst.getString(home_team):::"+rst.getString("home_team"));
        		HashMap<String, String> homeClub=new HashMap<String, String>();
        		String winQuery="select field_value from tbl_team_standings where club_code=? and field_key_name='win'";
        		String lossQuery="select field_value from tbl_team_standings where club_code=? and field_key_name='loss'";
        		String tieQuery="select field_value from tbl_team_standings where club_code=? and field_key_name='tie'";
        		standins="(";
        		rst2=jdbcTemplateObject.queryForRowSet(winQuery,new Object[]{ rst.getString("home_team")});
        		if(rst2.next())
        		standins+=rst2.getInt("field_value");
        		else
        			standins+=0;
        		rst2=jdbcTemplateObject.queryForRowSet(lossQuery,new Object[]{ rst.getString("home_team")});
        		if(rst2.next())
            		standins+="-"+rst2.getInt("field_value");
            		else
            			standins+="-"+0;
        		rst2=jdbcTemplateObject.queryForRowSet(tieQuery,new Object[]{ rst.getString("home_team")});
        		if(rst2.next())
            		standins+="-"+rst2.getInt("field_value");
            		else
            			standins+="-"+0;
        		standins+=")";
        		homeClub.put("standings", standins);
        		if(!rst.getString("home_team").equalsIgnoreCase("mia")){
        		String homeIcomQuery="select visit_team_icon from tbl_team_schedule where visit_team=? or home_team=?";
        		rst3=jdbcTemplateObject.queryForRowSet(homeIcomQuery,new Object[]{rst.getString("home_team"),rst.getString("home_team")});
        		if(rst3.next() && rst3.getString("visit_team_icon")!=null)
        		homeClub.put("teamLogo", rst3.getString("visit_team_icon"));
        		}else
        			homeClub.put("teamLogo", miaLogo);
        		teamSchedule.setHomeClub(homeClub);
        		standins="(";
        		HashMap<String, String> visitClub=new HashMap<String, String>();
        		rst2=jdbcTemplateObject.queryForRowSet(winQuery,new Object[]{ rst.getString("visit_team")});
        		if(rst2.next())
            		standins+=rst2.getInt("field_value");
            		else
            			standins+=0;
        		rst2=jdbcTemplateObject.queryForRowSet(lossQuery,new Object[]{ rst.getString("visit_team")});
        		if(rst2.next())
            		standins+="-"+rst2.getInt("field_value");
            		else
            			standins+="-"+0;
        		rst2=jdbcTemplateObject.queryForRowSet(tieQuery,new Object[]{ rst.getString("visit_team")});
        		if(rst2.next())
            		standins+="-"+rst2.getInt("field_value");
            		else
            			standins+="-"+0;
        		standins+=")";
        		visitClub.put("standings", standins);
        		if(!rst.getString("visit_team").equalsIgnoreCase("mia")){
        			String homeIcomQuery="select visit_team_icon from tbl_team_schedule where visit_team=? or home_team=?";
            		rst3=jdbcTemplateObject.queryForRowSet(homeIcomQuery,new Object[]{rst.getString("visit_team"),rst.getString("visit_team")});
            		if(rst3.next() && rst3.getString("visit_team_icon")!=null)
            			visitClub.put("teamLogo", rst3.getString("visit_team_icon"));
        		}else
        			visitClub.put("teamLogo", miaLogo);
        		teamSchedule.setVisitClub(visitClub);
        		//Game Score Implementation
        		rst1 = jdbcTemplateObject.queryForRowSet("select * from tbl_team_scores where game_date='"+rst.getString("game_date")+"'");
	        		while(rst1.next())
	            	{
	        			if(!rst.getString("visit_team").equalsIgnoreCase("mia")){
	        				teamSchedule.setHomeScore(rst1.getString("team_score"));
	       					teamSchedule.setOpponentScore(rst1.getString("opponent_score"));
	        			} else
	        			{
	        				teamSchedule.setOpponentScore(rst1.getString("team_score"));
	       					teamSchedule.setHomeScore(rst1.getString("opponent_score"));
	        			}
	        			
	        				
	            	}
        		//End

        		if(rst.getString("season_type") != null && rst.getString("season_type").equalsIgnoreCase("PRE"))
        			preSeason.add(teamSchedule);
        		if(rst.getString("season_type") != null && rst.getString("season_type").equalsIgnoreCase("REG"))
        			regularSeason.add(teamSchedule);
        		if(rst.getString("season_type") != null && rst.getString("season_type").equalsIgnoreCase("POST"))
        			postSeason.add(teamSchedule);
        	}
        	rst = jdbcTemplateObject.queryForRowSet("select * from tbl_team_schedule where game_date_time>now() order by game_date_time limit 1");
        	if(rst.next()){
        		nextGame=new ArrayList<TeamScheduleV2>();
        		standins="";
        		teamSchedule = new TeamScheduleV2();
        		if(rst.getString("week") != null)
        			teamSchedule.setWeek(rst.getString("week"));
        		if(rst.getString("game_date") != null)
        			teamSchedule.setGameDate(rst.getString("game_date"));
        		if(rst.getString("start_time") != null)
        			teamSchedule.setStartTime(rst.getString("start_time"));
        		if(rst.getString("game_date_time") != null)
        			teamSchedule.setGameDateTime(rst.getString("game_date_time"));
        		/*if(rst.getString("ticket_master_url") != null)
        			teamSchedule.setTicketmasterUrl(rst.getString("ticket_master_url"));*/
        		
        		//Added to fix JIRA issue DOL-498 - 28Jul2015
        		if(rst.getString("ticket_master_url") != null)
        			teamSchedule.setTicketmasterUrl(rst.getString("ticket_master_url"));
        		else
        			teamSchedule.setTicketmasterUrl(rst.getString("ticket_master_url2"));
        		//End
        		
        		logger.info("::rst.getString(home_team):::"+rst.getString("home_team"));
        		HashMap<String, String> homeClub=new HashMap<String, String>();
        		String winQuery="select field_value from tbl_team_standings where club_code=? and field_key_name='win'";
        		String lossQuery="select field_value from tbl_team_standings where club_code=? and field_key_name='loss'";
        		String tieQuery="select field_value from tbl_team_standings where club_code=? and field_key_name='tie'";
        		standins="(";
        		rst2=jdbcTemplateObject.queryForRowSet(winQuery,new Object[]{ rst.getString("home_team")});
        		if(rst2.next())
        		standins+=rst2.getInt("field_value");
        		else
        			standins+=0;
        		rst2=jdbcTemplateObject.queryForRowSet(lossQuery,new Object[]{ rst.getString("home_team")});
        		if(rst2.next())
            		standins+="-"+rst2.getInt("field_value");
            		else
            			standins+="-"+0;
        		rst2=jdbcTemplateObject.queryForRowSet(tieQuery,new Object[]{ rst.getString("home_team")});
        		if(rst2.next())
            		standins+="-"+rst2.getInt("field_value");
            		else
            			standins+="-"+0;
        		standins+=")";
        		homeClub.put("standings", standins);
        		if(!rst.getString("home_team").equalsIgnoreCase("mia")){
        		String homeIcomQuery="select visit_team_icon from tbl_team_schedule where visit_team=? or home_team=?";
        		rst3=jdbcTemplateObject.queryForRowSet(homeIcomQuery,new Object[]{rst.getString("home_team"),rst.getString("home_team")});
        		if(rst3.next() && rst3.getString("visit_team_icon")!=null)
        		homeClub.put("teamLogo", rst3.getString("visit_team_icon"));
        		}else
        			homeClub.put("teamLogo", miaLogo);
        		teamSchedule.setHomeClub(homeClub);
        		standins="(";
        		HashMap<String, String> visitClub=new HashMap<String, String>();
        		rst2=jdbcTemplateObject.queryForRowSet(winQuery,new Object[]{ rst.getString("visit_team")});
        		if(rst2.next())
            		standins+=rst2.getInt("field_value");
            		else
            			standins+=0;
        		rst2=jdbcTemplateObject.queryForRowSet(lossQuery,new Object[]{ rst.getString("visit_team")});
        		if(rst2.next())
            		standins+="-"+rst2.getInt("field_value");
            		else
            			standins+="-"+0;
        		rst2=jdbcTemplateObject.queryForRowSet(tieQuery,new Object[]{ rst.getString("visit_team")});
        		if(rst2.next())
            		standins+="-"+rst2.getInt("field_value");
            		else
            			standins+="-"+0;
        		standins+=")";
        		visitClub.put("standings", standins);
        		if(!rst.getString("visit_team").equalsIgnoreCase("mia")){
        			String homeIcomQuery="select visit_team_icon from tbl_team_schedule where visit_team=? or home_team=?";
            		rst3=jdbcTemplateObject.queryForRowSet(homeIcomQuery,new Object[]{rst.getString("visit_team"),rst.getString("visit_team")});
            		if(rst3.next() && rst3.getString("visit_team_icon")!=null)
            			visitClub.put("teamLogo", rst3.getString("visit_team_icon"));
        		}else
        			visitClub.put("teamLogo", miaLogo);
        		teamSchedule.setVisitClub(visitClub);
        		//Game Score Implementation
        		rst1 = jdbcTemplateObject.queryForRowSet("select * from tbl_team_scores where game_date='"+rst.getString("game_date")+"'");
	        		while(rst1.next())
	            	{
	        			if(!rst.getString("visit_team").equalsIgnoreCase("mia")){
	        				teamSchedule.setHomeScore(rst1.getString("team_score"));
	       					teamSchedule.setOpponentScore(rst1.getString("opponent_score"));
	        			} else
	        			{
	        				teamSchedule.setOpponentScore(rst1.getString("team_score"));
	       					teamSchedule.setHomeScore(rst1.getString("opponent_score"));
	        			}
	            	}
        		//End
	        		nextGame.add(teamSchedule);
        	}
        	logger.info("::preSeason size::" +preSeason.size());
        	logger.info("::regularSeason size::" +regularSeason.size());
        	logger.info("::postSeason size::" +postSeason.size());
        	scheduleSeasonData = new ScheduleSeasonDataV2();
        	if(nextGame != null && nextGame.size()>0) {
        		scheduleSeasonData.setHeader("NEXT GAME");
        		scheduleSeasonData.setSchedule(nextGame);
        	}
        	scheduleDetails.put("nextGame", scheduleSeasonData);
        	scheduleSeasonData = new ScheduleSeasonDataV2();
        	if(preSeason != null && preSeason.size()>0) {
        		scheduleSeasonData.setHeader("PRESEASON");
        		scheduleSeasonData.setSchedule(preSeason);
        	}
        	scheduleDetails.put("preSeason", scheduleSeasonData);
        	scheduleSeasonData = new ScheduleSeasonDataV2();
        	if(regularSeason != null && regularSeason.size()>0) {
        		scheduleSeasonData.setHeader("REGULAR SEASON");
        		scheduleSeasonData.setSchedule(regularSeason);
        	}
        	scheduleDetails.put("regularSeason", scheduleSeasonData);
        	scheduleSeasonData = new ScheduleSeasonDataV2();
        	if(postSeason != null && postSeason.size()>0) {
        		scheduleSeasonData.setHeader("POST SEASON");
        		scheduleSeasonData.setSchedule(postSeason);
        	}
        	scheduleDetails.put("postSeason", scheduleSeasonData);



		} catch(Exception e)
		{
			logger.error("::Exception in getTeamScheduleDetails::" +e);
			e.printStackTrace();
			errorCode = "500";
			scheduleDetails = null;
			return scheduleDetails;
		}
		return scheduleDetails;
	}

    /**
	 * Method to get game day URL
	 */
    public Object getGameDayURL()
	{
    	logger.info("::in getGameDayURL:");
    	Hashtable<String, String> gameDayURL = new Hashtable<String, String>();
    	SqlRowSet rst = null;
    	SqlRowSet rst1 = null;
    	String game_center_url="";

        try {
        	//rst = jdbcTemplateObject.queryForRowSet("select game_center_url from tbl_team_schedule where game_date = (select min(game_date) from tbl_team_schedule where game_date > now())");
        	rst = jdbcTemplateObject.queryForRowSet("select game_center_url from tbl_team_schedule where club_id=15 and game_date = (select min(game_date) from tbl_team_schedule where club_id=15 and game_date_time > DATE_SUB(now(), INTERVAL 1 DAY))");
        	while(rst.next())
        	{
        		game_center_url=rst.getString("game_center_url");
        		//gameDayURL.put("currentGameDayURL", rst.getString("game_center_url"));
        	}

        	if(game_center_url== null ||(game_center_url!=null && game_center_url.length()==0))
        	{
            	rst1 = jdbcTemplateObject.queryForRowSet("select game_center_url from tbl_team_schedule where club_id=15 order by game_date desc limit 1");
            	while(rst1.next())
            	{
            		game_center_url=rst1.getString("game_center_url");
            	}

        	}
    		gameDayURL.put("currentGameDayURL", game_center_url);

        } catch(Exception e)
		{
			logger.error("::Exception in getGameDayURL::" +e);
			e.printStackTrace();
			errorCode = "500";
			gameDayURL = null;
			return gameDayURL;
		}
		return gameDayURL;
	}

    /**
	 * Method to get team schedule response V2 in JSON format.
	 */
    public Object getTeamScheduleV2(String miaLogo) {
        try
        {
    		logger.info("::in getTeamSchedule::DAO::");
    		Object teamSchedule = getTeamScheduleDetailsV2(miaLogo);
        	if(teamSchedule != null)
            {
            	return teamSchedule;
            }else
            {
	            return ErrorMessage.getInstance().getErrorResponse(errorCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		}
    }


    /**
	 * Method to get team schedule details
	 */
    public Object getTeamScheduleDetails()
	{
    	logger.info("::in getTeamScheduleDetails:");
    	Hashtable<String, Object> scheduleDetails = new Hashtable<String, Object>();
    	Hashtable<String, Object> schedule = new Hashtable<String, Object>();
    	ArrayList<ScheduleSeasonData> seasonType = null;
    	ScheduleSeasonData scheduleSeasonData = null;
    	ArrayList<TeamSchedule> preSeason = null;
    	ArrayList<TeamSchedule> regularSeason = null;
    	ArrayList<TeamSchedule> postSeason = null;
    	TeamSchedule teamSchedule = null;
    	SqlRowSet rst = null;
    	SqlRowSet rst1 = null;

        try {
        	rst = jdbcTemplateObject.queryForRowSet("select * from tbl_team_schedule order by week");
        	preSeason = new ArrayList<TeamSchedule>();
        	regularSeason = new ArrayList<TeamSchedule>();
        	postSeason = new ArrayList<TeamSchedule>();
        	seasonType = new ArrayList<ScheduleSeasonData>();
        	while(rst.next())
        	{
        		teamSchedule = new TeamSchedule();
        		if(rst.getString("week") != null)
        			teamSchedule.setWeek(rst.getString("week"));
        		if(rst.getString("game_date") != null)
        			teamSchedule.setGameDate(rst.getString("game_date"));
        		if(rst.getString("start_time") != null)
        			teamSchedule.setStartTime(rst.getString("start_time"));
        		if(rst.getString("game_date_time") != null)
        			teamSchedule.setGameDateTime(rst.getString("game_date_time"));
        		if(rst.getString("ticket_master_url") != null)
        			teamSchedule.setPlaying(rst.getString("ticket_master_url"));
        		else
        			teamSchedule.setPlaying("AT");
        		teamSchedule.setVisitTeam(rst.getString("visit_team_icon"));

        		//Game Score Implementation
        		rst1 = jdbcTemplateObject.queryForRowSet("select * from tbl_team_scores where game_date='"+rst.getString("game_date")+"'");
        		while(rst1.next())
            	{
        			if(rst1.getString("result").equalsIgnoreCase("Loss")) {
        				teamSchedule.setGameScore(rst1.getString("team_score")+"-"+rst1.getString("opponent_score"));
        				teamSchedule.setGameResult("L");
        			}
       				if(rst1.getString("result").equalsIgnoreCase("Win")) {
       					teamSchedule.setGameScore(rst1.getString("team_score")+"-"+rst1.getString("opponent_score"));
       					teamSchedule.setGameResult("W");
       				}
            	}
        		//End

        		if(rst.getString("season_type") != null && rst.getString("season_type").equalsIgnoreCase("PRE"))
        			preSeason.add(teamSchedule);
        		if(rst.getString("season_type") != null && rst.getString("season_type").equalsIgnoreCase("REG"))
        			regularSeason.add(teamSchedule);
        		if(rst.getString("season_type") != null && rst.getString("season_type").equalsIgnoreCase("POST"))
        			postSeason.add(teamSchedule);
        	}
        	logger.info("::preSeason size::" +preSeason.size());
        	logger.info("::regularSeason size::" +regularSeason.size());
        	logger.info("::postSeason size::" +postSeason.size());
        	if(preSeason != null && preSeason.size()>0) {
        		scheduleSeasonData = new ScheduleSeasonData();
        		scheduleSeasonData.setHeader("PRESEASON");
        		scheduleSeasonData.setData(preSeason);
        		seasonType.add(scheduleSeasonData);
        	}
        	if(regularSeason != null && regularSeason.size()>0) {
        		scheduleSeasonData = new ScheduleSeasonData();
        		scheduleSeasonData.setHeader("REGULAR SEASON");
        		scheduleSeasonData.setData(regularSeason);
        		seasonType.add(scheduleSeasonData);
        	}
        	if(postSeason != null && postSeason.size()>0) {
        		scheduleSeasonData = new ScheduleSeasonData();
        		scheduleSeasonData.setHeader("POST SEASON");
        		scheduleSeasonData.setData(postSeason);
        		seasonType.add(scheduleSeasonData);
        	}
        	schedule.put("seasonType", seasonType);
        	scheduleDetails.put("schedule", schedule);
		} catch(Exception e)
		{
			logger.error("::Exception in getTeamScheduleDetails::" +e);
			e.printStackTrace();
			errorCode = "500";
			scheduleDetails = null;
			return scheduleDetails;
		}
		return scheduleDetails;
	}


}
