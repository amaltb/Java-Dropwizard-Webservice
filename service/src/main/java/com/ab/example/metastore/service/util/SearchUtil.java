package com.ab.example.metastore.service.util;

import com.expedia.www.doppler.metastore.commons.entities.Alert;
import com.expedia.www.doppler.metastore.commons.entities.BusinessEntityAttribute;
import com.expedia.www.doppler.metastore.commons.entities.Dashboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author _amal
 *
 * SearchUtil is an application util class, acting as a client SDK for global search service.
 */
public final class SearchUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchUtil.class);
    private SearchUtil() {
        throw new UnsupportedOperationException("Instantiating a utility class. Not supported...");
    }

    /**
     * Method to fetch global search store id for a given meta-store id.
     *
     * @param searchGetResponse
     * @param id
     * @return
     */
    private static Long findGlobalSearchIdForGivenMSId(SearchGetResponse searchGetResponse, long id) {
        for (final SearchResult result:searchGetResponse.getResults()) {
            if(result.getMsId() == id)
            {
                return result.getEsId();
            }
        }
        return null;
    }

    /**
     * Method to return a global search entity object.
     *
     * @param id
     * @param type
     * @param caption
     * @param text
     * @return
     */
    private static SearchRequestBody getGlobalSearchEntity(final long id, final String type, final SearchCaptionEntity caption,
                                                           final String text){
        return new SearchRequestBody(id, type, caption, text);
    }

    /**
     * Method to update an entity in global search store.
     *
     * @param client
     * @param searchServiceURI
     * @param entity
     * @return
     */
    private static SearchResponse execSearchUpdateRequest(Client client, String searchServiceURI,
                                                          final SearchRequestBody entity)
    {
        final WebTarget webTarget = client.target(searchServiceURI);
        final Invocation.Builder requestBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        try{
            final Response response = requestBuilder.put(Entity.entity(entity, MediaType.APPLICATION_JSON));
            return response.readEntity(new GenericType<SearchResponse>(){});
        } catch (Exception e)
        {
            LOGGER.error("unable to perform search due to exception. ", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to execute a delete request in global search store.
     *
     * @param client
     * @param searchServiceURI
     * @return
     */
    private static SearchResponse execSearchDeleteRequest(final Client client, final String searchServiceURI)
    {
        final WebTarget webTarget = client.target(searchServiceURI);
        final Invocation.Builder requestBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        try{
            final Response response = requestBuilder.delete();
            return response.readEntity(new GenericType<SearchResponse>(){});
        } catch (Exception e)
        {
            LOGGER.error("unable to perform search due to exception. ", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to execute a get request in global search store.
     *
     * @param client
     * @param searchServiceURI
     * @return
     */
    private static SearchGetResponse execSearchGetRequest(final Client client, final String searchServiceURI)
    {
        final WebTarget webTarget = client.target(searchServiceURI);
        final Invocation.Builder requestBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        try{
            final Response response = requestBuilder.get();
            return response.readEntity(new GenericType<SearchGetResponse>(){});
        } catch (Exception e)
        {
            LOGGER.error("unable to perform search due to exception. ", e);
            throw new RuntimeException(e);
        }
    }


    /**
     * Method to execute a post request in global search store.
     *
     * @param client
     * @param searchServiceURI
     * @param entity
     * @return
     */
    private static boolean execSearchPostRequest(final Client client, final String searchServiceURI,
                                                 final SearchRequestBody entity)
    {
        final WebTarget webTarget = client.target(searchServiceURI);
        final Invocation.Builder requestBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        try{
            final Response response = requestBuilder.post(Entity.entity(entity, MediaType.APPLICATION_JSON));
            final SearchResponse searchResponse = response.readEntity(new GenericType<SearchResponse>(){});
            return searchResponse.isSuccess();
        } catch (Exception e)
        {
            LOGGER.error("unable to persist given entity: {} to search service due to exception. ", entity, e);
        }
        return false;
    }


    /**
     * Overloaded method to persist an alert in global search store.
     *
     * @param alert
     * @param client
     * @param searchServiceURI
     * @return
     */
    public static boolean persistToGlobalSearch(final Alert alert, final Client client, final String searchServiceURI)
    {

        final SearchRequestBody entity = getGlobalSearchEntity(alert.getId(), "Alert",
                new SearchCaptionEntity(alert.getCreatedBy().getUser().getUserName(),
                        alert.getCreatedBy().getTeam().getTeamName(), alert.getDescription()), alert.getAlertName());
        return execSearchPostRequest(client, String.format("%s/%s",searchServiceURI, Constants.SEARCH_PERSIST_API), entity);
    }

    /**
     * Overloaded method to persist a dashboard in global search store.
     *
     * @param dashboard
     * @param client
     * @param searchServiceURI
     * @return
     */
    public static boolean persistToGlobalSearch(final Dashboard dashboard, final Client client,
                                                final String searchServiceURI)
    {
        final SearchRequestBody entity = getGlobalSearchEntity(dashboard.getId(), "Dashboard",
                new SearchCaptionEntity(dashboard.getCreatedBy().getUser().getUserName(),
                        dashboard.getCreatedBy().getTeam().getTeamName(), dashboard.getDesc()),
                dashboard.getDashboardName());
        return execSearchPostRequest(client, String.format("%s/%s",searchServiceURI, Constants.SEARCH_PERSIST_API), entity);
    }

    /**
     * Overloaded method to persist a business entity attribute (field) in global search store.
     *
     * @param businessAttribute
     * @param client
     * @param searchServiceURI
     * @return
     */
    public static boolean persistToGlobalSearch(final BusinessEntityAttribute businessAttribute, final Client client,
                                                final String searchServiceURI)
    {
        final SearchRequestBody entity = getGlobalSearchEntity(businessAttribute.getId(), "Field",
                new SearchCaptionEntity(businessAttribute.getCreatedBy().getUser().getUserName(),
                        businessAttribute.getCreatedBy().getTeam().getTeamName(), businessAttribute.getHelpText()),
                businessAttribute.getName());
        return execSearchPostRequest(client, String.format("%s/%s",searchServiceURI, Constants.SEARCH_PERSIST_API), entity);
    }

    /**
     * Overloaded method to delete an alert in global search store.
     *
     * @param alert
     * @param client
     * @param searchServiceURI
     * @return
     */
    public static boolean deleteFromGlobalSearch(final Alert alert, final Client client, final String searchServiceURI)
    {
        // getting the results from search store by business entity attribute name
        final SearchGetResponse searchGetResponse = execSearchGetRequest(client, String.format("%s/%s?FILTER=%s",
                searchServiceURI, Constants.SEARCH_PERSIST_API, alert.getAlertName()));

        // getting the right business entity attribute by matching metastore ids.
        final Long esId = findGlobalSearchIdForGivenMSId(searchGetResponse, alert.getId());

        if(esId == null)
        {
            throw new RuntimeException("Unable to find a corresponding entry in search store.");
        }

        return execSearchDeleteRequest(client, String.format("%s/%s/%s", searchServiceURI,
                Constants.SEARCH_PERSIST_API, esId)).isSuccess();
    }

    /**
     * Overloaded method to delete a dashboard in global search store.
     *
     * @param dashboard
     * @param client
     * @param searchServiceURI
     * @return
     */
    public static boolean deleteFromGlobalSearch(final Dashboard dashboard, final Client client,
                                                 final String searchServiceURI)
    {
        // getting the results from search store by business entity attribute name
        final SearchGetResponse searchGetResponse = execSearchGetRequest(client, String.format("%s/%s?FILTER=%s",
                searchServiceURI, Constants.SEARCH_PERSIST_API, dashboard.getDashboardName()));

        // getting the right business entity attribute by matching metastore ids.
        final Long esId = findGlobalSearchIdForGivenMSId(searchGetResponse, dashboard.getId());

        if(esId == null)
        {
            throw new RuntimeException("Unable to find a corresponding entry in search store.");
        }

        return execSearchDeleteRequest(client, String.format("%s/%s/%s", searchServiceURI,
                Constants.SEARCH_PERSIST_API, esId)).isSuccess();
    }

    /**
     * Overloaded method to delete a business entity attribute in global search store.
     *
     * @param businessAttribute
     * @param client
     * @param searchServiceURI
     * @return
     */
    public static boolean deleteFromGlobalSearch(final BusinessEntityAttribute businessAttribute,
                                                 final Client client, final String searchServiceURI)
    {
        // getting the results from search store by business entity attribute name
        final SearchGetResponse searchGetResponse = execSearchGetRequest(client, String.format("%s/%s?FILTER=%s",
                searchServiceURI, Constants.SEARCH_PERSIST_API, businessAttribute.getName()));

        // getting the right business entity attribute by matching metastore ids.
        final Long esId = findGlobalSearchIdForGivenMSId(searchGetResponse, businessAttribute.getId());

        if(esId == null)
        {
            throw new RuntimeException("Unable to find a corresponding entry in search store.");
        }

        return execSearchDeleteRequest(client, String.format("%s/%s/%s", searchServiceURI,
                Constants.SEARCH_PERSIST_API, esId)).isSuccess();

    }

    /**
     * Overloaded method to update a business entity attribute in global search store.
     *
     * @param newBusinessAttribute
     * @param oldBusinessAttribute
     * @param client
     * @param searchServiceURI
     * @return
     */
    public static boolean updateGlobalSearch(final BusinessEntityAttribute newBusinessAttribute,
                                             final BusinessEntityAttribute oldBusinessAttribute,
                                             final Client client, final String searchServiceURI) {
        // getting the results from search store by business entity attribute name
        final SearchGetResponse searchGetResponse = execSearchGetRequest(client, String.format("%s/%s?FILTER=%s",
                searchServiceURI, Constants.SEARCH_PERSIST_API, oldBusinessAttribute.getName()));

        // getting the right business entity attribute by matching metastore ids.
        final Long esId = findGlobalSearchIdForGivenMSId(searchGetResponse, oldBusinessAttribute.getId());

        if(esId == null)
        {
            throw new RuntimeException("Unable to find a corresponding entry in search store.");
        }

        final SearchRequestBody requestBody = new SearchRequestBody();
        requestBody.setEsId(esId);
        requestBody.setText(newBusinessAttribute.getName());

        return execSearchUpdateRequest(client, String.format("%s/%s/%s", searchServiceURI,
                Constants.SEARCH_PERSIST_API, esId), requestBody).isSuccess();
    }

    /**
     * Overloaded method to update an alert in global search store.
     *
     * @param newAlert
     * @param oldAlert
     * @param client
     * @param searchServiceURI
     * @return
     */
    public static boolean updateGlobalSearch(final Alert newAlert,
                                             final Alert oldAlert,
                                             final Client client, final String searchServiceURI) {
        // getting the results from search store by business entity attribute name
        final SearchGetResponse searchGetResponse = execSearchGetRequest(client, String.format("%s/%s?FILTER=%s",
                searchServiceURI, Constants.SEARCH_PERSIST_API, oldAlert.getAlertName()));

        // getting the right business entity attribute by matching metastore ids.
        final Long esId = findGlobalSearchIdForGivenMSId(searchGetResponse, oldAlert.getId());

        if(esId == null)
        {
            throw new RuntimeException("Unable to find a corresponding entry in search store.");
        }

        final SearchRequestBody requestBody = new SearchRequestBody();
        requestBody.setEsId(esId);
        requestBody.setText(newAlert.getAlertName());

        return execSearchUpdateRequest(client, String.format("%s/%s/%s", searchServiceURI,
                Constants.SEARCH_PERSIST_API, esId), requestBody).isSuccess();
    }

    /**
     * Overloaded method to delete a dashboard in global search store.
     *
     * @param newDashboard
     * @param oldDashboard
     * @param client
     * @param searchServiceURI
     * @return
     */
    public static boolean updateGlobalSearch(final Dashboard newDashboard,
                                             final Dashboard oldDashboard,
                                             final Client client, final String searchServiceURI) {
        // getting the results from search store by business entity attribute name
        final SearchGetResponse searchGetResponse = execSearchGetRequest(client, String.format("%s/%s?FILTER=%s",
                searchServiceURI, Constants.SEARCH_PERSIST_API, oldDashboard.getDashboardName()));

        // getting the right business entity attribute by matching metastore ids.
        final Long esId = findGlobalSearchIdForGivenMSId(searchGetResponse, oldDashboard.getId());

        if(esId == null)
        {
            throw new RuntimeException("Unable to find a corresponding entry in search store.");
        }

        final SearchRequestBody requestBody = new SearchRequestBody();
        requestBody.setEsId(esId);
        requestBody.setText(newDashboard.getDashboardName());

        return execSearchUpdateRequest(client, String.format("%s/%s/%s", searchServiceURI,
                Constants.SEARCH_PERSIST_API, esId), requestBody).isSuccess();
    }
}
