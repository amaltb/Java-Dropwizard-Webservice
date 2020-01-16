package com.ab.example.metastore.service.dao;

import com.expedia.www.doppler.metastore.commons.entities.Topic;
import com.expedia.www.doppler.metastore.commons.list_entities.TopicLight;
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
 * Dao layer for Topic entity
 */
public class TopicDao extends AbstractDAO<Topic> {
    public TopicDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Topic create(final Topic topic) {
        return persist(topic);
    }

    public void update(final Topic topic) {
        currentSession().saveOrUpdate(topic);
    }

    public Topic find(final long id) {
        return currentSession().get(Topic.class, id);
    }

    public void delete(final long id) {
        final Topic topic = currentSession().get(Topic.class, id);
        currentSession().delete(topic);
    }

    public List<Topic> findByName(final String topicName)
    {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<Topic> criteria = builder.createQuery(Topic.class);
        final Root<Topic> root = criteria.from(Topic.class);

        final Predicate nameCheck = builder.equal(root.get("name"), topicName);
        criteria.where(nameCheck);

        return currentSession().createQuery(criteria.select(root)).getResultList();
    }

    public List<TopicLight> findAll() {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<Topic> criteria = builder.createQuery(Topic.class);
        final Root<Topic> root = criteria.from(Topic.class);
        criteria.select(root);
        final List<Topic> topics = currentSession().createQuery(criteria).getResultList();

        final List<TopicLight> topicLights = new ArrayList<>();
        for (final Topic topic: topics) {
            topicLights.add(new TopicLight(topic));
        }

        return topicLights;
    }
}
