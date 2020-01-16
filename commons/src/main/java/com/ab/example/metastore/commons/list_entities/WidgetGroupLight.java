package com.ab.example.metastore.commons.list_entities;

import com.ab.example.metastore.commons.entities.Widget;
import com.ab.example.metastore.commons.entities.WidgetGroup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author _amal
 *
 * Light weight representation of WidgetGroup entity.
 */
@SuppressWarnings("PMD.ImmutableField")         //Added to avoid PMD warnings for non-final declaration of fields.
@Getter
@Setter
@NoArgsConstructor
public class WidgetGroupLight {

    public WidgetGroupLight(final WidgetGroup widgetGroup) {
        id = widgetGroup.getId();
        name = widgetGroup.getName();
        caption = widgetGroup.getCaption();
        description = widgetGroup.getDescription();
        enabled = widgetGroup.isEnabled();
        dashboardId = widgetGroup.getDashboard() == null? null: widgetGroup.getDashboard().getId();
        widgets = widgetGroup.getWidgets().stream().map(Widget::getId).collect(Collectors.toSet());
    }

    private long id;

    private String name;

    private String caption;

    private String description;

    private boolean enabled;

    private Long dashboardId;

    private Set<Long> widgets;
}
