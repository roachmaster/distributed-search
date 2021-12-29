package com.leonardo.rocha.distributedsearch.networking;

import com.leonardo.rocha.distributedsearch.model.Result;
import com.leonardo.rocha.distributedsearch.model.SerializationUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.concurrent.CompletableFuture;

@Component
public class WebClient {
    org.springframework.web.reactive.function.client.WebClient webClient;

    public WebClient() {
        webClient = org.springframework.web.reactive.function.client.WebClient.create();
    }

    public CompletableFuture<Result> sendTask(String url, byte[] requestPayload) {
        return CompletableFuture.supplyAsync(() -> getTask(url,requestPayload));
    }

    private Result getTask(String url, byte[] requestPayload){
        byte[] response = webClient.post()
                .uri(URI.create(url))
                .bodyValue(requestPayload)
                .retrieve().bodyToMono(ByteArrayResource.class)
                .map(ByteArrayResource::getByteArray)
                .block();
        return (Result) SerializationUtils.deserialize(response);
    }
}
