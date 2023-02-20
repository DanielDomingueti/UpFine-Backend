package com.domingueti.upfine.components.GPT3.implementations;

import java.io.IOException;
import java.util.ArrayList;

import lombok.NoArgsConstructor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class GptClientImpl {

    private final OkHttpClient client = new OkHttpClient();

    private final String endpoint = "https://api.openai.com";

//    @Value("%{gpt-api-key}")
    private final String apiKey = "apiKey";

    public String summarizeText(String text, int maxSummaryLength) throws IOException {
        String prompt = "Please summarize the following text:\n\n" + text + "\n\nSummary:";
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, buildRequestBody(prompt, maxSummaryLength));
        Request request = new Request.Builder()
                .url(endpoint + "/v1/engine/ada/completions")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        JSONObject json = new JSONObject(response.body().string());
        JSONArray choices = json.getJSONArray("choices");
        if (choices.length() == 0) {
            throw new IOException("Failed to summarize text using GPT-3 API");
        }

        return choices.getJSONObject(0).getString("text").replace(prompt, "").trim();
    }

    private String buildRequestBody(String prompt, int maxTokens) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("prompt", prompt);
        requestBody.put("max_tokens", maxTokens);
        requestBody.put("temperature", 0.7);
        requestBody.put("n", 1);
        requestBody.put("stop", "");
        requestBody.put("echo", true);
        requestBody.put("examples", new ArrayList<>());

        JSONArray presencePenalty = new JSONArray();
        presencePenalty.put(0);
        requestBody.put("presence_penalty", presencePenalty);

        JSONArray frequencyPenalty = new JSONArray();
        frequencyPenalty.put(0);
        requestBody.put("frequency_penalty", frequencyPenalty);

        return requestBody.toString();
    }
}
