package com.ab.example.metastore.service.dao;

import com.expedia.www.doppler.metastore.commons.entities.*;
import com.expedia.www.doppler.metastore.commons.list_entities.TopicLight;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author _amal
 *
 * Dao layer for TopicEntityAttribute entity
 */
public class BusinessEntityAttributeDao extends AbstractDAO<BusinessEntityAttribute> {
    public BusinessEntityAttributeDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public BusinessEntityAttribute create(final BusinessEntityAttribute businessEntityAttribute) {
        return persist(businessEntityAttribute);
    }

    public void update(final BusinessEntityAttribute businessEntityAttribute) {
        currentSession().saveOrUpdate(businessEntityAttribute);
    }

    public BusinessEntityAttribute find(final long id) {
        return currentSession().get(BusinessEntityAttribute.class, id);
    }

    public void delete(final long id) {
        final BusinessEntityAttribute businessEntityAttribute = currentSession().get(BusinessEntityAttribute.class, id);
        currentSession().delete(businessEntityAttribute);
    }

    public Set<BusinessEntityAttribute> findAllBusinessAttributes(String businessAttributeName) {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<BusinessEntityAttribute> criteria = builder.createQuery(BusinessEntityAttribute.class);
        final Root<BusinessEntityAttribute> root = criteria.from(BusinessEntityAttribute.class);

        // getting the right business entity attribute object.
        final Predicate nameCheck =  builder.equal(root.get("name"), businessAttributeName);
        criteria.where(nameCheck);
        final BusinessEntityAttribute businessAttribute = currentSession()
                .createQuery(criteria.select(root))
                .getSingleResult();

        // get all the topics, which are having an association with this business entity attribute
        final Topic topic = businessAttribute.getTopicEntityAttribute().getTopicEntity().getTopic();

        final Set<BusinessEntityAttribute> result = new HashSet<>();


            for (final TopicEntity topicEntity: topic.getTopicEntities()) {
                final Set<BusinessEntityAttribute> businessAttributes = topicEntity.getAttributes()
                        .stream().map(TopicEntityAttribute::getBusinessEntityAttributes)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toSet());
                result.addAll(businessAttributes);
            }

        return result;
    }

    public Set<TopicLight> findAllTopicsInCluster(final Long cluster_id) {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<Topic> criteria = builder.createQuery(Topic.class);
        final Root<Topic> root = criteria.from(Topic.class);

        final Predicate clusterPredicate = builder.equal(root.get("cluster").get("id"), cluster_id);
        criteria.where(clusterPredicate);

        final List<Topic> topics =  currentSession().createQuery(criteria.select(root)).getResultList();

        final Set<TopicLight> lightTopics = new HashSet<>();

        for (final Topic topic: topics) {
            lightTopics.add(new TopicLight(topic));
        }

        return lightTopics;
    }
}
