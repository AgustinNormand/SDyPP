package com.Ejercicio_4.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.rmi.Remote;
import java.rmi.RemoteException;

import com.maxmind.geoip2.WebServiceClient;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;


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
		try (WebServiceClient client = new WebServiceClient.Builder(539078, "5FVqmB9eWrRnJviN").host("geolite.info").build()) {

		    InetAddress ipAddress = InetAddress.getByName(getPublicIP());

		    // Do the lookup
		    CityResponse response = client.city(ipAddress);
		    return response.getMostSpecificSubdivision().getName();
		    } catch (Exception e) {
		    	e.printStackTrace();
		    }
		return null;
	}
	
  @Override
  public String getWeather() throws RemoteException {
		try {
			return "The weather in "+getLocationOfIp()+" is Cloudy."; // Está la localización, falta el wheather.
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Error";
  }
}
