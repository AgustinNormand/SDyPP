package com.Ejercicio_5.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.rmi.Remote;
import java.rmi.RemoteException;

import org.json.JSONObject;

import com.maxmind.geoip2.WebServiceClient;
import com.maxmind.geoip2.model.CityResponse;


public class ServerRunner implements RemoteInt, Remote{
	
	public static String getHTML(String urlToRead) throws Exception {
	      StringBuilder result = new StringBuilder();
	      URL url = new URL(urlToRead);
	      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	      conn.setRequestMethod("GET");
	      try (BufferedReader reader = new BufferedReader(
	                  new InputStreamReader(conn.getInputStream()))) {
	          for (String line; (line = reader.readLine()) != null; ) {
	              result.append(line);
	          }
	      }
	      return result.toString();
	   }
	
	public String getPublicIP() {
		try {
			return getHTML("https://ifconfig.me");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getLocationOfIp() {
		try {
			WebServiceClient client = 
					new WebServiceClient
					.Builder(539078, "5FVqmB9eWrRnJviN")
					.host("geolite.info")
					.build();

		    InetAddress ipAddress = InetAddress.getByName(getPublicIP());

		    CityResponse response = client.city(ipAddress);
		    
		    return response.getLocation().getLatitude() + "," + response.getLocation().getLongitude();
		    } catch (Exception e) {
		    	e.printStackTrace();
		    }
		return null;
	}
	
  @Override
  public String getWeather() throws RemoteException {
		try {
			//return "The weather in "+getLocationOfIp()+" is Cloudy."; // Está la localización, falta el wheather.
			String LatitudeLongitude = getLocationOfIp();
			return getWeatherOfLatitudeLongitude(LatitudeLongitude);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Error";
  }

private String getWeatherOfLatitudeLongitude(String latitudeLongitude) {
	String key = "AGRN2JTPDLE77BYSCQK4R6A7B";
	String url = "https://weather.visualcrossing.com"
			+ "/VisualCrossingWebServices/rest/"
			+ "services/timeline/"
			+latitudeLongitude
			+"?unitGroup=us&key="
			+key;
	
	try {
		JSONObject jo = new JSONObject(getHTML(url));
		String locationName = (String) jo.get("timezone");
		String weather = (String) jo.get("description");
		return "Weather in "+locationName+": "+weather;
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
}
}
