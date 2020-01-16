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
 * Alert represents a metric definition (metric can be count, sum etc.) in metastore.
 */

@Entity
@Table(name = Constants.METRIC_TABLE_NAME)
@Getter
@Setter
public class Metric extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 39L;
    private static final Logger LOGGER = LoggerFactory.getLogger(Metric.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metric_id")
    private long id;

    @Column(name = "name")
    private long name;

    @Column(name = "alias")
    private long alias;

    /**
     * ManyToMany mapping with TopicEntityAttributeType. Represents the set of attribute types possible for this
     * metric. Many to Many because one metric can be applicable to multiple attribute types and one attribute type
     * can have multiple possible metrics.
     *
     * Uni-Directional, because you are only interested in what all metric applicable for a particular attribute type.
     */
    @ManyToMany(targetEntity = TopicEntityAttributeType.class, cascade = {CascadeType.MERGE, CascadeType.PERSIST},
    mappedBy = "metricsApplicable")
    @JsonIgnore
    private Set<TopicEntityAttributeType> attributeTypes;

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
        if(that instanceof Metric)
        {
            final Metric temp = (Metric) that;
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
