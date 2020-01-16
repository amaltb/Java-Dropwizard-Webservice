package com.ab.example.metastore.service.dao;

import com.expedia.www.doppler.metastore.commons.entities.TopicEntity;
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
 * Dao layer for TopicEntity entity
 */
public class TopicEntityDao extends AbstractDAO<TopicEntity> {
    public TopicEntityDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public TopicEntity create(final TopicEntity topicEntity) {
        return persist(topicEntity);
    }

    public void update(final TopicEntity topicEntity) {
        currentSession().saveOrUpdate(topicEntity);
    }

    public TopicEntity find(final long id) {
        return currentSession().get(TopicEntity.class, id);
    }

    public void delete(final long id) {
        final TopicEntity topicEntity = currentSession().get(TopicEntity.class, id);
        currentSession().delete(topicEntity);
    }

    public List<TopicEntity> findByName(final String topicEntityName)
    {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<TopicEntity> criteria = builder.createQuery(TopicEntity.class);
        final Root<TopicEntity> root = criteria.from(TopicEntity.class);

        final Predicate nameCheck = builder.equal(root.get("name"), topicEntityName);
        criteria.where(nameCheck);

        return currentSession().createQuery(criteria.select(root)).getResultList();
    }
}
