package com.ab.example.metastore.commons.list_entities;

import com.ab.example.metastore.commons.entities.Widget;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author _amal
 *
 * Light weight representation of Widget entity.
 */
@SuppressWarnings("PMD.ImmutableField")         //Added to avoid PMD warnings for non-final declaration of fields.
@Getter
@Setter
@NoArgsConstructor
public class WidgetLight {

    public WidgetLight(final Widget widget) {
        id = widget.getId();
        name = widget.getName();
        description = widget.getDescription();
        caption = widget.getCaption();
        height = widget.getHeight();
        width = widget.getWidth();

        position = widget.getPosition();
        enabled = widget.isEnabled();

        widgetGroup = widget.getWidgetGroup() == null? null: widget.getWidgetGroup().getId();
        component = widget.getComponent() == null? null: widget.getComponent().getId();
        context = widget.getContext() == null? null: widget.getContext().getId();
    }

    private long id;

    private String name;

    private String description;

    private String caption;

    private double height;

    private double width;

    private String position;

    private boolean enabled;

    private Long widgetGroup;

    private Long component;

    private Long context;
}
