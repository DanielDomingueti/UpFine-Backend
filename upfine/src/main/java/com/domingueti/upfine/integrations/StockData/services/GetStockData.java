package com.domingueti.upfine.integrations.StockData.services;

import com.domingueti.upfine.integrations.StockData.dtos.StockIndicatorsDTO;
import com.domingueti.upfine.modules.Config.services.GetConfigByNameService;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class GetStockData {

    private GetConfigByNameService getConfigByNameService;

    public StockIndicatorsDTO execute(String ticker) {
        final String FINTZ_API_BASE_URL = getConfigByNameService.execute("FINTZ-API-BASE-URL").getValue();
        final String FINTZ_API_SUFIX_URL = getConfigByNameService.execute("FINTZ-API-SUFIX-URL").getValue();

        final HttpClient client = HttpClient.newHttpClient();
        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(FINTZ_API_BASE_URL + ticker + FINTZ_API_SUFIX_URL))
                .build();

        try {

            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            final Gson gson = new Gson();

            return gson.fromJson(response.body(), StockIndicatorsDTO.class);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error retrieving stock indicators", e);
        }
    }

}
