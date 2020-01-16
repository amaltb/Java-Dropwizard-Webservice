package com.ab.example.metastore.commons.list_entities;

import com.ab.example.metastore.commons.entities.*;
import com.expedia.www.doppler.metastore.common.entities.*;
import com.expedia.www.doppler.metastore.commons.entities.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;



/**
 * @author _amal
 *
 * Light weight representation of UserProfile entity.
 */
//Added to avoid PMD warnings for non-final declaration of fields and too many fields in class definition.
@SuppressWarnings({"PMD.TooManyFields", "PMD.ImmutableField"})
@Getter
@Setter
@NoArgsConstructor
public class UserProfileLight {
    public UserProfileLight(final UserProfile userProfile) {
        id = userProfile.getId();
        bio = userProfile.getBio();
        createdOn = userProfile.getCreatedOn();
        updatedOn = userProfile.getUpdatedOn();
        enabled = userProfile.isEnabled();
        userId = userProfile.getUser() == null ? null: userProfile.getUser().getId();
        teamId = userProfile.getTeam() == null ? null: userProfile.getTeam().getId();

        alertsCreated = userProfile.getAlertsCreated().stream().map(Alert::getId).collect(Collectors.toSet());
        alertsModified = userProfile.getAlertsModified().stream().map(Alert::getId).collect(Collectors.toSet());
        dashboardsCreated = userProfile.getDashboardsCreated().stream().map(Dashboard::getId).collect(Collectors.toSet());
        dashboardsModified = userProfile.getDashboardsModified().stream().map(Dashboard::getId).collect(Collectors.toSet());
        dashboardsSubscribed = userProfile.getDashboardsSubscribed().stream().map(Dashboard::getId).collect(Collectors.toSet());
        subscriptionsCreated = userProfile.getSubscriptionsCreated().stream().map(AlertSubscription::getId).collect(Collectors.toSet());
        subscriptionsModified = userProfile.getSubscriptionsModified().stream().map(AlertSubscription::getId).collect(Collectors.toSet());
        metricContextsCreated = userProfile.getContextsCreated().stream().map(Context::getId).collect(Collectors.toSet());
        metricContextsModified = userProfile.getContextsModified().stream().map(Context::getId).collect(Collectors.toSet());
        modelVersionsCreated = userProfile.getModelVersionsCreated().stream().map(ModelVersion::getId).collect(Collectors.toSet());
        modelVersionsModified = userProfile.getModelVersionsModified().stream().map(ModelVersion::getId).collect(Collectors.toSet());
        modelsCreated = userProfile.getModelsCreated().stream().map(Model::getId).collect(Collectors.toSet());
        modelsModified = userProfile.getModelsModified().stream().map(Model::getId).collect(Collectors.toSet());
        topicsCreated = userProfile.getTopicsCreated().stream().map(Topic::getId).collect(Collectors.toSet());
        topicsModified = userProfile.getTopicsModified().stream().map(Topic::getId).collect(Collectors.toSet());
    }

    private long id;

    private String bio;

    private Timestamp createdOn;

    private Timestamp updatedOn;

    private boolean enabled;

    private Long userId;

    private Long teamId;

    private Set<Long> alertsCreated;

    private Set<Long> alertsModified;

    private Set<Long> dashboardsCreated;

    private Set<Long> dashboardsModified;

    private Set<Long> dashboardsSubscribed;

    private Set<Long> subscriptionsCreated;

    private Set<Long> subscriptionsModified;

    private Set<Long> metricContextsCreated;

    private Set<Long> metricContextsModified;

    private Set<Long> modelVersionsCreated;

    private Set<Long> modelVersionsModified;

    private Set<Long> modelsCreated;

    private Set<Long> modelsModified;

    private Set<Long> topicsCreated;

    private Set<Long> topicsModified;
}
