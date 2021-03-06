/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javatpoint.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.ws.rs.POST;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;



//import com.sun.research.ws.wadl.Response;

@Path("/hello")
public class Hello {
	public static String Email="";
	@POST
	@Path("/login")
	@Produces(MediaType.TEXT_PLAIN)
	public Response Login(@FormParam("email") String email, @FormParam("password") String pwd ) throws URISyntaxException {
		Email=email;
    	try{  
    		Class.forName("com.mysql.jdbc.Driver");  
    		Connection con=DriverManager.getConnection(  
    		"jdbc:mysql://localhost:3306/jersey","root","ankurmangal");  
    		
    		Statement stmt=con.createStatement();  
    		
    		ResultSet rs=stmt.executeQuery("select email,password from AddUser");  
    		while(rs.next())  
    		{
    			if(email.equals(rs.getString(1)) && pwd.equals(rs.getString(2)))
    			{
    				URI location = new URI("http://localhost:8080/RestJersey/home.html");
    		    	return Response.seeOther(location).build(); 	
    			}
    			
    			
    		}
    		con.close();  
    		}catch(Exception e){ System.out.println(e);}  
    	
		URI location = new URI("http://localhost:8080/RestJersey/login.html");
    	return Response.seeOther(location).build();
		
    	
        
    }
    @POST
    @Path("/signup")
    @Produces(MediaType.TEXT_PLAIN)
    public Response signup(@FormParam("name") String name,@FormParam("email") String email, @FormParam("password") String pwd ) throws URISyntaxException {
    	try{  
    		Class.forName("com.mysql.jdbc.Driver");  
    		Connection con=DriverManager.getConnection(  
    		"jdbc:mysql://localhost:3306/jersey","root","ankurmangal");  
    		 
    		PreparedStatement ps=con.prepareStatement("insert into AddUser values(?,?,?)");  
    		
    		ps.setString(1,name);  
    		ps.setString(2,email);  
    		ps.setString(3,pwd);  
    		ps.executeUpdate();  	
    		
    		con.close();  
    		}catch(Exception e){ System.out.println(e);}  
    		  
    	URI location = new URI("http://localhost:8080/RestJersey/index.html");
    	return Response.seeOther(location).build(); 
    	
        
    }
    @GET
    @Path("/viewall")
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewall() throws URISyntaxException {
    	String output = "";
    	JSONArray jArray = new JSONArray();
    	
    	try{  
    		Class.forName("com.mysql.jdbc.Driver");  
    		Connection con=DriverManager.getConnection(  
    		"jdbc:mysql://localhost:3306/jersey","root","ankurmangal");  
    	
    		Statement stmt=con.createStatement();  
    		ResultSet rs1 = stmt.executeQuery("select * from UserComments");
    		while(rs1.next())
    		{
    			JSONObject jobj = new JSONObject();
    			jobj.put("Name",rs1.getString(1));
    			jobj.put("Comment", rs1.getString(2));
    			jArray.put(jobj);
    		}
			  return Response.status(200).entity(jArray.toString()).build();
}catch(Exception e){ System.out.println(e);} 
    		  

    	
return null;
    }
    @GET
    @Path("/YourComments")
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewYourComments() throws URISyntaxException {
    	String output = "";
    	String name="";
    	JSONArray jArray = new JSONArray();
    	
    	
    	
    	try{  
    		Class.forName("com.mysql.jdbc.Driver");  
    		Connection con=DriverManager.getConnection(  
    		"jdbc:mysql://localhost:3306/jersey","root","ankurmangal");  
    	
    		Statement stmt=con.createStatement();
    		String query = "select name from AddUser where email =?";
    		PreparedStatement ps = con.prepareStatement(query);
    		ps.setString(1,Email);
    		ResultSet rs = ps.executeQuery();
    		while(rs.next())  
    		{
    			name = rs.getString(1);	
    		}
    		ResultSet rs1=stmt.executeQuery("select * from UserComments where name='"+name+"'");
			  while(rs1.next()) 
			  { 
				  JSONObject jobj = new JSONObject();
				  jobj.put("Name",rs1.getString(1));
				  jobj.put("Comment",rs1.getString(2));
				  jArray.put(jobj);
			}
  		con.close();
  		return Response.status(200).entity(jArray.toString()).build();	
  		}catch(Exception e){ System.out.println(e);}  


return null;
    }
    @POST
    @Path("/AddComment")
    @Produces(MediaType.TEXT_PLAIN)
    public Response AddComment(@FormParam("comment") String comment ) throws URISyntaxException {
    	String name ="";
    	try{  
    		Class.forName("com.mysql.jdbc.Driver");  
    		Connection con=DriverManager.getConnection(  
    		"jdbc:mysql://localhost:3306/jersey","root","ankurmangal");  
    		String query = "select name from AddUser where email =?";
    		PreparedStatement ps = con.prepareStatement(query);
    		ps.setString(1,Email);
    		ResultSet rs = ps.executeQuery();
            
    		while(rs.next())  
    		{
    			
    			name = rs.getString(1);
    		}
    		PreparedStatement ps1=con.prepareStatement("insert into UserComments values(?,?)");  
    		
    		ps1.setString(1,name);  
    		ps1.setString(2,comment);  
    		  
    		ps1.executeUpdate();  	
    		
    		con.close();  
    		}catch(Exception e){ System.out.println(e);}  
    		  
    	URI location = new URI("http://localhost:8080/RestJersey/home.html");
    	return Response.seeOther(location).build(); 
    	
        
    }
    }