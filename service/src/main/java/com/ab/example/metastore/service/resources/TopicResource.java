package com.ab.example.metastore.service.resources;

import com.expedia.www.doppler.metastore.commons.entities.Topic;
import com.expedia.www.doppler.metastore.commons.list_entities.TopicLight;
import com.ab.example.metastore.service.dao.TopicDao;
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
 * HTTP resource for topic CRUD.
 *
 * paths: GET /api/v1/topics/all
 *        GET /api/v1/topic/{id}
 *        GET /api/v1/topics
 *        DELETE /api/v1/topic/{id}/delete
 *        PUT /api/v1/topic/{id}/update
 *        POST /api/v1/topic/create
 */
@SuppressWarnings("PMD.PreserveStackTrace")
@Path("/")
@Api("Topic")
public class TopicResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(TopicResource.class);

    private final TopicDao topicDao;

    public TopicResource(TopicDao topicDao) {
        this.topicDao = topicDao;
    }

    /**
     * api to fetch all topics in meta-store.
     *
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch all topics in meta-store")
    @Path(Constants.API_V1_VERSION + "/topics/all")
    public List<TopicLight> getAllTopics() throws MetaStoreException {
        try{
            return topicDao.findAll();
        } catch (Exception e)
        {
            LOGGER.error("failed to fetch all topics due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to fetch a particular topic by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch a particular topic by its id")
    @Path(Constants.API_V1_VERSION + "/topic/{id}")
    public Topic getTopicById(@PathParam("id") final long id) throws MetaStoreException {
        try {
            return topicDao.find(id);
        }catch (Exception e)
        {
            LOGGER.error("failed to fetch topic by id: {} due to exception." ,id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to fetch all topics with a given topic name.
     *
     * @param topic_name
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Path(Constants.API_V1_VERSION + "/topics")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("api to fetch all topics with a given topic name.")
    public List<Topic> getTopicsByName(@Valid @NotNull @HeaderParam("topic_name") final String topic_name)
            throws MetaStoreException {
        try{
            return topicDao.findByName(topic_name);
        } catch (Exception e)
        {
            LOGGER.error("failed to fetch all topics due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to delete a particular topic by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @DELETE
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Delete a particular topic by its id")
    @Path(Constants.API_V1_VERSION + "/topic/{id}/delete")
    public Response deleteTopic(@PathParam("id") final long id) throws MetaStoreException {
        try {
            topicDao.delete(id);
            return Response.status(HttpStatus.SC_OK).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to delete topic for id: {} due to exception." ,id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to update an existing topic.
     *
     * @param id
     * @param topic
     * @return
     * @throws MetaStoreException
     */
    @PUT
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Update an existing topic")
    @Path(Constants.API_V1_VERSION + "/topic/{id}/update")
    public Response updateTopic(@PathParam("id") final long id,
                                 @Valid @NotNull final Topic topic)
            throws MetaStoreException {
        try {
            topicDao.update(topic);
            return Response.status(HttpStatus.SC_NO_CONTENT).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to update topic for id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to create a new topic.
     *
     * @param topic
     * @return
     * @throws MetaStoreException
     */
    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create a new topic")
    @Path(Constants.API_V1_VERSION + "/topic/create")
    public Topic createTopic(@Valid @NotNull final Topic topic)
            throws MetaStoreException {
        try {
            return topicDao.create(topic);
        }catch (Exception e)
        {
            LOGGER.error("failed to create topic due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }
}
