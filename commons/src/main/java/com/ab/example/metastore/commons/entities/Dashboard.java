package com.ab.example.metastore.commons.entities;

import com.ab.example.metastore.commons.util.Constants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
 * Dashboard entity represents a dashboard in metastore. There is a list entity called
 * DashboardLight defined inside list_entities package. DashboardLight is a light weight
 * representation of a dashboard definition by trimming all the foreign references to respective object ids.
 */

@Entity
@Table(name = Constants.DASHBOARD_TABLE_NAME)
@Getter
@Setter
public class Dashboard extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 23L;
    private static final Logger LOGGER = LoggerFactory.getLogger(Dashboard.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dashboard_id")
    private long id;

    @Column(name = "dashboard_name")
    private String dashboardName;

    @Column(name = "description")
    private String desc;

    /** comma separated string */
    @Column(name = "tags")
    private String tags;

    @Column(name = "enabled")
    private boolean enabled;

    /**
     * Reference to the owner of this dashboard.
     *
     * Uni-directional relation.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
    CascadeType.REFRESH})
    @JoinColumn(name = "created_by")
    private UserProfile createdBy;

    /**
     * Reference to the latest modifier of this dashboard.
     *
     * Uni-directional relation.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
            CascadeType.REFRESH})
    @JoinColumn(name = "modified_by")
    private UserProfile modifiedBy;

    /**
     * Many to Many mapping with UserProfile. This gives the set of UserProfiles subscribed to this dashboard.
     *
     * Uni-directional relation.
     */
    @ManyToMany(targetEntity = UserProfile.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            mappedBy = "dashboardsSubscribed")
    @JsonIgnoreProperties({"dashboardsSubscribed"})
    private Set<UserProfile> usersSubscribed;

    /**
     * OneToMany mapping with WidgetGroup entity. Represents set of all widget-groups used in this dashboard.
     *
     * Bi-directional relation.
     */
    @OneToMany(mappedBy = "dashboard", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<WidgetGroup> widgetGroups;


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
        if(that instanceof Dashboard)
        {
            final Dashboard temp = (Dashboard) that;
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
