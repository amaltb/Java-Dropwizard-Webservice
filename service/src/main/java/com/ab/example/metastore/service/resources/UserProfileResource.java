package com.ab.example.metastore.service.resources;

import com.expedia.www.doppler.metastore.commons.entities.UserProfile;
import com.ab.example.metastore.service.dao.UserProfileDao;
import com.ab.example.metastore.service.exception.MetaStoreException;
import com.expedia.www.doppler.metastore.commons.list_entities.UserProfileLight;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author - _amal
 *
 * HTTP resource for user-profile CRUD.
 *
 * paths: GET /api/v1/user-profiles
 *        GET /api/v1/user-profile/{id}
 *        DELETE /api/v1/user-profile/{id}/delete
 *        PUT /api/v1/user-profile/{id}/update
 *        POST /api/v1/user-profile/create
 */
@SuppressWarnings("PMD.PreserveStackTrace")
@Path("/")
@Api("UserProfile")
public class UserProfileResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserResource.class);

    private final UserProfileDao userProfileDao;

    public UserProfileResource(UserProfileDao userProfileDao) {
        this.userProfileDao = userProfileDao;
    }

    /**
     * api to fetch all user-profiles in meta-store. Returning a list of trimmed UserProfiles by
     * minimizing foreign key reference object size (returning ids instead of whole object) to prevent any network
     * throttling.
     *
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch all user-profiles in meta-store")
    @Path(Constants.API_V1_VERSION + "/user-profiles")
    public List<UserProfileLight> getAllUserProfiles() throws MetaStoreException {
        try{
            return userProfileDao.findAll();
        } catch (Exception e)
        {
            LOGGER.error("failed to fetch all user-profiles due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to fetch a particular user-profile by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch a particular user-profile by its id")
    @Path(Constants.API_V1_VERSION + "/user-profile/{id}")
    public UserProfile getUserProfileById(@PathParam("id") final long id) throws MetaStoreException {
        try {
            return userProfileDao.find(id);
        }catch (Exception e)
        {
            LOGGER.error("failed to fetch user-profile by id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to delete a particular user-profile by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @DELETE
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Delete a particular user-profile by its id")
    @Path(Constants.API_V1_VERSION + "/user-profile/{id}/delete")
    public Response deleteUserProfile(@PathParam("id") final long id) throws MetaStoreException {
        try {
            userProfileDao.delete(id);
            return Response.status(HttpStatus.SC_OK).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to delete user-profile for id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to update an existing user-profile.
     *
     * @param id
     * @param userProfile
     * @return
     * @throws MetaStoreException
     */
    @PUT
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Update an existing user-profile")
    @Path(Constants.API_V1_VERSION + "/user-profile/{id}/update")
    public Response updateUserProfile(@PathParam("id") final long id,
                               @Valid @NotNull final UserProfile userProfile)
            throws MetaStoreException {
        try {
            userProfileDao.update(userProfile);
            return Response.status(HttpStatus.SC_NO_CONTENT).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to update user-profile for id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to create a new user-profile.
     *
     * @param userProfile
     * @return
     * @throws MetaStoreException
     */
    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create a new user-profile")
    @Path(Constants.API_V1_VERSION + "/user-profile/create")
    public UserProfile createUserProfile(@Valid @NotNull final UserProfile userProfile)
            throws MetaStoreException {
        try {
            return userProfileDao.create(userProfile);
        }catch (Exception e)
        {
            LOGGER.error("failed to create user-profile due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }
}
