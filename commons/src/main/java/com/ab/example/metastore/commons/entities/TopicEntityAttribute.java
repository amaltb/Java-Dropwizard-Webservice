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
 * Represents a TopicEntityAttribute registered in metastore. These are the leaf nodes in the
 * json tree. Ex: {
 *                  "MessegeContext": {
 *                      "id": 1,
 *                      "value": "bla"
 *                  }
 *                }
 *                  In the above example MessegeContext is stored as TopicEntity, id and value are stored as topic
 * entity attributes.
 */

@Entity
@Table(name = Constants.TOPIC_ENTITY_ATTRIBUTE_TABLE_NAME)
@Getter
@Setter
public class TopicEntityAttribute extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 37L;
    private static final Logger LOGGER = LoggerFactory.getLogger(TopicEntityAttribute.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "cardinality")
    private long cardinality;

    @Column(name = "size")
    private long size;

    @Column(name = "enabled")
    private boolean enabled;

    /**
     * ManyToOne mapping with TopicEntityAttributeType. Represents the data-type of a topic entity attribute. Keeping
     * data-type as a separate table bcs of the association between data-type and applicable metrics.
     *
     * Uni-directional relation.
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "topic_entity_attribute_type_id")
    private TopicEntityAttributeType type;

    /**
     * ManyToOne mapping with TopicEntity. Represents the immediate parent entity of this attribute.
     *
     * Bi-directional relation.
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "topic_entity_id")
    @JsonBackReference
    private TopicEntity topicEntity;

    /**
     * OneToMany mapping with BusinessEntityAttribute. Any one topic entity attribute can have multiple
     * aliases (BusinessEntityAttribute) possible.
     *
     * Bi-directional relation.
     */

    @OneToMany(mappedBy = "topicEntityAttribute", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonManagedReference
    private Set<BusinessEntityAttribute> businessEntityAttributes;

    /**
     * ManyToMany mapping with Granularity. Any one topic entity attribute can have a set of granularities possible and
     * vice versa.
     *
     * Uni-directional relationship
     */
    @ManyToMany(targetEntity = Granularity.class, fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "TOPIC_ENTITY_ATTRIBUTE_GRANULARITY", joinColumns = @JoinColumn(name = "id"),
    inverseJoinColumns = @JoinColumn(name = "granularity_id"))
    private Set<Granularity> granularities;

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
        if(that instanceof TopicEntityAttribute)
        {
            final TopicEntityAttribute temp = (TopicEntityAttribute) that;
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
