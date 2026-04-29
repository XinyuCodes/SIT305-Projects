package com.example.llm_project;

import android.util.Log;
import okhttp3.*;
import org.json.*;

import java.io.IOException;

public class QuizGenerator {
    private static final String URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=";
    private static final OkHttpClient client = new OkHttpClient();
    private static String apiKey = 'XXXXX';

    public interface QuestionCallback {
        void onQuestion(String question);
        void onError(String error);
    }

    public QuizGenerator(String apiKey) {
        this.apiKey = apiKey;
    }

    public static void generateQuestion(String topic, QuestionCallback callback) {
        String prompt = "Generate a single multiple choice sports quiz question about "
                + topic
                + ". Keep it short. Format it exactly like this:\\nQuestion: ...\\nA) ...\\nB) ...\\nC) ...\\nD) ...\\nAnswer: ...";
        generate(prompt, callback);
    }

    public static void generateHint(String question, QuestionCallback callback) {
        String prompt = "Give me a short one sentence hint for this question: " + question;
        generate(prompt, callback);
    }

    public static void generateExplanation(String question, String selected, String correct, QuestionCallback callback) {
        String prompt = "The question was: " + question +
                " The user answered " + selected + " but the correct answer is " + correct +
                " Explain in one sentence why the correct answer is right.";
        generate(prompt, callback);
    }

    //generating a summary
    public static void generateSummary(String topic, int score, String quizHistory, QuestionCallback callback){
        String prompt = "A student just completed a quiz on " + topic + " and scored " + score + "/3"
        + "here is a summary of their quiz:\n" + quizHistory
        + "\nWrite a short 2-3 sentence personalised summary of their performance based on what they got right and wrong\n"
        +"\nand provide a summary about the quiz that was just done as well"        ;
        generate(prompt, callback);
    }
    private static void generate(String prompt, QuestionCallback callback) {
        try {
            JSONObject jsonBody = new JSONObject();
            JSONArray contents = new JSONArray();
            JSONObject content = new JSONObject();
            JSONArray parts = new JSONArray();
            JSONObject part = new JSONObject();
            part.put("text", prompt);
            parts.put(part);
            content.put("parts", parts);
            contents.put(content);
            jsonBody.put("contents", contents);

            RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.get("application/json"));

            Request request = new Request.Builder()
                    .url(URL + apiKey)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callback.onError(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseBody = response.body().string();
                    if (!response.isSuccessful()) {
                        callback.onError("API Error: " + response.code() + " " + responseBody);
                        return;
                    }

                    try {
                        JSONObject json = new JSONObject(responseBody);
                        if (json.has("candidates") && json.getJSONArray("candidates").length() > 0) {
                            JSONObject firstCandidate = json.getJSONArray("candidates").getJSONObject(0);
                            if (firstCandidate.has("content")) {
                                String text = firstCandidate
                                        .getJSONObject("content")
                                        .getJSONArray("parts")
                                        .getJSONObject(0)
                                        .getString("text");
                                callback.onQuestion(text);
                            } else {
                                String finishReason = firstCandidate.optString("finishReason");
                                callback.onError("No content in candidate. Finish reason: " + finishReason + ". Response: " + responseBody);
                            }
                        } else {
                            callback.onError("No candidates returned. Response: " + responseBody);
                        }
                    } catch (JSONException e) {
                        callback.onError("Parsing error: " + e.getMessage() + ". Response: " + responseBody);
                    }
                }
            });
        } catch (JSONException e) {
            callback.onError("JSON construction error: " + e.getMessage());
        }
    }
}