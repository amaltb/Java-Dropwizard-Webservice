package com.ab.example.metastore.commons.entities;

import com.ab.example.metastore.commons.util.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * Alert represents a TopicEntityAttributeType in metastore.
 */

@Entity
@Table(name = Constants.TOPIC_ENTITY_ATTRIBUTE_TYPE_TABLE_NAME)
@Getter
@Setter
public class TopicEntityAttributeType extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 38L;
    private static final Logger LOGGER = LoggerFactory.getLogger(TopicEntityAttributeType.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "alias")
    private String alias;

    /**
     * ManyToMany relation with Metric. Represents the set of various metrics applicable for this attribute type.
     *
     * Uni-directional relation.
     */
    @ManyToMany(targetEntity = Metric.class, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "METRIC_TOPIC_ATTRIBUTE_TYPE_MAP", joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "metric_id"))
    private Set<Metric> metricsApplicable;


    /**
     * OneToMany relation with TopicEntityAttribute. Represents the set of topic entity attributes which has this
     * attribute type.
     *
     * Uni-directional relation from TopicEntityAttribute side.
     */
    @OneToMany(mappedBy = "type", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JsonIgnore
    private Set<TopicEntityAttribute> attributeSet;

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
        if(that instanceof TopicEntityAttributeType)
        {
            final TopicEntityAttributeType temp = (TopicEntityAttributeType) that;
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
