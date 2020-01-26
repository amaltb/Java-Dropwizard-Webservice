package com.expedia.www.doppler.metastore.service.resources;

import com.expedia.www.doppler.metastore.commons.entities.BusinessEntityAttribute;
import com.expedia.www.doppler.metastore.service.dao.BusinessEntityAttributeDao;
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
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author - _amal
 *
 * HTTP resource for topic-entity-attribute CRUD.
 *
 * paths: GET /api/v1/business-entity-attributes
 *        GET /api/v1/business-entity-attribute/{id}
 *        DELETE /api/v1/business-entity-attribute/{id}
 *        PUT /api/v1/business-entity-attribute/{id}
 *        POST /api/v1/business-entity-attribute
 */
@SuppressWarnings({"PMD.PreserveStackTrace", "PMD.PrematureDeclaration", "PMD.UnusedPrivateField", "PMD.SingularField",
        "PMD.UnusedLocalVariable"})
@Path("/")
@Api("BusinessEntityAttribute")
public class BusinessEntityAttributeResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessEntityAttributeResource.class);

    private final BusinessEntityAttributeDao businessEntityAttributeDao;
    private final Client client;
    private final String searchServiceURI;

    public BusinessEntityAttributeResource(BusinessEntityAttributeDao businessEntityAttributeDao, Client client,
                                           String searchServiceURI) {
        this.businessEntityAttributeDao = businessEntityAttributeDao;
        this.client = client;
        this.searchServiceURI = searchServiceURI;
    }

    /**
     * api to fetch a particular business-entity-attribute by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch a particular business-entity-attribute by its id")
    @Path(Constants.API_V1_VERSION + "/business-entity-attribute/{id}")
    public BusinessEntityAttribute getBusinessEntityAttributeById(@PathParam("id") final long id) throws MetaStoreException {
        try {
            return businessEntityAttributeDao.find(id);
        }catch (Exception e)
        {
            LOGGER.error("failed to fetch business-entity-attribute by id: {} due to exception." ,id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to delete a particular business-entity-attribute by its id.
     *
     * @param id
     * @return
     * @throws MetaStoreException
     */
    @DELETE
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Delete a particular business-entity-attribute by its id")
    @Path(Constants.API_V1_VERSION + "/business-entity-attribute/{id}")
    public Response deleteBusinessEntityAttribute(@PathParam("id") final long id) throws MetaStoreException {
        try {

            // getting business entity attribute to be deleted
            final BusinessEntityAttribute businessEntityAttribute = businessEntityAttributeDao.find(id);

            // deleting from search store first
            // TODO uncomment deleteFromGlobalSearch method call once successfully integrated with search service.
            final boolean status = true; /*SearchUtil.deleteFromGlobalSearch(businessEntityAttribute,
                    client, searchServiceURI);*/

            if(status)
            {
                // deleting from meta-store only if the search store delete was successful to avoid any data
                // inconsistency problems.
                businessEntityAttributeDao.delete(id);
                return Response.status(HttpStatus.SC_OK).build();
            }
            else
            {
                throw new RuntimeException("Filed to delete given business entity attribute in search-store");
            }
        }catch (Exception e)
        {
            LOGGER.error("failed to delete business-entity-attribute for id: {} due to exception." ,id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to update an existing business-entity-attribute.
     *
     * @param id
     * @param businessEntityAttribute
     * @return
     * @throws MetaStoreException
     */
    @PUT
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Update an existing business-entity-attribute")
    @Path(Constants.API_V1_VERSION + "/business-entity-attribute/{id}")
    public Response updateBusinessEntityAttribute(@PathParam("id") final long id,
                                               @Valid @NotNull final BusinessEntityAttribute businessEntityAttribute)
            throws MetaStoreException {
        try {

            final BusinessEntityAttribute oldBusinessEntityAttribute = businessEntityAttributeDao.find(id);

            // updating search store
            // TODO uncomment updateGlobalSearch method call once successfully integrated with search service.
            final boolean status = true; /*SearchUtil.updateGlobalSearch(businessEntityAttribute, oldBusinessEntityAttribute,
                    client, searchServiceURI);*/

            if(status)
            {
                /* updating from meta-store only if the search store update was successful
                to avoid any data inconsistency problems. */
                ResourceUtil.updateEntityParams(oldBusinessEntityAttribute, businessEntityAttribute, BusinessEntityAttribute.class);
                businessEntityAttributeDao.update(oldBusinessEntityAttribute);
                return Response.status(HttpStatus.SC_NO_CONTENT).build();
            }
            else
            {
                throw new RuntimeException("Filed to update given business entity attribute in search-store");
            }
        }catch (Exception e)
        {
            LOGGER.error("failed to update business-entity-attribute for id: {} due to exception.", id, e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to create a new business-entity-attribute.
     *
     * @param businessEntityAttribute
     * @return
     * @throws MetaStoreException
     */
    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create a new business-entity-attribute")
    @Path(Constants.API_V1_VERSION + "/business-entity-attribute")
    public BusinessEntityAttribute createBusinessEntityAttribute(@Valid @NotNull final BusinessEntityAttribute businessEntityAttribute)
            throws MetaStoreException {
        try {
            // persisting to meta-store
            final BusinessEntityAttribute businessEntityAttributeCreated =  businessEntityAttributeDao
                    .create(businessEntityAttribute);

            // persisting to search store
            // TODO uncomment persistToGlobalSearch method call once successfully integrated with search service.
            final boolean status = true; /*SearchUtil.persistToGlobalSearch(businessEntityAttributeCreated,
                    client, searchServiceURI);*/

            if(status)
            {
                return businessEntityAttributeCreated;
            }
            else
            {
                // roll-backing the persisted alert in meta-store to avoid inconsistency with search service.
                businessEntityAttributeDao.delete(businessEntityAttributeCreated.getId());
                throw new RuntimeException("Filed to persist given business entity attribute in search-store");
            }
        }catch (Exception e)
        {
            LOGGER.error("failed to create business-entity-attribute due to exception." , e);
            throw new MetaStoreException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }
}
