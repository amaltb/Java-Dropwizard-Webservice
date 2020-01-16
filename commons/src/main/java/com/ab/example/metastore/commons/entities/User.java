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
 * User entity defines a user in metastore.
 */

@Entity
@Table(name = Constants.USER_TABLE_NAME)
@Getter
@Setter
public class User extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 2L;
    private static final Logger LOGGER = LoggerFactory.getLogger(User.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "user_name", unique = true)
    private String userName;

    @Column(name = "enabled")
    private Boolean enabled;

    // foreign key to user profile table (one to many mapping)
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserProfile> userProfileSet;


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
        if(that instanceof User)
        {
            final User temp = (User) that;
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
