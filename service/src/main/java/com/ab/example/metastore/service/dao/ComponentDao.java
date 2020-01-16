package com.ab.example.metastore.service.dao;

import com.expedia.www.doppler.metastore.commons.entities.Component;
import com.expedia.www.doppler.metastore.commons.list_entities.ComponentLight;
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
 * Dao layer for Component entity
 */
public class ComponentDao extends AbstractDAO<Component> {

    public ComponentDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Component create(final Component component) {
        return persist(component);
    }

    public void update(final Component component) {
        currentSession().saveOrUpdate(component);
    }

    public Component find(final long id) {
        return currentSession().get(Component.class, id);
    }

    public void delete(final long id) {
        final Component component = currentSession().get(Component.class, id);
        currentSession().delete(component);
    }

    public List<ComponentLight> findAll() {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<Component> criteria = builder.createQuery(Component.class);
        final Root<Component> root = criteria.from(Component.class);
        criteria.select(root);
        final List<Component> components = currentSession().createQuery(criteria).getResultList();
        final ArrayList<ComponentLight> componentLights = new ArrayList<>();

        for (final Component component: components) {
            componentLights.add(new ComponentLight(component));
        }

        return componentLights;
    }
}
