package com.achiever.menschenfahren.admin.config;

import java.util.Objects;

import lombok.Data;

/**
 * Configuration for the base url of the application.
 * 
 * @author Srijan Bajracharya
 *
 */
@Data
public class AchieverServiceConfig implements ConfigValidation {

    /** Base URL for the service. **/
    private String baseUrl;
    /** Base URl for the api **/
    private String api;

    @Override
    public void validate() {
        Objects.requireNonNull(this.baseUrl, "The base URL must be set.");
    }

    public final String calculateFullUserUrl() {
        return baseUrl + api;
    }

}
