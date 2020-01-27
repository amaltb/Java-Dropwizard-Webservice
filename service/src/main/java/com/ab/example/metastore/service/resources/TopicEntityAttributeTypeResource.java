package com.expedia.www.doppler.metastore.service.resources;

import com.expedia.www.doppler.metastore.commons.entities.TopicEntityAttributeType;
import com.expedia.www.doppler.metastore.service.dao.TopicEntityAttributeTypeDao;
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
 * HTTP resource for topic-entity-attribute CRUD.
 *
 * paths: GET /api/v1/topic-entity-attribute-types
 *        GET /api/v1/topic-entity-attribute-type/{id}
 *        DELETE /api/v1/topic-entity-attribute-type/{id}/delete
 *        PUT /api/v1/topic-entity-attribute-type/{id}/update
 *        POST /api/v1/topic-entity-attribute-type/create
 */
@SuppressWarnings("PMD.PreserveStackTrace")
@Path("/")
@Api("TopicEntityAttributeType")
public class TopicEntityAttributeTypeResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(TopicEntityAttributeTypeResource.class);

    private final TopicEntityAttributeTypeDao topicEntityAttributeTypeDao;

    public TopicEntityAttributeTypeResource(TopicEntityAttributeTypeDao topicEntityAttributeTypeDao) {
        this.topicEntityAttributeTypeDao = topicEntityAttributeTypeDao;
    }

    /**
     * api to fetch a particular topic-entity-attribute-type by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch a particular topic-entity-attribute-type by its id")
    @Path(Constants.API_V1_VERSION + "/topic-entity-attribute-type/{id}")
    public TopicEntityAttributeType getTopicEntityAttributeTypeById(@PathParam("id") final long id) throws MetaStoreException {
        try {
            return topicEntityAttributeTypeDao.find(id);
        }catch (Exception e)
        {
            LOGGER.error("failed to fetch topic-entity-attribute-type by id: {} due to exception." ,id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to fetch all topic-entity-attribute-types.
     *
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Path(Constants.API_V1_VERSION + "/topic-entity-attribute-types")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("api to fetch all topic-entity-attribute-types with a given name.")
    public List<TopicEntityAttributeType> getAllTopicEntityAttributeTypes() throws MetaStoreException {
        try{
            return topicEntityAttributeTypeDao.findAll();
        } catch (Exception e)
        {
            LOGGER.error("failed to fetch all topic-entity-attribute-types due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to delete a particular topic-entity-attribute-type by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @DELETE
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Delete a particular topic-entity-attribute-type by its id")
    @Path(Constants.API_V1_VERSION + "/topic-entity-attribute-type/{id}/delete")
    public Response deleteTopicEntityAttributeType(@PathParam("id") final long id) throws MetaStoreException {
        try {
            topicEntityAttributeTypeDao.delete(id);
            return Response.status(HttpStatus.SC_OK).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to delete topic-entity-attribute-type for id: {} due to exception." ,id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to update an existing topic-entity-attribute-type.
     *
     * @param id
     * @param topicEntityAttributeType
     * @return
     * @throws MetaStoreException
     */
    @PUT
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Update an existing topic-entity-attribute-type")
    @Path(Constants.API_V1_VERSION + "/topic-entity-attribute-type/{id}/update")
    public Response updateTopicEntityAttributeType(@PathParam("id") final long id,
                                               @Valid @NotNull final TopicEntityAttributeType topicEntityAttributeType)
            throws MetaStoreException {
        try {
            final TopicEntityAttributeType curTopicEntityAttributeType = topicEntityAttributeTypeDao.find(id);
            ResourceUtil.updateEntityParams(curTopicEntityAttributeType, topicEntityAttributeType,
                    TopicEntityAttributeType.class);
            topicEntityAttributeTypeDao.update(curTopicEntityAttributeType);
            return Response.status(HttpStatus.SC_NO_CONTENT).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to update topic-entity-attribute-type for id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to create a new topic-entity-attribute-type.
     *
     * @param topicEntityAttributeType
     * @return
     * @throws MetaStoreException
     */
    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create a new topic-entity-attribute-type")
    @Path(Constants.API_V1_VERSION + "/topic-entity-attribute-type/create")
    public TopicEntityAttributeType createTopicEntityAttribute(@Valid @NotNull final TopicEntityAttributeType topicEntityAttributeType)
            throws MetaStoreException {
        try {
            return topicEntityAttributeTypeDao.create(topicEntityAttributeType);
        }catch (Exception e)
        {
            LOGGER.error("failed to create topic-entity-attribute-type due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }
}
