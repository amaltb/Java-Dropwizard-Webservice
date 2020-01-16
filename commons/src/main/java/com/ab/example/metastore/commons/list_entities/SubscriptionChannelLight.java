package com.ab.example.metastore.commons.list_entities;

import com.ab.example.metastore.commons.entities.SubscriptionChannel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author _amal
 *
 * Light weight representation of SubscriptionChannel entity.
 */
@SuppressWarnings("PMD.ImmutableField")         //Added to avoid PMD warnings for non-final declaration of fields.
@Getter
@Setter
@NoArgsConstructor
public class SubscriptionChannelLight {
    public SubscriptionChannelLight(final SubscriptionChannel subscriptionChannel) {
        id = subscriptionChannel.getId();
        type = subscriptionChannel.getType();
        subscriptionChannelName = subscriptionChannel.getSubscriptionChannelName();
        alertSubscription = subscriptionChannel.getAlertSubscription() == null? null: subscriptionChannel
                .getAlertSubscription().getId();
    }

    private long id;

    private String type;

    private String subscriptionChannelName;

    private Long alertSubscription;

}
