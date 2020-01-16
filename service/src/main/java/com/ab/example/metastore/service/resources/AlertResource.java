package com.ab.example.metastore.service.resources;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.expedia.www.doppler.metastore.commons.entities.Alert;
import com.expedia.www.doppler.metastore.commons.list_entities.AlertLight;
import com.ab.example.metastore.service.dao.AlertDao;
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
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author - _amal
 *
 * HTTP resource for alert CRUD.
 *
 * paths: GET /api/v1/alert-definitions
 *        GET /api/v1/alert-definition/{id}
 *        DELETE /api/v1/alert-definition/{id}/delete
 *        PUT /api/v1/alert-definition/{id}/update
 *        POST /api/v1/alert-definition/create
 */
@SuppressWarnings({"PMD.PreserveStackTrace", "PMD.PrematureDeclaration", "PMD.UnusedPrivateField", "PMD.SingularField",
        "PMD.UnusedLocalVariable"})
@Path("/")
@Api("Alert")
@Timed
@ExceptionMetered(cause = MetaStoreException.class)
public class AlertResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlertResource.class);

    private final AlertDao alertDao;
    private final Client client;
    private final String searchServiceURI;

    public AlertResource(AlertDao alertDao, Client client, String searchServiceURI) {
        this.alertDao = alertDao;
        this.client = client;
        this.searchServiceURI = searchServiceURI;
    }

    /**
     * api to fetch all alert definitions in meta-store.
     *
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch all alert definitions in meta-store")
    @Path(Constants.API_V1_VERSION + "/alert-definitions")
    public List<AlertLight> getAllAlertDefinitions() throws MetaStoreException {
        try{
            return alertDao.findAll();
        } catch (Exception e)
        {
            LOGGER.error("failed to fetch all alert definitions due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to fetch a particular alert definition by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch a particular alert definition with its id")
    @Path(Constants.API_V1_VERSION + "/alert-definition/{id}")
    public Alert getAlertDefinitionById(@PathParam("id") final long id) throws MetaStoreException {
        try {
            return alertDao.find(id);
        }catch (Exception e)
        {
            LOGGER.error("failed to fetch an alert definition for id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to delete a particular alert definition by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @DELETE
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Delete a particular alert definition with its id")
    @Path(Constants.API_V1_VERSION + "/alert-definition/{id}/delete")
    public Response deleteAlertDefinition(@PathParam("id") final long id) throws MetaStoreException {
        try {
            // fetching the alert to be deleted
            final Alert alert = alertDao.find(id);

            // deleting from search store first
            // TODO uncomment deleteFromGlobalSearch method call once successfully integrated with search service.
            final boolean status = true; /*SearchUtil.deleteFromGlobalSearch(alert, client, searchServiceURI);*/

            if(status)
            {
                // deleting from meta-store only if the search store delete was successful to avoid any data
                // inconsistency problems.
                alertDao.delete(id);
                return Response.status(HttpStatus.SC_OK).build();
            }
            else
            {
                throw new RuntimeException("Filed to delete given business entity attribute in search-store");
            }
        }catch (Exception e)
        {
            LOGGER.error("failed to delete alert definition for id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to update an existing alert definition.
     *
     * @param id
     * @param alertDefinition
     * @return
     * @throws MetaStoreException
     */
    @PUT
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Update an existing alert definition")
    @Path(Constants.API_V1_VERSION + "/alert-definition/{id}/update")
    public Response updateAlertDefinition(@PathParam("id") final long id, @Valid @NotNull final Alert alertDefinition)
            throws MetaStoreException {
        try {
            // fetching alert to be updated
            final Alert oldAlert = alertDao.find(id);

            // updating search store
            // TODO uncomment updateGlobalSearch method call once successfully integrated with search service.
            final boolean status = true; /*SearchUtil.updateGlobalSearch(alertDefinition, oldAlert, client, searchServiceURI);*/

            if(status)
            {
                /* updating from meta-store only if the search store update was successful
                to avoid any data inconsistency problems. */
                alertDao.update(alertDefinition);
                return Response.status(HttpStatus.SC_NO_CONTENT).build();
            }
            else
            {
                throw new RuntimeException("Filed to update given business entity attribute in search-store");
            }
        }catch (Exception e)
        {
            LOGGER.error("failed to update alert definition for id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to create a new alert definition.
     *
     * @param alertDefinition
     * @return
     * @throws MetaStoreException
     */
    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create a new alert definition")
    @Path(Constants.API_V1_VERSION + "/alert-definition/create")
    public Alert createAlertDefinition(@Valid @NotNull final Alert alertDefinition)
            throws MetaStoreException {
        try {
            // persisting to meta-store
            final Alert alert =  alertDao.create(alertDefinition);
            // persisting to search service
            final boolean status = true;
            // TODO uncomment below line once successfully integrated with search service.
//            final boolean status = SearchUtil.persistToGlobalSearch(alert, client, searchServiceURI);

            if(status)
            {
                return alert;
            }
            else
            {
                // roll-backing the persisted alert in meta-store to avoid inconsistency with search service.
                alertDao.delete(alert.getId());
                throw new RuntimeException("Filed to persist given alert entity in search-store");
            }
        }catch (Exception e)
        {
            LOGGER.error("failed to create alert definition for name:" + alertDefinition.getAlertName()
                    +" due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

}
