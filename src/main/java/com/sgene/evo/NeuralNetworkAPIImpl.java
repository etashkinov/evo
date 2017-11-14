/*
 * Copyright (c) 2017 EMC Corporation All Rights Reserved
 */

package com.sgene.evo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@Component
public class NeuralNetworkAPIImpl implements NeuralNetworkAPI {

    private final static Logger LOGGER = LoggerFactory.getLogger(NeuralNetworkAPIImpl.class);

    private static class ErrorHandler implements ResponseErrorHandler {

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            LOGGER.warn("Neural network responded with: " + response.getStatusCode());
        }

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            return !response.getStatusCode().is2xxSuccessful();
        }
    }

    private final RestTemplate restTemplate;

    @Autowired
    public NeuralNetworkAPIImpl(RestTemplateBuilder restTemplateBuilder, @Value("${neural.network.root.uri}") String neuralNetworkRootURI) {
        this.restTemplate = restTemplateBuilder.rootUri(neuralNetworkRootURI).build();
        this.restTemplate.setErrorHandler(new ErrorHandler());
        this.restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }

    @Override
    public Map<String, Double> predict(double[][] image) {
        ParameterizedTypeReference<Map<String, Double>> type = new ParameterizedTypeReference<Map<String, Double>>(){};
        return this.restTemplate.exchange("/predict/array", HttpMethod.POST, new HttpEntity<>(image),type).getBody();
    }
}
