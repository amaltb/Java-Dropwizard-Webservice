package com.ab.example.metastore.service.resources;

import com.expedia.www.doppler.metastore.commons.entities.Metric;
import com.ab.example.metastore.service.dao.MetricDao;
import com.ab.example.metastore.service.exception.MetaStoreException;
import com.ab.example.metastore.service.util.Constants;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author - _amal
 *
 * HTTP resource for metric CRUD.
 *
 * paths: GET /api/v1/metrics
 *        GET /api/v1/metric/{id}
 *        DELETE /api/v1/metric/{id}/delete
 *        PUT /api/v1/metric/{id}/update
 *        POST /api/v1/metric/create
 */
@SuppressWarnings("PMD.PreserveStackTrace")
@Path("/")
@Api("Metric")
public class MetricResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(MetricResource.class);
    private final MetricDao metricDao;

    public MetricResource(MetricDao metricDao) {
        this.metricDao = metricDao;
    }

    /**
     * api to fetch all available metrics in meta-store.
     *
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch all available metrics in meta-store")
    @Path(Constants.API_V1_VERSION + "/metrics")
    public List<Metric> getAllMetrics() throws MetaStoreException {
        try{
            return metricDao.findAll();
        } catch (Exception e)
        {
            LOGGER.error("failed to fetch all metrics due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to fetch a particular metric by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch a particular metric by its id")
    @Path(Constants.API_V1_VERSION + "/metric/{id}")
    public Metric getMetricById(@PathParam("id") final long id) throws MetaStoreException {
        try {
            return metricDao.find(id);
        }catch (Exception e)
        {
            LOGGER.error("failed to fetch a metric by id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to delete a particular metric by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @DELETE
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Delete a particular metric by its id")
    @Path(Constants.API_V1_VERSION + "/metric/{id}/delete")
    public Response deleteMetric(@PathParam("id") final long id) throws MetaStoreException {
        try {
            metricDao.delete(id);
            return Response.status(HttpStatus.SC_OK).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to delete metric with id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to update an existing metric.
     *
     * @param id
     * @param metric
     * @return
     * @throws MetaStoreException
     */
    @PUT
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Update an existing metric")
    @Path(Constants.API_V1_VERSION + "/metric/{id}/update")
    public Response updateMetric(@PathParam("id") final long id,
                                      @Valid @NotNull final Metric metric)
            throws MetaStoreException {
        try {
            metricDao.update(metric);
            return Response.status(HttpStatus.SC_NO_CONTENT).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to update metric with id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to create a new metric.
     *
     * @param metric
     * @return
     * @throws MetaStoreException
     */
    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create a new metric")
    @Path(Constants.API_V1_VERSION + "/metric/create")
    public Metric createMetric(@Valid @NotNull final Metric metric)
            throws MetaStoreException {
        try {
            return metricDao.create(metric);
        }catch (Exception e)
        {
            LOGGER.error("failed to create a metric due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }
}
