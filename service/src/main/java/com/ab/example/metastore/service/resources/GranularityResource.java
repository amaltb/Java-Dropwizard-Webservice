package com.ab.example.metastore.service.resources;

import com.expedia.www.doppler.metastore.commons.entities.Granularity;
import com.ab.example.metastore.service.dao.GranularityDao;
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
 * HTTP resource for granularity CRUD.
 *
 * paths: GET /api/v1/granularities
 *        GET /api/v1/granularity/{id}
 *        DELETE /api/v1/granularity/{id}/delete
 *        PUT /api/v1/granularity/{id}/update
 *        POST /api/v1/granularity/create
 */
@SuppressWarnings("PMD.PreserveStackTrace")
@Path("/")
@Api("Granularity")
public class GranularityResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(GranularityResource.class);
    private final GranularityDao granularityDao;

    public GranularityResource(GranularityDao granularityDao) {
        this.granularityDao = granularityDao;
    }

    /**
     * api to fetch all available granularities in meta-store.
     *
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch all available granularities in meta-store")
    @Path(Constants.API_V1_VERSION + "/granularities")
    public List<Granularity> getAllGranularities() throws MetaStoreException {
        try{
            return granularityDao.findAll();
        } catch (Exception e)
        {
            LOGGER.error("failed to fetch all granularities due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to fetch a particular granularity by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch a particular granularity by its id")
    @Path(Constants.API_V1_VERSION + "/granularity/{id}")
    public Granularity getGranularityById(@PathParam("id") final long id) throws MetaStoreException {
        try {
            return granularityDao.find(id);
        }catch (Exception e)
        {
            LOGGER.error("failed to fetch a granularity by id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to delete a particular granularity by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @DELETE
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Delete a particular granularity by its id")
    @Path(Constants.API_V1_VERSION + "/granularity/{id}/delete")
    public Response deleteGranularity(@PathParam("id") final long id) throws MetaStoreException {
        try {
            granularityDao.delete(id);
            return Response.status(HttpStatus.SC_OK).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to delete granularity with id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to update an existing granularity.
     *
     * @param id
     * @param granularity
     * @return
     * @throws MetaStoreException
     */
    @PUT
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Update an existing granularity")
    @Path(Constants.API_V1_VERSION + "/granularity/{id}/update")
    public Response updateGranularity(@PathParam("id") final long id,
                                          @Valid @NotNull final Granularity granularity)
            throws MetaStoreException {
        try {
            granularityDao.update(granularity);
            return Response.status(HttpStatus.SC_NO_CONTENT).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to update granularity with id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to create a new granularity.
     *
     * @param granularity
     * @return
     * @throws MetaStoreException
     */
    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create a new granularity")
    @Path(Constants.API_V1_VERSION + "/granularity/create")
    public Granularity createGranularity(@Valid @NotNull final Granularity granularity)
            throws MetaStoreException {
        try {
            return granularityDao.create(granularity);
        }catch (Exception e)
        {
            LOGGER.error("failed to create a granularity due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }
}
