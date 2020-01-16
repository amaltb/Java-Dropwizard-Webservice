package com.ab.example.metastore.commons.entities;

import com.ab.example.metastore.commons.util.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author _amal
 *
 * Permission entity defines a permission associated with a user role in metastore.
 */
@Entity
@Table(name = Constants.PERMISSION_TABLE_NAME)
@Getter
@Setter
public class Permission extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 22L;
    private static final Logger LOGGER = LoggerFactory.getLogger(Role.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private long id;

    @Column(name = "permissions")
    private String permissions;

    @Column(name = "enabled")
    private boolean enabled;

    /**
     * many to many mapping bw permission and role (one permission can be added to multiple roles and vice versa),
     * ignoring it here bcs not required as part of permission definition.
     *
     * Uni-directional relation from Role side.
     */
    @JsonIgnore
    @ManyToMany(targetEntity = Role.class, mappedBy = "permissions",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Role> roles;


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Long.toString(id).hashCode();
        return result;
    }

    @Override
    public boolean equals(Object that) {
        if(this == that)
        {
            return true;
        }
        if(that == null)
        {
            return false;
        }
        if(that instanceof Permission)
        {
            final Permission temp = (Permission) that;
            return this.id == temp.id;
        }
        else
        {
            return false;
        }
    }

    @Override
    public String toString() {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            LOGGER.error("unable to compute toString due to exception: ", e);
            return String.format("{ id: %s, obj: %s}", id, super.toString());
        }
    }
}
