package com.ab.example.metastore.service.configuration;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import java.util.Map;

public class AWSEnvironment {
    public static final String AWS_REGION = "AWS_REGION";
    public static final String AWS_DNS_ZONE = "EXPEDIA_DNS_ZONE";

    private final Map<String, String> properties;

    public AWSEnvironment() {
        this(System.getenv());
    }

    public AWSEnvironment(final Map<String, String> envProperties) {
        Validate.notNull(envProperties);
        this.properties = envProperties;
    }

    public String getAwsRegion() {
        return properties.get(AWS_REGION);
    }

    public String getAwsDnsZone() {
        return properties.get(AWS_DNS_ZONE);
    }

    public boolean isValid() {
        return StringUtils.isNotEmpty(getAwsRegion()) &&
                StringUtils.isNotEmpty(getAwsDnsZone());
    }

    public String buildApplicationFQDN(final String appName) {
        check();
        Validate.notEmpty(appName);
        return String.format("%s.%s.%s", appName, getAwsRegion(),
                getAwsDnsZone());
    }

    private void check() {
        if (!isValid()) {
            throw new IllegalStateException("This is not a valid AWS environment");
        }
    }
}