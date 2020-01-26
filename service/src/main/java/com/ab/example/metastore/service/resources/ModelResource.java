package com.expedia.www.doppler.metastore.service.resources;

import com.expedia.www.doppler.metastore.commons.entities.Model;
import com.expedia.www.doppler.metastore.service.dao.ModelDao;
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
 * HTTP resource for model CRUD.
 *
 * paths: GET /api/v1/models
 *        GET /api/v1/model/{id}
 *        DELETE /api/v1/model/{id}
 *        PUT /api/v1/model/{id}
 *        POST /api/v1/model
 */
@SuppressWarnings("PMD.PreserveStackTrace")
@Path("/")
@Api("Model")
public class ModelResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModelResource.class);
    private final ModelDao modelDao;

    public ModelResource(ModelDao modelDao) {
        this.modelDao = modelDao;
    }

    /**
     * api to fetch all available models in meta-store.
     *
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch all available models in meta-store")
    @Path(Constants.API_V1_VERSION + "/models")
    public List<Model> getAllModels() throws MetaStoreException {
        try{
            return modelDao.findAll();
        } catch (Exception e)
        {
            LOGGER.error("failed to fetch all models due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to fetch a particular model by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch a particular model by its id")
    @Path(Constants.API_V1_VERSION + "/model/{id}")
    public Model getModelById(@PathParam("id") final long id) throws MetaStoreException {
        try {
            return modelDao.find(id);
        }catch (Exception e)
        {
            LOGGER.error("failed to fetch a model by id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to delete a particular model by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @DELETE
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Delete a particular model by its id")
    @Path(Constants.API_V1_VERSION + "/model/{id}")
    public Response deleteModel(@PathParam("id") final long id) throws MetaStoreException {
        try {
            modelDao.delete(id);
            return Response.status(HttpStatus.SC_OK).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to delete model with id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to update an existing model.
     *
     * @param id
     * @param model
     * @return
     * @throws MetaStoreException
     */
    @PUT
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Update an existing model")
    @Path(Constants.API_V1_VERSION + "/model/{id}")
    public Response updateModel(@PathParam("id") final long id,
                                 @Valid @NotNull final Model model)
            throws MetaStoreException {
        try {
            final Model curModel = modelDao.find(id);
            ResourceUtil.updateEntityParams(curModel, model, Model.class);
            modelDao.update(curModel);
            return Response.status(HttpStatus.SC_NO_CONTENT).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to update model with id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to create a new model.
     *
     * @param model
     * @return
     * @throws MetaStoreException
     */
    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create a new model")
    @Path(Constants.API_V1_VERSION + "/model")
    public Model createModel(@Valid @NotNull final Model model)
            throws MetaStoreException {
        try {
            return modelDao.create(model);
        }catch (Exception e)
        {
            LOGGER.error("failed to create a model due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }
}
