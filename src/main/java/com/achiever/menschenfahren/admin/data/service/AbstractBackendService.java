package com.achiever.menschenfahren.admin.data.service;

import org.springframework.web.reactive.function.client.WebClient;

import com.achiever.menschenfahren.admin.config.MenschenFahrenConfig;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractBackendService {

    @NonNull
    protected final WebClient webClient;

    public AbstractBackendService(@NonNull final MenschenFahrenConfig appConfig) {
        webClient = WebClient.builder().baseUrl(appConfig.getAchieverService().calculateFullUserUrl()).build();

    }

    protected void handleException(final Throwable e) {
        log.warn("Could not get the data from the service({}). Error: {}.", this.getClass().getSimpleName(), e.getMessage());
        log.trace("Full error", e);
    }
}
