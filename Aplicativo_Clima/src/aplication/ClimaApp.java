package aplication;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ClimaApp {
	
	//Buscar a localização
	public static JSONObject getClimaData(String locationName) throws ParseException {
		
		JSONArray locationData = getLocationData(locationName);
		
		if (locationData != null && !locationData.isEmpty()) {
		    JSONObject firstResult = (JSONObject) locationData.get(0);
		    return firstResult;
		}
		
		//Latitude e Longitude
		
		JSONObject location = (JSONObject) locationData.get(0);
		double latitude = (double) location.get("latitude");
		double longitude = (double) location.get("longitude");
		
		//Pegando dados da API através das coordenadas
		String urlString = "https://api.open-meteo.com/v1/forecast?"
				+ "latitude="+ latitude +"&longitude="+ longitude
				+ "&hourly=temperature_2m,weather_code,wind_speed_10m,relative_humidity_2m&timezone=America%2FSao_Paulo";
		
		try {
			HttpURLConnection conn = fetchApiReponse(urlString);
			
			if(conn.getResponseCode()!= 200) {
				System.out.println("Erro: Não foi possível conectar a API");
				return null;
			}
			
			StringBuilder resultJson = new StringBuilder();
			Scanner sc = new Scanner(conn.getInputStream());
			
			while(sc.hasNext()) {
				resultJson.append(sc.nextLine());
			}
			
			sc.close();
			conn.disconnect();
			
			JSONParser parser = new JSONParser();
			JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));
			
			JSONObject hora = (JSONObject) resultJsonObj.get("hourly");
			
			JSONArray time = (JSONArray) hora.get("time");
			
			int index = findIndexHoraAtual(time);
			
			//Pegar a temperatura
			JSONArray temperaturaData = (JSONArray) hora.get("temperature_2m");
			double temperatura = (double) temperaturaData.get(index);
			
			//Pegar as Condições Climáticas
			JSONArray climaCode = (JSONArray) hora.get("weathercode");
			String climaCondition = convertClimaCode((long) climaCode.get(index));
			
			//Pegar a Umidade
			JSONArray umidadeRelativa = (JSONArray) hora.get("relativehumidity_2m");
			long umidade = (long) umidadeRelativa.get(index);
			
			//Velocidade do Vento
			JSONArray ventoData = (JSONArray) hora.get("windspeed_10m");
			double ventoVelocidade = (double) ventoData.get(index);
			
			JSONObject climaData = new JSONObject();
			climaData.put("Temperatura", temperatura);
			climaData.put("Condição_Climática", climaCondition);
			climaData.put("Umidade", umidade);
			climaData.put("Velocidade_do_Vento", ventoVelocidade);
			
			return climaData;
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		//Caso não consiga retornar a localização
		return null;

	}
	
	//Buscar a cidade
	public static JSONArray getLocationData(String locationName) throws ParseException {
		locationName = locationName.replaceAll(" ", "+");
		
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
	
	//
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
	//Encontrando a hora atual
	private static int findIndexHoraAtual(JSONArray timeList) {
		String horaAtual = getHoraAtual();
		
		for(int i=0;i<timeList.size();i++) {
			String time = (String) timeList.get(i);
			if(time.equalsIgnoreCase(horaAtual)) {
				return i;
			}
		}
		
		return 0;
		
	}
	
	//Pegar o horário atual
	private static String getHoraAtual() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");
		
		LocalDateTime currentDateTime = LocalDateTime.now();
		
		String horarioFormatado = currentDateTime.format(dtf);
		
		return horarioFormatado;
		
		
	}
	
	//Condições do Clima
	private static String convertClimaCode(long climacode) {
		String climaCondition = "";
		if(climacode == 0L) {
			climaCondition = "Limpo";
		}else if(climacode <= 3L && climacode > 0L) {
			climaCondition = "Nublado";
		}else if((climacode >=51L && climacode <= 67L || (climacode >= 80L && climacode <= 99L))) {
			climaCondition = "Chovendo";
		}else if(climacode >= 71L && climacode <= 77L) {
			climaCondition = "Nevando";
		}
		
		return climaCondition;
	}
}
