package com.ab.example.metastore.commons.entities;

import com.ab.example.metastore.commons.util.Constants;
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
 * AlertInstance represents an actual alert occurrence. There is a list entity called AlertInstanceLight defined inside
 * list_entities package. AlertInstanceLight is a light weight representation of an alert instance by trimming all
 * the foreign references to respective object ids.
 */
@Entity
@Table(name = Constants.ALERT_INSTANCE_TABLE_NAME)
@Setter
@Getter
public class AlertInstance extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 10L;
    private static final Logger LOGGER = LoggerFactory.getLogger(Role.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "context")
    private String context;

    @Column(name = "status")
    private boolean status;

    /**
     * ManyToOne mapping with Alert entity. Represents the anomaly definition corresponding to this alert instance.
     *
     * Uni-directional from alert_instance side.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "alert_type_id", nullable = false)
    private Alert alert;

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
        if(that instanceof AlertInstance)
        {
            final AlertInstance temp = (AlertInstance) that;
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
