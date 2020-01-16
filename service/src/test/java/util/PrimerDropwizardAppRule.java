package util;


import com.ab.example.metastore.service.Service;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.junit.DropwizardAppRule;
import com.ab.example.metastore.service.configuration.ApplicationEnvironment;
import com.ab.example.metastore.service.configuration.ServiceConfiguration;

import javax.annotation.Nullable;
import java.util.Properties;
import java.util.stream.Stream;

public class PrimerDropwizardAppRule extends DropwizardAppRule<ServiceConfiguration> {

    public static class ServiceTest extends Service {
        @Override
        public void initialize(Bootstrap<ServiceConfiguration> bootstrap) {
            super.initialize(bootstrap);
            bootstrap.setConfigurationSourceProvider(new ResourceConfigurationSourceProvider());
        }
    }

    public PrimerDropwizardAppRule(@Nullable String configPath, ConfigOverride... configOverrides) {
        super(ServiceTest.class, configPath, Stream.concat(
            Stream.of(
                ConfigOverride.config("server.applicationConnectors[0].port", "0"),
                ConfigOverride.config("server.adminConnectors[0].port", "0")),
            Stream.of(configOverrides)).toArray(ConfigOverride[]::new));
    }

    @Override
    protected void before() {
        //  Work around structure of ApplicationEnvironment using system properties,
        // Default it for tests before DW spins up
        final Properties properties = System.getProperties();
        if (properties.getProperty(ApplicationEnvironment.APPLICATION_NAME) == null) {
            properties.setProperty(ApplicationEnvironment.APPLICATION_NAME, "doppler-metastore-service");
        }
        if (properties.getProperty(ApplicationEnvironment.APPLICATION_HOME) == null) {
            properties.setProperty(ApplicationEnvironment.APPLICATION_HOME, ".");
        }
        if (properties.getProperty(ApplicationEnvironment.APPLICATION_ENVIRONMENT) == null) {
            properties.setProperty(ApplicationEnvironment.APPLICATION_ENVIRONMENT, "dev");
        }
        super.before();
    }
}
