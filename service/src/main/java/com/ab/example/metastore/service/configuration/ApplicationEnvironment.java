package com.ab.example.metastore.service.configuration;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import java.util.Properties;

public class ApplicationEnvironment {

    public static final String APPLICATION_NAME = "application.name";
    public static final String APPLICATION_ENVIRONMENT = "application.environment";
    public static final String APPLICATION_HOME = "application.home";
    public static final String APPLICATION_BUILD_VERSION = "application.build.version";
    public static final String APPLICATION_BUILD_TIME = "application.build.time";
    public static final String APPLICATION_BUILD_BRANCH = "application.build.branch";

    private final String applicationName;
    private final String applicationHome;
    private final String appEnvironment;
    private final AWSEnvironment awsEnvironment;
    private final String applicationVersion;
    private final String applicationBuildBranch;
    private final String applicationBuildTime;

    public ApplicationEnvironment() {
        this(System.getProperties());
    }

    public ApplicationEnvironment(Properties properties) {
        this(properties, new AWSEnvironment());
    }

    public ApplicationEnvironment(Properties properties, AWSEnvironment awsEnvironment) {
        Validate.notNull(properties);
        Validate.notNull(awsEnvironment);

        this.applicationName = properties.getProperty(APPLICATION_NAME);
        this.applicationHome = properties.getProperty(APPLICATION_HOME);
        this.appEnvironment = properties.getProperty(APPLICATION_ENVIRONMENT);

        Validate.isTrue(StringUtils.isNotBlank(this.applicationName),
                APPLICATION_NAME + " is a required property");
        Validate.isTrue(StringUtils.isNotBlank(this.applicationHome),
                APPLICATION_HOME + " is a required property");
        Validate.isTrue(StringUtils.isNotBlank(this.appEnvironment),
                APPLICATION_ENVIRONMENT + " is a required property");

        this.applicationVersion = checkAndSet(properties, APPLICATION_BUILD_VERSION, "not-available");
        this.applicationBuildBranch = checkAndSet(properties, APPLICATION_BUILD_BRANCH, "unknown");
        this.applicationBuildTime = checkAndSet(properties, APPLICATION_BUILD_TIME, "unknown");

        this.awsEnvironment = awsEnvironment;
    }

    private String checkAndSet(Properties properties, String propertyName, String defaultValue) {
        final String value = properties.getProperty(propertyName);
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        }
        return value;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getApplicationHome() {
        return applicationHome;
    }

    public String getApplicationEnvironment() {
        return appEnvironment;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public String getShortApplicationVersion() {
        if (applicationVersion.length() > 6) {
            return applicationVersion.substring(0, 6);
        }
        return applicationVersion;
    }

    public String getApplicationBuildBranch() {
        return applicationBuildBranch;
    }

    public String getApplicationBuildTime() {
        return applicationBuildTime;
    }

    public boolean isAWSEnvironment() {
        return awsEnvironment.isValid();
    }

    public AWSEnvironment getAwsEnvironment() {
        if (isAWSEnvironment()) {
            return awsEnvironment;
        }
        throw new NotAnAWSEnvironmentException();
    }

    public boolean isProduction() {
        return getApplicationEnvironment().equals("prod");
    }

    public boolean isDevelopment() {
        return getApplicationEnvironment().equals("dev");
    }

    public static class NotAnAWSEnvironmentException extends RuntimeException {
        public NotAnAWSEnvironmentException() {
            super("Application is not running in an AWS Environment. Please use isAWSEnvironment() before accessing cloud environment");
        }
    }
}