package com.ab.example.metastore.commons.list_entities;

import com.ab.example.metastore.commons.entities.Context;
import com.ab.example.metastore.commons.entities.ContextToContextMap;
import com.ab.example.metastore.commons.entities.Widget;
import com.ab.example.metastore.commons.entities.Alert;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author _amal
 *
 * Light weight representation of Context entity.
 */
@SuppressWarnings("PMD.ImmutableField")         //Added to avoid PMD warnings for non-final declaration of fields.
@Getter
@Setter
@NoArgsConstructor
public class ContextLight {

    public ContextLight(final Context context) {
        id = context.getId();
        name = context.getName();
        type = context.getType();
        definition = context.getDefinition();
        enabled = context.isEnabled();
        createdBy = context.getCreatedBy() == null? null: context.getCreatedBy().getId();
        modifiedBy = context.getModifiedBy() == null? null: context.getModifiedBy().getId();

        correlatedTo = context.getCorrelatedTo().stream().map(ContextToContextMap::getId).collect(Collectors.toSet());
        correlatedFrom = context.getCorrelatedFrom().stream().map(ContextToContextMap::getId).collect(Collectors.toSet());
        alerts = context.getAlerts().stream().map(Alert::getId).collect(Collectors.toSet());
        widgets = context.getWidgets().stream().map(Widget::getId).collect(Collectors.toSet());
    }

    private long id;

    private String name;

    private String type;

    private String definition;

    private boolean enabled;

    private Long createdBy;

    private Long modifiedBy;

    private Set<Long> correlatedTo;

    private Set<Long> correlatedFrom;

    private Set<Long> alerts;

    private Set<Long> widgets;
}
