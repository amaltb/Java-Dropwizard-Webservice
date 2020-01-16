package com.ab.example.metastore.service.dao;

import com.expedia.www.doppler.metastore.commons.entities.Dashboard;
import com.expedia.www.doppler.metastore.commons.entities.UserProfile;
import com.expedia.www.doppler.metastore.commons.list_entities.DashboardLight;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @author _amal
 *
 * Dao layer for Dashboard entity
 */
public class DashboardDao extends AbstractDAO<Dashboard> {
    public DashboardDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Dashboard create(final Dashboard dashboard) {
        return persist(dashboard);
    }

    public void update(final Dashboard dashboard) {
        currentSession().saveOrUpdate(dashboard);
    }

    public Dashboard find(final long id) {
        return currentSession().get(Dashboard.class, id);
    }

    public void delete(final long id) {
        final Dashboard dashboard = currentSession().get(Dashboard.class, id);
        currentSession().delete(dashboard);
    }

    public List<DashboardLight> findAll() {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<Dashboard> criteria = builder.createQuery(Dashboard.class);
        final Root<Dashboard> root = criteria.from(Dashboard.class);
        criteria.select(root);
        final List<Dashboard> dashboards = currentSession().createQuery(criteria).getResultList();

        final List<DashboardLight> dashboardLights = new ArrayList<>();
        for (final Dashboard dashboard: dashboards) {
            dashboardLights.add(new DashboardLight(dashboard));
        }

        return dashboardLights;
    }

    public List<Dashboard> findAllForUser(final UserProfile userProfile)
    {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<Dashboard> criteria = builder.createQuery(Dashboard.class);
        final Root<Dashboard> root = criteria.from(Dashboard.class);

        final Predicate createdBy =  builder.equal(root.get("createdBy"), userProfile);
        final Predicate createdByTeam =  builder.equal(root.get("createdBy").get("team"), userProfile.getTeam());
        final Predicate subscribedBy = builder.isMember(userProfile, root.get("usersSubscribed"));

        criteria.where(builder.or(createdBy, subscribedBy, createdByTeam));
        return currentSession().createQuery(criteria.select(root)).getResultList();
    }
}
