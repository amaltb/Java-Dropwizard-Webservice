package com.ab.example.metastore.service.dao;

import com.expedia.www.doppler.metastore.commons.entities.ModelVersion;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author _amal
 *
 * Dao layer for ModelVersion entity
 */
public class ModelVersionDao extends AbstractDAO<ModelVersion> {
    public ModelVersionDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public ModelVersion create(final ModelVersion modelVersion) {
        return persist(modelVersion);
    }

    public void update(final ModelVersion modelVersion) {
        currentSession().saveOrUpdate(modelVersion);
    }

    public ModelVersion find(final long id) {
        return currentSession().get(ModelVersion.class, id);
    }

    public void delete(final long id) {
        final ModelVersion modelVersion = currentSession().get(ModelVersion.class, id);
        currentSession().delete(modelVersion);
    }

    public List<ModelVersion> findAll() {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<ModelVersion> criteria = builder.createQuery(ModelVersion.class);
        final Root<ModelVersion> root = criteria.from(ModelVersion.class);
        criteria.select(root);
        return currentSession().createQuery(criteria).getResultList();
    }
}
