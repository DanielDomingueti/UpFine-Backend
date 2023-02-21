package com.domingueti.upfine.utils.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DownloadFileToByteArray {

    @Autowired
    private RestTemplate restTemplate;

    public DownloadFileToByteArray(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public byte[] execute(String url) {

        return restTemplate.getForObject(url, byte[].class);

    }
}
