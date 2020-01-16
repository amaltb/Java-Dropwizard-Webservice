package com.ab.example.metastore.service.dao;

import com.expedia.www.doppler.metastore.commons.entities.Alert;
import com.expedia.www.doppler.metastore.commons.entities.AlertInstance;
import com.expedia.www.doppler.metastore.commons.entities.UserProfile;
import com.expedia.www.doppler.metastore.commons.list_entities.AlertInstanceLight;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author _amal
 *
 * Dao layer for AlertInstance entity
 */
public class AlertInstanceDao extends AbstractDAO<AlertInstance> {
    public AlertInstanceDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public AlertInstance create(final AlertInstance alertInstance)
    {
        return persist(alertInstance);
    }

    public void update(final AlertInstance alertInstance)
    {
        currentSession().saveOrUpdate(alertInstance);
    }

    public AlertInstance find(final long id)
    {
        return currentSession().get(AlertInstance.class, id);
    }

    public void delete(final long id)
    {
        final AlertInstance alertInstance = currentSession().get(AlertInstance.class, id);
        currentSession().delete(alertInstance);
    }

    public List<AlertInstanceLight> findAll()
    {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<AlertInstance> criteria = builder.createQuery(AlertInstance.class);
        final Root<AlertInstance> root = criteria.from(AlertInstance.class);
        criteria.select(root);
        final List<AlertInstance> alertInstances =  currentSession().createQuery(criteria).getResultList();

        final List<AlertInstanceLight> alertInstanceLights = new ArrayList<>();
        for (final AlertInstance alertInstance: alertInstances) {
            alertInstanceLights.add(new AlertInstanceLight(alertInstance));
        }
        return alertInstanceLights;
    }

    public List<AlertInstance> findAllForUser(final long user_id, final Timestamp from,
                                              final int start, final int size)
    {
        final UserProfile userProfile = new UserProfile();
        userProfile.setId(user_id);

        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<AlertInstance> criteria = builder.createQuery(AlertInstance.class);
        final Root<AlertInstance> root = criteria.from(AlertInstance.class);

        final Predicate createdBy =  builder.equal(root.get("alert").get("createdBy"), userProfile);
        final Predicate timeCheck = builder.greaterThanOrEqualTo(root.get("createdOn"), from);
        criteria.where(builder.and(createdBy, timeCheck));

        final CriteriaQuery<AlertInstance> query = criteria.select(root).orderBy(builder.desc(root.get("createdOn")));

        final TypedQuery<AlertInstance> typedQuery = currentSession().createQuery(query);
        typedQuery.setFirstResult(start);
        typedQuery.setMaxResults(size);

        return typedQuery.getResultList();
    }

    public List<AlertInstance> findAllForAlert(final Long alert_id, final Timestamp from,
                                               final int start, final int size)
    {
        final Alert alert = new Alert();
        alert.setId(alert_id);

        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<AlertInstance> criteria = builder.createQuery(AlertInstance.class);
        final Root<AlertInstance> root = criteria.from(AlertInstance.class);

        final Predicate alertCheck =  builder.equal(root.get("alert"), alert);
        final Predicate timeCheck = builder.greaterThanOrEqualTo(root.get("createdOn"), from);
        criteria.where(builder.and(alertCheck, timeCheck));

        final CriteriaQuery<AlertInstance> query = criteria.select(root).orderBy(builder.desc(root.get("createdOn")));

        final TypedQuery<AlertInstance> typedQuery = currentSession().createQuery(query);
        typedQuery.setFirstResult(start);
        typedQuery.setMaxResults(size);

        return typedQuery.getResultList();

    }
}
