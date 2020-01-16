package com.ab.example.metastore.service.dao;

import com.expedia.www.doppler.metastore.commons.entities.AlertType;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author _amal
 *
 * Dao layer for AlertType entity
 */
public class AlertTypeDao extends AbstractDAO<AlertType> {
    public AlertTypeDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public AlertType create(final AlertType alertType) {
        return persist(alertType);
    }

    public void update(final AlertType alertType) {
        currentSession().saveOrUpdate(alertType);
    }

    public AlertType find(final long id) {
        return currentSession().get(AlertType.class, id);
    }

    public void delete(final long id) {
        final AlertType alertType = currentSession().get(AlertType.class, id);
        currentSession().delete(alertType);
    }

    public List<AlertType> findAll() {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<AlertType> criteria = builder.createQuery(AlertType.class);
        final Root<AlertType> root = criteria.from(AlertType.class);
        criteria.select(root);
        return currentSession().createQuery(criteria).getResultList();
    }
}
