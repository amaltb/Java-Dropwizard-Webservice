package com.ab.example.metastore.service.dao;

import com.expedia.www.doppler.metastore.commons.entities.TopicEntityAttributeType;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author _amal
 *
 * Dao layer for TopicEntityAttribute entity
 */
public class TopicEntityAttributeTypeDao extends AbstractDAO<TopicEntityAttributeType> {

    public TopicEntityAttributeTypeDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public TopicEntityAttributeType create(final TopicEntityAttributeType topicEntityAttributeType) {
        return persist(topicEntityAttributeType);
    }

    public void update(final TopicEntityAttributeType topicEntityAttributeType) {
        currentSession().saveOrUpdate(topicEntityAttributeType);
    }

    public TopicEntityAttributeType find(final long id) {
        return currentSession().get(TopicEntityAttributeType.class, id);
    }

    public void delete(final long id) {
        final TopicEntityAttributeType topicEntityAttributeType = currentSession().get(TopicEntityAttributeType.class, id);
        currentSession().delete(topicEntityAttributeType);
    }

    public List<TopicEntityAttributeType> findAll() {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<TopicEntityAttributeType> criteria = builder.createQuery(TopicEntityAttributeType.class);
        final Root<TopicEntityAttributeType> root = criteria.from(TopicEntityAttributeType.class);
        criteria.select(root);
        return currentSession().createQuery(criteria).getResultList();
    }
}
