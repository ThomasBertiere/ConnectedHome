package Luminosite;
import java.io.*;

import org.json.*;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class JSONParser {
	
	/* Important
	 * main.temp (Temperature en Celsius)
	 * main.humidity (Pourcentage d'humidité)
	 * wind.speed (Vitesse du vent en km/h)
	 * clouds.all (Pourcentage de nuages)
	 * rain.3h (Pluie les 3 dernières heures en mm)
	 */
	
	private static String data;
	private static String overall;
	private static double temperature;
	private static double humidity;
	private static double speed;
	private static double clouds;
	private static double rain;
	private static String location;
	
	@SuppressWarnings("static-access")
	public JSONParser(String address){
		this.setData("");
		this.setOverall("");
		this.setTemperature(0.0);
		this.setHumidity(0.0);
		this.setSpeed(0.0);
		this.setClouds(0.0);
		this.setRain(0.0);
		this.location=address;
	}
	
	@SuppressWarnings("static-access")
	public void parse() throws JSONException{
		JSONObject obj = new JSONObject(this.data);
		
		JSONObject main = obj.getJSONObject("main");
		this.setTemperature(main.getDouble("temp"));
		this.setHumidity(main.getDouble("humidity"));
		
		JSONObject wind = obj.getJSONObject("wind");
		this.setSpeed(wind.getDouble("speed")*3.6);
		
		JSONObject clouds = obj.getJSONObject("clouds");
		this.setClouds(clouds.getDouble("all"));
		
		if(!obj.isNull("rain")){
			JSONObject rain = obj.getJSONObject("rain");
			if(!rain.isNull("3h")){
				this.setRain(rain.getDouble("3h"));
			}
		}
		
		JSONObject overall = obj.getJSONArray("weather").getJSONObject(0);
		this.setOverall(overall.getString("description"));

	}

	public static String getData() {
		return data;
	}
	public static void setData(String data) {
		JSONParser.data = data;
	}
	public static String getOverall() {
		return overall;
	}

	public static void setOverall(String overall) {
		JSONParser.overall = overall;
	}

	public static double getTemperature() {
		return temperature;
	}
	public static void setTemperature(double temperature) {
		JSONParser.temperature = temperature;
	}
	public static double getHumidity() {
		return humidity;
	}
	public static void setHumidity(double humidity) {
		JSONParser.humidity = humidity;
	}
	public static double getSpeed() {
		return speed;
	}
	public static void setSpeed(double speed) {
		JSONParser.speed = speed;
	}
	public static double getClouds() {
		return clouds;
	}
	public static void setClouds(double clouds) {
		JSONParser.clouds = clouds;
	}
	public static double getRain() {
		return rain;
	}
	public static void setRain(double rain) {
		JSONParser.rain = rain;
	}
	
	@SuppressWarnings("static-access")
	public void askWeather(){
		YahooWeatherService yws=new YahooWeatherService(this.location);
		try {
			this.setData(yws.askWeather());
			this.parse();
			System.out.println(this.toString()) ; 
			//this.sayWeather();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void sayWeather(){
		Voice voice;
		
		VoiceManager voiceManager = VoiceManager.getInstance();
		//System.out.println(voiceManager.getVoices());
		voice = voiceManager.getVoice("kevin16");
		voice.setPitch(120);
		voice.setStyle("robotic");
		voice.setRate(130);
		voice.setDurationStretch((float) 1.25);
		voice.allocate();
		voice.speak(this.toString());

	}
	
	@Override
	public String toString(){
		return "Overall: "+getOverall()+"\nTemperature: "+getTemperature()+"°C\nHumidity: "+getHumidity()+"%\nWind speed: "+getSpeed()+"km/h\nClouds: "+getClouds()
		+"%\nRain past 3h: "+getRain()+"mm";
	}
	
}
