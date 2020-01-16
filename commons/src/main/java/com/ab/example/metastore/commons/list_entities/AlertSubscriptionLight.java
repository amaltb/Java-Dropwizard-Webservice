package com.ab.example.metastore.commons.list_entities;

import com.ab.example.metastore.commons.entities.AlertSubscription;
import com.ab.example.metastore.commons.entities.SubscriptionChannel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author _amal
 *
 * Light weight version of AlertSubscription for list endpoints.
 */
@SuppressWarnings("PMD.ImmutableField")         //Added to avoid PMD warnings for non-final declaration of fields.
@Getter
@Setter
@NoArgsConstructor
public class AlertSubscriptionLight {

    public AlertSubscriptionLight(final AlertSubscription subscription) {
        id = subscription.getId();
        alert = subscription.getAlert().getId();
        enabled = subscription.isEnabled();
        subscriptionChannel = subscription.getSubscriptionChannel().stream().map(SubscriptionChannel::getId)
                .collect(Collectors.toSet());

        createdBy = subscription.getCreatedBy() == null? null: subscription.getCreatedBy().getId();
        modifiedBy = subscription.getModifiedBy() == null? null: subscription.getModifiedBy().getId();
    }

    private Long id;

    private Long alert;

    private boolean enabled;

    private Set<Long> subscriptionChannel;

    protected Long createdBy;

    protected Long modifiedBy;
}
