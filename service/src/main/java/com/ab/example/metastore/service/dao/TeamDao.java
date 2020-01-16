package com.ab.example.metastore.service.dao;

import com.expedia.www.doppler.metastore.commons.entities.Team;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author _amal
 *
 * Dao layer for Team entity
 */
public class TeamDao extends AbstractDAO<Team> {
    public TeamDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Team create(final Team team) {
        return persist(team);
    }

    public void update(final Team team) {
        currentSession().saveOrUpdate(team);
    }

    public Team find(final long id) {
        return currentSession().get(Team.class, id);
    }

    public void delete(final long id) {
        final Team team = currentSession().get(Team.class, id);
        currentSession().delete(team);
    }

    public List<Team> findAll() {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<Team> criteria = builder.createQuery(Team.class);
        final Root<Team> root = criteria.from(Team.class);
        criteria.select(root);
        return currentSession().createQuery(criteria).getResultList();
    }
}
