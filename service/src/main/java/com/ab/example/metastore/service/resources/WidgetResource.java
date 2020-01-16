package com.ab.example.metastore.service.resources;

import com.expedia.www.doppler.metastore.commons.entities.Widget;
import com.ab.example.metastore.service.dao.WidgetDao;
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
 * paths: GET /api/v1/widgets
 *        GET /api/v1/widget/{id}
 *        DELETE /api/v1/widget/{id}/delete
 *        PUT /api/v1/widget/{id}/update
 *        POST /api/v1/widget/create
 */
@SuppressWarnings("PMD.PreserveStackTrace")
@Path("/")
@Api("Widget")
public class WidgetResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(WidgetResource.class);
    private final WidgetDao widgetDao;

    public WidgetResource(WidgetDao widgetDao) {
        this.widgetDao = widgetDao;
    }


    /**
     * api to fetch a particular widget by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch a particular widget by its id")
    @Path(Constants.API_V1_VERSION + "/widget/{id}")
    public Widget getWidgetById(@PathParam("id") final long id) throws MetaStoreException {
        try {
            return widgetDao.find(id);
        }catch (Exception e)
        {
            LOGGER.error("failed to fetch an widget by id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to delete a particular widget by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @DELETE
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Delete a particular widget by its id")
    @Path(Constants.API_V1_VERSION + "/widget/{id}/delete")
    public Response deleteWidget(@PathParam("id") final long id) throws MetaStoreException {
        try {
            widgetDao.delete(id);
            return Response.status(HttpStatus.SC_OK).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to delete widget for id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to update an existing widget.
     *
     * @param id
     * @param widget
     * @return
     * @throws MetaStoreException
     */
    @PUT
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Update an existing widget")
    @Path(Constants.API_V1_VERSION + "/widget/{id}/update")
    public Response updateWidget(@PathParam("id") final long id,
                                              @Valid @NotNull final Widget widget)
            throws MetaStoreException {
        try {
            widgetDao.update(widget);
            return Response.status(HttpStatus.SC_NO_CONTENT).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to update subscription-channel for id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to create a new widget.
     *
     * @param widget
     * @return
     * @throws MetaStoreException
     */
    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create a new widget")
    @Path(Constants.API_V1_VERSION + "/widget/create")
    public Widget createSubscriptionChannel(@Valid @NotNull final Widget widget)
            throws MetaStoreException {
        try {
            return widgetDao.create(widget);
        }catch (Exception e)
        {
            LOGGER.error("failed to create widget due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }
}
