package com.domingueti.upfine.integrations.GPT3;

import com.domingueti.upfine.modules.Config.services.GetConfigByNameService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;

@Component
@NoArgsConstructor
@AllArgsConstructor
public class GptClient {

    private GetConfigByNameService getConfigByNameService;

    public String summarizeText(String text, int maxSummaryLength) throws IOException {
        final OkHttpClient client = new OkHttpClient();

        final String GPT_API_URL = getConfigByNameService.execute("GPT-API-URL").getValue();
        final String GPT_API_KEY = getConfigByNameService.execute("GPT-API-KEY").getValue();
        final String FINAL_PROMPT = getConfigByNameService.execute("GPT-API-PROMPT").getValue() + text;
        final String MEDIA_TYPE_STR = getConfigByNameService.execute("REQUEST-MEDIA-TYPE").getValue();
        final MediaType MEDIA_TYPE = MediaType.parse(MEDIA_TYPE_STR);

        final RequestBody BODY = RequestBody.create(MEDIA_TYPE, buildRequestBody(FINAL_PROMPT, maxSummaryLength));

        final Request request = new Request.Builder()
                .url(GPT_API_URL)
                .post(BODY)
                .addHeader("Content-Type", MEDIA_TYPE_STR)
                .addHeader("Authorization", GPT_API_KEY)
                .build();

        final Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        final JSONObject json = new JSONObject(response.body().string());
        final JSONArray choices = json.getJSONArray("choices");
        if (choices.length() == 0) {
            throw new IOException("Failed to summarize text using GPT-3 API");
        }

        return choices.getJSONObject(0).getString("text").replace(FINAL_PROMPT, "").trim();
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
