package aplication;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ClimaApp {
	
	// Retorna os dados climáticos da cidade informada
	public static JSONObject getClimaData(String locationName) {
		
		// Busca as coordenadas da cidade através da API de geolocalização
		JSONArray locationData = getLocationData(locationName);

		
		// Obtém latitude e longitude do primeiro resultado retornado
		
		JSONObject location = (JSONObject) locationData.get(0);
		double latitude = (double) location.get("latitude");
		double longitude = (double) location.get("longitude");
		
		// Monta a URL para consulta da previsão do tempo usando as coordenadas
		String urlString = "https://api.open-meteo.com/v1/forecast?" + "latitude=" + latitude + "&longitude="
				+ longitude
				+ "&hourly=temperature_2m,relativehumidity_2m,weathercode,windspeed_10m&timezone=America%2FSao_Paulo";
		
		try {
			// Faz a requisição à API
			HttpURLConnection conn = fetchApiReponse(urlString);
			
			if(conn.getResponseCode()!= 200) {
				System.out.println("Erro: Não foi possível conectar a API");
				return null;
			}
			
			// Lê a resposta da API
			StringBuilder resultJson = new StringBuilder();
			Scanner sc = new Scanner(conn.getInputStream());
			
			while(sc.hasNext()) {
				resultJson.append(sc.nextLine());
			}
			
			sc.close();
			conn.disconnect();

			// Converte a resposta JSON
			JSONParser parser = new JSONParser();
			JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));
			
			JSONObject hora = (JSONObject) resultJsonObj.get("hourly");
			
			JSONArray time = (JSONArray) hora.get("time");
			
			// Encontra o índice da hora atual na lista de horários da API
			int index = findIndexHoraAtual(time);
			
			// Obtém os dados correspondentes ao horário atual
			JSONArray temperaturaData = (JSONArray) hora.get("temperature_2m");
			double temperatura = (double) temperaturaData.get(index);
			
			
			JSONArray climaCode = (JSONArray) hora.get("weathercode");
			String climaCondition = convertClimaCode((long) climaCode.get(index));
			
			
			JSONArray umidadeRelativa = (JSONArray) hora.get("relativehumidity_2m");
			long umidade = (long) umidadeRelativa.get(index);
			
			
			JSONArray ventoData = (JSONArray) hora.get("windspeed_10m");
			double ventoVelocidade = (double) ventoData.get(index);
			
			// Cria um objeto JSON contendo os dados climáticos finais
			JSONObject climaData = new JSONObject();
			climaData.put("temperature", temperatura);
			climaData.put("weather_condition", climaCondition);
			climaData.put("humidity", umidade);
			climaData.put("windspeed", ventoVelocidade);
			
			return climaData;
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;

	}
	
	// Consulta as coordenadas a partir do nome da cidade
	public static JSONArray getLocationData(String locationName) {
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
				
				// Lê a resposta da API
				while(sc.hasNext()) {
					resultJson.append(sc.nextLine());
				}
				sc.close();
				
				conn.disconnect();
				
				// Converte a resposta JSON e retorna os resultados
				JSONParser parser = new JSONParser();
				JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));
				
				JSONArray locationData = (JSONArray) resultJsonObj.get("results");
				return locationData;
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// Realiza uma requisição GET para a URL fornecida e retorna a conexão
	private static HttpURLConnection fetchApiReponse(String urlString) {
		try {
			
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			
			conn.setRequestMethod("GET");
			conn.connect();
			return conn;
			
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	// Retorna o índice correspondente à hora atual na lista de horários da API
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
	
	// Retorna a hora atual no formato utilizado pela API
	private static String getHoraAtual() {
		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");
		String horarioFormatado = currentDateTime.format(dtf);
		
		return horarioFormatado;
		
		
	}
	
	// Converte o código numérico do clima em uma descrição textual
	private static String convertClimaCode(long climacode) {
		String climaCondition = "";
		if(climacode == 0L) {
			climaCondition = "Limpo";
		}else if(climacode > 0L && climacode <= 3L) {
			climaCondition = "Nublado";
		}else if((climacode >=51L && climacode <= 67L || (climacode >= 80L && climacode <= 99L))) {
			climaCondition = "Chovendo";
		}else if(climacode >= 71L && climacode <= 77L) {
			climaCondition = "Nevando";
		}
		
		return climaCondition;
	}
}
