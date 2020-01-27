package com.expedia.www.doppler.metastore.service.resources;

import com.expedia.www.doppler.metastore.commons.entities.ContextToContextMap;
import com.expedia.www.doppler.metastore.service.dao.ContextToContextMapDao;
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
 * HTTP resource for context map CRUD. A context map represents how a context is related to another context.
 *
 * paths: GET /api/v1/context-correlations
 *        GET /api/v1/context-correlation/{id}
 *        DELETE /api/v1/context-correlation/{id}/delete
 *        PUT /api/v1/context-correlation/{id}/update
 *        POST /api/v1/context-correlation/create
 */
@SuppressWarnings("PMD.PreserveStackTrace")
@Path("/")
@Api("ContextCorrelation")
public class ContextCorrelationResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContextCorrelationResource.class);
    private final ContextToContextMapDao contextToContextMapDao;

    public ContextCorrelationResource(ContextToContextMapDao contextToContextMapDao) {
        this.contextToContextMapDao = contextToContextMapDao;
    }

    /**
     * api to fetch a particular context correlation by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch a particular context correlation by its id")
    @Path(Constants.API_V1_VERSION + "/context-correlation/{id}")
    public ContextToContextMap getContextCorrelationById(@PathParam("id") final long id) throws MetaStoreException {
        try {
            return contextToContextMapDao.find(id);
        }catch (Exception e)
        {
            LOGGER.error("failed to fetch a context correlation by id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to delete a particular context correlation by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @DELETE
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Delete a particular context correlation by its id")
    @Path(Constants.API_V1_VERSION + "/context-correlation/{id}/delete")
    public Response deleteContextCorrelation(@PathParam("id") final long id) throws MetaStoreException {
        try {
            contextToContextMapDao.delete(id);
            return Response.status(HttpStatus.SC_OK).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to delete context correlation with id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to update an existing context correlation.
     *
     * @param id
     * @param contextMap
     * @return
     * @throws MetaStoreException
     */
    @PUT
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Update an existing context correlation")
    @Path(Constants.API_V1_VERSION + "/context-correlation/{id}/update")
    public Response updateContextCorrelation(@PathParam("id") final long id,
                                          @Valid @NotNull final ContextToContextMap contextMap)
            throws MetaStoreException {
        try {
            final ContextToContextMap curContextMap = contextToContextMapDao.find(id);
            ResourceUtil.updateEntityParams(curContextMap, contextMap, ContextToContextMap.class);
            contextToContextMapDao.update(curContextMap);
            return Response.status(HttpStatus.SC_NO_CONTENT).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to update context-correlation with id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to create a new context correlation.
     *
     * @param contextMap
     * @return
     * @throws MetaStoreException
     */
    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create a new context correlation")
    @Path(Constants.API_V1_VERSION + "/context-correlation/create")
    public ContextToContextMap createContextCorrelation(@Valid @NotNull final ContextToContextMap contextMap)
            throws MetaStoreException {
        try {
            return contextToContextMapDao.create(contextMap);
        }catch (Exception e)
        {
            LOGGER.error("failed to create a context-correlation due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }
}
