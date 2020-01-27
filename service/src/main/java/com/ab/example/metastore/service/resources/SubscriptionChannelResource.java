package com.expedia.www.doppler.metastore.service.resources;

import com.expedia.www.doppler.metastore.commons.entities.SubscriptionChannel;
import com.expedia.www.doppler.metastore.service.dao.SubscriptionChannelDao;
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
 * HTTP resource for subscription-channel CRUD.
 *
 * paths: GET /api/v1/subscription-channels
 *        GET /api/v1/subscription-channel/{id}
 *        DELETE /api/v1/subscription-channel/{id}/delete
 *        PUT /api/v1/subscription-channel/{id}/update
 *        POST /api/v1/subscription-channel/create
 */
@SuppressWarnings("PMD.PreserveStackTrace")
@Path("/")
@Api("SubscriptionChannel")
public class SubscriptionChannelResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionChannelResource.class);

    private final SubscriptionChannelDao subscriptionChannelDao;

    public SubscriptionChannelResource(SubscriptionChannelDao subscriptionChannelDao) {
        this.subscriptionChannelDao = subscriptionChannelDao;
    }

    /**
     * api to fetch a particular subscription channel by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch a particular subscription channel by its id")
    @Path(Constants.API_V1_VERSION + "/subscription-channel/{id}")
    public SubscriptionChannel getAlertSubscriptionChannelById(@PathParam("id") final long id) throws MetaStoreException {
        try {
            return subscriptionChannelDao.find(id);
        }catch (Exception e)
        {
            LOGGER.error("failed to fetch an alert-subscription by id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to delete a particular subscription channel by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @DELETE
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Delete a particular subscription channel by its id")
    @Path(Constants.API_V1_VERSION + "/subscription-channel/{id}/delete")
    public Response deleteSubscriptionChannel(@PathParam("id") final long id) throws MetaStoreException {
        try {
            subscriptionChannelDao.delete(id);
            return Response.status(HttpStatus.SC_OK).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to delete subscription-channel for id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to update an existing subscription channel.
     *
     * @param id
     * @param subscriptionChannel
     * @return
     * @throws MetaStoreException
     */
    @PUT
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Update an existing subscription channel")
    @Path(Constants.API_V1_VERSION + "/subscription-channel/{id}/update")
    public Response updateSubscriptionChannel(@PathParam("id") final long id,
                                        @Valid @NotNull final SubscriptionChannel subscriptionChannel)
            throws MetaStoreException {
        try {
            final SubscriptionChannel curSubscriptionChannel = subscriptionChannelDao.find(id);
            ResourceUtil.updateEntityParams(curSubscriptionChannel, subscriptionChannel, SubscriptionChannel.class);
            subscriptionChannelDao.update(curSubscriptionChannel);
            return Response.status(HttpStatus.SC_NO_CONTENT).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to update subscription-channel for id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to create a new subscription channel.
     *
     * @param subscriptionChannel
     * @return
     * @throws MetaStoreException
     */
    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create a new subscription channel")
    @Path(Constants.API_V1_VERSION + "/subscription-channel/create")
    public SubscriptionChannel createSubscriptionChannel(@Valid @NotNull final SubscriptionChannel subscriptionChannel)
            throws MetaStoreException {
        try {
            return subscriptionChannelDao.create(subscriptionChannel);
        }catch (Exception e)
        {
            LOGGER.error("failed to create subscription-channel due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }
}
