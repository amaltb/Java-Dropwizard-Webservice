package com.ab.example.metastore.service.dao;

import com.expedia.www.doppler.metastore.commons.entities.WidgetGroup;
import com.expedia.www.doppler.metastore.commons.list_entities.WidgetGroupLight;
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
 * Dao layer for WidgetGroup entity
 */
public class WidgetGroupDao extends AbstractDAO<WidgetGroup> {
    public WidgetGroupDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public WidgetGroup create(final WidgetGroup widgetGroup) {
        return persist(widgetGroup);
    }

    public void update(final WidgetGroup widgetGroup) {
        currentSession().saveOrUpdate(widgetGroup);
    }

    public WidgetGroup find(final long id) {
        return currentSession().get(WidgetGroup.class, id);
    }

    public void delete(final long id) {
        final WidgetGroup widgetGroup = currentSession().get(WidgetGroup.class, id);
        currentSession().delete(widgetGroup);
    }

    public List<WidgetGroupLight> findAll() {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<WidgetGroup> criteria = builder.createQuery(WidgetGroup.class);
        final Root<WidgetGroup> root = criteria.from(WidgetGroup.class);
        criteria.select(root);
        final List<WidgetGroup> widgetGroups = currentSession().createQuery(criteria).getResultList();
        final List<WidgetGroupLight> widgetGroupLights = new ArrayList<>();

        for (final WidgetGroup widgetGroup: widgetGroups) {
            widgetGroupLights.add(new WidgetGroupLight(widgetGroup));
        }

        return widgetGroupLights;
    }
}
