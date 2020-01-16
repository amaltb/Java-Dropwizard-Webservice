package com.ab.example.metastore.commons.entities;

import com.ab.example.metastore.commons.util.Constants;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author _amal
 *
 * Widget entity represents a widget inside a template widget group in metastore. There is a list entity called
 * WidgetLight defined inside list_entities package. WidgetLight is a light weight
 * representation of a widget by trimming all the foreign references to respective object ids.
 */
@Entity
@Table(name = Constants.WIDGET_TABLE_NAME)
@Getter
@Setter
@JsonIgnoreProperties("inspection")
public class Widget extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 26L;
    private static final Logger LOGGER = LoggerFactory.getLogger(Widget.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "caption")
    private String caption;

    @Column(name = "height")
    private double height;

    @Column(name = "width")
    private double width;

    @Column(name = "position")
    private String position;

    @Column(name = "enabled")
    private boolean enabled;

    /**
     * ManyToOne mapping with WidgetGroup. Reference to the widget group that contains this widget. (A widget is always
     * is part of a widget group.)
     *
     * Bi-directional relation.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "widget_group_id")
    @JsonBackReference(value = "widgetGroup")
    private WidgetGroup widgetGroup;


    /**
     * ManyToOne mapping with Component. Reference to the component used to render the resource inside this widget.
     *
     * Uni-directional relation.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "component_id")
    private Component component;

    /**
     * ManyToOne mapping with Context. Reference to the metric context serving the data for this widget.
     *
     * Uni-directional relation.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "metric_context_id")
    private Context context;


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
        if(that instanceof Widget)
        {
            final Widget temp = (Widget) that;
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
