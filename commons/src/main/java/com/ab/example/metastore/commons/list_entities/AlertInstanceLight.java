package com.ab.example.metastore.commons.list_entities;

import com.ab.example.metastore.commons.entities.AlertInstance;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * @author _amal
 *
 * Light weight representation of AlertInstance for list endpoints.
 */
@SuppressWarnings("PMD.ImmutableField")         //Added to avoid PMD warnings for non-final declaration of fields.
@Getter
@Setter
@NoArgsConstructor
public class AlertInstanceLight {

    public AlertInstanceLight(final AlertInstance alertInstance) {
        id = alertInstance.getId();
        context = alertInstance.getContext();
        createdOn = alertInstance.getCreatedOn();
        status = alertInstance.isStatus();
        alert = alertInstance.getAlert().getId();
    }

    private long id;

    private String context;

    private Timestamp createdOn;

    private boolean status;

    private Long alert;
}
