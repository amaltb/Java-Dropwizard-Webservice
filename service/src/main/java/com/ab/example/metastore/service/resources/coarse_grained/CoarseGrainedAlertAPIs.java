package com.expedia.www.doppler.metastore.service.resources.coarse_grained;

import com.expedia.www.doppler.metastore.commons.entities.AlertInstance;
import com.expedia.www.doppler.metastore.service.dao.AlertInstanceDao;
import com.expedia.www.doppler.metastore.service.exception.MetaStoreException;
import com.expedia.www.doppler.metastore.service.util.Constants;
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
 * HTTP resource for alert level coarse grained APIs.
 *
 * paths: GET /api/v1/alert/alert-instances
 */
@SuppressWarnings("PMD.PreserveStackTrace")
@Path("/")
@Api("AlertLevelCoarseGrainedAPIs")
public class CoarseGrainedAlertAPIs {
    private static final Logger LOGGER = LoggerFactory.getLogger(CoarseGrainedUserAPIs.class);
    private final AlertInstanceDao alertInstanceDao;

    public CoarseGrainedAlertAPIs(AlertInstanceDao alertInstanceDao) {
        this.alertInstanceDao = alertInstanceDao;
    }

    /**
     * api to fetch all alert-instances for a given alert definition.
     *
     * @param alert_id
     * @param from
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch all alert instances for the given alert after a given time.")
    @Path(Constants.API_V1_VERSION + "/alert/instances")
    public List<AlertInstance> getAllAlertInstanceForUser(@Valid @NotNull @HeaderParam("alert_id") final Long alert_id,
                                                          @Valid @NotNull @QueryParam("from") final Timestamp from,
                                                          @Valid @QueryParam("start") final int start,
                                                          @Valid @QueryParam("size") final int size)
            throws MetaStoreException {
        try {
            final int page_size = (size == 0)?10: size;
            return alertInstanceDao.findAllForAlert(alert_id, from, start, page_size);
        }catch (Exception e)
        {
            LOGGER.error("failed to fetch all alert_instances for alert_id: {} after timestamp: {} " +
                    "due to exception.", alert_id, from, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }
}
