package com.ab.example.metastore.commons.list_entities;

import com.ab.example.metastore.commons.entities.Dashboard;
import com.ab.example.metastore.commons.entities.UserProfile;
import com.ab.example.metastore.commons.entities.WidgetGroup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author _amal
 *
 * Light weight representation of Dashboard entity.
 */
@SuppressWarnings("PMD.ImmutableField")         //Added to avoid PMD warnings for non-final declaration of fields.
@Getter
@Setter
@NoArgsConstructor
public class DashboardLight {

    public DashboardLight(final Dashboard dashboard) {
        id = dashboard.getId();
        dashboardName = dashboard.getDashboardName();
        desc = dashboard.getDesc();
        tags = dashboard.getTags();
        enabled = dashboard.isEnabled();

        createdBy = dashboard.getCreatedBy() == null? null: dashboard.getModifiedBy().getId();
        modifiedBy = dashboard.getModifiedBy() == null ? null: dashboard.getModifiedBy().getId();

        usersSubscribed = dashboard.getUsersSubscribed().stream().map(UserProfile::getId).collect(Collectors.toSet());
        widgetGroups = dashboard.getWidgetGroups().stream().map(WidgetGroup::getId).collect(Collectors.toSet());
    }

    private long id;

    private String dashboardName;

    private String desc;

    private String tags;

    private boolean enabled;

    private Long createdBy;

    private Long modifiedBy;

    private Set<Long> usersSubscribed;

    private Set<Long> widgetGroups;
}
