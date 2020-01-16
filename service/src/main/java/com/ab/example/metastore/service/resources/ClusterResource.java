package com.ab.example.metastore.service.resources;

import com.expedia.www.doppler.metastore.commons.entities.Cluster;
import com.ab.example.metastore.service.dao.ClusterDao;
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
 * paths: GET /api/v1/cluster/{id}
 *        GET /api/v1/clusters/by-name
 *        GET /api/v1/clusters/by-type
 *        DELETE /api/v1/cluster/{id}/delete
 *        PUT /api/v1/cluster/{id}/update
 *        POST /api/v1/cluster/create
 */
@SuppressWarnings("PMD.PreserveStackTrace")
@Path("/")
@Api("Cluster")
public class ClusterResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterResource.class);
    private final ClusterDao clusterDao;

    public ClusterResource(ClusterDao clusterDao) {
        this.clusterDao = clusterDao;
    }

    /**
     * api to fetch a particular cluster by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch a particular cluster by its id")
    @Path(Constants.API_V1_VERSION + "/cluster/{id}")
    public Cluster getClusterById(@PathParam("id") final long id) throws MetaStoreException {
        try {
            return clusterDao.find(id);
        }catch (Exception e)
        {
            LOGGER.error("failed to fetch cluster by id: {} due to exception." ,id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to fetch all clusters with a given cluster name.
     *
     * @param cluster_name
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Path(Constants.API_V1_VERSION + "/clusters/by-name")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("api to fetch all clusters with a given name.")
    public List<Cluster> getClustersByName(@Valid @NotNull @HeaderParam("cluster_name") final String cluster_name)
            throws MetaStoreException {
        try{
            return clusterDao.findByName(cluster_name);
        } catch (Exception e)
        {
            LOGGER.error("failed to fetch all clusters due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to fetch all clusters with a given cluster type.
     *
     * @param cluster_type
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Path(Constants.API_V1_VERSION + "/clusters/by-type")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("api to fetch all clusters with a given cluster type.")
    public List<Cluster> getClustersByType(@Valid @NotNull @HeaderParam("cluster_type") final String cluster_type,
                                           @QueryParam("isActive") final boolean activeFlag)
            throws MetaStoreException {
        try{
            return clusterDao.findByType(cluster_type, activeFlag);
        } catch (Exception e)
        {
            LOGGER.error("failed to fetch all clusters with given type: {} due to exception." , cluster_type, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to delete a particular cluster by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @DELETE
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Delete a particular cluster by its id")
    @Path(Constants.API_V1_VERSION + "/cluster/{id}/delete")
    public Response deleteCluster(@PathParam("id") final long id) throws MetaStoreException {
        try {
            clusterDao.delete(id);
            return Response.status(HttpStatus.SC_OK).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to delete cluster for id: {} due to exception." ,id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to update an existing cluster.
     *
     * @param id
     * @param cluster
     * @return
     * @throws MetaStoreException
     */
    @PUT
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Update an existing cluster")
    @Path(Constants.API_V1_VERSION + "/cluster/{id}/update")
    public Response updateCluster(@PathParam("id") final long id,
                                @Valid @NotNull final Cluster cluster)
            throws MetaStoreException {
        try {
            clusterDao.update(cluster);
            return Response.status(HttpStatus.SC_NO_CONTENT).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to update cluster for id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to create a new cluster.
     *
     * @param cluster
     * @return
     * @throws MetaStoreException
     */
    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create a new cluster")
    @Path(Constants.API_V1_VERSION + "/cluster/create")
    public Cluster createCluster(@Valid @NotNull final Cluster cluster)
            throws MetaStoreException {
        try {
            return clusterDao.create(cluster);
        }catch (Exception e)
        {
            LOGGER.error("failed to create cluster due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }
}
