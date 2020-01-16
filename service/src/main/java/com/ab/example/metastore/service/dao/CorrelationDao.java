package com.ab.example.metastore.service.dao;

import com.expedia.www.doppler.metastore.commons.entities.Correlation;
import com.expedia.www.doppler.metastore.commons.list_entities.CorrelationLight;
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
 * Dao layer for Correlation entity
 */
public class CorrelationDao extends AbstractDAO<Correlation> {
    public CorrelationDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Correlation create(final Correlation correlation) {
        return persist(correlation);
    }

    public void update(final Correlation correlation) {
        currentSession().saveOrUpdate(correlation);
    }

    public Correlation find(final long id) {
        return currentSession().get(Correlation.class, id);
    }

    public void delete(final long id) {
        final Correlation correlation = currentSession().get(Correlation.class, id);
        currentSession().delete(correlation);
    }

    public List<CorrelationLight> findAll() {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<Correlation> criteria = builder.createQuery(Correlation.class);
        final Root<Correlation> root = criteria.from(Correlation.class);
        criteria.select(root);
        final List<Correlation> correlations = currentSession().createQuery(criteria).getResultList();

        final List<CorrelationLight> correlationLights = new ArrayList<>();

        for (final Correlation correlation:correlations) {
            correlationLights.add(new CorrelationLight(correlation));
        }

        return correlationLights;
    }
}
