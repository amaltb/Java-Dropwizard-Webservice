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
 * ContextToContextMap entity represents the correlation between one metric context with other contexts.
 */
@Entity
@Table(name = Constants.METRIC_CONTEXT_TO_CONTEXT_MAP_TABLE_NAME)
@Getter
@Setter
public class ContextToContextMap extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 29L;
    private static final Logger LOGGER = LoggerFactory.getLogger(ContextToContextMap.class);


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "from_context_id")
    @JsonBackReference("correlatedTo")
    private Context fromContext;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "to_context_id")
    private Context toContext;

    /**
     * many to one mapping with Correlation entity. Represents the type of relation between the from and to contexts.
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "correlation_id", nullable = false)
    private Correlation correlation;

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
        if(that instanceof ContextToContextMap)
        {
            final ContextToContextMap temp = (ContextToContextMap) that;
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
