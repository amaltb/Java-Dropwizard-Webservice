package com.ab.example.metastore.service.dao;

import com.expedia.www.doppler.metastore.commons.entities.Model;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author _amal
 *
 * Dao layer for Model entity
 */
public class ModelDao extends AbstractDAO<Model> {
    public ModelDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Model create(final Model model) {
        return persist(model);
    }

    public void update(final Model model) {
        currentSession().saveOrUpdate(model);
    }

    public Model find(final long id) {
        return currentSession().get(Model.class, id);
    }

    public void delete(final long id) {
        final Model model = currentSession().get(Model.class, id);
        currentSession().delete(model);
    }

    public List<Model> findAll() {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<Model> criteria = builder.createQuery(Model.class);
        final Root<Model> root = criteria.from(Model.class);
        criteria.select(root);
        return currentSession().createQuery(criteria).getResultList();
    }
}
