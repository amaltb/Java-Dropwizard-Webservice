package com.ab.example.metastore.commons.entities;

import com.ab.example.metastore.commons.util.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author _amal
 *
 * Tenant entity represents a tenant registered in metastore. Each tenant is an organisational unit and act as the origin
 * of all the other resources in metastore.
 */

@Entity
@Table(name = Constants.TENANT_TABLE_NAME)
@Getter
@Setter
public class Tenant extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 5L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "tenant_name")
    private String tenantName;

    @Column(name = "email")
    private String email;

    @Column(name = "description")
    private String description;

    @Column(name = "enabled")
    private boolean enabled;

    // one to many mapping with Team table.
    @JsonIgnore
    @OneToMany(mappedBy = "tenant", cascade = CascadeType.ALL)
    private Set<Team> teamSet;
}
