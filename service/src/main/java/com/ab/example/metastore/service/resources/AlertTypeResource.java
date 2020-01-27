package com.expedia.www.doppler.metastore.service.resources;

import com.expedia.www.doppler.metastore.commons.entities.AlertType;
import com.expedia.www.doppler.metastore.service.dao.AlertTypeDao;
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
 * HTTP resource for alert-type CRUD.
 *
 * paths: GET /api/v1/alert-types
 *        GET /api/v1/alert-type/{id}
 *        DELETE /api/v1/alert-type/{id}/delete
 *        PUT /api/v1/alert-type/{id}/update
 *        POST /api/v1/alert-type/create
 */
@SuppressWarnings("PMD.PreserveStackTrace")
@Path("/")
@Api("AlertType")
public class AlertTypeResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlertTypeResource.class);

    private final AlertTypeDao alertTypeDao;

    public AlertTypeResource(AlertTypeDao alertTypeDao) {
        this.alertTypeDao = alertTypeDao;
    }

    /**
     * api to fetch all alert types in meta-store. Returning a list of trimmed alert-types by
     * minimizing foreign key reference object size (returning ids instead of whole object) to prevent any network
     * throttling.
     *
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch all alert types in meta-store")
    @Path(Constants.API_V1_VERSION + "/alert-types")
    public List<AlertType> getAllAlertTypes() throws MetaStoreException {
        try{
            return alertTypeDao.findAll();
        } catch (Exception e)
        {
            LOGGER.error("failed to fetch all alert types due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to fetch a particular alert type by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch a particular alert type by its id")
    @Path(Constants.API_V1_VERSION + "/alert-type/{id}")
    public AlertType getAlertTypeById(@PathParam("id") final long id) throws MetaStoreException {
        try {
            return alertTypeDao.find(id);
        }catch (Exception e)
        {
            LOGGER.error("failed to fetch an alert-type by id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to delete a particular alert type by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @DELETE
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Delete a particular alert type by its id")
    @Path(Constants.API_V1_VERSION + "/alert-type/{id}/delete")
    public Response deleteAlertType(@PathParam("id") final long id) throws MetaStoreException {
        try {
            alertTypeDao.delete(id);
            return Response.status(HttpStatus.SC_OK).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to delete alert-type for id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to update an existing alert type.
     *
     * @param id
     * @param alertType
     * @return
     * @throws MetaStoreException
     */
    @PUT
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Update an existing alert type")
    @Path(Constants.API_V1_VERSION + "/alert-type/{id}/update")
    public Response updateAlertType(@PathParam("id") final long id,
                                        @Valid @NotNull final AlertType alertType)
            throws MetaStoreException {
        try {
            final AlertType curAlertType = alertTypeDao.find(id);
            ResourceUtil.updateEntityParams(curAlertType, alertType, AlertType.class);
            alertTypeDao.update(curAlertType);
            return Response.status(HttpStatus.SC_NO_CONTENT).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to update alert-type for id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to create a new alert type.
     *
     * @param alertType
     * @return
     * @throws MetaStoreException
     */
    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create a new alert type")
    @Path(Constants.API_V1_VERSION + "/alert-type/create")
    public AlertType createAlertType(@Valid @NotNull final AlertType alertType)
            throws MetaStoreException {
        try {
            return alertTypeDao.create(alertType);
        }catch (Exception e)
        {
            LOGGER.error("failed to create alert-type due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }
}
