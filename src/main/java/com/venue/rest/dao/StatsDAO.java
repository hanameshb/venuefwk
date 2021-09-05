package com.venue.rest.dao;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.venue.rest.model.StatsKeyValues;
import com.venue.rest.model.StatsSectionData;
import com.venue.rest.util.ErrorMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
@Repository
public class StatsDAO
{
	private static Logger logger = Logger.getLogger(StatsDAO.class);
	String errorCode = "";
	private JdbcTemplate jdbcTemplateObject = null;
	private JdbcTemplate eMprofileObject = null;
	@Autowired
	@Qualifier("dataSourceVenue")
	DataSource dataSourceVenue;
	

	@PostConstruct
    public void init() {
		jdbcTemplateObject = new JdbcTemplate(dataSourceVenue);	
    }
	
    /**
	 * Method to get team stats response in JSON format.
	 */
    public Object getTeamStats() {
        try
        {
    		logger.info("::in getTeamStats::DAO::");
    		Object teamStats = getTeamStatsDetails();
        	if(teamStats != null)
            {
        		return teamStats;
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
	 * Method to get player recent stats response in JSON format.
	 */
    public Object getPlayerRecentStats(String nflgsisPlayerID) {
        try
        {
    		logger.info("::in getPlayerRecentStats::DAO::");
    		Object teamStats = getPlayerRecentStatsDetails(nflgsisPlayerID);
        	if(teamStats != null)
            {
        		return teamStats;
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
	 * Method to get player historical stats response in JSON format.
	 */
    public Object getPlayerHistoricalStats(String nflgsisPlayerID) {
        try
        {
    		logger.info("::in getPlayerHistoricalStats::DAO::");
    		Object teamStats = getPlayerHistoricalStatsDetails(nflgsisPlayerID);
        	if(teamStats != null)
            {
        		return teamStats;
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
	 * Method to get team stats details
	 */
    public Object getTeamStatsDetails()
	{
    	logger.info("::in getTeamStatsDetails:");
    	SqlRowSet rst = null;
    	SqlRowSet rst1 = null;
    	SqlRowSet rst2 = null;
    	SqlRowSet rst3 = null;
    	
    	Hashtable<String, Hashtable> teamStatsInfo = new Hashtable<String, Hashtable>();
    	Hashtable<String, ArrayList<StatsSectionData>> sections = new Hashtable<String, ArrayList<StatsSectionData>>();
    	ArrayList<StatsSectionData> sectionsList = null;
    	StatsSectionData statsSectionData = null;
    	ArrayList<LinkedHashMap<String, String>> sectionDataList = null;
    	LinkedHashMap<String, String> sectionData = null;
    	ArrayList<StatsKeyValues> keyValues = null;
    	StatsKeyValues statsKeyValues = null;
    	//StatsKeyValues jersetNumberKeyValues = null;
    	//StatsKeyValues nameKeyValues = null;
    	ArrayList<String> sectionTypeList = new ArrayList<String>();
    	String sectionName = "";
    	String playerId = "";
    	
        try {
        	//rst = jdbcTemplateObject.queryForRowSet("select distinct(stats_section_type) from tbl_player_season_stats a, tbl_current_season b where a.year=b.season and a.season_type=b.season_type");
        	sectionTypeList.add("PASSING");
        	sectionTypeList.add("RUSHING");
        	sectionTypeList.add("RECEIVING");
        	//sectionTypeList.add("INTERCEPTIONS");
        	sectionTypeList.add("DEFENSE");
        	sectionTypeList.add("KICK RETURNS");
        	sectionTypeList.add("PUNT RETURN");
        	sectionTypeList.add("FIELD GOALS");
        	sectionTypeList.add("PUNTING");
        	
        	sectionsList = new ArrayList<StatsSectionData>();
        	//while(rst.next())
        	for(int i=0; i<sectionTypeList.size(); i++)
        	{
        		statsSectionData = new StatsSectionData();
        		//sectionName = rst.getString("stats_section_type");
        		sectionName = sectionTypeList.get(i);
        		logger.info("::sectionName::" +sectionName);
        		statsSectionData.setSectionName(sectionName);
        		rst1 = jdbcTemplateObject.queryForRowSet("select distinct(field_key_name),short_key_name from tbl_player_season_stats a, tbl_current_season b where a.year=b.season and a.season_type=b.season_type and stats_section_type='"+sectionName+"' order by sort_stats_level");
        		keyValues = new ArrayList<StatsKeyValues>();
        		while(rst1.next()) {
        			/*if(rst1.getString("field_key_name").equalsIgnoreCase("JerseyNumber")) {
        				jersetNumberKeyValues = new StatsKeyValues();
        				jersetNumberKeyValues.setKey(rst1.getString("field_key_name"));
        				jersetNumberKeyValues.setValue(rst1.getString("short_key_name"));
        			} else if(rst1.getString("field_key_name").equalsIgnoreCase("Name")) {
        				nameKeyValues = new StatsKeyValues();
        				nameKeyValues.setKey(rst1.getString("field_key_name"));
        				nameKeyValues.setValue(rst1.getString("short_key_name"));
        			} else {*/
	        			statsKeyValues = new StatsKeyValues();
	        			statsKeyValues.setKey(rst1.getString("field_key_name"));
	        			statsKeyValues.setValue(rst1.getString("short_key_name"));
	        			keyValues.add(statsKeyValues);
        			//}
        		}
        		logger.info("::keyValues size::" +keyValues.size());
        		
        		/*ListIterator<StatsKeyValues> listIterator = keyValues.listIterator();
        		listIterator.add(jersetNumberKeyValues);
        		listIterator.add(nameKeyValues);
        		logger.info("::keyValues size after iteration::" +keyValues.size());*/
        		statsSectionData.setKeyValues(keyValues);
        		
        		rst2 = jdbcTemplateObject.queryForRowSet("select distinct(nflgsis_player_id) from tbl_player_season_stats a, tbl_current_season b where a.year=b.season and a.season_type=b.season_type and stats_section_type='"+sectionName+"' order by sort_player_level");
        		sectionDataList = new ArrayList<LinkedHashMap<String, String>>();
        		while(rst2.next()) {
        			playerId = rst2.getString("nflgsis_player_id");
        			logger.info("::playerId::" +playerId);
        			
        			//Check whether player is available in roster - 28Aug2014
        			if(checkPlayerStatus(playerId)) {
        				logger.info("::player is available in roster::");
	        			rst3 = jdbcTemplateObject.queryForRowSet("select field_key_name,field_value from tbl_player_season_stats a, tbl_current_season b where a.year=b.season and a.season_type=b.season_type and stats_section_type='"+sectionName+"' and nflgsis_player_id='"+playerId+"' order by sort_stats_level");
	        			sectionData = new LinkedHashMap<String, String>();
	        			while(rst3.next()) {
	        				sectionData.put(rst3.getString("field_key_name"), rst3.getString("field_value"));
	            		}
	        			sectionDataList.add(sectionData);
        			}
        		}
        		logger.info("::sectionDataList size::" +sectionDataList.size());
        		statsSectionData.setSectionData(sectionDataList);
        		sectionsList.add(statsSectionData);
        	}
        	logger.info("::sectionsList size::" +sectionsList.size());
        	sections.put("sections", sectionsList);
        	teamStatsInfo.put("teamStatsInfo", sections);
		} catch(Exception e)
		{
			logger.error("::Exception in getTeamStatsDetails::" +e);
			e.printStackTrace();
			errorCode = "500";
			teamStatsInfo = null;
			return teamStatsInfo;
		}
		return teamStatsInfo;
	}
    
    /**
     * Method to check player status
     * @param playerId
     * @return
     */
    public boolean checkPlayerStatus(String playerId) {
    	logger.info("::in checkPlayerStatus:" +playerId);
    	SqlRowSet rst = null;
    	boolean playerStatus = false;
    	
    	try {
    		rst = eMprofileObject.queryForRowSet("select * from tbl_profile_master where category_id=1 and nflgsis_player_id='"+playerId+"' and player_status!='InActive'");
        	if(rst != null && rst.next()) {
        		playerStatus = true;
        	}
        	logger.info("::playerStatus:" +playerStatus);
    	} catch (Exception e) {
    		logger.error("::Exception in checkPlayerStatus::" +e);
		}
		return playerStatus;
    }
    
    /**
	 * Method to get player recent stats details
	 */
    public Object getPlayerRecentStatsDetails(String nflgsisPlayerID)
	{
    	logger.info("::in getPlayerRecentStatsDetails:");
    	SqlRowSet rst = null;
    	SqlRowSet rst1 = null;
    	SqlRowSet rst2 = null;
    	SqlRowSet rst3 = null;
    	SqlRowSet rst4 = null;
    	
    	Hashtable<String, Hashtable> playerRecentStatsInfo = new Hashtable<String, Hashtable>();
    	Hashtable<String, ArrayList<StatsSectionData>> sections = new Hashtable<String, ArrayList<StatsSectionData>>();
    	ArrayList<StatsSectionData> sectionsList = null;
    	StatsSectionData statsSectionData = null;
    	ArrayList<LinkedHashMap<String, String>> sectionDataList = null;
    	LinkedHashMap<String, String> sectionData = null;
    	ArrayList<StatsKeyValues> keyValues = null;
    	StatsKeyValues statsKeyValues = null;
    	
    	String sectionName = "";
    	String opponentTeamAbbr = "";
    	String week = "";
    	
        try {
        	logger.info("::nflgsisPlayerID in getPlayerRecentStatsDetails:" +nflgsisPlayerID);
        	rst = jdbcTemplateObject.queryForRowSet("select stats_section_type from tbl_position_sections_map where position in (select distinct(position) from tbl_player_weekly_stats a, tbl_current_season b where a.year=b.season and a.season_type=b.season_type and nflgsis_player_id='"+nflgsisPlayerID+"')");
        	sectionsList = new ArrayList<StatsSectionData>();
        	while(rst.next())
        	{
        		statsSectionData = new StatsSectionData();
        		sectionName = rst.getString("stats_section_type");
        		logger.info("::sectionName::" +sectionName);
        		statsSectionData.setSectionName(sectionName);
        		rst1 = jdbcTemplateObject.queryForRowSet("select distinct(field_key_name),short_key_name from tbl_player_weekly_stats a, tbl_current_season b where a.year=b.season and a.season_type=b.season_type and stats_section_type='"+sectionName+"' order by sort_stats_level");
        		keyValues = new ArrayList<StatsKeyValues>();
        		
        		//Add Week and Name to keyValues list
        		statsKeyValues = new StatsKeyValues();
    			statsKeyValues.setKey("Week");
    			statsKeyValues.setValue("WEEK");
    			keyValues.add(statsKeyValues);
    			
    			statsKeyValues = new StatsKeyValues();
    			statsKeyValues.setKey("Name");
    			statsKeyValues.setValue("NAME");
    			keyValues.add(statsKeyValues);
        		//End
    			
        		while(rst1.next()) {
        			statsKeyValues = new StatsKeyValues();
        			if(rst1.getString("field_key_name") != null && !rst1.getString("field_key_name").equalsIgnoreCase("passingYardsPerGame") 
        					&& rst1.getString("field_key_name") != null && !rst1.getString("field_key_name").equalsIgnoreCase("passingAttemptsPerGame") 
        					&& rst1.getString("field_key_name") != null && !rst1.getString("field_key_name").equalsIgnoreCase("rushingYardsPerGame") 
        					&& rst1.getString("field_key_name") != null && !rst1.getString("field_key_name").equalsIgnoreCase("rushingTouchdownsPerGame") 
        					&& rst1.getString("field_key_name") != null && !rst1.getString("field_key_name").equalsIgnoreCase("rushingAttemptsPerGame") 
        					&& rst1.getString("field_key_name") != null && !rst1.getString("field_key_name").equalsIgnoreCase("receivingYardsPerGame") 
        					&& rst1.getString("field_key_name") != null && !rst1.getString("field_key_name").equalsIgnoreCase("receivingTouchdownsPerGame") 
        					&& rst1.getString("field_key_name") != null && !rst1.getString("field_key_name").equalsIgnoreCase("receivingReceptionsPerGame") 
        					&& rst1.getString("field_key_name") != null && !rst1.getString("field_key_name").equalsIgnoreCase("kickingXkMadePerGame") 
        					&& rst1.getString("field_key_name") != null && !rst1.getString("field_key_name").equalsIgnoreCase("kickingXkAttemptsPerGame") 
        					&& rst1.getString("field_key_name") != null && !rst1.getString("field_key_name").equalsIgnoreCase("kickingFgMadePerGame")) {
        				statsKeyValues.setKey(rst1.getString("field_key_name"));
        				statsKeyValues.setValue(rst1.getString("short_key_name"));
        				keyValues.add(statsKeyValues);
        			}
        		}
        		logger.info("::keyValues size::" +keyValues.size());
        		statsSectionData.setKeyValues(keyValues);
        		
        		rst2 = jdbcTemplateObject.queryForRowSet("select distinct(a.week),opponent_team_abbr from tbl_player_weekly_stats a, tbl_current_season b where a.year=b.season and a.season_type=b.season_type and stats_section_type='"+sectionName+"' and nflgsis_player_id='"+nflgsisPlayerID+"' order by week");
        		sectionDataList = new ArrayList<LinkedHashMap<String, String>>();
        		while(rst2.next()) {
        			week = rst2.getString("week");
        			opponentTeamAbbr = rst2.getString("opponent_team_abbr");
        			logger.info("::week::" +week);
        			logger.info("::opponentTeamAbbr::" +opponentTeamAbbr);
        			rst3 = jdbcTemplateObject.queryForRowSet("select field_key_name,field_value from tbl_player_weekly_stats a, tbl_current_season b where a.year=b.season and a.season_type=b.season_type and stats_section_type='"+sectionName+"' and nflgsis_player_id='"+nflgsisPlayerID+"' and opponent_team_abbr='"+opponentTeamAbbr+"' and a.week="+week+" order by sort_stats_level");
        			sectionData = new LinkedHashMap<String, String>();
        			sectionData.put("Week", week);
        			sectionData.put("Name", opponentTeamAbbr);
        			while(rst3.next()) {
        				if(rst3.getString("field_key_name") != null && !rst3.getString("field_key_name").equalsIgnoreCase("passingYardsPerGame") 
            					&& rst3.getString("field_key_name") != null && !rst3.getString("field_key_name").equalsIgnoreCase("passingAttemptsPerGame") 
            					&& rst3.getString("field_key_name") != null && !rst3.getString("field_key_name").equalsIgnoreCase("rushingYardsPerGame") 
            					&& rst3.getString("field_key_name") != null && !rst3.getString("field_key_name").equalsIgnoreCase("rushingTouchdownsPerGame") 
            					&& rst3.getString("field_key_name") != null && !rst3.getString("field_key_name").equalsIgnoreCase("rushingAttemptsPerGame") 
            					&& rst3.getString("field_key_name") != null && !rst3.getString("field_key_name").equalsIgnoreCase("receivingYardsPerGame") 
            					&& rst3.getString("field_key_name") != null && !rst3.getString("field_key_name").equalsIgnoreCase("receivingTouchdownsPerGame") 
            					&& rst3.getString("field_key_name") != null && !rst3.getString("field_key_name").equalsIgnoreCase("receivingReceptionsPerGame") 
            					&& rst3.getString("field_key_name") != null && !rst3.getString("field_key_name").equalsIgnoreCase("kickingXkMadePerGame") 
            					&& rst3.getString("field_key_name") != null && !rst3.getString("field_key_name").equalsIgnoreCase("kickingXkAttemptsPerGame") 
            					&& rst3.getString("field_key_name") != null && !rst3.getString("field_key_name").equalsIgnoreCase("kickingFgMadePerGame")) {
        					if(rst3.getString("field_key_name") != null && rst3.getString("field_key_name").equalsIgnoreCase("passingRating")) {
        						Double roundOff = Math.round(Double.parseDouble(rst3.getString("field_value")) * 10.0) / 10.0;
        						sectionData.put(rst3.getString("field_key_name"), roundOff.toString());
        					} else
        						sectionData.put(rst3.getString("field_key_name"), rst3.getString("field_value"));
        				}
            		}
        			sectionDataList.add(sectionData);
        		}
        		
        		//Stats Total Implementation on 22Aug2014
        		sectionData = new LinkedHashMap<String, String>();
    			sectionData.put("Week", "");
    			sectionData.put("Name", "TOTAL");
        		for(int i=0; i<keyValues.size(); i++) {
        			if(!keyValues.get(i).getKey().equalsIgnoreCase("Week") 
        					&& !keyValues.get(i).getKey().equalsIgnoreCase("Name")) {
	        			rst4 = jdbcTemplateObject.queryForRowSet("select field_value from tbl_player_weekly_stats_totals a, tbl_current_season b where a.year=b.season and stats_section_type='"+sectionName+"' and nflgsis_player_id='"+nflgsisPlayerID+"' and field_key_name='"+keyValues.get(i).getKey()+"'");
	        			while(rst4.next()) {
	        				if(keyValues.get(i).getKey() != null && keyValues.get(i).getKey().equalsIgnoreCase("passingRating")) {
        						Double roundOff = Math.round(Double.parseDouble(rst4.getString("field_value")) * 10.0) / 10.0;
        						sectionData.put(keyValues.get(i).getKey(), roundOff.toString());
        					} else
        						sectionData.put(keyValues.get(i).getKey(), rst4.getString("field_value"));
	        			}
        			}
        		}
        		sectionDataList.add(sectionData);
        		//End
        		
        		logger.info("::sectionDataList size::" +sectionDataList.size());
        		statsSectionData.setSectionData(sectionDataList);
        		sectionsList.add(statsSectionData);
        	}
        	logger.info("::sectionsList size::" +sectionsList.size());
        	sections.put("sections", sectionsList);
        	playerRecentStatsInfo.put("playerRecentStatsInfo", sections);
		} catch(Exception e)
		{
			logger.error("::Exception in getPlayerRecentStatsDetails::" +e);
			e.printStackTrace();
			errorCode = "500";
			playerRecentStatsInfo = null;
			return playerRecentStatsInfo;
		}
		return playerRecentStatsInfo;
	}
    
    /**
	 * Method to get player historical stats details
	 */
    public Object getPlayerHistoricalStatsDetails(String nflgsisPlayerID)
	{
    	logger.info("::in getPlayerHistoricalStatsDetails:");
    	SqlRowSet rst = null;
    	SqlRowSet rst1 = null;
    	SqlRowSet rst2 = null;
    	SqlRowSet rst3 = null;
    	SqlRowSet rst4 = null;
    	
    	Hashtable<String, Hashtable> playerHistoricalStatsInfo = new Hashtable<String, Hashtable>();
    	Hashtable<String, ArrayList<StatsSectionData>> sections = new Hashtable<String, ArrayList<StatsSectionData>>();
    	ArrayList<StatsSectionData> sectionsList = null;
    	StatsSectionData statsSectionData = null;
    	ArrayList<LinkedHashMap<String, String>> sectionDataList = null;
    	LinkedHashMap<String, String> sectionData = null;
    	ArrayList<StatsKeyValues> keyValues = null;
    	StatsKeyValues statsKeyValues = null;
    	boolean keyValuesCheck = false;
    	
    	String playerPosition = "";
    	String sectionName = "";
    	String year = "";
    	
        try {
        	logger.info("::nflgsisPlayerID in getPlayerHistoricalStatsDetails:" +nflgsisPlayerID);
        	
        	//get player position from profile master db
        	rst = eMprofileObject.queryForRowSet("select player_position from tbl_profile_master where nflgsis_player_id='"+nflgsisPlayerID+"'");
        	while(rst.next()) {
        		playerPosition = rst.getString("player_position");
        	}
        	logger.info("::playerPosition:" +playerPosition);
        	
        	rst1 = jdbcTemplateObject.queryForRowSet("select stats_section_type from tbl_position_sections_map where position='"+playerPosition+"'");
        	sectionsList = new ArrayList<StatsSectionData>();
        	while(rst1.next())
        	{
        		statsSectionData = new StatsSectionData();
        		sectionName = rst1.getString("stats_section_type");
        		logger.info("::sectionName::" +sectionName);
        		statsSectionData.setSectionName(sectionName);
        		rst2 = jdbcTemplateObject.queryForRowSet("select distinct(field_key_name),short_key_name from tbl_player_season_stats where stats_section_type='"+sectionName+"' and nflgsis_player_id='"+nflgsisPlayerID+"' order by sort_stats_level");
        		keyValues = new ArrayList<StatsKeyValues>();
        		
        		//Add Week and Name to keyValues list
        		statsKeyValues = new StatsKeyValues();
    			statsKeyValues.setKey("Year");
    			statsKeyValues.setValue("YEAR");
    			keyValues.add(statsKeyValues);
    			
    			statsKeyValues = new StatsKeyValues();
    			statsKeyValues.setKey("Name");
    			statsKeyValues.setValue("NAME");
    			keyValues.add(statsKeyValues);
        		//End
    			
        		while(rst2.next()) {
        			if(!rst2.getString("field_key_name").equalsIgnoreCase("Name") 
        					&& !rst2.getString("field_key_name").equalsIgnoreCase("JerseyNumber")) {
        				statsKeyValues = new StatsKeyValues();
	        			statsKeyValues.setKey(rst2.getString("field_key_name"));
	        			statsKeyValues.setValue(rst2.getString("short_key_name"));
	        			keyValues.add(statsKeyValues);
        			}
        			keyValuesCheck = true;
        		}
        		logger.info("::keyValues size::" +keyValues.size());
        		logger.info("::keyValuesCheck::" +keyValuesCheck);
        		
        		if(keyValuesCheck) {
        			logger.info("::keyValues are available::");
	        		statsSectionData.setKeyValues(keyValues);
	        		
	        		sectionDataList = new ArrayList<LinkedHashMap<String, String>>();
	        		rst3 = jdbcTemplateObject.queryForRowSet("select distinct(year),a.season_type from tbl_player_season_stats a, tbl_current_season b where stats_section_type='"+sectionName+"' and nflgsis_player_id='"+nflgsisPlayerID+"' and a.year!=b.season and a.season_type='REG' order by year DESC");
	        		while(rst3.next()) {
	        			sectionData = new LinkedHashMap<String, String>();
	        			year = rst3.getString("year");
	        			logger.info("::stats year::" +year);
		        		sectionData.put("Year", year);
	        			sectionData.put("Name", "");
	        			for(int i=0; i<keyValues.size(); i++) {
		        			if(!keyValues.get(i).getKey().equalsIgnoreCase("Year") 
		        					&& !keyValues.get(i).getKey().equalsIgnoreCase("Name") 
		        					&& !keyValues.get(i).getKey().equalsIgnoreCase("JerseyNumber")) {
		        				rst4 = jdbcTemplateObject.queryForRowSet("select field_value from tbl_player_season_stats where stats_section_type='"+sectionName+"' and nflgsis_player_id='"+nflgsisPlayerID+"' and field_key_name='"+keyValues.get(i).getKey()+"' and year='"+year+"' and season_type='REG'");
			        			while(rst4.next()) {
			        				sectionData.put(keyValues.get(i).getKey(), rst4.getString("field_value"));
			            		}
		        			}
		        		}
	        			sectionDataList.add(sectionData);
        			}
	        		logger.info("::sectionDataList size::" +sectionDataList.size());
	        		statsSectionData.setSectionData(sectionDataList);
	        		sectionsList.add(statsSectionData);
        		}
        	}
        	logger.info("::sectionsList size::" +sectionsList.size());
        	sections.put("sections", sectionsList);
        	playerHistoricalStatsInfo.put("playerHistoricalStatsInfo", sections);
		} catch(Exception e)
		{
			logger.error("::Exception in getPlayerHistoricalStatsDetails::" +e);
			e.printStackTrace();
			errorCode = "500";
			playerHistoricalStatsInfo = null;
			return playerHistoricalStatsInfo;
		}
		return playerHistoricalStatsInfo;
	}
    
	
}
