package com.expedia.www.doppler.metastore.service.resources;

import com.expedia.www.doppler.metastore.commons.entities.Permission;
import com.expedia.www.doppler.metastore.service.dao.PermissionDao;
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
 * HTTP resource for permission CRUD.
 *
 * paths: GET /api/v1/permissions
 *        GET /api/v1/permission/{id}
 *        DELETE /api/v1/permission/{id}
 *        PUT /api/v1/permission/{id}
 *        POST /api/v1/permission
 */
@SuppressWarnings("PMD.PreserveStackTrace")
@Path("/")
@Api("Permission")
public class PermissionResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionResource.class);
    private final PermissionDao permissionDao;

    public PermissionResource(PermissionDao permissionDao) {
        this.permissionDao = permissionDao;
    }

    /**
     * api to fetch all available permissions in meta-store.
     *
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch all available permissions in meta-store")
    @Path(Constants.API_V1_VERSION + "/permissions")
    public List<Permission> getAllPermissions() throws MetaStoreException {
        try{
            return permissionDao.findAll();
        } catch (Exception e)
        {
            LOGGER.error("failed to fetch all permissions due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to fetch a particular permission by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch a particular permission by its id")
    @Path(Constants.API_V1_VERSION + "/permission/{id}")
    public Permission getPermissionById(@PathParam("id") final long id) throws MetaStoreException {
        try {
            return permissionDao.find(id);
        }catch (Exception e)
        {
            LOGGER.error("failed to fetch a permission by id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to delete a particular permission by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @DELETE
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Delete a particular permission by its id")
    @Path(Constants.API_V1_VERSION + "/permission/{id}")
    public Response deletePermission(@PathParam("id") final long id) throws MetaStoreException {
        try {
            permissionDao.delete(id);
            return Response.status(HttpStatus.SC_OK).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to delete permission with id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to update an existing permission.
     *
     * @param id
     * @param permission
     * @return
     * @throws MetaStoreException
     */
    @PUT
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Update an existing permission")
    @Path(Constants.API_V1_VERSION + "/permission/{id}")
    public Response updatePermission(@PathParam("id") final long id,
                                @Valid @NotNull final Permission permission)
            throws MetaStoreException {
        try {
            final Permission curPermission = permissionDao.find(id);
            ResourceUtil.updateEntityParams(curPermission, permission, Permission.class);
            permissionDao.update(curPermission);
            return Response.status(HttpStatus.SC_NO_CONTENT).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to update permission with id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to create a new permission.
     *
     * @param permission
     * @return
     * @throws MetaStoreException
     */
    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create a new permission")
    @Path(Constants.API_V1_VERSION + "/permission")
    public Permission createPermission(@Valid @NotNull final Permission permission)
            throws MetaStoreException {
        try {
            return permissionDao.create(permission);
        }catch (Exception e)
        {
            LOGGER.error("failed to create a permission due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }
}
