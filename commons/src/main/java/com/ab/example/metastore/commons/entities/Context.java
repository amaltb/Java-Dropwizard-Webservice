package com.ab.example.metastore.commons.entities;

import com.ab.example.metastore.commons.util.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * Metric context entity represents context(root data source) in metastore. There is a list entity called
 * ContextLight defined inside list_entities package. ContextLight is a light weight
 * representation of a metric context by trimming all the foreign references to respective object ids.
 */
@Entity
@Table(name = Constants.METRIC_CONTEXT_TABLE_NAME)
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown=true, value = {"hibernateLazyInitializer", "handler"})
public class Context extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 28L;
    private static final Logger LOGGER = LoggerFactory.getLogger(Context.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "definition", columnDefinition = "varchar")
    private String definition;

    @Column(name = "enabled")
    private boolean enabled;

    /**
     * ManyToOne mapping with UserProfile entity. Represents the creator of this metric context.
     *
     * Uni-directional relation.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "created_by")
    private UserProfile createdBy;

    /**
     * ManyToOne mapping with UserProfile entity. Represents the user who most recently modified this context.
     *
     * Uni-directional relation.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "modified_by")
    private UserProfile modifiedBy;

    /**
     * One to many mapping with Widget entity. One metric context may be rendered by multiple widgets.
     *
     * Uni-directional relation from widget side.
     */
    @OneToMany(mappedBy = "context", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Widget> widgets;


    /**
     * One to many mapping with Alert entity. One metric context may be used in multiple alert definitions.
     *
     * Uni-directional relation from alert side.
     */
    @OneToMany(mappedBy = "context", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Alert> alerts;


    /**
     * One to many mapping with ContextToContextMap entity. Represents a set of metric contexts to which this metric
     * context is related. Ex: if booking context is related to checkout and traffic contexts, this field will be like
     * [map<Booking - checkout>, Map<Booking - Traffic>]
     *
     * Bi-directional relation.
     */
    @OneToMany(mappedBy = "fromContext", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
            CascadeType.REFRESH})
    @Column(nullable = true)
    @JsonManagedReference(value = "correlatedTo")
    private Set<ContextToContextMap> correlatedTo;


    /**
     * One to many mapping with ContextToContextMap entity. Represents a set of metric contexts which has a correlation
     * to this metric context. Ex: if checkout and traffic contexts are correlated to booking,
     * then in booking context you get [map<Traffic - Booking>, map<Checkout - Booking>]
     *
     * Uni-directional relation from ContextToContextMap side. Because we r more interested in contexts that are related
     * to this context and that is answered by the above field.
     */
    @OneToMany(mappedBy = "toContext", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
            CascadeType.REFRESH})
    @Column(nullable = true)
    @JsonIgnore
    private Set<ContextToContextMap> correlatedFrom;



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
        if(that instanceof Context)
        {
            final Context temp = (Context) that;
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
