package com.ab.example.metastore.service.configuration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.db.DataSourceFactory;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class ServiceConfiguration extends Configuration {

    @JsonProperty
    private boolean useSwagger = true;

    @JsonIgnore
    private final ApplicationEnvironment appEnvironment;

    @Valid
    @NotNull
    @JsonProperty("httpClientConfiguration")
    private final JerseyClientConfiguration httpClientConfiguration = new JerseyClientConfiguration();


    @JsonProperty("swagger")
    public SwaggerBundleConfiguration swaggerBundleConfiguration;

    @JsonProperty("searchServiceURI")
    public String searchServiceURI;

    @Valid
    @NotNull
    @JsonProperty("database")
    private final DataSourceFactory dataSourceFactory = new DataSourceFactory();

    public ServiceConfiguration() {
        appEnvironment = new ApplicationEnvironment();
    }


    public boolean isUseSwagger(){
        return useSwagger;
    }

    public void setUseSwagger(boolean useSwagger) {
        this.useSwagger = useSwagger;
    }
}
