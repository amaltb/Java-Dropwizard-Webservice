package com.ab.example.metastore.service.dao;

import com.expedia.www.doppler.metastore.commons.entities.TopicEntityAttribute;
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
 * Dao layer for TopicEntityAttribute entity
 */
public class TopicEntityAttributeDao extends AbstractDAO<TopicEntityAttribute> {
    public TopicEntityAttributeDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public TopicEntityAttribute create(final TopicEntityAttribute topicEntityAttribute) {
        return persist(topicEntityAttribute);
    }

    public void update(final TopicEntityAttribute topicEntityAttribute) {
        currentSession().saveOrUpdate(topicEntityAttribute);
    }

    public TopicEntityAttribute find(final long id) {
        return currentSession().get(TopicEntityAttribute.class, id);
    }

    public void delete(final long id) {
        final TopicEntityAttribute topicEntityAttribute = currentSession().get(TopicEntityAttribute.class, id);
        currentSession().delete(topicEntityAttribute);
    }

    public List<TopicEntityAttribute> findByName(final String topicEntityAttributeName)
    {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<TopicEntityAttribute> criteria = builder.createQuery(TopicEntityAttribute.class);
        final Root<TopicEntityAttribute> root = criteria.from(TopicEntityAttribute.class);

        final Predicate nameCheck = builder.equal(root.get("name"), topicEntityAttributeName);
        criteria.where(nameCheck);

        return currentSession().createQuery(criteria.select(root)).getResultList();
    }
}
