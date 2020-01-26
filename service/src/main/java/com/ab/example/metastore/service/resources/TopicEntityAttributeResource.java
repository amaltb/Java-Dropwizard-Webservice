package com.expedia.www.doppler.metastore.service.resources;

import com.expedia.www.doppler.metastore.commons.entities.TopicEntityAttribute;
import com.expedia.www.doppler.metastore.service.dao.TopicEntityAttributeDao;
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
 * paths: GET /api/v1/topic-entity-attributes
 *        GET /api/v1/topic-entity-attribute/{id}
 *        DELETE /api/v1/topic-entity-attribute/{id}
 *        PUT /api/v1/topic-entity-attribute/{id}
 *        POST /api/v1/topic-entity-attribute
 */
@SuppressWarnings("PMD.PreserveStackTrace")
@Path("/")
@Api("TopicEntityAttribute")
public class TopicEntityAttributeResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(TopicEntityAttributeResource.class);

    private final TopicEntityAttributeDao topicEntityAttributeDao;

    public TopicEntityAttributeResource(TopicEntityAttributeDao topicEntityAttributeDao) {
        this.topicEntityAttributeDao = topicEntityAttributeDao;
    }

    /**
     * api to fetch a particular topic-entity-attribute by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch a particular topic-entity-attribute by its id")
    @Path(Constants.API_V1_VERSION + "/topic-entity-attribute/{id}")
    public TopicEntityAttribute getTopicEntityAttributeById(@PathParam("id") final long id) throws MetaStoreException {
        try {
            return topicEntityAttributeDao.find(id);
        }catch (Exception e)
        {
            LOGGER.error("failed to fetch topic-entity-attribute by id: {} due to exception." ,id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to fetch all topic_entity_attributes with a given name.
     *
     * @param attribute_name
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Path(Constants.API_V1_VERSION + "/topic-entity-attributes")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("api to fetch all topic_entity_attributes with a given name.")
    public List<TopicEntityAttribute> getTopicEntityAttributesByName(
            @Valid @NotNull @HeaderParam("topic_entity_attribute_name") final String attribute_name)
            throws MetaStoreException {
        try{
            return topicEntityAttributeDao.findByName(attribute_name);
        } catch (Exception e)
        {
            LOGGER.error("failed to fetch all topic_entity_attributes due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to delete a particular topic-entity-attribute by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @DELETE
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Delete a particular topic-entity-attribute by its id")
    @Path(Constants.API_V1_VERSION + "/topic-entity-attribute/{id}")
    public Response deleteTopicEntityAttribute(@PathParam("id") final long id) throws MetaStoreException {
        try {
            topicEntityAttributeDao.delete(id);
            return Response.status(HttpStatus.SC_OK).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to delete topic-entity-attribute for id: {} due to exception." ,id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to update an existing topic-entity-attribute.
     *
     * @param id
     * @param topicEntityAttribute
     * @return
     * @throws MetaStoreException
     */
    @PUT
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Update an existing topic-entity-attribute")
    @Path(Constants.API_V1_VERSION + "/topic-entity-attribute/{id}")
    public Response updateTopicEntityAttribute(@PathParam("id") final long id,
                                @Valid @NotNull final TopicEntityAttribute topicEntityAttribute)
            throws MetaStoreException {
        try {
            final TopicEntityAttribute curTopicEntityAttribute = topicEntityAttributeDao.find(id);
            ResourceUtil.updateEntityParams(curTopicEntityAttribute, topicEntityAttribute, TopicEntityAttribute.class);
            topicEntityAttributeDao.update(curTopicEntityAttribute);
            return Response.status(HttpStatus.SC_NO_CONTENT).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to update topic-entity-attribute for id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to create a new topic-entity-attribute.
     *
     * @param topicEntityAttribute
     * @return
     * @throws MetaStoreException
     */
    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create a new topic-entity-attribute")
    @Path(Constants.API_V1_VERSION + "/topic-entity-attribute")
    public TopicEntityAttribute createTopicEntityAttribute(@Valid @NotNull final TopicEntityAttribute topicEntityAttribute)
            throws MetaStoreException {
        try {
            return topicEntityAttributeDao.create(topicEntityAttribute);
        }catch (Exception e)
        {
            LOGGER.error("failed to create topic-entity-attribute due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }
}
