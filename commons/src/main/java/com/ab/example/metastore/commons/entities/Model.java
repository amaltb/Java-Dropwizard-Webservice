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
 * Model represents an anomaly model(algorithm) in metastore.
 */

@Entity
@Table(name = Constants.MODEL_TABLE_NAME)
@Getter
@Setter
public class Model extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 32L;
    private static final Logger LOGGER = LoggerFactory.getLogger(Model.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "alias")
    private String alias;

    @Column(name = "modelConfiguration")
    private String modelConfiguration;

    @Column(name = "provider")
    private String provider;

    /**
     * Referencing the user profile, who has created this model.
     *
     * Uni-directional relation.
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "created_by")
    private UserProfile createdBy;


    /**
     * Referencing the user profile, who has last updated this model.
     *
     * Uni-directional relation.
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "modified_by")
    private UserProfile modifiedBy;

    /**
     * OneToMany mapping with ModelVersion. Represents a set of all the versions created on top of this model.
     *
     * Bi-directional relation.
     */
    @OneToMany(mappedBy = "model", cascade = {CascadeType.ALL})
//    @JsonIgnoreProperties({"model"})
    @JsonManagedReference(value = "modelVersionReference")
    private Set<ModelVersion> modelVersions;

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
        if(that instanceof Model)
        {
            final Model temp = (Model) that;
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
