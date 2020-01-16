package com.ab.example.metastore.commons.entities;

import com.ab.example.metastore.commons.util.Constants;
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
 * Team entity represents a topic registered in metastore. There is a list entity called
 * Topic defined inside list_entities package. Topic is a light weight
 * representation of a topic by trimming all the foreign references to respective object ids.
 */

@Entity
@Table(name = Constants.TOPIC_TABLE_NAME)
@Getter
@Setter
public class Topic extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 35L;
    private static final Logger LOGGER = LoggerFactory.getLogger(Topic.class);

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
     * ManyToOne relation with UserProfile entity. Represents the user created this Topic in metastore.
     *
     * Uni-directional relation.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "created_by")
    private UserProfile createdBy;

    /**
     * ManyToOne relation with UserProfile entity. Represents the user last modified this Topic in metastore.
     *
     * Uni-directional relation.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "modified_by")
    private UserProfile modifiedBy;

    /**
     * OneToMany relation with TopicEntity. Represents a set of all non leaf json nodes present in this Topic.
     *
     * Bi-directional relation.
     */
    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<TopicEntity> topicEntities;


    /**
     * ManyToOne relation with Cluster. Represents the cluster to which this topic belong to.
     *
     * Uni-directional relation.
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "cluster_id")
    private Cluster cluster;

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
        if(that instanceof Topic)
        {
            final Topic temp = (Topic) that;
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
