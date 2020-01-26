package com.expedia.www.doppler.metastore.service.resources;

import com.expedia.www.doppler.metastore.commons.entities.AlertInstance;
import com.expedia.www.doppler.metastore.service.dao.AlertInstanceDao;
import com.expedia.www.doppler.metastore.service.exception.MetaStoreException;
import com.expedia.www.doppler.metastore.service.util.Constants;
import com.expedia.www.doppler.metastore.service.util.ResourceUtil;
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

/**
 * @author - _amal
 *
 * HTTP resource for alert-instance CRUD.
 *
 * paths: GET /api/v1/alerts
 *        GET /api/v1/alert/{id}
 *        DELETE /api/v1/alert/{id}/delete
 *        PUT /api/v1/alert/{id}/update
 *        POST /api/v1/alert/create
 */
@SuppressWarnings("PMD.PreserveStackTrace")
@Path("/")
@Api("AlertInstance")
public class AlertInstanceResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlertInstanceResource.class);

    private final AlertInstanceDao alertInstanceDao;

    public AlertInstanceResource(AlertInstanceDao alertInstanceDao) {
        this.alertInstanceDao = alertInstanceDao;
    }

    /**
     * api to fetch a particular alert instance by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch a particular alert instance by its id")
    @Path(Constants.API_V1_VERSION + "/alert/{id}")
    public AlertInstance getAlertById(@PathParam("id") final long id) throws MetaStoreException {
        try {
            return alertInstanceDao.find(id);
        }catch (Exception e)
        {
            LOGGER.error("failed to fetch an alert for id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to delete a particular alert instance by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @DELETE
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Delete a particular alert instance by its id")
    @Path(Constants.API_V1_VERSION + "/alert/{id}")
    public Response deleteAlert(@PathParam("id") final long id) throws MetaStoreException {
        try {
            alertInstanceDao.delete(id);
            return Response.status(HttpStatus.SC_OK).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to delete alert instance for id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to update an existing alert instance.
     *
     * @param id
     * @param alertInstance
     * @return
     * @throws MetaStoreException
     */
    @PUT
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Update an existing alert instance")
    @Path(Constants.API_V1_VERSION + "/alert/{id}")
    public Response updateAlert(@PathParam("id") final long id,
                                        @Valid @NotNull final AlertInstance alertInstance)
            throws MetaStoreException {
        try {
            final AlertInstance curInstance = alertInstanceDao.find(id);
            ResourceUtil.updateEntityParams(curInstance, alertInstance, AlertInstance.class);
            alertInstanceDao.update(curInstance);
            return Response.status(HttpStatus.SC_NO_CONTENT).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to update alert instance for id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to create a new alert instance.
     *
     * @param alertInstance
     * @return
     * @throws MetaStoreException
     */
    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create a new alert instance")
    @Path(Constants.API_V1_VERSION + "/alert")
    public AlertInstance createAlert(@Valid @NotNull final AlertInstance alertInstance)
            throws MetaStoreException {
        try {
            return alertInstanceDao.create(alertInstance);
        }catch (Exception e)
        {
            LOGGER.error("failed to create alert instance due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

}
