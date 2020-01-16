package com.ab.example.metastore.service.dao;

import com.expedia.www.doppler.metastore.commons.entities.Widget;
import com.expedia.www.doppler.metastore.commons.list_entities.WidgetLight;
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
 * Dao layer for Widget entity
 */
public class WidgetDao extends AbstractDAO<Widget> {
    public WidgetDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Widget create(final Widget widget) {
        return persist(widget);
    }

    public void update(final Widget widget) {
        currentSession().saveOrUpdate(widget);
    }

    public Widget find(final long id) {
        return currentSession().get(Widget.class, id);
    }

    public void delete(final long id) {
        final Widget widget = currentSession().get(Widget.class, id);
        currentSession().delete(widget);
    }

    public List<WidgetLight> findAll() {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<Widget> criteria = builder.createQuery(Widget.class);
        final Root<Widget> root = criteria.from(Widget.class);
        criteria.select(root);
        final List<Widget> widgets = currentSession().createQuery(criteria).getResultList();
        final ArrayList<WidgetLight> widgetLights = new ArrayList<>();

        for (final Widget widget: widgets) {
            widgetLights.add(new WidgetLight(widget));
        }

        return widgetLights;
    }
}
