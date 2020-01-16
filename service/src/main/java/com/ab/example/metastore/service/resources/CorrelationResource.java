package com.ab.example.metastore.service.resources;

import com.expedia.www.doppler.metastore.commons.entities.Correlation;
import com.ab.example.metastore.service.dao.CorrelationDao;
import com.ab.example.metastore.service.exception.MetaStoreException;
import com.expedia.www.doppler.metastore.commons.list_entities.CorrelationLight;
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
 * HTTP resource for correlation CRUD.
 *
 * paths: GET /api/v1/correlations
 *        GET /api/v1/correlation/{id}
 *        DELETE /api/v1/correlation/{id}/delete
 *        PUT /api/v1/correlation/{id}/update
 *        POST /api/v1/correlation/create
 */
@SuppressWarnings("PMD.PreserveStackTrace")
@Path("/")
@Api("CorrelationType")
public class CorrelationResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(CorrelationResource.class);
    private final CorrelationDao correlationDao;

    public CorrelationResource(CorrelationDao correlationDao) {
        this.correlationDao = correlationDao;
    }

    /**
     * api to fetch all available correlation-types in meta-store. Returning a list of trimmed correlations by
     * minimizing foreign key reference object size (returning ids instead of whole object) to prevent any network
     * throttling.
     *
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch all available correlation-types in meta-store")
    @Path(Constants.API_V1_VERSION + "/correlations")
    public List<CorrelationLight> getAllCorrelationTypes() throws MetaStoreException {
        try{
            return correlationDao.findAll();
        } catch (Exception e)
        {
            LOGGER.error("failed to fetch all correlation-types due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to fetch a particular correlation-type by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch a particular correlation-type by its id")
    @Path(Constants.API_V1_VERSION + "/correlation/{id}")
    public Correlation getCorrelationTypeById(@PathParam("id") final long id) throws MetaStoreException {
        try {
            return correlationDao.find(id);
        }catch (Exception e)
        {
            LOGGER.error("failed to fetch a correlation-type by id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to delete a particular correlation-type by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @DELETE
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Delete a particular correlation-type by its id")
    @Path(Constants.API_V1_VERSION + "/correlation/{id}/delete")
    public Response deleteCorrelationType(@PathParam("id") final long id) throws MetaStoreException {
        try {
            correlationDao.delete(id);
            return Response.status(HttpStatus.SC_OK).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to delete correlation-type with id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to update an existing correlation-type.
     *
     * @param id
     * @param correlation
     * @return
     * @throws MetaStoreException
     */
    @PUT
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Update an existing correlation-type")
    @Path(Constants.API_V1_VERSION + "/correlation/{id}/update")
    public Response updateCorrelationType(@PathParam("id") final long id,
                                             @Valid @NotNull final Correlation correlation)
            throws MetaStoreException {
        try {
            correlationDao.update(correlation);
            return Response.status(HttpStatus.SC_NO_CONTENT).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to update correlation-type with id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to create a new correlation-type.
     *
     * @param correlation
     * @return
     * @throws MetaStoreException
     */
    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create a new correlation-type")
    @Path(Constants.API_V1_VERSION + "/correlation/create")
    public Correlation createCorrelationType(@Valid @NotNull final Correlation correlation)
            throws MetaStoreException {
        try {
            return correlationDao.create(correlation);
        }catch (Exception e)
        {
            LOGGER.error("failed to create a correlation-type due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }
}
