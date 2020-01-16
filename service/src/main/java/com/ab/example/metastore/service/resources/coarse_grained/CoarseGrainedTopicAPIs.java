package com.ab.example.metastore.service.resources.coarse_grained;

import com.expedia.www.doppler.metastore.commons.entities.BusinessEntityAttribute;
import com.expedia.www.doppler.metastore.commons.list_entities.TopicLight;
import com.ab.example.metastore.service.dao.BusinessEntityAttributeDao;
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
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Set;

/**
 * @author - _amal
 *
 * HTTP resource for user level coarse grained APIs.
 *
 * paths: GET /api/v1/topic/entity/attributes
 *        GET /api/v1/topic/entity/attributes
 *        GET /api/v1//entity/alert-instances
 */
@SuppressWarnings("PMD.PreserveStackTrace")
@Path("/")
@Api("TopicLevelCoarseGrainedAPIs")
public class CoarseGrainedTopicAPIs {
    private static final Logger LOGGER = LoggerFactory.getLogger(CoarseGrainedTopicAPIs.class);

    private final BusinessEntityAttributeDao businessEntityAttributeDao;

    public CoarseGrainedTopicAPIs(BusinessEntityAttributeDao businessEntityAttributeDao) {
        this.businessEntityAttributeDao = businessEntityAttributeDao;
    }

    /**
     * api to fetch all attributes in a given topic.
     *
     * @param businessAttributeName
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Path(Constants.API_V1_VERSION + "/topic/attributes")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("api to fetch all attributes in a given topic.")
    public Set<BusinessEntityAttribute> getAllRelatedBusinessAttributesForGivenBusinessAttribute
            (@Valid @NotNull @HeaderParam("attribute_name") final String businessAttributeName)
            throws MetaStoreException {
        try{
            return businessEntityAttributeDao.findAllBusinessAttributes(businessAttributeName);
        } catch (Exception e)
        {
            LOGGER.error("failed to fetch all business entity attributes due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }


    /**
     * api to fetch all topics in a given cluster.
     *
     * @param cluster_id
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Path(Constants.API_V1_VERSION + "/cluster/topics")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("api to fetch all topics in a given cluster.")
    public Set<TopicLight> getAllTopicsInACluster
    (@Valid @NotNull @HeaderParam("cluster_id") final Long cluster_id)
            throws MetaStoreException {
        try{
            return businessEntityAttributeDao.findAllTopicsInCluster(cluster_id);
        } catch (Exception e)
        {
            LOGGER.error("failed to fetch all topics in given cluster: {} due to exception." , cluster_id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }
}
