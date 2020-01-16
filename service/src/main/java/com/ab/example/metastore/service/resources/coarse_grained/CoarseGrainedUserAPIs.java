package com.ab.example.metastore.service.resources.coarse_grained;

import com.ab.example.metastore.service.dao.*;
import com.expedia.www.doppler.metastore.commons.entities.Alert;
import com.expedia.www.doppler.metastore.commons.entities.AlertInstance;
import com.expedia.www.doppler.metastore.commons.entities.Dashboard;
import com.expedia.www.doppler.metastore.commons.entities.UserProfile;
import com.expedia.www.doppler.metastore.service.dao.*;
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
import javax.ws.rs.core.MediaType;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author - _amal
 *
 * HTTP resource for user level coarse grained APIs.
 *
 * paths: GET /api/v1/user/alerts
 *        GET /api/v1//user/dashboards
 *        GET /api/v1//user/alert-instances
 */
@SuppressWarnings("PMD.PreserveStackTrace")
@Path("/")
@Api("UserLevelCoarseGrainedAPIs")
public class CoarseGrainedUserAPIs {
    private static final Logger LOGGER = LoggerFactory.getLogger(CoarseGrainedUserAPIs.class);

    private final AlertDao alertDao;
    private final UserProfileDao userProfileDao;
    private final UserDao userDao;
    private final DashboardDao dashboardDao;
    private final AlertInstanceDao alertInstanceDao;
    private final AlertSubscriptionDao alertSubscriptionDao;

    public CoarseGrainedUserAPIs(AlertDao alertDao, DashboardDao dashboardDao,
                                 AlertInstanceDao alertInstanceDao, UserProfileDao userProfileDao,
                                 UserDao userDao, AlertSubscriptionDao alertSubscriptionDao) {
        this.alertDao = alertDao;
        this.dashboardDao = dashboardDao;
        this.alertInstanceDao = alertInstanceDao;
        this.userProfileDao = userProfileDao;
        this.userDao = userDao;
        this.alertSubscriptionDao = alertSubscriptionDao;
    }

    /**
     * api to return all alert definitions
     *      1. created by given user
     *      2. subscribed by given user
     *      3. created by given user's team mates
     *
     * @param user_profile_id
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Path(Constants.API_V1_VERSION + "/user/alerts")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch all alert definitions \n1. created by given user \n2. subscribed by given user " +
            "\n3. created by given user's team mates.")
    public List<Alert> getAllAlertDefinitionsForUser(@Valid @NotNull @HeaderParam("user_profile_id") final Long user_profile_id)
            throws MetaStoreException {
        try {
            final UserProfile userProfile = userProfileDao.find(user_profile_id);
            final List<Alert> alertsCreated = alertDao.findAllForUser(userProfile);
            final List<Alert> alertsSubscribed = alertSubscriptionDao.findAllForUser(userProfile);
            alertsCreated.addAll(alertsSubscribed);
            return alertsCreated;
        }catch (Exception e)
        {
            LOGGER.error("failed to fetch all alert definitions for user_profile_id: {} due to exception.", user_profile_id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to return all dashboards
     *      1. created by given user
     *      2. subscribed by given user
     *      3. created by given user's team mates
     *
     * @param user_profile_id
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Path(Constants.API_V1_VERSION + "/user/dashboards")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch all dashboards \n1. created by given user \n2. subscribed by given user " +
            "\n3. created by given user's team mates.")
    public List<Dashboard> getAllDashboardsForUser(@Valid @NotNull @HeaderParam("user_profile_id")
                                                       final Long user_profile_id) throws MetaStoreException {
        try {
            return dashboardDao.findAllForUser(userProfileDao.find(user_profile_id));
        }catch (Exception e)
        {
            LOGGER.error("failed to fetch all dashboards for user_profile_id: {} due to exception.", user_profile_id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to fetch the active profile for given user.
     *
     * @param user_name
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Path(Constants.API_V1_VERSION + "/user/profile")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch the active profile for given user.")
    public UserProfile getActiveProfileForUser(@Valid @NotNull @HeaderParam("user_name")
                                                   final String user_name) throws MetaStoreException {
        try {
            return userDao.findActiveProfileForUser(user_name);
        }catch (Exception e)
        {
            LOGGER.error("failed to fetch active profile for user_id: {} due to exception.", user_name, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }


    /**
     * api to fetch all alert-instances for given user after last login.
     *
     * @param user_profile_id
     * @param from
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Path(Constants.API_V1_VERSION + "/user/alerts/instances")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch all alert instances for the given user after his last login.")
    public List<AlertInstance> getAllAlertInstanceForUser(@Valid @NotNull @HeaderParam("user_profile_id") final Long user_profile_id,
                                                          @Valid @NotNull @QueryParam("from") final Timestamp from,
                                                          @Valid @QueryParam("start") final int start,
                                                          @Valid @QueryParam("size") final int size)
            throws MetaStoreException {
        try {
            final int page_size = (size == 0)?10: size;
            return alertInstanceDao.findAllForUser(user_profile_id, from, start, page_size);
        }catch (Exception e)
        {
            LOGGER.error("failed to fetch all alert_instances for user_profile_id: {} after timestamp: {} " +
                    "due to exception.", user_profile_id, from, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }
}
