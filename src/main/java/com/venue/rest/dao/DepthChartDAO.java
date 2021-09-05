package com.venue.rest.dao;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.venue.rest.model.DepthCharClubFormations;
import com.venue.rest.model.DepthChartClubPositions;
import com.venue.rest.model.DepthChartMainModel;
import com.venue.rest.util.ErrorMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
@Repository
public class DepthChartDAO 
{
	private static Logger logger = Logger.getLogger(DepthChartDAO.class);
	String errorCode = "";
	private JdbcTemplate jdbcTemplateObject=null;
	@Autowired
	@Qualifier("dataSourceVenue")
	DataSource dataSourceVenue;
	
	
	@PostConstruct
    public void init() {
		jdbcTemplateObject = new JdbcTemplate(dataSourceVenue);	
    	
    }

   
    /**
	 * Method to get DepthChart response in JSON format.
	 */
	@SuppressWarnings("unchecked")
	public Object getDepthChart() {
        try 
        {
    		logger.info("::in getDepthChart:");

        	Object depthchart = GetDepthChartDetails();
        	if(depthchart!=null) 
            {
            	return  depthchart;
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
	 * Method to get DepthChart V2 response in JSON format.
	 */
	@SuppressWarnings("unchecked")
	public Object getDepthChartV2() {
        try 
        {
    		logger.info("::in getDepthChartV2:");

        	Object depthchart = GetDepthChartDetailsV2();
        	if(depthchart!=null) 
            {
            	return  depthchart;
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
	 * Method to get depth chart response in JSON format.
	 */
	@SuppressWarnings("unchecked")
	public Object GetDepthChartDetails() 
	{
		logger.info("::in GetDepthChartDetails:");
    	SqlRowSet rst=null;
    	SqlRowSet rst1=null;
    	SqlRowSet rst2=null;
    	DepthChartMainModel mainmodel = null;
		String formation ="";

        try
		{
        	 DepthChartMainModel depthchartmain = null;	        	 
			 rst = jdbcTemplateObject.queryForRowSet("select formation from tbl_depthchart group by formation") ;
			 ArrayList<DepthCharClubFormations> clubFormations = new ArrayList<DepthCharClubFormations>();
			 ArrayList<DepthChartClubPositions> clubPostions=null;
			 ArrayList<Hashtable<String, String>> depthChartClubPlayers=null;
			 logger.info("GetDepthChartDetails:::");
			 DepthChartClubPositions deptchartclubpositions =null;
			 Hashtable<String,String> playerslist=null;
			 mainmodel = new DepthChartMainModel();
			 while(rst.next())
			 {
				formation="";
				DepthCharClubFormations depthclubformation = new DepthCharClubFormations();
	        	clubPostions = new ArrayList<DepthChartClubPositions>();
	        	formation = rst.getString("formation");
	        	logger.info("formation is:::"+formation);
		        depthclubformation.setFormation(formation);		
		        if(formation!=null && formation.equalsIgnoreCase("offense"))
		        {
		        	rst1 = jdbcTemplateObject.queryForRowSet("select position,displayposition from tbl_depthchart where formation='"+formation+"' group by position order by position_offense_order") ;
		        }
		        else  if(formation!=null && formation.equalsIgnoreCase("defense"))
		        {
		        	rst1 = jdbcTemplateObject.queryForRowSet("select position,displayposition from tbl_depthchart where formation='"+formation+"' group by position order by position_defense_order") ;
		        	
		        }else
		        	
		        	rst1 = jdbcTemplateObject.queryForRowSet("select position,displayposition from tbl_depthchart where formation='"+formation+"' group by position") ;

				logger.info("query::select position from tbl_depthchart where formation='"+formation+"'group by position");
				
			    while (rst1.next())
		        {
		        	depthChartClubPlayers= new ArrayList<Hashtable<String, String>>();
		        	deptchartclubpositions = new DepthChartClubPositions();
		        	String position="";			        	
		        	position = rst1.getString("position");
		        	deptchartclubpositions.setPosition(position);
		        	deptchartclubpositions.setDisplayPosition(rst1.getString("displayposition"));
		        	logger.info("position::::"+position);
					rst2 = jdbcTemplateObject.queryForRowSet("select firstname,lastname,depthteam,player_number,profile_image_path,position,displayposition from tbl_depthchart where position='"+position+"' and formation='"+formation+"'") ;
					logger.info("query::::select firstname,lastname,depthteam,player_number,profile_image_path from tbl_depthchart where position='"+position+"' and formation='"+formation+"' ");
                    while(rst2.next())
                    { 
                    	playerslist= new  Hashtable<String,String>();
                    	playerslist.put("firstName", rst2.getString("firstname"));
                    	playerslist.put("lastName", rst2.getString("lastname"));
                    	playerslist.put("depthTeam", rst2.getString("depthteam"));
                    	playerslist.put("player_number", rst2.getString("player_number"));
                    	playerslist.put("profile_image_path", rst2.getString("profile_image_path"));
                    	depthChartClubPlayers.add(playerslist);
                    }
                    deptchartclubpositions.setDepthChartClubPlayers(depthChartClubPlayers); 
                    clubPostions.add(deptchartclubpositions);
		        }
		        
		        depthclubformation.setDepthChartClubPositions(clubPostions);
		        clubFormations.add(depthclubformation);
		        logger.info("clubFormations"+clubFormations.size());
			 }
			 mainmodel.setDepthChartClubFormations(clubFormations);
		}catch(Exception e)
		{
			e.printStackTrace();
			errorCode="500";
	        return ErrorMessage.getInstance().getErrorResponse(errorCode);
		}
		
		return mainmodel;
	}
	
	/**
	 * Method to get depth chart V2 response in JSON format.
	 */
	@SuppressWarnings("unchecked")
	public Object GetDepthChartDetailsV2() 
	{
		logger.info("::in GetDepthChartDetailsV2:");
    	SqlRowSet rst=null;
    	SqlRowSet rst1=null;
    	SqlRowSet rst2=null;
    	SqlRowSet rst3=null;
    	DepthChartMainModel mainmodel = null;
		String formation ="";
		int depthchartVisible = 0;
		
        try
		{
        	rst3 = jdbcTemplateObject.queryForRowSet("select depth_chart_visible from tbl_depthchart_visible");
        	if(rst3 != null && rst3.next())
        		depthchartVisible = rst3.getInt("depth_chart_visible");
        	logger.info("::depthchartVisible::" +depthchartVisible);
        	if(depthchartVisible == 1) {
        		logger.info("::depthchart is visible::");
        		DepthChartMainModel depthchartmain = null;	        	 
        		rst = jdbcTemplateObject.queryForRowSet("select formation from tbl_depthchart group by formation");
        		ArrayList<DepthCharClubFormations> clubFormations = new ArrayList<DepthCharClubFormations>();
        		ArrayList<DepthChartClubPositions> clubPostions=null;
        		ArrayList<Hashtable<String, String>> depthChartClubPlayers=null;
        		logger.info("GetDepthChartDetailsV2:::");
        		DepthChartClubPositions deptchartclubpositions =null;
        		Hashtable<String,String> playerslist=null;
        		mainmodel = new DepthChartMainModel();
        		while(rst.next())
        		{
					formation="";
					DepthCharClubFormations depthclubformation = new DepthCharClubFormations();
		        	clubPostions = new ArrayList<DepthChartClubPositions>();
		        	formation = rst.getString("formation");
		        	logger.info("formation is:::"+formation);
			        depthclubformation.setFormation(formation);		
			        if(formation!=null && formation.equalsIgnoreCase("offense"))
			        {
			        	rst1 = jdbcTemplateObject.queryForRowSet("select position,displayposition from tbl_depthchart where formation='"+formation+"' group by position order by position_offense_order") ;
			        }
			        else  if(formation!=null && formation.equalsIgnoreCase("defense"))
			        {
			        	rst1 = jdbcTemplateObject.queryForRowSet("select position,displayposition from tbl_depthchart where formation='"+formation+"' group by position order by position_defense_order") ;
			        	
			        }else
			        	
			        	rst1 = jdbcTemplateObject.queryForRowSet("select position,displayposition from tbl_depthchart where formation='"+formation+"' group by position") ;

					logger.info("query::select position from tbl_depthchart where formation='"+formation+"'group by position");
					
				    while (rst1.next())
			        {
			        	depthChartClubPlayers= new ArrayList<Hashtable<String, String>>();
			        	deptchartclubpositions = new DepthChartClubPositions();
			        	String position="";			        	
			        	position = rst1.getString("position");
			        	deptchartclubpositions.setPosition(position);
			        	deptchartclubpositions.setDisplayPosition(rst1.getString("displayposition"));
			        	logger.info("position::::"+position);
						rst2 = jdbcTemplateObject.queryForRowSet("select firstname,lastname,depthteam,player_number,profile_image_path,position,displayposition from tbl_depthchart where position='"+position+"' and formation='"+formation+"'") ;
						logger.info("query::::select firstname,lastname,depthteam,player_number,profile_image_path from tbl_depthchart where position='"+position+"' and formation='"+formation+"' ");
                        while(rst2.next())
                        { 
                        	playerslist= new  Hashtable<String,String>();
                        	playerslist.put("firstName", rst2.getString("firstname"));
                        	playerslist.put("lastName", rst2.getString("lastname"));
                        	playerslist.put("depthTeam", rst2.getString("depthteam"));
                        	playerslist.put("player_number", rst2.getString("player_number"));
                        	playerslist.put("profile_image_path", rst2.getString("profile_image_path"));
                        	depthChartClubPlayers.add(playerslist);
                        }
                        deptchartclubpositions.setDepthChartClubPlayers(depthChartClubPlayers); 
                        clubPostions.add(deptchartclubpositions);
			        }
			        
			        depthclubformation.setDepthChartClubPositions(clubPostions);
			        clubFormations.add(depthclubformation);
			        logger.info("clubFormations"+clubFormations.size());
				 }
				 mainmodel.setDepthChartClubFormations(clubFormations);
        	} else {
        		logger.info("::depthchart is not visible::");
        		mainmodel = new DepthChartMainModel();
        		ArrayList<DepthCharClubFormations> clubFormations = new ArrayList<DepthCharClubFormations>();
        		mainmodel.setDepthChartClubFormations(clubFormations);
        	}
		}catch(Exception e)
		{
			logger.error("::Exception in GetDepthChartDetailsV2::" +e);
			e.printStackTrace();
			errorCode="500";
	        return ErrorMessage.getInstance().getErrorResponse(errorCode);
		}
		return mainmodel;
	}
	
	
}
