package com.ab.example.metastore.service.dao;

import com.expedia.www.doppler.metastore.commons.entities.Role;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author _amal
 *
 * Dao layer for Role entity
 */
public class RoleDao extends AbstractDAO<Role> {
    public RoleDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Role create(final Role role) {
        return persist(role);
    }

    public void update(final Role role) {
        currentSession().saveOrUpdate(role);
    }

    public Role find(final long id) {
        return currentSession().get(Role.class, id);
    }

    public void delete(final long id) {
        final Role role = currentSession().get(Role.class, id);
        currentSession().delete(role);
    }

    public List<Role> findAll() {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<Role> criteria = builder.createQuery(Role.class);
        final Root<Role> root = criteria.from(Role.class);
        criteria.select(root);
        return currentSession().createQuery(criteria).getResultList();
    }
}
