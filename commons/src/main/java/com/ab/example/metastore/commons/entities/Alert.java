package com.ab.example.metastore.commons.entities;


import com.ab.example.metastore.commons.util.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

/**
 * @author _amal
 *
 * Alert represents an anomaly definition created in metastore. There is a list entity called AlertLight defined inside
 * list_entities package. AlertLight is a light weight representation of an alert definition by trimming all the foreign
 * references to respective object ids.
 *
 */

@Entity
@Table(name = Constants.ALERT_TABLE_NAME)
@Getter
@Setter
public class Alert extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 6L;
    private static final Logger LOGGER = LoggerFactory.getLogger(Alert.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "alert_name")
    private String alertName;

    @Column(name = "description")
    private String description;

    /**
     * Json definition of a anomaly. This can be converted to an object using jackson objectmapper
     */
    @Column(name = "alert_definition", columnDefinition = "varchar")
    private String alertDefinition;

    @Column(name = "valid_from")
    private Timestamp validFrom;

    @Column(name = "valid_until")
    private Timestamp validUntil;

    @Column(name = "enabled")
    private boolean enabled;

    /**
     * foreign key to UserProfile table. Reference to the owner of this alert definition.
     *
     * Uni-directional relationship from alert side, so fetching a user_profile does not automatically fetches all
     * the alert.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "created_by")
    private UserProfile createdBy;

    /**
     * foreign key to UserProfile table. Reference to the UserProfile who modified this alert-definition most recently.
     *
     * Uni-directional relationship from alert side, so fetching a user_profile does not automatically fetches all
     * the alert.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "modified_by")
    private UserProfile modifiedBy;

    /**
     * foreign key to AlertType table. Reference to the alert type (error, changepoint etc.)
     *
     * Uni-directional relationship from alert side.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "alert_type_id")
    private AlertType alertType;

    /**
     * One to many mapping with AlertSubscription. Set of all subscriptions added for this alert-definition.
     *
     * Bi-directional relationship.
     */
    @OneToMany(mappedBy = "alert", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"alert"})
    private Set<AlertSubscription> alertSubscriptionSet;

    /**
     * One to many mapping with AlertInstance. Set of all alert instances generated for this alert-definition.
     *
     * Uni-directional from alert_instance side. Because pulling all alert instances for an alert definition can
     * easily overwhelm the application. So intentionally ignoring this side of the relation to avoid any io, memory
     * or network throttling.
     */
    @OneToMany(mappedBy = "alert", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<AlertInstance> alertInstanceSet;


    /**
     * ManyToOne mapping with ModelVersion. Represents the version of anomaly model used in this alert definition.
     *
     * Uni-directional relationship.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "model_version_id", nullable = false)
    private ModelVersion modelVersion;


    /**
     * ManyToOne mapping with Context. Represents the metric_context at the root of this alert definition.
     *
     * Uni-directional from alert side.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "metric_context_id", nullable = false)
    private Context context;


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
        if(that instanceof Alert)
        {
            final Alert temp = (Alert) that;
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
