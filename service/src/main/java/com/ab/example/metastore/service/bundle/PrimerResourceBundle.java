package com.ab.example.metastore.service.bundle;


import com.ab.example.metastore.service.configuration.ServiceConfiguration;
import com.expedia.www.platform.isactive.providers.ActiveVersionProvider;
import com.expedia.www.platform.isactive.providers.FileBasedActiveVersionProvider;
import com.expedia.www.platform.isactive.resources.BuildInfoResource;
import com.expedia.www.platform.isactive.resources.IsActiveResource;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.StringWriter;

public class PrimerResourceBundle implements ConfiguredBundle<ServiceConfiguration> {

    final ActiveVersionProvider activeVersionProvider = new FileBasedActiveVersionProvider();

    @Override
    public void run(ServiceConfiguration serviceConfiguration, Environment environment) throws Exception {
        final String buildInfoJson;
        final ClassLoader classLoader = PrimerResourceBundle.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream("buildInfo.json")) {
            final StringWriter writer = new StringWriter();
            IOUtils.copy(inputStream, writer, "UTF-8");
            buildInfoJson = writer.toString();
        }
        final BuildInfoResource buildInfoResource = new BuildInfoResource(buildInfoJson);
        environment.jersey().register(buildInfoResource);
        environment.jersey().register(new IsActiveResource(buildInfoResource, activeVersionProvider));
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        //NOPMD
    }
}
