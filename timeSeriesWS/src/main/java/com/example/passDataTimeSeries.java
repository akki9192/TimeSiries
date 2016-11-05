package com.example;

import java.sql.ResultSet;
import java.sql.SQLException;

//import java.text.DateFormat;
//import java.util.Date;
//import java.util.Locale;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class passDataTimeSeries {
	
	Integer appTot;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@CrossOrigin
	@RequestMapping("/acceptAppData")
	public String passData(@RequestParam (value="appname") String appname, @RequestParam(value="datetimestamp") String datetimestamp, @RequestParam(value="username") String username){
		
	    try {
	       	System.out.println("In Insert");
			jdbcTemplate.update("INSERT INTO userapplog(uname,app,datetimeStamp) VALUES (?,?, ?)",
					username,appname, datetimestamp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
			
		return "{\"appname\":" + "\"" + appname + "\"" + "," + "\"datetimestamp\":" + "\"" + datetimestamp + "\"" + "," + "\"username\":" + "\"" + username + "\"" + "}";
	}
	
	@CrossOrigin
	@RequestMapping("/getAppData")
	public ArrayList<Object> getAppData(){
		
	    
		System.out.println("In Select");
	    ArrayList<Object> al1 = new ArrayList<Object>();
		al1.add("Spring Tool Suite");
		al1.add(getAppCount("Suite"));
		ArrayList<Object> al2 = new ArrayList<Object>();
		al2.add("Eclipse");
		al2.add(getAppCount("Eclipse"));
		ArrayList<Object> al3 = new ArrayList<Object>();
		al3.add("Google Chrome");
		al3.add(getAppCount("Chrome"));
		ArrayList<Object> al4 = new ArrayList<Object>();
		al4.add("Command Prompt");
		al4.add(getAppCount("cmd"));
		ArrayList<Object> al5 = new ArrayList<Object>();
		al5.add("Internet Explorer");
		al5.add(getAppCount("Explorer"));
		ArrayList<Object> al = new ArrayList<Object>();
		al.add(al1);
		al.add(al2);
		al.add(al3);
		al.add(al4);
		al.add(al5);
	    return al;
	}
	
	public Integer getAppCount(String appname)
	{
		Integer total = jdbcTemplate.queryForObject("select count(app) from userapplog where app like '%" + appname + "%'", Integer.class);
		appTot = total;
		return appTot;
	}
	
	@CrossOrigin
	@RequestMapping("/getUserAppLogs")
	public List<UserAppLog> getUserAppLogs(){
		
		String appLogQuery = "select logid,uname,app,datetimestamp FROM public.userapplog";
		
		return jdbcTemplate.query(appLogQuery, new Object[] {},new  RowMapper<UserAppLog>(){ 
	              public UserAppLog mapRow(ResultSet rs, int arg1) throws SQLException {
	              
	              UserAppLog d = new UserAppLog();
	              d.setLogID(rs.getInt("logid"));
	              d.setuName(rs.getString("uname"));
	              d.setApp(rs.getString("app"));
	              d.setDatetimestamp(rs.getString("datetimestamp"));
	                 
	                 return d;
	                 }
	                 });
		
		
		
		
	}
	
	@CrossOrigin
	@RequestMapping("/getUserAppLogs/{userID}")
	public @ResponseBody List<UserAppLog> getUserAppLogs(@PathVariable("userID") String userID){
		
		String appLogQuery = "select logid,uname,app,datetimestamp FROM public.userapplog where uname='" + userID +"'";
		
		return jdbcTemplate.query(appLogQuery, new Object[] {},new  RowMapper<UserAppLog>(){ 
	              public UserAppLog mapRow(ResultSet rs, int arg1) throws SQLException {
	              
	              UserAppLog d = new UserAppLog();
	              d.setLogID(rs.getInt("logid"));
	              d.setuName(rs.getString("uname"));
	              d.setApp(rs.getString("app"));
	              d.setDatetimestamp(rs.getString("datetimestamp"));
	                 
	                 return d;
	                 }
	                 });
		
		
		
		
	}
	
	@CrossOrigin
	@RequestMapping("/getUserAppSummary")
	public List<UserAppSummary> getUserAppSummary(){
		
		String appLogQuery = "select ltrim(replace(substring(app, (length(app) - position('-' in reverse(app)) + 1),length(app)),'-','')) as app, count(*) as appCount from userapplog group by  ltrim(replace(substring(app, (length(app) - position('-' in reverse(app)) + 1),length(app)),'-','')) order by 2 desc";
		
//		List<String>
//		List<Integer>
		
		return jdbcTemplate.query(appLogQuery, new Object[] {},new  RowMapper<UserAppSummary>(){ 
	              public UserAppSummary mapRow(ResultSet rs, int arg1) throws SQLException {
	              UserAppSummary obj = new UserAppSummary();
	              obj.setApp(rs.getString("app"));
	              obj.setAppCount(rs.getInt("appCount"));
	              return obj;
	           }
	            });
	}
}
