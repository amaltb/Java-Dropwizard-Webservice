package com.ab.example.metastore.commons.entities;

import com.ab.example.metastore.commons.util.Constants;
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
 * Role entity defines an access role that a user can take in metastore.
 */
@Entity
@Table(name = Constants.ROLE_TABLE_NAME)
@Getter
@Setter
public class Role extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 21L;
    private static final Logger LOGGER = LoggerFactory.getLogger(Role.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private long id;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "description")
    private String desc;

    @Column(name = "enabled")
    private boolean enabled;

//    /**
//     * OneToMany mapping with UserProfile entity. Represents a set of user profiles holding this role.
//     *
//     * Uni-directional from UserProfile side.
//     */
//    @OneToMany(mappedBy = "role", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH,
//            CascadeType.REFRESH})
//    @JsonIgnore
//    private Set<UserProfile> profiles;

    /**
     * ManyToMany mapping with Permission entity. One role can have multiple permissions and vice versa.
     *
     * Uni-directional relation.
     */
    @ManyToMany(targetEntity = Permission.class, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "ROLE_PERMISSIONS", joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<Permission> permissions;

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
        if(that instanceof Role)
        {
            final Role temp = (Role) that;
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
