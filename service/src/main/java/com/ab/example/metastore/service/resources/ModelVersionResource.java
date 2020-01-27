package com.expedia.www.doppler.metastore.service.resources;

import com.expedia.www.doppler.metastore.commons.entities.ModelVersion;
import com.expedia.www.doppler.metastore.service.dao.ModelVersionDao;
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
 * HTTP resource for model-version CRUD.
 *
 * paths: GET /api/v1/model-versions
 *        GET /api/v1/model-version/{id}
 *        DELETE /api/v1/model-version/{id}/delete
 *        PUT /api/v1/model-version/{id}/update
 *        POST /api/v1/model-version/create
 */
@SuppressWarnings("PMD.PreserveStackTrace")
@Path("/")
@Api("ModelVersion")
public class ModelVersionResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModelVersionResource.class);
    private final ModelVersionDao modelVersionDao;

    public ModelVersionResource(ModelVersionDao modelVersionDao) {
        this.modelVersionDao = modelVersionDao;
    }

    /**
     * api to fetch all available model-versions in meta-store.
     *
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch all available model-versions in meta-store")
    @Path(Constants.API_V1_VERSION + "/model-versions")
    public List<ModelVersion> getAllModelVersions() throws MetaStoreException {
        try{
            return modelVersionDao.findAll();
        } catch (Exception e)
        {
            LOGGER.error("failed to fetch all model-versions due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to fetch a particular model-version by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch a particular model-version by its id")
    @Path(Constants.API_V1_VERSION + "/model-version/{id}")
    public ModelVersion getModelVersionById(@PathParam("id") final long id) throws MetaStoreException {
        try {
            return modelVersionDao.find(id);
        }catch (Exception e)
        {
            LOGGER.error("failed to fetch a model-version by id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to delete a particular model-version by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @DELETE
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Delete a particular model-version by its id")
    @Path(Constants.API_V1_VERSION + "/model-version/{id}/delete")
    public Response deleteModelVersion(@PathParam("id") final long id) throws MetaStoreException {
        try {
            modelVersionDao.delete(id);
            return Response.status(HttpStatus.SC_OK).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to delete model-version with id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to update an existing model-version.
     *
     * @param id
     * @param modelVersion
     * @return
     * @throws MetaStoreException
     */
    @PUT
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Update an existing model-version")
    @Path(Constants.API_V1_VERSION + "/model-version/{id}/update")
    public Response updateModelVersion(@PathParam("id") final long id,
                                 @Valid @NotNull final ModelVersion modelVersion)
            throws MetaStoreException {
        try {
            final ModelVersion curModelVersion = modelVersionDao.find(id);
            ResourceUtil.updateEntityParams(curModelVersion, modelVersion, ModelVersion.class);
            modelVersionDao.update(curModelVersion);
            return Response.status(HttpStatus.SC_NO_CONTENT).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to update model-version with id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to create a new model-version.
     *
     * @param modelVersion
     * @return
     * @throws MetaStoreException
     */
    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create a new model-version")
    @Path(Constants.API_V1_VERSION + "/model-version/create")
    public ModelVersion createModelVersion(@Valid @NotNull final ModelVersion modelVersion)
            throws MetaStoreException {
        try {
            return modelVersionDao.create(modelVersion);
        }catch (Exception e)
        {
            LOGGER.error("failed to create a model-version due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }
}
