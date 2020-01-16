package com.ab.example.metastore.commons.entities;

import com.ab.example.metastore.commons.util.Constants;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author _amal
 *
 * Represents a BusinessEntityAttribute registered in metastore. These are the aiases defined for each of the
 * topic entity attributes in metastore.
 */

@Entity
@Table(name = Constants.BUSINESS_ENTITY_ATTRIBUTE_TABLE_NAME)
@Getter
@Setter
public class BusinessEntityAttribute extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 40L;
    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessEntityAttribute.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "keyAttribute")
    private boolean keyAttribute;

    @Column(name = "nameAttribute")
    private boolean nameAttribute;

    @Column(name = "filterEnabled")
    private boolean filterEnabled;

    @Column(name = "measurable")
    private boolean measurable;

    @Column(name = "format")
    private String format;

    @Column(name = "help_text")
    private String helpText;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "created_by")
    private UserProfile createdBy;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "modified_by")
    private UserProfile modifiedBy;

    /**
     * ManyToOne mapping with TopicEntityAttribute. Any given business entity attribute can have only one
     * corresponding topic entity attribute.
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "topic_attribute_id")
    @JsonBackReference
    private TopicEntityAttribute topicEntityAttribute;

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
        if(that instanceof BusinessEntityAttribute)
        {
            final BusinessEntityAttribute temp = (BusinessEntityAttribute) that;
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
