package com.code.connection.createConnection;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

/**
 * Hello world!
 *
 */
public class App 
{
    @SuppressWarnings("deprecation")
	public static void main( String[] args )
    {
    	String url = "jdbc:postgresql://localhost:5432/BookStall";
		String user = "postgres";
		String pass = "admin";
		
		List<BookDetails> listOfBooks = new ArrayList<BookDetails>();
		Gson gson = new Gson();
		JSONObject jObject = new JSONObject();
		JSONArray jArray = new JSONArray();
		ObjectMapper objMapper = new ObjectMapper();
		
		
		try
		{
			File newFile = new File(System.getProperty("user.dir")+"/jsonFiles/jsonData.json");
			FileWriter fw = new FileWriter(newFile);
			
			//Class.forName("org.postgresql.Driver");
			Connection connection = DriverManager.getConnection(url,user,pass);
			
			if(connection != null)
				System.out.println("true");
			else
				System.out.println("false");
			
			Statement st = connection.createStatement();
			
			ResultSet rs = st.executeQuery("select * from bookdetails where location = 'Asia'");
			
			while(rs.next())
			{
				//System.out.println(rs.getInt(1)+" "+rs.getString(2)+" "+rs.getString(3));
				BookDetails bd = new BookDetails();
				bd.setBookname(rs.getString("bookname"));
				bd.setId(rs.getInt("id"));
				bd.setLocation(rs.getString("location"));
				
				listOfBooks.add(bd);
				
				//System.out.println(bd.getId()+" "+bd.getBookname()+" "+bd.getLocation());
			}
			
			/*ObjectMapper objMapper = new ObjectMapper();
			objMapper.writeValue(new File("jsonFiles/bookdetails.json"), bd);*/
			
			//Create JSON Object from Java Object
			
			for(int i=0; i<listOfBooks.size(); i++)
			{
				String jsonString = gson.toJson(listOfBooks.get(i));
				jArray.add(jsonString);
				jObject.put("data",jArray);
			}
			
			
			System.out.println("Before Escaping Characters: "+jObject.toJSONString());
			
			String formattedString = StringEscapeUtils.unescapeJava(jObject.toJSONString());
			System.out.println("Formatted String Is: "+formattedString);
			
			formattedString = formattedString.replace("\"{", "{");
			
			String finalString = formattedString.replace("}\"", "}");
			System.out.println("Final String Is:" +finalString);
			
			fw.write(finalString);
			
			fw.close();
				
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
    }
}
