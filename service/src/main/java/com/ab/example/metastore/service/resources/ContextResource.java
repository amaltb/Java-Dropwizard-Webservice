package com.expedia.www.doppler.metastore.service.resources;

import com.expedia.www.doppler.metastore.commons.entities.Context;
import com.expedia.www.doppler.metastore.commons.list_entities.ContextLight;
import com.expedia.www.doppler.metastore.service.dao.ContextDao;
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
import java.util.List;

/**
 * @author - _amal
 *
 * HTTP resource for metric-context CRUD.
 *
 * paths: GET /api/v1/contexts
 *        GET /api/v1/context/{id}
 *        DELETE /api/v1/context/{id}/delete
 *        PUT /api/v1/context/{id}/update
 *        POST /api/v1/context/create
 */
@SuppressWarnings("PMD.PreserveStackTrace")
@Path("/")
@Api("Context")
public class ContextResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContextResource.class);
    private final ContextDao contextDao;

    public ContextResource(ContextDao contextDao) {
        this.contextDao = contextDao;
    }

    /**
     * api to fetch all available contexts in meta-store.
     *
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch all available contexts in meta-store")
    @Path(Constants.API_V1_VERSION + "/contexts")
    public List<ContextLight> getAllMetricContexts() throws MetaStoreException {
        try{
            return contextDao.findAll();
        } catch (Exception e)
        {
            LOGGER.error("failed to fetch all contexts due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to fetch a particular context by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch a particular context by its id")
    @Path(Constants.API_V1_VERSION + "/context/{id}")
    public Context getMetricContextById(@PathParam("id") final long id) throws MetaStoreException {
        try {
            return contextDao.find(id);
        }catch (Exception e)
        {
            LOGGER.error("failed to fetch a context by id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to delete a particular context by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @DELETE
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Delete a particular context by its id")
    @Path(Constants.API_V1_VERSION + "/context/{id}/delete")
    public Response deleteMetricContext(@PathParam("id") final long id) throws MetaStoreException {
        try {
            contextDao.delete(id);
            return Response.status(HttpStatus.SC_OK).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to delete context with id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to update an existing context.
     *
     * @param id
     * @param metricContext
     * @return
     * @throws MetaStoreException
     */
    @PUT
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Update an existing context")
    @Path(Constants.API_V1_VERSION + "/context/{id}/update")
    public Response updateMetricContext(@PathParam("id") final long id,
                                 @Valid @NotNull final Context metricContext)
            throws MetaStoreException {
        try {
            final Context curContext = contextDao.find(id);
            ResourceUtil.updateEntityParams(curContext, metricContext, Context.class);
            contextDao.update(curContext);
            return Response.status(HttpStatus.SC_NO_CONTENT).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to update context with id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to create a new context.
     *
     * @param metricContext
     * @return
     * @throws MetaStoreException
     */
    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create a new context")
    @Path(Constants.API_V1_VERSION + "/context/create")
    public Context createMetricContext(@Valid @NotNull final Context metricContext)
            throws MetaStoreException {
        try {
            return contextDao.create(metricContext);
        }catch (Exception e)
        {
            LOGGER.error("failed to create a context due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }
}
