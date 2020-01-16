package com.ab.example.metastore.commons.list_entities;

import com.ab.example.metastore.commons.entities.Alert;
import com.ab.example.metastore.commons.entities.AlertSubscription;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author _amal
 *
 * Light weight version of Alert entity for list endpoints.
 */
@SuppressWarnings("PMD.ImmutableField")         //Added to avoid PMD warnings for non-final declaration of fields.
@Getter
@Setter
@NoArgsConstructor
public class AlertLight {
    public AlertLight(final Alert alert) {
        id = alert.getId();
        alertName = alert.getAlertName();
        description = alert.getDescription();
        alertDefinition = alert.getAlertDefinition();
        validFrom = alert.getValidFrom();
        validUntil = alert.getValidUntil();
        enabled = alert.isEnabled();
        createdBy = alert.getCreatedBy() == null? null: alert.getCreatedBy().getId();
        modifiedBy = alert.getModifiedBy() == null? null: alert.getModifiedBy().getId();
        alertType = alert.getAlertType().getId();
        alertSubscriptionSet = alert.getAlertSubscriptionSet().stream().map(AlertSubscription::getId)
                .collect(Collectors.toSet());
        metricContext = alert.getContext().getId();
        modelVersion = alert.getModelVersion().getId();
    }

    private long id;

    private String alertName;

    private String description;

    private String alertDefinition;

    private Timestamp validFrom;

    private Timestamp validUntil;

    private boolean enabled;

    private Long createdBy;

    private Long modifiedBy;

    private Long alertType;

    private Set<Long> alertSubscriptionSet;

    private Long metricContext;

    private Long modelVersion;
}
