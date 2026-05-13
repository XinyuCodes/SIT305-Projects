package com.example.llmchat;

import android.util.Log;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class GeminiClient {

    private static final String TAG = "GeminiClient";

    //this is my API key
    private static final String API_KEY = "AIzaSyAozXX_dwWgrQ2sgWdrhS33sAnIr0k-TuA";

    //updated to my previous assignment's URL to make sure this works
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient client;

    // Callback interface
    public interface GeminiCallback {
        void onResponse(String response);
        void onError(String error);
    }

    // Constructor
    public GeminiClient() {
        client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();;
    }

    // Sends a message to Gemini and returns the response via callback
    public void sendMessage(String message, GeminiCallback callback) {
        Log.d(TAG, "sendMessage: " + message);
        try {
            // Build the request body as JSON
            JSONObject jsonRequest = new JSONObject();
            JSONArray contentsArray = new JSONArray();
            JSONObject contentObject = new JSONObject();
            JSONArray partsArray = new JSONArray();
            JSONObject partObject = new JSONObject();

            partObject.put("text", message);
            partsArray.put(partObject);
            contentObject.put("parts", partsArray);
            contentsArray.put(contentObject);
            jsonRequest.put("contents", contentsArray);

            String requestJson = jsonRequest.toString();
            Log.d(TAG, "Request JSON: " + requestJson);

            RequestBody body = RequestBody.create(requestJson, JSON);

            // Build the HTTP request
            Request request = new Request.Builder()
                    .url(API_URL + API_KEY)
                    .post(body)
                    .build();

            // Make the async call
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "onFailure: " + e.getMessage(), e);
                    callback.onError("Network error: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try (Response res = response) {
                        String responseData = res.body() != null ? res.body().string() : null;
                        Log.d(TAG, "onResponse code: " + res.code());
                        Log.d(TAG, "onResponse body: " + responseData);

                        if (!res.isSuccessful()) {
                            callback.onError("Server error: " + res.code());
                            return;
                        }

                        if (responseData == null) {
                            callback.onError("Empty response body");
                            return;
                        }

                        JSONObject jsonResponse = new JSONObject(responseData);
                        JSONArray candidates = jsonResponse.optJSONArray("candidates");
                        if (candidates != null && candidates.length() > 0) {
                            JSONObject firstCandidate = candidates.getJSONObject(0);
                            JSONObject content = firstCandidate.optJSONObject("content");
                            if (content != null) {
                                JSONArray parts = content.optJSONArray("parts");
                                if (parts != null && parts.length() > 0) {
                                    String text = parts.getJSONObject(0).optString("text");
                                    callback.onResponse(text);
                                } else {
                                    callback.onError("No text parts found in response.");
                                }
                            } else {
                                callback.onError("No content found in candidate.");
                            }
                        } else {
                            callback.onError("No candidates found in response.");
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON parsing error", e);
                        callback.onError("JSON parsing error: " + e.getMessage());
                    }
                }
            });
        } catch (JSONException e) {
            Log.e(TAG, "Request creation error", e);
            callback.onError("Request creation error: " + e.getMessage());
        }
    }
}
