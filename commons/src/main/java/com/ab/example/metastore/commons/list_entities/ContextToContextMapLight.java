package com.ab.example.metastore.commons.list_entities;

import com.ab.example.metastore.commons.entities.ContextToContextMap;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author _amal
 *
 * Light weight representation of ContextToContextMap entity.
 */
@SuppressWarnings("PMD.ImmutableField")         //Added to avoid PMD warnings for non-final declaration of fields.
@Getter
@Setter
@NoArgsConstructor
public class ContextToContextMapLight {

    public ContextToContextMapLight(final ContextToContextMap contextToContextMap) {
        id = contextToContextMap.getId();
        fromContext = new ContextLight(contextToContextMap.getFromContext());
        toContext = new ContextLight(contextToContextMap.getToContext());
        correlationId = contextToContextMap.getCorrelation().getId();
    }

    private long id;

    private ContextLight fromContext;

    private ContextLight toContext;

    private long correlationId;
}
