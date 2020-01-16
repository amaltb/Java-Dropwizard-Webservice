package com.ab.example.metastore.service.dao;

import com.expedia.www.doppler.metastore.commons.entities.Context;
import com.expedia.www.doppler.metastore.commons.list_entities.ContextLight;
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
 * Dao layer for MetricContext entity
 */
public class ContextDao extends AbstractDAO<Context> {
    public ContextDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Context create(final Context metricContext) {
        return persist(metricContext);
    }

    public void update(final Context metricContext) {
        currentSession().saveOrUpdate(metricContext);
    }

    public Context find(final long id) {
        return currentSession().get(Context.class, id);
    }

    public void delete(final long id) {
        final Context metricContext = currentSession().get(Context.class, id);
        currentSession().delete(metricContext);
    }

    public List<ContextLight> findAll() {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<Context> criteria = builder.createQuery(Context.class);
        final Root<Context> root = criteria.from(Context.class);
        criteria.select(root);
        final List<Context> metricContexts = currentSession().createQuery(criteria).getResultList();

        final List<ContextLight> metricContextLights = new ArrayList<>();
        for (final Context metricContext: metricContexts) {
            metricContextLights.add(new ContextLight(metricContext));
        }
        return metricContextLights;
    }
}
