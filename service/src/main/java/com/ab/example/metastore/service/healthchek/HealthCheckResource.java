package com.ab.example.metastore.service.healthchek;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.ab.example.metastore.service.exception.MetaStoreException;
import com.ab.example.metastore.service.util.Constants;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import org.apache.http.HttpStatus;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.SortedMap;

/**
 * @author _amal
 *
 * Http resource for performing health checks
 */
@Path("/")
@Api("HealthCheck")
public class HealthCheckResource {

    private final HealthCheckRegistry healthCheckRegistry;

    public HealthCheckResource(HealthCheckRegistry healthCheckRegistry) {
        this.healthCheckRegistry = healthCheckRegistry;
    }

    /**
     * Method performing all registered health checks and return 500 if any of the health checks fails.
     *
     * @return
     */
    @POST
    @UnitOfWork
    @Path(Constants.API_V1_VERSION + "/health")
    @Metered
    @ExceptionMetered(cause = MetaStoreException.class)
    public Response runHealthChecks() throws MetaStoreException {
        final SortedMap<String, HealthCheck.Result> results = healthCheckRegistry.runHealthChecks();
        final boolean unhealthy = results.values().stream().anyMatch(result -> !result.isHealthy());

        if(unhealthy)
        {
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "One of the health check has failed");
        }

        return Response.status(HttpStatus.SC_OK).build();
    }
}
