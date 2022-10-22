package pl.sky0x.app.data.implementation.http;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.LinkedList;
import java.util.UUID;

import pl.sky0x.app.data.Click;
import pl.sky0x.app.data.DataSystem;

public class HTTPDataSystem implements DataSystem {

    private static final String URL = "http://127.0.0.1:3000/clicks";

    @Override
    public Collection<Click> getClicks() {
        try {
            URL url = new URL(URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(1000);

            BufferedReader response = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder responseContext = new StringBuilder();

            String line;
            while ((line = response.readLine()) != null) {
                responseContext.append(line);
            }

            response.close();

            JsonArray array = new Gson().fromJson(responseContext.toString(), JsonArray.class);
            Collection<Click> clicks = new LinkedList<>();

            for (JsonElement element : array) {
                clicks.add(getClickFromJson(element.getAsJsonObject()));
            }

            return clicks;
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean addClick(Click click) {
        try {
            URL url = new URL(URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setConnectTimeout(3000);
            connection.setReadTimeout(1000);
            connection.setDoOutput(true);

            //doesnt working, why? idk
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
            connection.setRequestProperty("Accept","*/*");
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(getJsonFromClick(click).toString());
            out.close();
            System.out.println(connection.getErrorStream());

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    private JsonObject getJsonFromClick(Click click) {
        JsonObject clickJson = new JsonObject();
        clickJson.addProperty("id", click.getUuid().toString());
        clickJson.addProperty("time", click.getTime());
        clickJson.addProperty("ip", click.getIp());

        return clickJson;
    }

    private Click getClickFromJson(JsonObject clickJson) {
        return new Click(
                UUID.fromString(clickJson.get("id").getAsString()),
                clickJson.get("time").getAsLong(),
                clickJson.get("ip").getAsString());
    }
}
