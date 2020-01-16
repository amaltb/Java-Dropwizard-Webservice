package com.ab.example.metastore.commons.list_entities;

import com.ab.example.metastore.commons.entities.ModelVersion;
import com.ab.example.metastore.commons.entities.Alert;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author _amal
 *
 * Light weight representation of ModelVersion entity.
 */
@SuppressWarnings("PMD.ImmutableField")         //Added to avoid PMD warnings for non-final declaration of fields.
@Getter
@Setter
@NoArgsConstructor
public class ModelVersionLight {
    public ModelVersionLight(final ModelVersion modelVersion) {
        id = modelVersion.getId();
        version = modelVersion.getVersion();
        createdBy = modelVersion.getCreatedBy() == null? null: new UserProfileLight(modelVersion.getCreatedBy());
        modifiedBy = modelVersion.getModifiedBy() == null? null : new UserProfileLight(modelVersion.getModifiedBy());
        model = new ModelLight(modelVersion.getModel());
        alertSet = modelVersion.getAlertSet().stream().map(Alert::getId).collect(Collectors.toSet());
    }

    private long id;

    private String version;

    private UserProfileLight createdBy;

    private UserProfileLight modifiedBy;

    private ModelLight model;

    private Set<Long> alertSet;
}
