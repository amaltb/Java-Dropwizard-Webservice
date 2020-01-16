package com.ab.example.metastore.service.dao;


import com.expedia.www.doppler.metastore.commons.entities.Alert;
import com.expedia.www.doppler.metastore.commons.entities.UserProfile;
import com.expedia.www.doppler.metastore.commons.list_entities.AlertLight;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @author _amal
 *
 * Dao layer for Alert entity.
 */

public class AlertDao extends AbstractDAO<Alert> {

    public AlertDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Alert create(final Alert alert)
    {
        return persist(alert);
    }

    public void update(final Alert alert)
    {
        currentSession().saveOrUpdate(alert);
    }

    public Alert find(final long id)
    {
        return currentSession().load(Alert.class, id);
    }

    public void delete(final long id)
    {
        final Alert alert = currentSession().load(Alert.class, id);
        currentSession().delete(alert);
    }

    public List<AlertLight> findAll()
    {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<Alert> criteria = builder.createQuery(Alert.class);
        final Root<Alert> root = criteria.from(Alert.class);
        criteria.select(root);
        final List<Alert> alerts = currentSession().createQuery(criteria).getResultList();
        final List<AlertLight> alertLights = new ArrayList<>();

        for (final Alert alert: alerts) {
            alertLights.add(new AlertLight(alert));
        }

        return alertLights;
    }

    public List<Alert> findAllForUser(final UserProfile userProfile)
    {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<Alert> criteria = builder.createQuery(Alert.class);
        final Root<Alert> root = criteria.from(Alert.class);

        final Predicate createdBy =  builder.equal(root.get("createdBy"), userProfile);
        final Predicate createdByTeam =  builder.equal(root.get("createdBy").get("team"), userProfile.getTeam());

        criteria.where(builder.or(createdBy, createdByTeam));
        return currentSession().createQuery(criteria.select(root)).getResultList();
    }
}
