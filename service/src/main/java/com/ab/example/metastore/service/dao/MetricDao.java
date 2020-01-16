package com.ab.example.metastore.service.dao;

import com.expedia.www.doppler.metastore.commons.entities.Metric;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author _amal
 *
 * Dao layer for Metric entity
 */
public class MetricDao extends AbstractDAO<Metric> {
    public MetricDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Metric create(final Metric metric) {
        return persist(metric);
    }

    public void update(final Metric metric) {
        currentSession().saveOrUpdate(metric);
    }

    public Metric find(final long id) {
        return currentSession().get(Metric.class, id);
    }

    public void delete(final long id) {
        final Metric metric = currentSession().get(Metric.class, id);
        currentSession().delete(metric);
    }

    public List<Metric> findAll() {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<Metric> criteria = builder.createQuery(Metric.class);
        final Root<Metric> root = criteria.from(Metric.class);
        criteria.select(root);
        return currentSession().createQuery(criteria).getResultList();
    }
}
