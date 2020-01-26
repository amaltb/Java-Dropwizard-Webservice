package com.expedia.www.doppler.metastore.service.resources;

import com.expedia.www.doppler.metastore.commons.detail_entities.TeamDetail;
import com.expedia.www.doppler.metastore.commons.entities.Team;
import com.expedia.www.doppler.metastore.service.dao.TeamDao;
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
 * HTTP resource for team CRUD.
 *
 * paths: GET /api/v1/teams
 *        GET /api/v1/team/{id}
 *        DELETE /api/v1/team/{id}
 *        PUT /api/v1/team/{id}
 *        POST /api/v1/team
 */
@SuppressWarnings("PMD.PreserveStackTrace")
@Path("/")
@Api("Team")
public class TeamResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(TeamResource.class);

    private final TeamDao teamDao;

    public TeamResource(TeamDao teamDao) {
        this.teamDao = teamDao;
    }

    /**
     * api to fetch all teams in meta-store.
     *
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch all teams in meta-store")
    @Path(Constants.API_V1_VERSION + "/teams")
    public List<Team> getAllTeams() throws MetaStoreException {
        try{
            return teamDao.findAll();
        } catch (Exception e)
        {
            LOGGER.error("failed to fetch all teams due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to fetch a particular team by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch a particular team by its id")
    @Path(Constants.API_V1_VERSION + "/team/{id}")
    public TeamDetail getTeamById(@PathParam("id") final long id) throws MetaStoreException {
        try {
            return new TeamDetail(teamDao.find(id));
        }catch (Exception e)
        {
            LOGGER.error("failed to fetch team by id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to delete a particular team by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @DELETE
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Delete a particular team by its id")
    @Path(Constants.API_V1_VERSION + "/team/{id}")
    public Response deleteTeam(@PathParam("id") final long id) throws MetaStoreException {
        try {
            teamDao.delete(id);
            return Response.status(HttpStatus.SC_OK).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to delete team for id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to update an existing team.
     *
     * @param id
     * @param team
     * @return
     * @throws MetaStoreException
     */
    @PUT
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Update an existing team")
    @Path(Constants.API_V1_VERSION + "/team/{id}")
    public Response updateTeam(@PathParam("id") final long id,
                                              @Valid @NotNull final Team team)
            throws MetaStoreException {
        try {
            final Team curTeam = teamDao.find(id);
            ResourceUtil.updateEntityParams(curTeam, team, Team.class);
            teamDao.update(curTeam);
            return Response.status(HttpStatus.SC_NO_CONTENT).build();
        }catch (Exception e)
        {
            LOGGER.error("failed to update team for id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to create a new team.
     *
     * @param team
     * @return
     * @throws MetaStoreException
     */
    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create a new team")
    @Path(Constants.API_V1_VERSION + "/team")
    public Team createTeam(@Valid @NotNull final Team team)
            throws MetaStoreException {
        try {
            return teamDao.create(team);
        }catch (Exception e)
        {
            LOGGER.error("failed to create team due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }
}
