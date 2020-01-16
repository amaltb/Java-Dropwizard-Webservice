package com.ab.example.metastore.commons.entities;

import com.ab.example.metastore.commons.util.Constants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
 * AlertSubscription represents a subscription entry added for an alert definition. There is a list entity called
 * AlertSubscriptionLight defined inside list_entities package. AlertSubscriptionLight is a light weight
 * representation of an alert subscription by trimming all the foreign references to respective object ids.
 */
@Entity
@Table(name = Constants.ALERT_SUBSCRIPTION_TABLE_NAME)
@Getter
@Setter
public class AlertSubscription extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 8L;
    private static final Logger LOGGER = LoggerFactory.getLogger(AlertSubscription.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * ManyToOne mapping with Alert entity. Represents anomaly definition corresponding to this subscription.
     *
     * Bi-directional relationship.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "alert_id")
    @JsonIgnoreProperties({"alertSubscriptionSet"})
    private Alert alert;

    /**
     * OneToMany mapping with SubscriptionChannel entity. Represents all subscription channels added to this
     * subscription.
     *
     * Bi-directional relationship.
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Column(nullable = true)
    @JsonManagedReference
    private Set<SubscriptionChannel> subscriptionChannel;

    @Column(name = "enabled")
    private boolean enabled;

    /**
     * ManyToOne mapping with UserProfile entity. Represents the user_profile who has created this alert_subscription.
     *
     * Uni-directional from alert_subscription side.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "created_by")
    private UserProfile createdBy;

    /**
     * ManyToOne mapping with UserProfile entity. Represents the user_profile who has last modified
     * this alert_subscription.
     *
     * Uni-directional from alert_subscription side.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "modified_by")
    private UserProfile modifiedBy;

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
        if(that instanceof AlertSubscription)
        {
            final AlertSubscription temp = (AlertSubscription) that;
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
