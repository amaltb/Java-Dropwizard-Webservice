package com.ab.example.metastore.commons.entities;

import com.ab.example.metastore.commons.util.Constants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
 * Team entity represents a team registered in metastore.
 */

@Entity
@Table(name = Constants.TEAM_TABLE_NAME)
@Getter
@Setter
public class Team extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 4L;
    private static final Logger LOGGER = LoggerFactory.getLogger(Team.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "team_name")
    private String teamName;

    @Column(name = "description")
    private String description;

    @Column(name = "team_dl", unique = true)
    private String teamDl;

    @Column(name = "enabled")
    private boolean enabled;

    /**
     * OneToMany relationship with UserProfile.
     *
     * Bi-directional relation.
     */
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"team"})
    private Set<UserProfile> userProfileSet;

    /**
     * ManyToOne mapping with Tenant entity. Represents the organisational unit this team is part of.
     *
     * Uni-directional relation.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

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
        if(that instanceof Team)
        {
            final Team temp = (Team) that;
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
