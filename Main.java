import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) throws IOException {

        String api_key = System.getenv("OPENWEATHER_KEY");

        URL url = new URL("https://api.openweathermap.org/data/2.5/weather?lat=48.44&lon=8.69&appid=" + api_key+ "&units=metric");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        //schauen, ob verbindung geht
        System.out.println(connection.getResponseCode());

        //printed den Inhalt als JSON aus
        Scanner scanner = new Scanner(connection.getInputStream());
        while(scanner.hasNext()){
            System.out.println(scanner.nextLine());
        }
        scanner.close();


    }
}