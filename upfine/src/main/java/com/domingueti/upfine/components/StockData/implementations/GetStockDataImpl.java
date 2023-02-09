package com.domingueti.upfine.components.StockData.implementations;

import com.domingueti.upfine.components.StockData.dtos.StockIndicatorsDTO;
import com.domingueti.upfine.components.StockData.interfaces.GetStockData;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GetStockDataImpl implements GetStockData {

    private final String BASE_URL = "https://fintz.herokuapp.com/api/b3/acoes/";

    @Override
    public StockIndicatorsDTO execute(String ticker) {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + ticker + "/indicadores"))
                .build();

        try {

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();

            return gson.fromJson(response.body(), StockIndicatorsDTO.class);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error retrieving stock indicators", e);
        }
    }

}
