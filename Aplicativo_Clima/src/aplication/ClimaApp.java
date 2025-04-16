package aplication;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ClimaApp {
	public static JSONObject getClimaData(String locationName) throws ParseException {
		
		JSONArray locationData = getLocationData(locationName);
		
		return null;
	}
	
	private static JSONArray getLocationData(String locationName) throws ParseException {
		locationName = locationName.replaceAll("", "+");
		
		String urlString = "https://geocoding-api.open-meteo.com/v1/search?name="
				+locationName+"&count=10&language=en&format=json";
		
		try {
			HttpURLConnection conn  = fetchApiReponse(urlString);
			
			if (conn.getResponseCode() != 200) {
				System.out.println("Erro: Não foi possível conectar a API");
				return null;
			}else {
				StringBuilder resultJson = new StringBuilder();
				Scanner sc = new Scanner(conn.getInputStream());
				
				//Lê e guarda os resultados do StringBuilder
				while(sc.hasNext()) {
					resultJson.append(sc.nextLine());
				}
				sc.close();
				
				conn.disconnect();
				
				JSONParser parser = new JSONParser();
				JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));
				
				JSONArray locationData = (JSONArray) resultJsonObj.get("results");
				return locationData;
			}
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static HttpsURLConnection fetchApiReponse(String urlString) {
		try {
			
			URL url = new URL(urlString);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			
			
			conn.setRequestMethod("GET");
			conn.connect();
			return conn;
			
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
}


