package com.ab.example.metastore.commons.list_entities;

import com.ab.example.metastore.commons.entities.Model;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author _amal
 *
 * Light weight representation of Model entity.
 */
@SuppressWarnings("PMD.ImmutableField")         //Added to avoid PMD warnings for non-final declaration of fields.
@Getter
@Setter
@NoArgsConstructor
public class ModelLight {

    public ModelLight(final Model model) {
        id = model.getId();
        name = model.getName();
        alias = model.getAlias();
        modelConfiguration = model.getModelConfiguration();
        provider = model.getProvider();

        createdBy = model.getCreatedBy() == null? null: model.getCreatedBy().getId();
        modifiedBy = model.getModifiedBy() == null? null: model.getModifiedBy().getId();
        modelVersions = model.getModelVersions().stream().map(ModelVersionLight::new).collect(Collectors.toSet());
    }

    private long id;

    private String name;

    private String alias;

    private String modelConfiguration;

    private String provider;

    private Long createdBy;

    private Long modifiedBy;

    private Set<ModelVersionLight> modelVersions;
}
