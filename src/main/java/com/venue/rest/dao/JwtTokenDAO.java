package com.venue.rest.dao;
import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.venue.rest.model.JwtTokenBean;
import com.venue.rest.util.ErrorMessage;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Repository
public class JwtTokenDAO {
	private static Logger logger = Logger.getLogger(JwtTokenDAO.class);
	String errorCode = "";
	JdbcTemplate jdbcTemplateVenue = null;
	@Autowired
	@Qualifier("dataSourceVenue")
	DataSource dataSourceVenue;
	
	
	@PostConstruct
    public void init() {
		jdbcTemplateVenue = new JdbcTemplate(dataSourceVenue);	
    	
    }

	public Object getToken( JwtTokenBean jwtTokenBean,String jwtSecretKey) {
		logger.info("::inside getToken::");
		try {
			Object Token = generateJWTToken(jwtTokenBean,jwtSecretKey);
			if (Token != null) {
				return Token;
			} else {
				return ErrorMessage.getInstance().getErrorResponse(errorCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public Object generateJWTToken( JwtTokenBean jwtTokenBean,String jwtSecretKey) {
		logger.info("::inside getToken::");
		String jwsStr="";
		HashMap<String, String> token=new HashMap<String, String>();
		try {
			ArrayList<String> scope = new ArrayList<String>();
			scope.add("read");
			scope.add("write");
			long now = System.currentTimeMillis() / 1000;
			long exp = now + 86400;		 
			
			jwsStr = Jwts.builder()
					.setHeaderParam("typ", "JWT")
					.setHeaderParam("alg", "HS256")
					.claim("user_name",jwtTokenBean.getEmp2UserId())
					.claim("scope", scope)
					.claim("iss", "https://www.venuetize.com/")
					.claim("exp", exp)
					.claim("iat", now)
					.claim("uuid", jwtTokenBean.getEmp2UserId())
					.claim("client_id", "venuetize")	
					.signWith(SignatureAlgorithm.HS256, jwtSecretKey)
					.compact();

			logger.info("::jwsStr ::"+jwsStr);
			if(token.size()>=0)
			{
				token.put("jwt_token",jwsStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return token;
	}

	
}
