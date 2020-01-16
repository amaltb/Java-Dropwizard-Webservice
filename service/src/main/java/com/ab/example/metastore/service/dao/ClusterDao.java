package com.ab.example.metastore.service.dao;

import com.expedia.www.doppler.metastore.commons.entities.Cluster;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author _amal
 *
 * Dao layer for Cluster entity
 */
public class ClusterDao extends AbstractDAO<Cluster> {
    public ClusterDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Cluster create(final Cluster cluster) {
        return persist(cluster);
    }

    public void update(final Cluster cluster) {
        currentSession().saveOrUpdate(cluster);
    }

    public Cluster find(final long id) {
        return currentSession().get(Cluster.class, id);
    }

    public void delete(final long id) {
        final Cluster cluster = currentSession().get(Cluster.class, id);
        currentSession().delete(cluster);
    }

    public List<Cluster> findByName(final String clusterName)
    {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<Cluster> criteria = builder.createQuery(Cluster.class);
        final Root<Cluster> root = criteria.from(Cluster.class);

        final Predicate nameCheck = builder.equal(root.get("name"), clusterName);
        criteria.where(nameCheck);

        return currentSession().createQuery(criteria.select(root)).getResultList();
    }

    public List<Cluster> findByType(final String clusterType, final boolean activeFlag) {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<Cluster> criteria = builder.createQuery(Cluster.class);
        final Root<Cluster> root = criteria.from(Cluster.class);

        final Predicate typeCheck = builder.equal(root.get("type"), clusterType);

        Predicate activeCheck;
        if(activeFlag){
            activeCheck = builder.equal(root.get("active"), true);
        }
        else
        {
            activeCheck = builder.and();
        }
        criteria.where(builder.and(typeCheck, activeCheck));

        return currentSession().createQuery(criteria.select(root)).getResultList();
    }
}
