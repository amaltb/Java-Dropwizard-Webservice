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
 * SubscriptionChannel represents a subscription channel like email added for an alert subscription.
 * Ex: {
 *          id: 1,
 *          type: email,
 *          subscriptionChannelName: ambabu@expedia.com, xyz@expedia.com etc.,
 *
 *     }
 */
@Entity
@Table(name = Constants.ALERT_SUBSCRIPTION_CHANNEL_TABLE_NAME)
@Getter
@Setter
public class SubscriptionChannel extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 9L;
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionChannel.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "type")
    private String type;

    @Column(name = "subscription_channel_name")
    private String subscriptionChannelName;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "subscription_id")
    @JsonBackReference
    private AlertSubscription alertSubscription;

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
        if(that instanceof SubscriptionChannel)
        {
            final SubscriptionChannel temp = (SubscriptionChannel) that;
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
