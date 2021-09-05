package com.venue.rest.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Matchers.eq;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.http.HttpEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class MenuDAOTest {

	private static Logger logger = Logger.getLogger(MenuDAOTest.class);
	
	private static final String menuId = "229";
	private static final String appUserId = "14f4a8a6e0d";
	private static final String menuType = "menu";
	private static final String calendarId = "0";
	private static final String emp2UserId = "IPHONEcc605529-25f2-4b62-927e-48cc8c1ff177";
	
	private MenuDAO dao;
	private JdbcTemplate jdbcTemplate;
	private SqlRowSet sqlRowSetMock;
	private Map<SqlRowSet, MockRow> mockRows;
	private RestTemplate restTemplate;
	
	@Before
	public void setUp() {
		jdbcTemplate = Mockito.mock(JdbcTemplate.class);
		restTemplate = Mockito.mock(RestTemplate.class);
        // you need to add another constructor to MenuDAO to accept an external JDBCTemplate instance rather because default constructor calls initializeDB internally
		// which tries to create a real JDBCTemplate from spring config. For testing MenuDAO you don't want it doing that, instead you want to pass in a 'mocked' 
		// JDBCTemplate instance to MenuDAO here instead.
		dao = new MenuDAO(jdbcTemplate, restTemplate);
	    mockRows = new HashMap<SqlRowSet, MockRow>();
	}
	
	/**
	 * Test method to checkPlaceSegmentsWhenUserInplace
	 * @throws Exception
	 */
	@Test
	public void checkPlaceSegmentsWhenUserInplace() throws Exception {
		System.out.println("*******Executing case 1********");
		System.out.println("in checkPlaceSegmentsWhenUserInplace");
        // build the list of fake result sets that should be returned for each invocation of jdbcTemplate.queryFroRowSet() that checkPlaceAndAudienceSegments() performs
        
        // Fake results for menu place sql query
        List<Map<String,String>> placeResult = new ArrayList<Map<String,String>>();
        placeResult.add(new HashMap<String,String>());
        placeResult.get(0).put("place_segment_id","123");
        placeResult.get(0).put("place_segment_name","fake_name_row_1");
        placeResult.get(0).put("negative","0");
        placeResult.add(new HashMap<String,String>());
        placeResult.get(1).put("place_segment_id","456");
        placeResult.get(1).put("place_segment_name","fake_name_row_2");
        placeResult.get(1).put("negative","0");
        // define the matcher that will assign this fake result set to the correct sql query execution
        doReturn(generateFakeRowSet(placeResult)).when(jdbcTemplate).queryForRowSet("select * from tbl_menu_place_map where id=? and menu_type=?", new Object[]{menuId, menuType});
        
        
        // Fake results for menu audience sql query
        List<Map<String,String>> audienceResult = new ArrayList<Map<String,String>>();
        // define the matcher that will assign this fake result set to the correct sql query execution
        doReturn(generateFakeRowSet(audienceResult)).when(jdbcTemplate).queryForRowSet("select * from tbl_menu_audience_map where id=? and menu_type=?", new Object[]{menuId, menuType});
        
        
        // Fake response for REST API to confirm user belongs to app boy segments or normal segments
        doReturn(false).when(restTemplate).postForObject(any(URI.class), any(HttpEntity.class), eq(boolean.class));
        
        
        // Fake results for menu scheduler sql query
        List<Map<String,String>> schedulerResult = new ArrayList<Map<String,String>>();
        schedulerResult.add(new HashMap<String,String>());
        schedulerResult.get(0).put("start_datetime","00:00:00");
        schedulerResult.get(0).put("end_datetime","00:00:00");
        schedulerResult.get(0).put("repeat_type","0");
        schedulerResult.get(0).put("repeat_weekly_on","0");
        // define the matcher that will assign this fake result set to the correct sql query execution
        doReturn(generateFakeRowSet(schedulerResult)).when(jdbcTemplate).queryForRowSet("select * from tbl_menu_scheduler where menu_id=? and calendar_id=?", new Object[]{menuId, calendarId});
        
        
        // now run the method and verify the result
        HashMap<Object, Object> places = new HashMap<Object, Object>();
		places.put("placeName", "fake_name_row_1");
		ArrayList<HashMap<Object, Object>> userCurrentPlaces = new ArrayList<HashMap<Object, Object>>();
		userCurrentPlaces.add(places);
		
		boolean status = dao.checkPlaceAndCalendarSegments(menuId, 0, "", false,
				appUserId, userCurrentPlaces, menuType, calendarId);
		
		// assuming you've added fake sql result set data to get a 'true' back
        assertTrue(status);
	}
	
	/**
	 * Test method to checkPlaceSegmentsWhenUserNotInPlace
	 * @throws Exception
	 */
	@Test
	public void checkPlaceSegmentsWhenUserNotInPlace() throws Exception {
		System.out.println("*******Executing case 2********");
		System.out.println("in checkPlaceSegmentsWhenUserNotInPlace");
        // build the list of fake result sets that should be returned for each invocation of jdbcTemplate.queryFroRowSet() that checkPlaceAndAudienceSegments() performs
        
        // Fake results for menu place sql query
        List<Map<String,String>> placeResult = new ArrayList<Map<String,String>>();
        placeResult.add(new HashMap<String,String>());
        placeResult.get(0).put("place_segment_id","123");
        placeResult.get(0).put("place_segment_name","fake_name_row_1");
        placeResult.get(0).put("negative","1");
        placeResult.add(new HashMap<String,String>());
        placeResult.get(1).put("place_segment_id","456");
        placeResult.get(1).put("place_segment_name","fake_name_row_2");
        placeResult.get(1).put("negative","1");
        // define the matcher that will assign this fake result set to the correct sql query execution
        doReturn(generateFakeRowSet(placeResult)).when(jdbcTemplate).queryForRowSet("select * from tbl_menu_place_map where id=? and menu_type=?", new Object[]{menuId, menuType});
        
        
        // Fake results for menu audience sql query
        List<Map<String,String>> audienceResult = new ArrayList<Map<String,String>>();
        // define the matcher that will assign this fake result set to the correct sql query execution
        doReturn(generateFakeRowSet(audienceResult)).when(jdbcTemplate).queryForRowSet("select * from tbl_menu_audience_map where id=? and menu_type=?", new Object[]{menuId, menuType});
        
        
        // Fake response for REST API to confirm user belongs to app boy segments or normal segments
        doReturn(false).when(restTemplate).postForObject(any(URI.class), any(HttpEntity.class), eq(boolean.class));
        
        
        // Fake results for menu scheduler sql query
        List<Map<String,String>> schedulerResult = new ArrayList<Map<String,String>>();
        schedulerResult.add(new HashMap<String,String>());
        schedulerResult.get(0).put("start_datetime","00:00:00");
        schedulerResult.get(0).put("end_datetime","00:00:00");
        schedulerResult.get(0).put("repeat_type","0");
        schedulerResult.get(0).put("repeat_weekly_on","0");
        // define the matcher that will assign this fake result set to the correct sql query execution
        doReturn(generateFakeRowSet(schedulerResult)).when(jdbcTemplate).queryForRowSet("select * from tbl_menu_scheduler where menu_id=? and calendar_id=?", new Object[]{menuId, calendarId});
        
        
        // now run the method and verify the result
        HashMap<Object, Object> places = new HashMap<Object, Object>();
		places.put("placeName", "fake_name_row_3");
		ArrayList<HashMap<Object, Object>> userCurrentPlaces = new ArrayList<HashMap<Object, Object>>();
		userCurrentPlaces.add(places);
		
		boolean status = dao.checkPlaceAndCalendarSegments(menuId, 0, "",
				false, appUserId, userCurrentPlaces, menuType, calendarId);
		
		// assuming you've added fake sql result set data to get a 'true' back
        assertTrue(status);
	}
	
	/**
	 * Test method to checkNormalAudienceSegments
	 * @throws Exception
	 */
	@Test
	public void checkNormalAudienceSegments() throws Exception {
		System.out.println("*******Executing case 3********");
		System.out.println("in checkNormalAudienceSegments");
        // build the list of fake result sets that should be returned for each invocation of jdbcTemplate.queryFroRowSet() that checkPlaceAndAudienceSegments() performs
        
        // Fake results for menu place sql query
        List<Map<String,String>> placeResult = new ArrayList<Map<String,String>>();
        // define the matcher that will assign this fake result set to the correct sql query execution
        doReturn(generateFakeRowSet(placeResult)).when(jdbcTemplate).queryForRowSet("select * from tbl_menu_place_map where id=? and menu_type=?", new Object[]{menuId, menuType});
        
        
        // Fake results for menu audience sql query
        List<Map<String,String>> audienceResult = new ArrayList<Map<String,String>>();
        audienceResult.add(new HashMap<String,String>());
        audienceResult.get(0).put("audience_segment_id","1");
        // define the matcher that will assign this fake result set to the correct sql query execution
        doReturn(generateFakeRowSet(audienceResult)).when(jdbcTemplate).queryForRowSet("select * from tbl_menu_audience_map where id=? and menu_type=?", new Object[]{menuId, menuType});
        
        
        // Fake response for REST API to confirm user belongs to app boy segments or normal segments
        doReturn(true).when(restTemplate).postForObject(any(URI.class), any(HttpEntity.class), eq(boolean.class));
        
        
        // Fake results for menu scheduler sql query
        List<Map<String,String>> schedulerResult = new ArrayList<Map<String,String>>();
        schedulerResult.add(new HashMap<String,String>());
        schedulerResult.get(0).put("start_datetime","00:00:00");
        schedulerResult.get(0).put("end_datetime","00:00:00");
        schedulerResult.get(0).put("repeat_type","0");
        schedulerResult.get(0).put("repeat_weekly_on","0");
        // define the matcher that will assign this fake result set to the correct sql query execution
        doReturn(generateFakeRowSet(schedulerResult)).when(jdbcTemplate).queryForRowSet("select * from tbl_menu_scheduler where menu_id=? and calendar_id=?", new Object[]{menuId, calendarId});
        
        
        // now run the method and verify the result
        HashMap<Object, Object> places = new HashMap<Object, Object>();
		places.put("placeName", "fake_name_row_1");
		ArrayList<HashMap<Object, Object>> userCurrentPlaces = new ArrayList<HashMap<Object, Object>>();
		userCurrentPlaces.add(places);
		
		boolean status = dao.checkPlaceAndCalendarSegments(menuId, 0, "", false,
				appUserId, userCurrentPlaces, menuType, calendarId);
		
		// assuming you've added fake sql result set data to get a 'true' back
        assertTrue(status);
	}
	
	/**
	 * Test method to checkAppBoyAudienceSegments
	 * @throws Exception
	 */
	@Test
	public void checkAppBoyAudienceSegments() throws Exception {
		System.out.println("*******Executing case 4********");
		System.out.println("in checkAppBoyAudienceSegments");
        // build the list of fake result sets that should be returned for each invocation of jdbcTemplate.queryFroRowSet() that checkPlaceAndAudienceSegments() performs
        
        // Fake results for menu place sql query
        List<Map<String,String>> placeResult = new ArrayList<Map<String,String>>();
        // define the matcher that will assign this fake result set to the correct sql query execution
        doReturn(generateFakeRowSet(placeResult)).when(jdbcTemplate).queryForRowSet("select * from tbl_menu_place_map where id=? and menu_type=?", new Object[]{menuId, menuType});
        
        
        // Fake results for menu audience sql query
        List<Map<String,String>> audienceResult = new ArrayList<Map<String,String>>();
        audienceResult.add(new HashMap<String,String>());
        audienceResult.get(0).put("audience_segment_id","1");
        // define the matcher that will assign this fake result set to the correct sql query execution
        doReturn(generateFakeRowSet(audienceResult)).when(jdbcTemplate).queryForRowSet("select * from tbl_menu_audience_map where id=? and menu_type=?", new Object[]{menuId, menuType});
        
        
        // Fake response for REST API to confirm user belongs to app boy segments or normal segments
        doReturn(true).when(restTemplate).postForObject(any(URI.class), any(HttpEntity.class), eq(boolean.class));
        
        
        // Fake results for menu scheduler sql query
        List<Map<String,String>> schedulerResult = new ArrayList<Map<String,String>>();
        schedulerResult.add(new HashMap<String,String>());
        schedulerResult.get(0).put("start_datetime","00:00:00");
        schedulerResult.get(0).put("end_datetime","00:00:00");
        schedulerResult.get(0).put("repeat_type","0");
        schedulerResult.get(0).put("repeat_weekly_on","0");
        // define the matcher that will assign this fake result set to the correct sql query execution
        doReturn(generateFakeRowSet(schedulerResult)).when(jdbcTemplate).queryForRowSet("select * from tbl_menu_scheduler where menu_id=? and calendar_id=?", new Object[]{menuId, calendarId});
        
        
        // now run the method and verify the result
        HashMap<Object, Object> places = new HashMap<Object, Object>();
		places.put("placeName", "fake_name_row_1");
		ArrayList<HashMap<Object, Object>> userCurrentPlaces = new ArrayList<HashMap<Object, Object>>();
		userCurrentPlaces.add(places);
		
		boolean status = dao.checkPlaceAndCalendarSegments(menuId, 0, "",
				false, appUserId, userCurrentPlaces, menuType, calendarId);
		
		// assuming you've added fake sql result set data to get a 'true' back
        assertTrue(status);
	}
	
	/**
	 * Test method to checkPlaceAndAudienceSegmentsWhenUserInplace
	 * @throws Exception
	 */
	@Test
	public void checkPlaceAndAudienceSegmentsWhenUserInplace() throws Exception {
		System.out.println("*******Executing case 5********");
		System.out.println("in checkPlaceAndAudienceSegmentsWhenUserInplace");
        // build the list of fake result sets that should be returned for each invocation of jdbcTemplate.queryFroRowSet() that checkPlaceAndAudienceSegments() performs
        
        // Fake results for menu place sql query
        List<Map<String,String>> placeResult = new ArrayList<Map<String,String>>();
        placeResult.add(new HashMap<String,String>());
        placeResult.get(0).put("place_segment_id","123");
        placeResult.get(0).put("place_segment_name","fake_name_row_1");
        placeResult.get(0).put("negative","0");
        placeResult.add(new HashMap<String,String>());
        placeResult.get(1).put("place_segment_id","456");
        placeResult.get(1).put("place_segment_name","fake_name_row_2");
        placeResult.get(1).put("negative","0");
        // define the matcher that will assign this fake result set to the correct sql query execution
        doReturn(generateFakeRowSet(placeResult)).when(jdbcTemplate).queryForRowSet("select * from tbl_menu_place_map where id=? and menu_type=?", new Object[]{menuId, menuType});
        
        
        // Fake results for menu audience sql query
        List<Map<String,String>> audienceResult = new ArrayList<Map<String,String>>();
        audienceResult.add(new HashMap<String,String>());
        audienceResult.get(0).put("audience_segment_id","1");
        // define the matcher that will assign this fake result set to the correct sql query execution
        doReturn(generateFakeRowSet(audienceResult)).when(jdbcTemplate).queryForRowSet("select * from tbl_menu_audience_map where id=? and menu_type=?", new Object[]{menuId, menuType});
        
        
        // Fake response for REST API to confirm user belongs to app boy segments or normal segments
        doReturn(true).when(restTemplate).postForObject(any(URI.class), any(HttpEntity.class), eq(boolean.class));
        
        
        // Fake results for menu scheduler sql query
        List<Map<String,String>> schedulerResult = new ArrayList<Map<String,String>>();
        schedulerResult.add(new HashMap<String,String>());
        schedulerResult.get(0).put("start_datetime","00:00:00");
        schedulerResult.get(0).put("end_datetime","00:00:00");
        schedulerResult.get(0).put("repeat_type","0");
        schedulerResult.get(0).put("repeat_weekly_on","0");
        // define the matcher that will assign this fake result set to the correct sql query execution
        doReturn(generateFakeRowSet(schedulerResult)).when(jdbcTemplate).queryForRowSet("select * from tbl_menu_scheduler where menu_id=? and calendar_id=?", new Object[]{menuId, calendarId});
        
        
        // now run the method and verify the result
        HashMap<Object, Object> places = new HashMap<Object, Object>();
		places.put("placeName", "fake_name_row_1");
		ArrayList<HashMap<Object, Object>> userCurrentPlaces = new ArrayList<HashMap<Object, Object>>();
		userCurrentPlaces.add(places);
		
		boolean status = dao.checkPlaceAndCalendarSegments(menuId, 0, "",
				false, appUserId, userCurrentPlaces, menuType, calendarId);
		
		// assuming you've added fake sql result set data to get a 'true' back
        assertTrue(status);
	}
	
	/**
	 * Test method to checkPlaceAndAudienceSegmentsWhenUserNotInPlace
	 * @throws Exception
	 */
	@Test
	public void checkPlaceAndAudienceSegmentsWhenUserNotInPlace() throws Exception {
		System.out.println("*******Executing case 6********");
		System.out.println("in checkPlaceAndAudienceSegmentsWhenUserNotInPlace");
        // build the list of fake result sets that should be returned for each invocation of jdbcTemplate.queryFroRowSet() that checkPlaceAndAudienceSegments() performs
        
        setUpNegativePlaces();
        
        // now run the method and verify the result
        HashMap<Object, Object> places = new HashMap<Object, Object>();
		places.put("placeName", "fake_name_row_3");
		ArrayList<HashMap<Object, Object>> userCurrentPlaces = new ArrayList<HashMap<Object, Object>>();
		userCurrentPlaces.add(places);
		
		boolean status = dao.checkPlaceAndCalendarSegments(menuId, 0, "",
				false, appUserId, userCurrentPlaces, menuType, calendarId);
		
		// assuming you've added fake sql result set data to get a 'true' back
        assertTrue(status);
	}

	@Test
	public void checkPlaceAndAudienceSegmentsWhenUserNotInPlaceAndNoPlacesSubmitted() throws Exception {
		System.out.println("*******Executing case 7********");
		System.out.println("in checkPlaceAndAudienceSegmentsWhenUserNotInPlaceAndNoPlacesSubmitted");
		// build the list of fake result sets that should be returned for each invocation of jdbcTemplate.queryFroRowSet() that checkPlaceAndAudienceSegments() performs

		setUpNegativePlaces();

		boolean status = dao.checkPlaceAndCalendarSegments(menuId, 0, "",
				false, appUserId, new ArrayList(), menuType, calendarId);

		assertTrue(status);
	}

	@Test
	public void checkPlaceAndAudienceSegmentsWhenUserNotInPlaceAndSubmittedOnePlaceThatMatched() throws Exception {
		System.out.println("*******Executing case 8********");
		System.out.println("in checkPlaceAndAudienceSegmentsWhenUserNotInPlaceAndSubmittedOnePlaceThatMatched");
		// build the list of fake result sets that should be returned for each invocation of jdbcTemplate.queryFroRowSet() that checkPlaceAndAudienceSegments() performs

		setUpNegativePlaces();

		// now run the method and verify the result
		HashMap<Object, Object> places = new HashMap<Object, Object>();
		places.put("placeName", "fake_name_row_3");
		ArrayList<HashMap<Object, Object>> userCurrentPlaces = new ArrayList<HashMap<Object, Object>>();
		userCurrentPlaces.add(places);
		places = new HashMap<Object, Object>();
		places.put("placeName", "fake_name_row_2");
		userCurrentPlaces.add(places);

		boolean status = dao.checkPlaceAndCalendarSegments(menuId, 1, "anything",
				false, appUserId, userCurrentPlaces, menuType, calendarId);

		assertFalse(status);
	}


	private void setUpNegativePlaces() {
		// Fake results for menu place sql query
		List<Map<String,String>> placeResult = new ArrayList<Map<String,String>>();
		placeResult.add(new HashMap<String,String>());
		placeResult.get(0).put("place_segment_id","123");
		placeResult.get(0).put("place_segment_name","fake_name_row_1");
		placeResult.get(0).put("negative","1");
		placeResult.add(new HashMap<String,String>());
		placeResult.get(1).put("place_segment_id","456");
		placeResult.get(1).put("place_segment_name","fake_name_row_2");
		placeResult.get(1).put("negative","1");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(placeResult)).when(jdbcTemplate).queryForRowSet("select * from tbl_menu_place_map where id=? and menu_type=?", new Object[]{menuId, menuType});


		// Fake results for menu audience sql query
		List<Map<String,String>> audienceResult = new ArrayList<Map<String,String>>();
		audienceResult.add(new HashMap<String,String>());
		audienceResult.get(0).put("audience_segment_id","1");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(audienceResult)).when(jdbcTemplate).queryForRowSet("select * from tbl_menu_audience_map where id=? and menu_type=?", new Object[]{menuId, menuType});


		// Fake response for REST API to confirm user belongs to app boy segments or normal segments
		doReturn(true).when(restTemplate).postForObject(any(URI.class), any(HttpEntity.class), eq(boolean.class));


		// Fake results for menu scheduler sql query
		List<Map<String,String>> schedulerResult = new ArrayList<Map<String,String>>();
		schedulerResult.add(new HashMap<String,String>());
		schedulerResult.get(0).put("start_datetime","00:00:00");
		schedulerResult.get(0).put("end_datetime","00:00:00");
		schedulerResult.get(0).put("repeat_type","0");
		schedulerResult.get(0).put("repeat_weekly_on","0");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(schedulerResult)).when(jdbcTemplate).queryForRowSet("select * from tbl_menu_scheduler where menu_id=? and calendar_id=?", new Object[]{menuId, calendarId});

	}
	
	private SqlRowSet generateFakeRowSet(List<Map<String,String>> data) {
		sqlRowSetMock = Mockito.mock(SqlRowSet.class);
		MockRow mockRowData = new MockRow(data);
		mockRows.put(sqlRowSetMock, mockRowData);
		
        doAnswer(new Answer<Boolean>() {
	        @Override
	        public Boolean answer(InvocationOnMock invocation) throws Throwable {
	        	//mockRows.get(invocation.getMock()).nextRow();
	            return mockRows.get(invocation.getMock()).nextRow();
	        }
        }).when(sqlRowSetMock).next();

	    doAnswer(new Answer<String>() {
	        @Override
	        public String answer(InvocationOnMock invocation) throws Throwable {
	            Object[] args = invocation.getArguments();
	            String name = ((String) args[0]);
	            return mockRows.get(invocation.getMock()).getColumn(name);
	        }

	        ;
	    }).when(sqlRowSetMock).getString(anyString());
	    
	    doAnswer(new Answer<Integer>() {
	        @Override
	        public Integer answer(InvocationOnMock invocation) throws Throwable {
	            Object[] args = invocation.getArguments();
	            String id = ((String) args[0]);
	            return Integer.parseInt(mockRows.get(invocation.getMock()).getColumn(id));
	        }

	        ;
	    }).when(sqlRowSetMock).getInt(anyString());

		doAnswer(new Answer<Boolean>() {
			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				String id = ((String) args[0]);
				return mockRows.get(invocation.getMock()).getColumn(id).equals("1");
			}

			;
		}).when(sqlRowSetMock).getBoolean(anyString());
	    
	    return sqlRowSetMock;
	}

	static class MockRow {
	    List<Map<String,String>> rowData;
	    int rowIndex = -1;

	    public MockRow(List<Map<String,String>> rowData) {
	        this.rowData = rowData;
	    }

        public boolean nextRow() {
        	rowIndex++;
        	if(rowData.size() == rowIndex)
        		return false;
        	else
        		return true;
        }
        
	    public String getColumn(String name) {
	    	return rowData.get(rowIndex).get(name);
	    }
    }
}