package com.ab.example.metastore.service.resources;

import com.expedia.www.doppler.metastore.commons.entities.TopicEntity;
import com.ab.example.metastore.service.dao.TopicEntityDao;
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
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author - _amal
 *
 * HTTP resource for TopicEntity CRUD.
 *
 * paths: GET /api/v1/topic-entity/{id}
 *        GET /api/v1/topic-entity
 *        DELETE /api/v1/topic-entity/{id}/delete
 *        PUT /api/v1/topic-entity/{id}/update
 *        POST /api/v1/topic-entity/create
 */
@SuppressWarnings("PMD.PreserveStackTrace")
@Path("/")
@Api("TopicEntity")
public class TopicEntityResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(TopicEntityResource.class);
    private final TopicEntityDao topicEntityDao;

    public TopicEntityResource(TopicEntityDao topicEntityDao) {
        this.topicEntityDao = topicEntityDao;
    }

    /**
     * api to fetch a particular topic-entity by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch a particular topic-entity by its id")
    @Path(Constants.API_V1_VERSION + "/topic-entity/{id}")
    public TopicEntity getTopicEntityById(@PathParam("id") final long id) throws MetaStoreException {
        try {
            return topicEntityDao.find(id);
        }catch (Exception e)
        {
            LOGGER.error("failed to fetch topic-entity by id: {} due to exception." ,id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to fetch all topic-entities with a given name.
     *
     * @param topic_entity_name
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Path(Constants.API_V1_VERSION + "/topic-entities")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("api to fetch all topic-entities with a given name.")
    public List<TopicEntity> getTopicEntitiesByName(@Valid @NotNull @HeaderParam("topic_entity_name")
                                                   final String topic_entity_name) throws MetaStoreException {
        try{
            return topicEntityDao.findByName(topic_entity_name);
        } catch (Exception e)
        {
            LOGGER.error("failed to fetch all topic-entities due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to delete a particular topic-entity by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @DELETE
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Delete a particular topic-entity by its id")
    @Path(Constants.API_V1_VERSION + "/topic-entity/{id}/delete")
    public Response deleteTopicEntity(@PathParam("id") final long id) throws MetaStoreException {
        try {
            topicEntityDao.delete(id);
            return Response.status(HttpStatus.SC_OK).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to delete topic-entity for id: {} due to exception." ,id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to update an existing topic-entity.
     *
     * @param id
     * @param topic_entity
     * @return
     * @throws MetaStoreException
     */
    @PUT
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Update an existing topic-entity")
    @Path(Constants.API_V1_VERSION + "/topic-entity/{id}/update")
    public Response updateTopicEntity(@PathParam("id") final long id,
                                @Valid @NotNull final TopicEntity topic_entity)
            throws MetaStoreException {
        try {
            topicEntityDao.update(topic_entity);
            return Response.status(HttpStatus.SC_NO_CONTENT).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to update topic-entity for id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to create a new topic-entity.
     *
     * @param topic_entity
     * @return
     * @throws MetaStoreException
     */
    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create a new topic-entity")
    @Path(Constants.API_V1_VERSION + "/topic-entity/create")
    public TopicEntity createTopicEntity(@Valid @NotNull final TopicEntity topic_entity)
            throws MetaStoreException {
        try {
            return topicEntityDao.create(topic_entity);
        }catch (Exception e)
        {
            LOGGER.error("failed to create topic-entity due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }
}
