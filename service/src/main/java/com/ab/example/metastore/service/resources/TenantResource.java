package com.expedia.www.doppler.metastore.service.resources;

import com.expedia.www.doppler.metastore.commons.entities.Tenant;
import com.expedia.www.doppler.metastore.service.dao.TenantDao;
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
 * HTTP resource for tenant CRUD.
 *
 * paths: GET /api/v1/tenants
 *        GET /api/v1/tenant/{id}
 *        DELETE /api/v1/tenant/{id}
 *        PUT /api/v1/tenant/{id}
 *        POST /api/v1/tenant
 */
@SuppressWarnings("PMD.PreserveStackTrace")
@Path("/")
@Api("Tenant")
public class TenantResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(TenantResource.class);

    private final TenantDao tenantDao;

    public TenantResource(TenantDao tenantDao) {
        this.tenantDao = tenantDao;
    }

    /**
     * api to fetch all tenants in meta-store.
     *
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch all tenants in meta-store")
    @Path(Constants.API_V1_VERSION + "/tenants")
    public List<Tenant> getAllTenants() throws MetaStoreException {
        try{
            return tenantDao.findAll();
        } catch (Exception e)
        {
            LOGGER.error("failed to fetch all tenants due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to fetch a particular tenant by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch a particular tenant by its id")
    @Path(Constants.API_V1_VERSION + "/tenant/{id}")
    public Tenant getTenantById(@PathParam("id") final long id) throws MetaStoreException {
        try {
            return tenantDao.find(id);
        }catch (Exception e)
        {
            LOGGER.error("failed to fetch tenant by id: {} due to exception." ,id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to delete a particular tenant by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @DELETE
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Delete a particular tenant by its id")
    @Path(Constants.API_V1_VERSION + "/tenant/{id}")
    public Response deleteTenant(@PathParam("id") final long id) throws MetaStoreException {
        try {
            tenantDao.delete(id);
            return Response.status(HttpStatus.SC_OK).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to delete tenant for id: {} due to exception." ,id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to update an existing tenant.
     *
     * @param id
     * @param tenant
     * @return
     * @throws MetaStoreException
     */
    @PUT
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Update an existing tenant")
    @Path(Constants.API_V1_VERSION + "/tenant/{id}")
    public Response updateTenant(@PathParam("id") final long id,
                               @Valid @NotNull final Tenant tenant)
            throws MetaStoreException {
        try {
            final Tenant curTenant = tenantDao.find(id);
            ResourceUtil.updateEntityParams(curTenant, tenant, Tenant.class);
            tenantDao.update(curTenant);
            return Response.status(HttpStatus.SC_NO_CONTENT).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to update tenant for id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to create a new tenant.
     *
     * @param tenant
     * @return
     * @throws MetaStoreException
     */
    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create a new tenant")
    @Path(Constants.API_V1_VERSION + "/tenant")
    public Tenant createTenant(@Valid @NotNull final Tenant tenant)
            throws MetaStoreException {
        try {
            return tenantDao.create(tenant);
        }catch (Exception e)
        {
            LOGGER.error("failed to create tenant due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }
}
