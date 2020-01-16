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
 * Component entity represents a component dimension used inside a widget to render the resource (metric context)
 * in metastore.
 */
@Entity
@Table(name = Constants.COMPONENT_TABLE_NAME)
@Getter
@Setter
public class Component extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 27L;
    private static final Logger LOGGER = LoggerFactory.getLogger(Component.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "version")
    private String version;

    /**
     * One to many mapping with Widget entity. One component may be used by multiple widgets.
     *
     * Uni-directional from widget side.
     */
    @OneToMany(mappedBy = "component", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Widget> widgets;

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
        if(that instanceof Component)
        {
            final Component temp = (Component) that;
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
