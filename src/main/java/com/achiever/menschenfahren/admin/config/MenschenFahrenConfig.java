package com.achiever.menschenfahren.admin.config;

import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MenschenFahrenConfig extends BaseConfig {

    /** Configuration for achiever service. **/
    private AchieverServiceConfig      achieverService;
    /** End point config for user service. **/
    private UserServiceEndpointConfig  userServiceEndpoints;
    /** End point config for event service. **/
    private EventServiceEndpointConfig eventServiceEndpoints;

    @Override
    public void validate() {
        super.validate();
        Objects.requireNonNull(this.achieverService, "User service config must be provided.");
    }

    @Data
    public static class UserServiceEndpointConfig {

        /** The endpoint for the users **/
        private String users;
        /** The endpoint for the user. **/
        private String user;
    }

    @Data
    public static class EventServiceEndpointConfig {
        /** The endpoint for the events **/
        private String events;
        /** The endpoint for the event **/
        private String event;
    }

}