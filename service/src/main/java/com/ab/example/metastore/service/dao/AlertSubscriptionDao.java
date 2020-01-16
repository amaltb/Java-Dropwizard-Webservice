package com.ab.example.metastore.service.dao;

import com.expedia.www.doppler.metastore.commons.entities.Alert;
import com.expedia.www.doppler.metastore.commons.entities.AlertSubscription;
import com.expedia.www.doppler.metastore.commons.entities.UserProfile;
import com.expedia.www.doppler.metastore.commons.list_entities.AlertSubscriptionLight;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author _amal
 * <p>
 * Dao layer for AlertSubscription entity
 */
public class AlertSubscriptionDao extends AbstractDAO<AlertSubscription> {
    public AlertSubscriptionDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public AlertSubscription create(final AlertSubscription alertSubscription) {
        return persist(alertSubscription);
    }

    public void update(final AlertSubscription alertSubscription) {
        currentSession().saveOrUpdate(alertSubscription);
    }

    public AlertSubscription find(final long id) {
        return currentSession().get(AlertSubscription.class, id);
    }

    public void delete(final long id) {
        final AlertSubscription alertSubscription = currentSession().get(AlertSubscription.class, id);
        currentSession().delete(alertSubscription);
    }

    public List<AlertSubscriptionLight> findAll() {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<AlertSubscription> criteria = builder.createQuery(AlertSubscription.class);
        final Root<AlertSubscription> root = criteria.from(AlertSubscription.class);
        criteria.select(root);
        final List<AlertSubscription> subscriptions = currentSession().createQuery(criteria).getResultList();

        final ArrayList<AlertSubscriptionLight> subscriptionLights = new ArrayList<>();

        for (final AlertSubscription subscription: subscriptions) {
            subscriptionLights.add(new AlertSubscriptionLight(subscription));
        }

        return subscriptionLights;
    }

    public List<Alert> findAllForUser(final UserProfile user_profile) {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<AlertSubscription> criteria = builder.createQuery(AlertSubscription.class);
        final Root<AlertSubscription> root = criteria.from(AlertSubscription.class);

        final Predicate createdBy = builder.equal(root.get("createdBy"), user_profile);
        criteria.where(createdBy);
        return currentSession().createQuery(criteria.select(root)).getResultList().stream()
                .map(AlertSubscription::getAlert).collect(Collectors.toList());
    }
}
