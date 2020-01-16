package com.ab.example.metastore.service.dao;

import com.expedia.www.doppler.metastore.commons.entities.ContextToContextMap;
import com.expedia.www.doppler.metastore.commons.list_entities.ContextToContextMapLight;
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
 * Dao layer for ContextToContextMap entity
 */
public class ContextToContextMapDao extends AbstractDAO<ContextToContextMap> {
    public ContextToContextMapDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public ContextToContextMap create(final ContextToContextMap contextToContextMap) {
        return persist(contextToContextMap);
    }

    public void update(final ContextToContextMap contextToContextMap) {
        currentSession().saveOrUpdate(contextToContextMap);
    }

    public ContextToContextMap find(final long id) {
        return currentSession().get(ContextToContextMap.class, id);
    }

    public void delete(final long id) {
        final ContextToContextMap contextToContextMap = currentSession().get(ContextToContextMap.class, id);
        currentSession().delete(contextToContextMap);
    }

    public List<ContextToContextMapLight> findAll() {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<ContextToContextMap> criteria = builder.createQuery(ContextToContextMap.class);
        final Root<ContextToContextMap> root = criteria.from(ContextToContextMap.class);
        criteria.select(root);
        final List<ContextToContextMap> contextMaps = currentSession().createQuery(criteria).getResultList();

        final List<ContextToContextMapLight> contextMapLights = new ArrayList<>();

        for (final ContextToContextMap contextMap:contextMaps) {
            contextMapLights.add(new ContextToContextMapLight(contextMap));
        }
        return contextMapLights;
    }
}
