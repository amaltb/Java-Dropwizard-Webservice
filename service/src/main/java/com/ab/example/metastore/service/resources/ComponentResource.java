package com.expedia.www.doppler.metastore.service.resources;

import com.expedia.www.doppler.metastore.commons.entities.Component;
import com.expedia.www.doppler.metastore.service.dao.ComponentDao;
import com.expedia.www.doppler.metastore.service.exception.MetaStoreException;
import com.expedia.www.doppler.metastore.commons.list_entities.ComponentLight;
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
 * HTTP resource for dashboard template widget component CRUD.
 *
 * paths: GET /api/v1/widget-components
 *        GET /api/v1/widget-component/{id}
 *        DELETE /api/v1/widget-component/{id}/delete
 *        PUT /api/v1/widget-component/{id}/update
 *        POST /api/v1/widget-component/create
 */
@SuppressWarnings("PMD.PreserveStackTrace")
@Path("/")
@Api("WidgetComponent")
public class ComponentResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlertTypeResource.class);
    private final ComponentDao componentDao;

    public ComponentResource(ComponentDao componentDao) {
        this.componentDao = componentDao;
    }

    /**
     * api to fetch all widget components in meta-store. Returning a list of trimmed components by
     * minimizing foreign key reference object size (returning ids instead of whole object) to prevent any network
     * throttling.
     *
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch all available widget components in meta-store")
    @Path(Constants.API_V1_VERSION + "/widget-components")
    public List<ComponentLight> getAllWidgetComponents() throws MetaStoreException {
        try{
            return componentDao.findAll();
        } catch (Exception e)
        {
            LOGGER.error("failed to fetch all widget components due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to fetch a particular widget component by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch a particular widget component by its id")
    @Path(Constants.API_V1_VERSION + "/widget-component/{id}")
    public Component getWidgetComponentById(@PathParam("id") final long id) throws MetaStoreException {
        try {
            return componentDao.find(id);
        }catch (Exception e)
        {
            LOGGER.error("failed to fetch a widget component by id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to delete a particular widget component by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @DELETE
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Delete a particular widget component by its id")
    @Path(Constants.API_V1_VERSION + "/widget-component/{id}/delete")
    public Response deleteWidgetComponent(@PathParam("id") final long id) throws MetaStoreException {
        try {
            componentDao.delete(id);
            return Response.status(HttpStatus.SC_OK).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to delete widget-component with id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to update an existing widget component.
     *
     * @param id
     * @param component
     * @return
     * @throws MetaStoreException
     */
    @PUT
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Update an existing widget component")
    @Path(Constants.API_V1_VERSION + "/widget-component/{id}/update")
    public Response updateWidgetComponent(@PathParam("id") final long id,
                                    @Valid @NotNull final Component component)
            throws MetaStoreException {
        try {
            final Component curComponent = componentDao.find(id);
            ResourceUtil.updateEntityParams(curComponent, component, Component.class);
            componentDao.update(curComponent);
            return Response.status(HttpStatus.SC_NO_CONTENT).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to update widget-component with id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to create a new widget component.
     *
     * @param component
     * @return
     * @throws MetaStoreException
     */
    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create a new widget component")
    @Path(Constants.API_V1_VERSION + "/widget-component/create")
    public Component createAlertType(@Valid @NotNull final Component component)
            throws MetaStoreException {
        try {
            return componentDao.create(component);
        }catch (Exception e)
        {
            LOGGER.error("failed to create widget-component due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }
}
