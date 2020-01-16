package com.ab.example.metastore.commons.entities;

import com.ab.example.metastore.commons.util.Constants;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
 * ModelVersion represents an anomaly model(algorithm) version in metastore.
 */

@Entity
@Table(name = Constants.MODEL_VERSION_TABLE_NAME)
@Getter
@Setter
public class ModelVersion extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 31L;
    private static final Logger LOGGER = LoggerFactory.getLogger(ModelVersion.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "version")
    private String version;

    /**
     * Referencing the user profile, who has created this model version.
     *
     * Uni-directional relation.
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "created_by")
    private UserProfile createdBy;

    /**
     * Referencing the user profile, who has last modified this model version.
     *
     * Uni-directional relation.
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "modified_by")
    private UserProfile modifiedBy;

    /**
     * Referencing the base model of this model version.
     *
     * Bi-directional relation.
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "model_id", nullable = false)
//    @JsonIgnoreProperties({"modelVersions"})
    @JsonBackReference(value = "modelVersionReference")
    private Model model;

    /**
     * OneToMany mapping with Alert. Represents a set of alert definitions created using this model version.
     *
     * Uni-directional from Alert side.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "modelVersion")
    @JsonIgnore
    private Set<Alert> alertSet;

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
        if(that instanceof ModelVersion)
        {
            final ModelVersion temp = (ModelVersion) that;
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
