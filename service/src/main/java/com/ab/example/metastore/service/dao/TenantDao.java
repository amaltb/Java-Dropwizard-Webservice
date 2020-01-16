package com.ab.example.metastore.service.dao;

import com.expedia.www.doppler.metastore.commons.entities.Tenant;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author _amal
 *
 * Dao layer for Tenant entity
 */
public class TenantDao extends AbstractDAO<Tenant> {
    public TenantDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Tenant create(final Tenant tenant) {
        return persist(tenant);
    }

    public void update(final Tenant tenant) {
        currentSession().saveOrUpdate(tenant);
    }

    public Tenant find(final long id) {
        return currentSession().get(Tenant.class, id);
    }

    public void delete(final long id) {
        final Tenant tenant = currentSession().get(Tenant.class, id);
        currentSession().delete(tenant);
    }

    public List<Tenant> findAll() {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<Tenant> criteria = builder.createQuery(Tenant.class);
        final Root<Tenant> root = criteria.from(Tenant.class);
        criteria.select(root);
        return currentSession().createQuery(criteria).getResultList();
    }
}
