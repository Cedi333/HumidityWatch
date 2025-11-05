import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
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

        URL url = new URL("https://api.openweathermap.org/data/2.5/weather?lat=48.78&lon=9.177&appid=" + api_key+ "&units=metric");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        //schauen, ob verbindung geht
        System.out.println(connection.getResponseCode());

        String location = "";

        //Speichert den Inhalt in "location"
        Scanner scanner = new Scanner(connection.getInputStream());
        while(scanner.hasNext()){
            location += scanner.nextLine();
           // System.out.println(scanner.nextLine());
        }
        scanner.close();


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
