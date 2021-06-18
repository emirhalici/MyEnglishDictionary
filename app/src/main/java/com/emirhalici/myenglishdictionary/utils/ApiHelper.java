package com.emirhalici.myenglishdictionary.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class ApiHelper {
    private static String getResponseFromHttpUrl(URL url) throws IOException {

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.addRequestProperty("Authorization","Token 0079c3f37f84c622aaf92b8ea65159aacf23a7ed");

        try {
            InputStream in = connection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            connection.disconnect();
        }
    }


    public static JSONObject searchWord(String word) {
        URL url = null;
        JSONObject json = null;
        JSONArray definitions;
        try {
            url = new URL("https://owlbot.info/api/v4/dictionary/"+word);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            String response = getResponseFromHttpUrl(url);
            json = new JSONObject(response);
            //definitions = json.getJSONArray("definitions");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

}
