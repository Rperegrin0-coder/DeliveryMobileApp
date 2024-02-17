package com.example.sustainablemobileapp.APIs;

import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RandomGenerator {

    private static final String TAG = "RandomGenerator"; // TAG for logging

    public interface RandomNumberCallback {
        void onNumberGenerated(String randomNumber);
    }

    public void getRandomNumber(final RandomNumberCallback callback) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                // Correct API URL
                String apiUrl = "https://www.random.org/integers/?num=1&min=1&max=1000&col=1&base=10&format=plain&rnd=new";
                String jsonResponse = "";

                try {
                    URL url = new URL(apiUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");

                    // Replace "YOUR_API_KEY" with your actual API key
                    String requestBody = "{ \"jsonrpc\": \"2.0\", \"method\": \"generateIntegers\", \"params\": { \"apiKey\": \"bfe54739-dde8-4495-a01e-b57ab656194d \", \"n\": 1, \"min\": 1, \"max\": 100 }, \"id\": 1 }";
                    connection.setDoOutput(true);
                    connection.getOutputStream().write(requestBody.getBytes("UTF-8"));

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        jsonResponse = response.toString();
                        // Log the response for debugging
                        Log.d(TAG, "API Response: " + jsonResponse);

                    } else {
                        jsonResponse = "Error: Unable to retrieve data. Response Code: " + responseCode;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    jsonResponse = "Error: " + e.getMessage();
                }
                return jsonResponse;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                // Invoke the callback with the result
                if (callback != null) {
                    callback.onNumberGenerated(result);
                }
            }
        }.execute();
    }
}