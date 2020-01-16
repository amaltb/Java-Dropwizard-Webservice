package com.ab.example.metastore.commons.list_entities;

import com.ab.example.metastore.commons.entities.Topic;
import com.ab.example.metastore.commons.entities.TopicEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author _amal
 *
 * Light weight representation of Topic entity for list endpoints.
 */
@SuppressWarnings("PMD.ImmutableField")         //Added to avoid PMD warnings for non-final declaration of fields.
@Getter
@Setter
@NoArgsConstructor
public class TopicLight {

    public TopicLight(final Topic topic) {
        id = topic.getId();
        name = topic.getName();
        caption = topic.getCaption();
        enabled = topic.isEnabled();
        createdBy = topic.getCreatedBy() == null? null: topic.getCreatedBy().getId();
        modifiedBy = topic.getModifiedBy() == null? null: topic.getModifiedBy().getId();
        clusterId = topic.getCluster() == null? null: topic.getCluster().getId();
        topicEntities = topic.getTopicEntities().stream().map(TopicEntity::getId).collect(Collectors.toSet());

    }

    private long id;

    private String name;

    private String caption;

    private boolean enabled;

    private Long createdBy;

    private Long modifiedBy;

    private Long clusterId;

    private Set<Long> topicEntities;
}
