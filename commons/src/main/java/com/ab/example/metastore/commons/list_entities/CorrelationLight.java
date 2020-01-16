package com.ab.example.metastore.commons.list_entities;

import com.ab.example.metastore.commons.entities.Correlation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author _amal
 *
 * Light weight representation of Correlation entity.
 */
@SuppressWarnings("PMD.ImmutableField")         //Added to avoid PMD warnings for non-final declaration of fields.
@Getter
@Setter
@NoArgsConstructor
public class CorrelationLight
{
    public CorrelationLight(final Correlation correlation) {
        id = correlation.getId();
        name = correlation.getName();
        definition = correlation.getDefinition();
        contextToContextMaps = correlation.getContextToContextMaps().stream().map(ContextToContextMapLight::new)
                .collect(Collectors.toSet());
    }

    private long id;

    private String name;

    private String definition;

    private Set<ContextToContextMapLight> contextToContextMaps;
}
