package com.expedia.www.doppler.metastore.service.resources;


import com.expedia.www.doppler.metastore.commons.detail_entities.UserDetail;
import com.expedia.www.doppler.metastore.commons.detail_entities.UserProfileDetail;
import com.expedia.www.doppler.metastore.commons.entities.User;
import com.expedia.www.doppler.metastore.commons.entities.UserProfile;
import com.expedia.www.doppler.metastore.service.dao.UserDao;
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
 * HTTP resource for user CRUD.
 *
 * paths: GET /api/v1/users
 *        GET /api/v1/user/{id}
 *        DELETE /api/v1/user/{id}
 *        PUT /api/v1/user/{id}
 *        POST /api/v1/user
 */
@SuppressWarnings("PMD.PreserveStackTrace")
@Path("/")
@Api("User")
public class UserResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserResource.class);
    private final UserDao userDao;

    public UserResource(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * api to fetch all users in meta-store.
     *
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Path(Constants.API_V1_VERSION + "/users")
    @ApiOperation("Fetch all users in meta-store")
    public List<User> getAllUsers() throws MetaStoreException {
        try{
            return userDao.findAll();
        } catch (Exception e)
        {
            LOGGER.error("failed to fetch all users due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to fetch a particular user by id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch a particular user by id")
    @Path(Constants.API_V1_VERSION + "/user/{id}")
    public UserDetail getUserById(@PathParam("id") final long id) throws MetaStoreException {
        try {
            final User user = userDao.find(id);
            final UserDetail userDetail = new UserDetail(user);

            for (final UserProfile up: user.getUserProfileSet()) {
                final UserProfileDetail profileDetail = new UserProfileDetail(up);
                profileDetail.setUser(null);
                userDetail.getUserProfiles().add(profileDetail);
            }
            return userDetail;
        }catch (Exception e)
        {
            LOGGER.error("failed to fetch user by id: {} due to exception." , id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to fetch a particular user by user name.
     *
     * @param userName
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch a particular user by user_name")
    @Path(Constants.API_V1_VERSION + "/user")
    public UserDetail getUserByName(@Valid @NotNull @HeaderParam("user_name") final String userName) throws MetaStoreException {
        try {
            final User user = userDao.findByName(userName);
            final UserDetail userDetail = new UserDetail(user);

            for (final UserProfile up: user.getUserProfileSet()) {
                final UserProfileDetail profileDetail = new UserProfileDetail(up);
                profileDetail.setUser(null);
                userDetail.getUserProfiles().add(profileDetail);
            }
            return userDetail;
        }catch (Exception e)
        {
            LOGGER.error("failed to fetch user by name: {} due to exception." , userName, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }


    /**
     * api to delete a particular user by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @DELETE
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Delete a particular user by its id")
    @Path(Constants.API_V1_VERSION + "/user/{id}")
    public Response deleteUser(@PathParam("id") final long id) throws MetaStoreException {
        try {
            userDao.delete(id);
            return Response.status(HttpStatus.SC_OK).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to delete user for id: {} due to exception." ,id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to update an existing user.
     *
     * @param id
     * @param user
     * @return
     * @throws MetaStoreException
     */
    @PUT
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Update an existing user")
    @Path(Constants.API_V1_VERSION + "/user/{id}")
    public Response updateUser(@PathParam("id") final long id,
                               @Valid @NotNull final User user)
            throws MetaStoreException {
        try {
            final User currentUser = userDao.find(id);
            ResourceUtil.updateEntityParams(currentUser, user, User.class);
            userDao.update(currentUser);
            return Response.status(HttpStatus.SC_NO_CONTENT).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to update user for id: {} due to exception." ,id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }


    /**
     * api to create a new user.
     *
     * @param user
     * @return
     * @throws MetaStoreException
     */
    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create a new user")
    @Path(Constants.API_V1_VERSION + "/user")
    public User createUser(@Valid @NotNull final User user)
            throws MetaStoreException {
        try {
            return userDao.create(user);
        }catch (Exception e)
        {
            LOGGER.error("failed to create user due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }
}
