package com.ab.example.metastore.commons.entities;

import com.ab.example.metastore.commons.util.Constants;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author _amal
 *
 * Team entity represents a topic_entity registered in metastore. A topic entity is a non leaf node in the json tree.
 */

@Entity
@Table(name = Constants.TOPIC_ENTITY_TABLE_NAME)
@Getter
@Setter
public class TopicEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 36L;
    private static final Logger LOGGER = LoggerFactory.getLogger(TopicEntity.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "caption")
    private String caption;

    @Column(name = "enabled")
    private boolean enabled;

    /**
     * OneToOne relation with TopicEntity. Represents the parent topic_entity (if any) for this topic_entity.
     *
     * Uni-directional relation.
     */
    @OneToOne
    @JoinColumn(name = "parent_id")
    private TopicEntity parent;

    /**
     * ManyToOne mapping with Topic entity. Represents topic corresponding to this entity.
     *
     * Bi-directional relation.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "topic_id")
    @JsonBackReference
    private Topic topic;

    /**
     * OneToMany relation with TopicEntityAttribute. Represents the set of all attributes of this entity.
     *
     * Bi-directional relation.
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "topicEntity")
    @JsonManagedReference
    private Set<TopicEntityAttribute> attributes;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Long.toString(id).hashCode();
        return result;
    }

    @Override
    public boolean equals(Object that) {
        if(this == that)
        {
            return true;
        }
        if(that == null)
        {
            return false;
        }
        if(that instanceof TopicEntity)
        {
            final TopicEntity temp = (TopicEntity) that;
            return this.id == temp.id;
        }
        else
        {
            return false;
        }
    }

    @Override
    public String toString() {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            LOGGER.error("unable to compute toString due to exception: ", e);
            return String.format("{ id: %s, obj: %s}", id, super.toString());
        }
    }
}
