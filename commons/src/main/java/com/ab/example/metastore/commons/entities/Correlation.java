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
 * Correlation entity represents the correlation type for each matric_context to metric_context map.
 */
@Entity
@Table(name = Constants.CORRELATION_TABLE_NAME)
@Getter
@Setter
public class Correlation extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 30L;
    private static final Logger LOGGER = LoggerFactory.getLogger(ContextToContextMap.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "definition")
    private String definition;

    /**
     * OneToMany relation with ContextToContextMap. Represents a set of all ContextToContextMap entries that uses
     * this correlation.
     */
    @OneToMany(mappedBy = "correlation", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<ContextToContextMap> contextToContextMaps;

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
        if(that instanceof Correlation)
        {
            final Correlation temp = (Correlation) that;
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
