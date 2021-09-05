package com.venue.rest.dao;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.ListIterator;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.venue.rest.model.StandingsKeyValues;
import com.venue.rest.model.StandingsSectionData;
import com.venue.rest.util.ErrorMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
@Repository
public class TeamStandingsDAO
{
	private static Logger logger = Logger.getLogger(TeamStandingsDAO.class);
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
	 * Method to get team standings response in JSON format.
	 */
    public Object getTeamStandings() {
        try
        {
    		logger.info("::in getTeamStandings::DAO::");
    		Object teamStats = getTeamStandingsDetails();
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
	 * Method to get team standings details
	 */
    public Object getTeamStandingsDetails()
	{
    	logger.info("::in getTeamStandingsDetails:");
    	SqlRowSet rst = null;
    	SqlRowSet rst1 = null;
    	SqlRowSet rst2 = null;
    	SqlRowSet rst3 = null;
    	
    	LinkedHashMap<String, Hashtable> teamStandingsInfo = new LinkedHashMap<String, Hashtable>();
    	//Hashtable<String, ArrayList<StandingsSectionData>> sections = new Hashtable<String, ArrayList<StandingsSectionData>>();
    	ArrayList<StandingsSectionData> sectionsList = null;
    	StandingsSectionData standingsSectionData = null;
    	ArrayList<LinkedHashMap<String, String>> sectionDataList = null;
    	LinkedHashMap<String, String> sectionData = null;
    	ArrayList<StandingsKeyValues> keyValues = null;
    	StandingsKeyValues standingsKeyValues = null;
    	ArrayList<String> conferenceNameList = new ArrayList<String>();

    	String sectionName = "";
    	String cityName = "";
    	
        try {
        	
        	rst = jdbcTemplateObject.queryForRowSet("select distinct(conference) from tbl_team_standings a, tbl_current_season b where a.year=b.season order by conference");
        	logger.info("select distinct(conference) from tbl_team_standings a, tbl_current_season b where a.year=b.season order by conference");
        	while(rst.next())
        	{
        		conferenceNameList.add(rst.getString("conference"));
        	}
			
        	logger.info("::conferenceNameList size is::" +conferenceNameList.size());
        	
        	if(conferenceNameList!=null && conferenceNameList.size()>0)
        	{
        		sectionsList = new ArrayList<StandingsSectionData>();
        		for(int j=0;j<conferenceNameList.size();j++)
        		{	
        			ArrayList<String> sectionTypeList = new ArrayList<String>();
        			sectionsList = new ArrayList<StandingsSectionData>();
        			Hashtable<String, ArrayList<StandingsSectionData>> sections = new Hashtable<String, ArrayList<StandingsSectionData>>();

        			rst = jdbcTemplateObject.queryForRowSet("select distinct(standings_section_type) from tbl_team_standings a, tbl_current_season b where a.year=b.season and a.conference='"+conferenceNameList.get(j)+"' order by standings_section_type");
        			logger.info("select distinct(standings_section_type) from tbl_team_standings a, tbl_current_season b where a.year=b.season and a.conference='"+conferenceNameList.get(j)+"' order by standings_section_type");
        			while(rst.next())
        			{
        				sectionTypeList.add(rst.getString("standings_section_type"));
        			}

        			logger.info("::sectionTypeList size is::" +sectionTypeList.size());        		
        			for(int i=0; i<sectionTypeList.size(); i++)
        			{
        				standingsSectionData = new StandingsSectionData();
        				sectionName = sectionTypeList.get(i);
        				logger.info("::sectionName::" +sectionName);
        				standingsSectionData.setSectionName(sectionName);
        				//rst1 = jdbcTemplateObject.queryForRowSet("select distinct(field_key_name),short_key_name from tbl_team_standings a, tbl_current_season b where a.year=b.season and standings_section_type='"+sectionName+"' order by sort_stats_level");
        				rst1 = jdbcTemplateObject.queryForRowSet("select distinct(field_key_name),short_key_name from tbl_team_standings a, tbl_current_season b where a.year=b.season and standings_section_type='"+sectionName+"' order by sort_standings_level");
        				logger.info("select distinct(field_key_name),short_key_name from tbl_team_standings a, tbl_current_season b where a.year=b.season and standings_section_type='"+sectionName+"' order by sort_standings_level");
        				keyValues = new ArrayList<StandingsKeyValues>();
        				while(rst1.next()) 
        				{
        					standingsKeyValues = new StandingsKeyValues();
        					standingsKeyValues.setKey(rst1.getString("field_key_name"));
        					standingsKeyValues.setValue(rst1.getString("short_key_name"));
        					keyValues.add(standingsKeyValues);
        				}
        				logger.info("::keyValues size::" +keyValues.size());
        				standingsSectionData.setKeyValues(keyValues);
        				//rst2 = jdbcTemplateObject.queryForRowSet("select distinct(city_name) from tbl_team_standings a, tbl_current_season b where a.year=b.season and standings_section_type='"+sectionName+"' order by sort_standings_level");
        				rst2 = jdbcTemplateObject.queryForRowSet("select distinct(city_name) from tbl_team_standings a, tbl_current_season b where a.year=b.season and standings_section_type='"+sectionName+"' and field_key_name='Percentage' order by field_value desc");
        				logger.info("select distinct(city_name) from tbl_team_standings a, tbl_current_season b where a.year=b.season and standings_section_type='"+sectionName+"' and field_key_name='Percentage' order by field_value desc");
        				sectionDataList = new ArrayList<LinkedHashMap<String, String>>();
        				while(rst2.next()) 
        				{
        					cityName = rst2.getString("city_name");
        					logger.info("::cityName::" +cityName);
        					rst3 = jdbcTemplateObject.queryForRowSet("select field_key_name,field_value from tbl_team_standings a, tbl_current_season b where a.year=b.season and standings_section_type='"+sectionName+"' and city_name='"+cityName+"' order by sort_standings_level");
        					logger.info("select field_key_name,field_value from tbl_team_standings a, tbl_current_season b where a.year=b.season and standings_section_type='"+sectionName+"' and city_name='"+cityName+"' order by sort_standings_level");
        					sectionData = new LinkedHashMap<String, String>();
        					while(rst3.next()) 
        					{
        						String fieldValue = "";
        						fieldValue = rst3.getString("field_value");
        						if(rst3.getString("field_key_name")!=null && (rst3.getString("field_key_name").equalsIgnoreCase("Percentage") ||rst3.getString("field_key_name").equalsIgnoreCase("StrengthOfVictory") || rst3.getString("field_key_name").equalsIgnoreCase("StrengthOfSchedule")))
        						{
        							fieldValue = roundFloatValue(fieldValue);
        						}
        						sectionData.put(rst3.getString("field_key_name"), fieldValue);
        					}
        					sectionDataList.add(sectionData);
        				}
        				logger.info("::sectionDataList size::" +sectionDataList.size());
        				standingsSectionData.setSectionData(sectionDataList);
        				sectionsList.add(standingsSectionData);
        			}
        			logger.info("::sectionsList size::" +sectionsList.size());
        			sections.put("sections", sectionsList);
        			teamStandingsInfo.put(conferenceNameList.get(j), sections);
        		}
        	}else
        	{	
        		return getDefaultTeamStandingsDetails();
        	}	
        		
		} catch(Exception e)
		{
			logger.error("::Exception in getTeamStandingsDetails::" +e);
			e.printStackTrace();
			errorCode = "500";
			teamStandingsInfo = null;
			return teamStandingsInfo;
		}
		return teamStandingsInfo;
	}
    
    /**
	 * Method to get default team standings details
	 */
    public Object getDefaultTeamStandingsDetails()
	{
    	logger.info("::in getTeamStandingsDetails:");
    	SqlRowSet rst = null;
    	SqlRowSet rst1 = null;
    	SqlRowSet rst2 = null;
    	SqlRowSet rst3 = null;
    	
    	LinkedHashMap<String, Hashtable> teamStandingsInfo = new LinkedHashMap<String, Hashtable>();
    	ArrayList<StandingsSectionData> sectionsList = null;
    	StandingsSectionData standingsSectionData = null;
    	ArrayList<LinkedHashMap<String, String>> sectionDataList = null;
    	LinkedHashMap<String, String> sectionData = null;
    	ArrayList<StandingsKeyValues> keyValues = null;
    	StandingsKeyValues standingsKeyValues = null;
    	ArrayList<String> conferenceNameList = new ArrayList<String>();

    	String sectionName = "";
    	String cityName = "";
    	
        try {
        	
        	rst = jdbcTemplateObject.queryForRowSet("select distinct(conference) from tbl_team_standings a, tbl_current_season b where a.year=2013 order by conference");
        	while(rst.next())
        	{
        		conferenceNameList.add(rst.getString("conference"));
        	}
			
        	logger.info("::conferenceNameList size is::" +conferenceNameList.size());
        	
        	if(conferenceNameList!=null && conferenceNameList.size()>0)
        	{
        		sectionsList = new ArrayList<StandingsSectionData>();
        		for(int j=0;j<conferenceNameList.size();j++)
        		{	
        			ArrayList<String> sectionTypeList = new ArrayList<String>();
        			sectionsList = new ArrayList<StandingsSectionData>();
        			Hashtable<String, ArrayList<StandingsSectionData>> sections = new Hashtable<String, ArrayList<StandingsSectionData>>();

        			rst = jdbcTemplateObject.queryForRowSet("select distinct(standings_section_type) from tbl_team_standings a, tbl_current_season b where a.year=2013 and a.conference='"+conferenceNameList.get(j)+"' order by standings_section_type");
        			while(rst.next())
        			{
        				sectionTypeList.add(rst.getString("standings_section_type"));
        			}

        			logger.info("::sectionTypeList size is::" +sectionTypeList.size());        		
        			for(int i=0; i<sectionTypeList.size(); i++)
        			{
        				standingsSectionData = new StandingsSectionData();
        				sectionName = sectionTypeList.get(i);
        				logger.info("::sectionName::" +sectionName);
        				standingsSectionData.setSectionName(sectionName);
        				rst1 = jdbcTemplateObject.queryForRowSet("select distinct(field_key_name),short_key_name from tbl_team_standings a, tbl_current_season b where a.year=2013 and standings_section_type='"+sectionName+"' order by sort_standings_level");
        				keyValues = new ArrayList<StandingsKeyValues>();
        				while(rst1.next()) 
        				{
        					standingsKeyValues = new StandingsKeyValues();
        					standingsKeyValues.setKey(rst1.getString("field_key_name"));
        					standingsKeyValues.setValue(rst1.getString("short_key_name"));
        					keyValues.add(standingsKeyValues);
        				}
        				logger.info("::keyValues size::" +keyValues.size());
        				standingsSectionData.setKeyValues(keyValues);
        				rst2 = jdbcTemplateObject.queryForRowSet("select distinct(city_name) from tbl_team_standings a, tbl_current_season b where a.year=2013 and standings_section_type='"+sectionName+"' and field_key_name='Percentage' order by field_value desc");
        				sectionDataList = new ArrayList<LinkedHashMap<String, String>>();
        				while(rst2.next()) 
        				{
        					cityName = rst2.getString("city_name");
        					logger.info("::cityName::" +cityName);
        					rst3 = jdbcTemplateObject.queryForRowSet("select field_key_name,field_value from tbl_team_standings a, tbl_current_season b where a.year=2013 and standings_section_type='"+sectionName+"' and city_name='"+cityName+"' order by sort_standings_level");
        					sectionData = new LinkedHashMap<String, String>();
        					while(rst3.next()) 
        					{
        						String fieldValue = "";
        						String fieldKeyName="";
        						fieldValue = rst3.getString("field_value");
        						fieldKeyName = rst3.getString("field_key_name");
        						if(fieldKeyName!=null && (fieldKeyName.equalsIgnoreCase("Percentage") ||fieldKeyName.equalsIgnoreCase("StrengthOfVictory") || fieldKeyName.equalsIgnoreCase("StrengthOfSchedule")))
        						{
        							fieldValue = "0.0";
        						}
        						if(fieldKeyName!=null && (fieldKeyName.equalsIgnoreCase("Win") ||fieldKeyName.equalsIgnoreCase("Loss") || fieldKeyName.equalsIgnoreCase("Tie") || fieldKeyName.equalsIgnoreCase("OverallPF") || fieldKeyName.equalsIgnoreCase("OverallPA")))
        						{
        							fieldValue="0";
        						}
        						if(fieldKeyName!=null && (fieldKeyName.equalsIgnoreCase("Streak")))
        						{
        							fieldValue="--";
        						}
        						if(fieldKeyName!=null && (fieldKeyName.equalsIgnoreCase("DivisionRecord")))
        						{
        							fieldValue="0-0";
        						}        						
        						sectionData.put(fieldKeyName, fieldValue);
        					}
        					sectionDataList.add(sectionData);
        				}
        				logger.info("::sectionDataList size::" +sectionDataList.size());
        				standingsSectionData.setSectionData(sectionDataList);
        				sectionsList.add(standingsSectionData);
        			}
        			logger.info("::sectionsList size::" +sectionsList.size());
        			sections.put("sections", sectionsList);
        			teamStandingsInfo.put(conferenceNameList.get(j), sections);
        		}
        	}
        		
		} catch(Exception e)
		{
			logger.error("::Exception in getTeamStandingsDetails::" +e);
			e.printStackTrace();
			errorCode = "500";
			teamStandingsInfo = null;
			return teamStandingsInfo;
		}
		return teamStandingsInfo;
	}
    private String roundFloatValue(String fieldValue)
    {
    	Double roundOff = Math.round(Double.parseDouble(fieldValue) * 10.0) / 10.0;
    	return roundOff.toString();
    }
   
	
}
