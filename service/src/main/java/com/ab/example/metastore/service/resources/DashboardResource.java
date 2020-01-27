package com.expedia.www.doppler.metastore.service.resources;


import com.expedia.www.doppler.metastore.commons.entities.Dashboard;
import com.expedia.www.doppler.metastore.commons.list_entities.DashboardLight;
import com.expedia.www.doppler.metastore.service.dao.DashboardDao;
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
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author - _amal
 *
 * HTTP resource for dashboard CRUD.
 *
 * paths: GET /api/v1/dashboards
 *        GET /api/v1/dashboard/{id}
 *        DELETE /api/v1/dashboard/{id}/delete
 *        PUT /api/v1/dashboard/{id}/update
 *        POST /api/v1/dashboard/create
 */
@SuppressWarnings({"PMD.PreserveStackTrace", "PMD.PrematureDeclaration", "PMD.UnusedPrivateField", "PMD.SingularField",
        "PMD.UnusedLocalVariable"})
@Path("/")
@Api("Dashboard")
public class DashboardResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardResource.class);
    private final DashboardDao dashboardDao;
    private final Client client;
    private final String searchServiceURI;

    public DashboardResource(DashboardDao dashboardDao, Client client, String searchServiceURI) {
        this.dashboardDao = dashboardDao;
        this.client = client;
        this.searchServiceURI = searchServiceURI;
    }

    /**
     * api to fetch all available dashboards in meta-store. Returning a list of trimmed dashboards by
     * minimizing foreign key reference object size (returning ids instead of whole object) to prevent any network
     * throttling.
     *
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch all available dashboards in meta-store")
    @Path(Constants.API_V1_VERSION + "/dashboards")
    public List<DashboardLight> getAllDashboards() throws MetaStoreException {
        try{
            return dashboardDao.findAll();
        } catch (Exception e)
        {
            LOGGER.error("failed to fetch all dashboards due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to fetch a particular dashboard by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch a particular dashboard by its id")
    @Path(Constants.API_V1_VERSION + "/dashboard/{id}")
    public Dashboard getDashboardById(@PathParam("id") final long id) throws MetaStoreException {
        try {
            return dashboardDao.find(id);
        }catch (Exception e)
        {
            LOGGER.error("failed to fetch a dashboard by id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to delete a particular dashboard by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @DELETE
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Delete a particular dashboard by its id")
    @Path(Constants.API_V1_VERSION + "/dashboard/{id}/delete")
    public Response deleteDashboard(@PathParam("id") final long id) throws MetaStoreException {
        try {
            // fetching dashboard to be deleted.
            final Dashboard dashboard = dashboardDao.find(id);

            // deleting from search store first
            // TODO uncomment deleteFromGlobalSearch method call once successfully integrated with search service.
            final boolean status = true;/*SearchUtil.deleteFromGlobalSearch(dashboard, client, searchServiceURI);*/

            if(status)
            {
                // deleting from meta-store only if the search store delete was successful to avoid any data
                // inconsistency problems.
                dashboardDao.delete(id);
                return Response.status(HttpStatus.SC_OK).build();
            }
            else
            {
                throw new RuntimeException("Filed to delete given business entity attribute in search-store");
            }
        }catch (Exception e)
        {
            LOGGER.error("failed to delete dashboard with id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to update an existing dashboard.
     *
     * @param id
     * @param dashboard
     * @return
     * @throws MetaStoreException
     */
    @PUT
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Update an existing dashboard")
    @Path(Constants.API_V1_VERSION + "/dashboard/{id}/update")
    public Response updateDashboard(@PathParam("id") final long id,
                                          @Valid @NotNull final Dashboard dashboard)
            throws MetaStoreException {
        try {
            // fetching the dashboard to be modified
            final Dashboard oldDashboard = dashboardDao.find(id);

            // updating search store
            // TODO uncomment updateGlobalSearch method call once successfully integrated with search service.
            final boolean status = true; /*SearchUtil.updateGlobalSearch(dashboard, oldDashboard, client, searchServiceURI);*/

            if(status)
            {
                /* updating from meta-store only if the search store update was successful
                to avoid any data inconsistency problems. */
                ResourceUtil.updateEntityParams(oldDashboard, dashboard, Dashboard.class);
                dashboardDao.update(oldDashboard);
                return Response.status(HttpStatus.SC_NO_CONTENT).build();
            }
            else
            {
                throw new RuntimeException("Filed to update given business entity attribute in search-store");
            }
        }catch (Exception e)
        {
            LOGGER.error("failed to update dashboard with id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to create a new dashboard.
     *
     * @param dashboard
     * @return
     * @throws MetaStoreException
     */
    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create a new dashboard")
    @Path(Constants.API_V1_VERSION + "/dashboard/create")
    public Dashboard createDashboard(@Valid @NotNull final Dashboard dashboard)
            throws MetaStoreException {
        try {
            // adding to meta-store
            final Dashboard dashboardCreated = dashboardDao.create(dashboard);

            // persisting to search store
            // TODO uncomment persistToGlobalSearch method call once successfully integrated with search service.
            final boolean status = true; /*SearchUtil.persistToGlobalSearch(dashboardCreated, client, searchServiceURI);*/

            if(status)
            {
                return dashboardCreated;
            }
            else
            {
                // roll-backing the persisted alert in meta-store to avoid inconsistency with search service.
                dashboardDao.delete(dashboardCreated.getId());
                throw new RuntimeException("Filed to persist given dashboard entity in search-store");
            }
        }catch (Exception e)
        {
            LOGGER.error("failed to create a dashboard due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

}
