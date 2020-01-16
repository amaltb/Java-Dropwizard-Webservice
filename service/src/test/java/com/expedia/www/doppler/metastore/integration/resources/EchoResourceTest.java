package com.expedia.www.doppler.metastore.integration.resources;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.ClassRule;
import org.junit.Test;
import util.PrimerDropwizardAppRule;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test suite for echo resource.
 */
public class EchoResourceTest {

    final static Client CLIENT = new JerseyClientBuilder().build();

    @ClassRule
    public static final PrimerDropwizardAppRule RULE =
            new PrimerDropwizardAppRule("int_test.yml"
                    // example:  ConfigOverride.config("someConfigProperty", "someTestConfigValue")
        );

    @Test
    public void givenANameWhenEchoThenNameReturned() {
        final Response response = echo();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.readEntity(String.class)).isEqualTo("{\"name\":\"test\"}");
    }

    @Test
    public void whenEchoCalledMetricsIncrementedAndReported() throws IOException {
        echo();
        final JsonNode metrics = load(RULE.getAdminPort());
        assertThat(
                metrics.get("timers")
                        .get("com.expedia.www.doppler.metastore.service.resources.EchoResource.echo")
                        .get("count").asInt())
                .isGreaterThan(0);
    }

    private Response echo(){
        return CLIENT.target(
                String.format("http://localhost:%d/api/v1/echo/test", RULE.getLocalPort()))
                .request()
                .get();
    }

    private static JsonNode load(int port) throws IOException {
        final Client client = new JerseyClientBuilder().build();
        final String response = client.target("http://localhost:" + port + "/metrics")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get().readEntity(String.class);
        return new ObjectMapper().readTree(response);
    }
}
