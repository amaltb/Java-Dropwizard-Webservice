package com.ab.example.metastore.commons.entities;

import com.ab.example.metastore.commons.util.Constants;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
 * WidgetGroup entity represents a widget group inside a dashboard template in metastore. There is a list entity called
 * WidgetGroupLight defined inside list_entities package. WidgetGroupLight is a light weight
 * representation of a widget group by trimming all the foreign references to respective object ids.
 */

@Entity
@Table(name = Constants.WIDGET_GROUP_TABLE_NAME)
@Getter
@Setter
public class WidgetGroup extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 25L;
    private static final Logger LOGGER = LoggerFactory.getLogger(WidgetGroup.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "caption")
    private String caption;

    @Column(name = "description")
    private String description;

    @Column(name = "enabled")
    private boolean enabled;

    /**
     * Many to one mapping with Dashboard entity. Widget group is always part of a Dashboard.
     * Reference to the parent dashboard for this widget group.
     *
     * Bi-directional relation.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "dashboard_id")
    @JsonBackReference
    private Dashboard dashboard;

    /**
     * One to many mapping with Widget entity. One widget group always contains one or more widgets.
     *
     * Bi-directional relation.
     */
    @OneToMany(mappedBy = "widgetGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "widgetGroup")
    private Set<Widget> widgets;

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
        if(that instanceof WidgetGroup)
        {
            final WidgetGroup temp = (WidgetGroup) that;
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
