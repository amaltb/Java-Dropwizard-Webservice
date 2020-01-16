package com.ab.example.metastore.service.bundle;


import com.ab.example.metastore.service.configuration.ServiceConfiguration;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public class SwitchableSwaggerBundle extends SwaggerBundle<ServiceConfiguration> {

    @Override
    protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(ServiceConfiguration configuration) {
        return configuration.swaggerBundleConfiguration;
    }

    @Override
    public void run(ServiceConfiguration configuration, Environment environment) throws Exception {
        if (configuration.isUseSwagger()) {
            super.run(configuration, environment);
        }
    }
}
