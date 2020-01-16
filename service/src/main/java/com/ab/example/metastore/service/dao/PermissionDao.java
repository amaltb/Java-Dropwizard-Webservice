package com.ab.example.metastore.service.dao;

import com.expedia.www.doppler.metastore.commons.entities.Permission;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author _amal
 *
 * Dao layer for Permission entity
 */
public class PermissionDao extends AbstractDAO<Permission> {
    public PermissionDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Permission create(final Permission permission) {
        return persist(permission);
    }

    public void update(final Permission permission) {
        currentSession().saveOrUpdate(permission);
    }

    public Permission find(final long id) {
        return currentSession().get(Permission.class, id);
    }

    public void delete(final long id) {
        final Permission permission = currentSession().get(Permission.class, id);
        currentSession().delete(permission);
    }

    public List<Permission> findAll() {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<Permission> criteria = builder.createQuery(Permission.class);
        final Root<Permission> root = criteria.from(Permission.class);
        criteria.select(root);
        return currentSession().createQuery(criteria).getResultList();
    }
}
