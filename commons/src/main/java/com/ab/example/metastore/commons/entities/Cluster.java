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
 * Team entity represents a streaming cluster(like a kafka cluster) registered in metastore.
 */
@Entity
@Table(name = Constants.CLUSTER_TABLE_NAME)
@Getter
@Setter
public class Cluster extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 48L;
    private static final Logger LOGGER = LoggerFactory.getLogger(Cluster.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "configuration", columnDefinition = "varchar")
    private String configuration;

    @Column(name = "type")
    private String type;

    @Column(name = "active")
    private boolean active;

    /**
     * OneToMany mapping with topics. Represents a list all topics registered with this cluster.
     *
     * Uni-directional from topic side.
     */
    @OneToMany(mappedBy = "cluster", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Topic> topics;

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
        if(that instanceof Cluster)
        {
            final Cluster temp = (Cluster) that;
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
