package Luminosite;
import java.io.*;
import java.net.*;
import java.util.Scanner;

import org.json.*;

public class YahooWeatherService {

	private static String result;
	private static String addr; 
	private static double longitude;
	private static double latitude;

	@SuppressWarnings("static-access")
	public YahooWeatherService(String _addr){
		this.setResult("");
		this.setAddr(_addr);
		this.setLongitude(0.0);
		this.setLatitude(0.0);
	}

	@SuppressWarnings("static-access")
	public String askWeather() throws IOException, JSONException{
		try {
			String s = "http://maps.google.com/maps/api/geocode/json?" + "sensor=false&address=";
			s += URLEncoder.encode(this.addr, "UTF-8");
			URL url = new URL(s);

			Scanner scan = new Scanner(url.openStream());
			String str = new String();
			while (scan.hasNext())
				str += scan.nextLine();
			scan.close();

			JSONObject obj = new JSONObject(str);
			//Si la requête s'est bien passé, on a l'attribut status qui passe à OK dans la réponse JSON
			if (! obj.getString("status").equals("OK")){
				return "";
			}

			//getting longitude and latitude of given address
			JSONObject res = obj.getJSONArray("results").getJSONObject(0);
			this.setAddr((res.getString("formatted_address")));
			JSONObject loc = res.getJSONObject("geometry").getJSONObject("location");
			this.setLatitude(loc.getDouble("lat"));
			this.setLongitude(loc.getDouble("lng"));

			String meteo_url ="http://api.openweathermap.org/data/2.5/forecast?lat="+this.getLatitude()+"&lon="+this.getLongitude()+"&units=metric&APPID=54976f408a942655f0062c0e546c6312";

			URL url_weather = new URL(meteo_url) ;
			//Id de Toulouse : 2972315
			//Donne la météo toutes les 3h à partir de minuit pendant 5 jours (en comptant aujourd'hui)

			HttpURLConnection conn =  (HttpURLConnection) url_weather.openConnection() ;
			conn.setRequestMethod("GET");

			if(conn.getResponseCode()!=200) { // ON REGARDE SI LA REQUETE EST BIEN PASSÉ
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream())) ; 
			String output=""; 
			while((output = br.readLine()) != null) {
				result+=output;
			}
			conn.disconnect();

			// get the first result corresponding to weather closest to current time
			JSONObject object = new JSONObject(this.result);
			JSONObject result_json = object.getJSONArray("list").getJSONObject(0);
			this.result=result_json.toString();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.result;

	}


	public static String getResult() {
		return result;
	}

	public static void setResult(String result) {
		YahooWeatherService.result = result;
	}

	public static String getAddr() {
		return addr;
	}

	public static void setAddr(String addr) {
		YahooWeatherService.addr = addr;
	}

	public static double getLongitude() {
		return longitude;
	}

	public static void setLongitude(double longitude) {
		YahooWeatherService.longitude = longitude;
	}

	public static double getLatitude() {
		return latitude;
	}

	public static void setLatitude(double latitude) {
		YahooWeatherService.latitude = latitude;
	}
}
