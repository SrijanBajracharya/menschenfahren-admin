package com.achiever.menschenfahren.admin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@ConfigurationProperties(prefix = BaseConfig.PREFIX)
@Data
@NoArgsConstructor
public class BaseConfig implements ConfigValidation {

    /**Prefix for the base config.**/
    public static final String PREFIX = "menschen";

    @Override
    public void validate() {
        // nothing here.
    }

}
