package com.ab.example.metastore.service.dao;

import com.expedia.www.doppler.metastore.commons.entities.Granularity;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author _amal
 *
 * Dao layer for Granularity entity
 */
public class GranularityDao extends AbstractDAO<Granularity> {
    public GranularityDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Granularity create(final Granularity granularity) {
        return persist(granularity);
    }

    public void update(final Granularity granularity) {
        currentSession().saveOrUpdate(granularity);
    }

    public Granularity find(final long id) {
        return currentSession().get(Granularity.class, id);
    }

    public void delete(final long id) {
        final Granularity granularity = currentSession().get(Granularity.class, id);
        currentSession().delete(granularity);
    }

    public List<Granularity> findAll() {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<Granularity> criteria = builder.createQuery(Granularity.class);
        final Root<Granularity> root = criteria.from(Granularity.class);
        criteria.select(root);
        return currentSession().createQuery(criteria).getResultList();
    }
}
