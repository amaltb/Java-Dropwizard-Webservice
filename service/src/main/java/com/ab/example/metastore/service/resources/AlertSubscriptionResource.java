package com.ab.example.metastore.service.resources;

import com.expedia.www.doppler.metastore.commons.entities.AlertSubscription;
import com.ab.example.metastore.service.dao.AlertSubscriptionDao;
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

/**
 * @author - _amal
 *
 * HTTP resource for alert-subscription CRUD.
 *
 * paths: GET /api/v1/alert-subscriptions
 *        GET /api/v1/alert-subscription/{id}
 *        DELETE /api/v1/alert-subscription/{id}/delete
 *        PUT /api/v1/alert-subscription/{id}/update
 *        POST /api/v1/alert-subscription/create
 */
@SuppressWarnings("PMD.PreserveStackTrace")
@Path("/")
@Api("AlertSubscription")
public class AlertSubscriptionResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlertSubscriptionResource.class);

    private final AlertSubscriptionDao alertSubscriptionDao;

    public AlertSubscriptionResource(AlertSubscriptionDao alertSubscriptionDao) {
        this.alertSubscriptionDao = alertSubscriptionDao;
    }

    /**
     * api to fetch a particular alert subscription by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch a particular alert subscription by its id")
    @Path(Constants.API_V1_VERSION + "/alert-subscription/{id}")
    public AlertSubscription getAlertSubscriptionById(@PathParam("id") final long id) throws MetaStoreException {
        try {
            return alertSubscriptionDao.find(id);
        }catch (Exception e)
        {
            LOGGER.error("failed to fetch an alert-subscription for id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to delete a particular alert subscription by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @DELETE
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Delete a particular alert subscription by its id")
    @Path(Constants.API_V1_VERSION + "/alert-subscription/{id}/delete")
    public Response deleteAlertSubscription(@PathParam("id") final long id) throws MetaStoreException {
        try {
            alertSubscriptionDao.delete(id);
            return Response.status(HttpStatus.SC_OK).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to delete alert-subscription for id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to update an existing alert subscription.
     *
     * @param id
     * @param alertSubscription
     * @return
     * @throws MetaStoreException
     */
    @PUT
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Update an existing alert subscription")
    @Path(Constants.API_V1_VERSION + "/alert-subscription/{id}/update")
    public Response updateAlertSubscription(@PathParam("id") final long id,
                                        @Valid @NotNull final AlertSubscription alertSubscription)
            throws MetaStoreException {
        try {
            alertSubscriptionDao.update(alertSubscription);
            return Response.status(HttpStatus.SC_NO_CONTENT).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to update alert-subscription for id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to create a new alert subscription.
     *
     * @param alertSubscription
     * @return
     * @throws MetaStoreException
     */
    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create a new alert subscription")
    @Path(Constants.API_V1_VERSION + "/alert-subscription/create")
    public AlertSubscription createAlertSubscription(@Valid @NotNull final AlertSubscription alertSubscription)
            throws MetaStoreException {
        try {
            return alertSubscriptionDao.create(alertSubscription);
        }catch (Exception e)
        {
            LOGGER.error("failed to create alert-subscription due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }
}
