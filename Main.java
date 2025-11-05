import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Zur Erinnerung: Man fragt einen Server mit HTTPUrlConnection an (und dem API key),
 * bekommt als Antwort einen JSON-String. Mit dem JSONParser wandelt man ihn in ein JSON-Object,
 * mit diesem Objekt werden die Daten in nutzbare Schlüssel/Wert paare gespeichert (mit .get("key"))
 *
 */

public class Main{
    public static void main(String[] args) throws IOException, ParseException {

        String api_key = System.getenv("OPENWEATHER_KEY");
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter city name: ");
        String userInput = sc.nextLine();
        userInput = userInput.replace(" ", "+");

        sc.close();
        //ERSTMAL DIE GEOCODING API************************************************
        String q = URLEncoder.encode(userInput, StandardCharsets.UTF_8);
        URL url2 = new URL("https://geocoding-api.open-meteo.com/v1/search?name=" + q + "&count=1&language=en&format=json");
        HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
        connection2.setRequestMethod("GET");
        connection2.setRequestProperty("User-Agent", "Mozilla/5.0");


        String koordinaten = "";
        Scanner scanner = new Scanner(connection2.getInputStream());
        while(scanner.hasNext()){
            koordinaten += scanner.nextLine();
        }

        JSONParser jsonParser2 = new JSONParser();
        //Konvertiert JSON zu JSONObject (Schlüssel/Wert paare)
        JSONObject json2 = (JSONObject)  jsonParser2.parse(koordinaten);
        JSONArray results = (JSONArray) json2.get("results");
        JSONObject firstResult = (JSONObject)  results.get(0);

        double latitude = ((Number) firstResult.get("latitude")).doubleValue();
        double longitude = ((Number) firstResult.get("longitude")).doubleValue();

        scanner.close();

// GEOCODING API ENDE*************************************************************************

        URL url = new URL("https://api.openweathermap.org/data/2.5/weather?lat="+ latitude + "&lon=" + longitude + "&appid=" + api_key+ "&units=metric");


        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        //schauen, ob verbindung geht
        System.out.println(connection.getResponseCode());

        String location = "";

        //Speichert den Inhalt in "location"
        Scanner scanner3 = new Scanner(connection.getInputStream());
        while(scanner3.hasNext()){
            location += scanner3.nextLine();
            // System.out.println(scanner.nextLine());
        }
        scanner3.close();


        JSONParser jsonParser = new JSONParser();
        //Konvertiert JSON zu JSONObject (Schlüssel/Wert paare)
        JSONObject json = (JSONObject)  jsonParser.parse(location);
        //"main" ist ein Unter-Objekt des JSON strings -> hier ist die Temperatur etc gespeichert
        JSONObject main = (JSONObject) json.get("main");


        // Werte rausziehen:
        String city = json.get("name").toString();
        double temp = ((Number) main.get("temp")).doubleValue();
        double humidity = ((Number) main.get("humidity")).doubleValue();

        // Ausgabe
        System.out.println("City: " + city);
        System.out.println("Temp: " + temp + " °C");
        System.out.println("Humidity: " + humidity + " %");


    }
}