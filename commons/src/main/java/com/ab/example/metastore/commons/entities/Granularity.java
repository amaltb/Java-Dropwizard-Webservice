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
 * Represents a Granularity defined in metastore.
 * Ex: 5 min, 10 mins, 1 hr etc.
 *
 */

@Entity
@Table(name = Constants.GRANULARITY_TABLE_NAME)
@Getter
@Setter
public class Granularity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 42L;
    private static final Logger LOGGER = LoggerFactory.getLogger(Granularity.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "granularity_id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "alias")
    private String alias;


    /**
     * ManyToMany relation with TopicEntityAttribute. Any granularity can be assumed by a number of topic entity
     * attributes and vice versa.
     *
     * Uni-directional from TopicEntityAttribute side.
     */
    @ManyToMany(targetEntity = TopicEntityAttribute.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE},
    mappedBy = "granularities")
    @JsonIgnore
    private Set<TopicEntityAttribute> topicEntityAttributes;

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
        if(that instanceof Granularity)
        {
            final Granularity temp = (Granularity) that;
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
