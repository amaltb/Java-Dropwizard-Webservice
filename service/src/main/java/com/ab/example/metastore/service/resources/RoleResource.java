package com.expedia.www.doppler.metastore.service.resources;

import com.expedia.www.doppler.metastore.commons.entities.Role;
import com.expedia.www.doppler.metastore.service.dao.RoleDao;
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
 * HTTP resource for role CRUD.
 *
 * paths: GET /api/v1/roles
 *        GET /api/v1/role/{id}
 *        DELETE /api/v1/role/{id}/delete
 *        PUT /api/v1/role/{id}/update
 *        POST /api/v1/role/create
 */
@SuppressWarnings("PMD.PreserveStackTrace")
@Path("/")
@Api("Role")
public class RoleResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleResource.class);
    private final RoleDao roleDao;

    public RoleResource(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    /**
     * api to fetch all available roles in meta-store.
     *
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch all available roles in meta-store")
    @Path(Constants.API_V1_VERSION + "/roles")
    public List<Role> getAllRoles() throws MetaStoreException {
        try{
            return roleDao.findAll();
        } catch (Exception e)
        {
            LOGGER.error("failed to fetch all roles due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to fetch a particular role by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch a particular role by its id")
    @Path(Constants.API_V1_VERSION + "/role/{id}")
    public Role getRoleById(@PathParam("id") final long id) throws MetaStoreException {
        try {
            return roleDao.find(id);
        }catch (Exception e)
        {
            LOGGER.error("failed to fetch a role by id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to delete a particular role by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @DELETE
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Delete a particular role by its id")
    @Path(Constants.API_V1_VERSION + "/role/{id}/delete")
    public Response deleteRole(@PathParam("id") final long id) throws MetaStoreException {
        try {
            roleDao.delete(id);
            return Response.status(HttpStatus.SC_OK).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to delete role with id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to update an existing role.
     *
     * @param id
     * @param role
     * @return
     * @throws MetaStoreException
     */
    @PUT
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Update an existing role")
    @Path(Constants.API_V1_VERSION + "/role/{id}/update")
    public Response updateRole(@PathParam("id") final long id,
                                     @Valid @NotNull final Role role)
            throws MetaStoreException {
        try {
            final Role curRole = roleDao.find(id);
            ResourceUtil.updateEntityParams(curRole, role, Role.class);
            roleDao.update(curRole);
            return Response.status(HttpStatus.SC_NO_CONTENT).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to update role with id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to create a new role.
     *
     * @param role
     * @return
     * @throws MetaStoreException
     */
    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create a new role")
    @Path(Constants.API_V1_VERSION + "/role/create")
    public Role createRole(@Valid @NotNull final Role role)
            throws MetaStoreException {
        try {
            return roleDao.create(role);
        }catch (Exception e)
        {
            LOGGER.error("failed to create a role due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }
}
