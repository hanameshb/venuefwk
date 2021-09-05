package com.venue.rest.dao;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.venue.rest.model.Application;

@Repository
public class ApplicationDAO {
	
	private static Logger logger = LogManager.getLogger(ApplicationDAO.class);
	JdbcTemplate jdbcTemplateVenue = null;
	
	@Autowired
	@Qualifier("dataSourceVenue")
	DataSource dataSourceVenue;
	
	@PostConstruct
    public void init() {
		jdbcTemplateVenue = new JdbcTemplate(dataSourceVenue);	
	}
	
	public List<Application> fetchApplications() throws Exception {
		logger.info("::in fetchApplications DAO::");
		List<Application> applications = newArrayList();
		Application application = null;
		SqlRowSet rstApplication = null;
		
		rstApplication = jdbcTemplateVenue.queryForRowSet("select * from tbl_applications_master");
		while (rstApplication.next()) {
			application = new Application();
			application.setApplicationId(rstApplication.getLong("application_id"));
			application.setApplicationName(rstApplication.getString("application_name"));
			application.setAccountApiKey(rstApplication.getString("emkit_api_key"));
			applications.add(application);
		}
		logger.info("::fetched {} applications::", applications.size());
		return applications;
	}
}