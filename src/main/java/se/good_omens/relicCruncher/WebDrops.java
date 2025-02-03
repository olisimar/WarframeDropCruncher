package se.good_omens.relicCruncher;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;
import java.net.*;

public class WebDrops {

    private final URI uri;

    public WebDrops(URI uri) {
        this.uri = uri;
    }

    public WebDrops(String uri) {
        this.uri = URI.create(uri);
    }

    public String getDropTableData() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(this.uri).GET().build();

        HttpResponse<String> response = null;
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Status Code: "+ response.statusCode());

        return response.body().toString();
    }
    @SuppressWarnings("deprecation")
	public String getDropTableData(String urlToRead) throws IOException {
        URL url;
        HttpURLConnection conn; // The actual connection to the web page
        BufferedReader rd; // Used to read results from the web page
        String line; // An individual line of the web page HTML
        //String result = ""; // A long string containing all the HTML
        StringBuilder contentBuilder = new StringBuilder();
            url = new URL(urlToRead);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = rd.readLine()) != null) {
               contentBuilder.append(line).append("\n");
            }
            rd.close();

        return contentBuilder.toString();
    }

    public String writeToFile(String data) {
        Path path = Paths.get( "drops.html" );
        try {
//            Files.writeString(path, data.toString());
            Files.writeString(path, data.toString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("IO Exception: "+ e.getMessage());
        }
        return "drops.html";
    }
}
