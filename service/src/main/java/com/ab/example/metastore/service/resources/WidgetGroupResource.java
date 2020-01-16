package com.ab.example.metastore.service.resources;

import com.expedia.www.doppler.metastore.commons.entities.WidgetGroup;
import com.ab.example.metastore.service.dao.WidgetGroupDao;
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
 * HTTP resource for user CRUD.
 *
 * paths: GET /api/v1/widget-groups
 *        GET /api/v1/widget-group/{id}
 *        DELETE /api/v1/widget-group/{id}/delete
 *        PUT /api/v1/widget-group/{id}/update
 *        POST /api/v1/widget-group/create
 */
@SuppressWarnings("PMD.PreserveStackTrace")
@Path("/")
@Api("WidgetGroup")
public class WidgetGroupResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(WidgetGroupResource.class);
    private final WidgetGroupDao widgetGroupDao;

    public WidgetGroupResource(WidgetGroupDao widgetGroupDao) {
        this.widgetGroupDao = widgetGroupDao;
    }


    /**
     * api to fetch a particular widget-group by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch a particular widget-group by its id")
    @Path(Constants.API_V1_VERSION + "/widget-group/{id}")
    public WidgetGroup getWidgetGroupById(@PathParam("id") final long id) throws MetaStoreException {
        try {
            return widgetGroupDao.find(id);
        }catch (Exception e)
        {
            LOGGER.error("failed to fetch an widget-group by id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to delete a particular widget-group by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @DELETE
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Delete a particular widget-group by its id")
    @Path(Constants.API_V1_VERSION + "/widget-group/{id}/delete")
    public Response deleteWidgetGroup(@PathParam("id") final long id) throws MetaStoreException {
        try {
            widgetGroupDao.delete(id);
            return Response.status(HttpStatus.SC_OK).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to delete widget-group for id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to update an existing widget-group.
     *
     * @param id
     * @param widgetGroup
     * @return
     * @throws MetaStoreException
     */
    @PUT
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Update an existing widget-group")
    @Path(Constants.API_V1_VERSION + "/widget-group/{id}/update")
    public Response updateWidgetGroup(@PathParam("id") final long id,
                                 @Valid @NotNull final WidgetGroup widgetGroup)
            throws MetaStoreException {
        try {
            widgetGroupDao.update(widgetGroup);
            return Response.status(HttpStatus.SC_NO_CONTENT).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to update subscription-channel for id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to create a new widget-group.
     *
     * @param widgetGroup
     * @return
     * @throws MetaStoreException
     */
    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create a new widget-group")
    @Path(Constants.API_V1_VERSION + "/widget-group/create")
    public WidgetGroup createSubscriptionChannel(@Valid @NotNull final WidgetGroup widgetGroup)
            throws MetaStoreException {
        try {
            return widgetGroupDao.create(widgetGroup);
        }catch (Exception e)
        {
            LOGGER.error("failed to create widget due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }
}
