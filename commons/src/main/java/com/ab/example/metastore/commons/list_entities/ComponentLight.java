package com.ab.example.metastore.commons.list_entities;


import com.ab.example.metastore.commons.entities.Component;
import com.ab.example.metastore.commons.entities.Widget;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author _amal
 *
 * Light weight representation of Component entity.
 */
@SuppressWarnings("PMD.ImmutableField")         //Added to avoid PMD warnings for non-final declaration of fields.
@Getter
@Setter
@NoArgsConstructor
public class ComponentLight {

    public ComponentLight(final Component component) {
        id = component.getId();
        name = component.getName();
        version = component.getVersion();
        widgets = component.getWidgets().stream().map(Widget::getId).collect(Collectors.toSet());
    }

    private long id;
    private String name;
    private String version;
    private Set<Long> widgets;
}
