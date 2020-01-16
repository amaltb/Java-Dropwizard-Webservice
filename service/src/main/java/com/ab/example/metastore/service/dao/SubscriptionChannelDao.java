package com.ab.example.metastore.service.dao;

import com.expedia.www.doppler.metastore.commons.entities.SubscriptionChannel;
import com.expedia.www.doppler.metastore.commons.list_entities.SubscriptionChannelLight;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @author _amal
 *
 * Dao layer for SubscriptionChannel entity
 */
public class SubscriptionChannelDao extends AbstractDAO<SubscriptionChannel> {
    public SubscriptionChannelDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public SubscriptionChannel create(final SubscriptionChannel subscriptionChannel) {
        return persist(subscriptionChannel);
    }

    public void update(final SubscriptionChannel subscriptionChannel) {
        currentSession().saveOrUpdate(subscriptionChannel);
    }

    public SubscriptionChannel find(final long id) {
        return currentSession().get(SubscriptionChannel.class, id);
    }

    public void delete(final long id) {
        final SubscriptionChannel subscriptionChannel = currentSession().get(SubscriptionChannel.class, id);
        currentSession().delete(subscriptionChannel);
    }

    public List<SubscriptionChannelLight> findAll() {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<SubscriptionChannel> criteria = builder.createQuery(SubscriptionChannel.class);
        final Root<SubscriptionChannel> root = criteria.from(SubscriptionChannel.class);
        criteria.select(root);
        final List<SubscriptionChannel> subscriptionChannels =  currentSession().createQuery(criteria).getResultList();
        final ArrayList<SubscriptionChannelLight> subscriptionChannelLights = new ArrayList<>();

        for (final SubscriptionChannel subscriptionChannel: subscriptionChannels) {
            subscriptionChannelLights.add(new SubscriptionChannelLight(subscriptionChannel));
        }

        return subscriptionChannelLights;
    }
}
