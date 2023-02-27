package com.domingueti.upfine.utils.statics;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.AllArgsConstructor;

import java.io.IOException;

@AllArgsConstructor
public class GetOAuth2AccessToken {

    private String clientId;
    private String clientSecret;
    private String refreshToken;

    public String getAccessToken() throws IOException {
        // Create a new instance of the GoogleNetHttpTransport
        HttpTransport transport = new NetHttpTransport();

        // Create a new instance of the JacksonFactory JSON parser
        JsonFactory jsonFactory = new JacksonFactory();

        // Create a new GoogleCredential object with the client ID, client secret, and refresh token
        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(transport)
                .setJsonFactory(jsonFactory)
                .setClientSecrets(clientId, clientSecret)
                .build()
                .setRefreshToken(refreshToken);

        // Use the credential to refresh the access token
        credential.refreshToken();

        // Return the access token
        return credential.getAccessToken();
    }

}
