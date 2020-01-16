package com.ab.example.metastore.commons.entities;

import com.ab.example.metastore.commons.util.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

/**
 * @author _amal
 *
 * UserProfile entity defines all the profiles registered for all users in the system. One user can have more than
 * one profile if he/ she belongs to multiple team.
 */

@SuppressWarnings("PMD.TooManyFields")
@Entity
@Table(name = Constants.USER_PROFILE_TABLE_NAME)
@Setter
@Getter
public class UserProfile extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 3L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private long id;

    @Column(name = "bio")
    private String bio;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "lastLogIn")
    private final Timestamp lastLogIn = new Timestamp(System.currentTimeMillis());

    /**
     * many to one mapping with user table, one user may have more than one user profiles (in case of multiple teams and all)
     *
     * Bi-directional relation.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * foreign key to team table (one to one mapping), one user_profile is assumed to be associated with only one team
     * in case of user being part of multiple teams, it is assumed that multiple user_profiles will exist in metastore.
     *
     * Bi-directional relation.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "team_id")
    @JsonIgnoreProperties({"userProfileSet"})
    private Team team;

//    /**
//     * foreign key to role table (ManyToMany mapping), one user_profile might play multiple roles.
//     *
//     * Uni-directional relation.
//     */
//    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    @JoinColumn(name = "role_id")
//    private Role role;

    /**
     * One to many mapping with alert entity. Set of all alert-definitions created by this user. This set will not
     * contain alert-definitions that the user has subscribed as those are maintained in alert-subscription table.
     * Relation is one to many as one user can create multiple alerts.
     *
     * Uni-directional (ignoring from user_profile side)
     */
    @OneToMany(mappedBy = "createdBy", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @Column(nullable = true)
    @JsonIgnore
    private Set<Alert> alertsCreated;

    /**
     * One to many mapping with alert entity. Set of all alert-definitions modified by this user. one to many bcs one
     * user may modify multiple alert definitions.
     *
     * Uni-directional (ignoring from user_profile side)
     */
    @OneToMany(mappedBy = "modifiedBy",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @Column(nullable = true)
    @JsonIgnore
    private Set<Alert> alertsModified;

    /**
     * One to many mapping with dashboard entity. Set of all dashboards created by this user. Relation is one to many
     * as one user can create multiple dashboards.
     */
    @OneToMany(mappedBy = "createdBy", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @Column(nullable = true)
    @JsonIgnore
    private Set<Dashboard> dashboardsCreated;

    /**
     * One to many mapping with dashboard entity. Set of all dashboards modified by this user. Relation is one to many
     * as one user may modify multiple dashboards.
     *
     * Uni-directional (ignoring from user_profile side)
     */
    @OneToMany(mappedBy = "modifiedBy",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @Column(nullable = true)
    @JsonIgnore
    private Set<Dashboard> dashboardsModified;

    /**
     * Many to many mapping with dashboard entity. This is the set of all dashboards that are subscribed by this user.
     * it is a many to many mapping as one user can subscribe multiple dashboards and each dashboard can be subscribed by
     * multiple users. ignoring usersSubscribed to avoid infinite recursion.
     *
     * Uni-directional (ignoring from user_profile side)
     */
    @ManyToMany(targetEntity = Dashboard.class, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "USER_PROFILE_DASHOBOARD_SUBSCRIPTIONS", joinColumns = @JoinColumn(name = "profile_id"),
    inverseJoinColumns = @JoinColumn(name = "dashboard_id"))
    @JsonIgnore
    private Set<Dashboard> dashboardsSubscribed;


    /**
     * One to many mapping with AlertSubscription. This is the set of alert subscriptions that this user has created.
     * Transitively this is the set of alert definitions that this user has subscribed.
     *
     * Uni-directional (ignoring from user_profile side)
     */
    @OneToMany(mappedBy = "createdBy",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @Column(nullable = true)
    @JsonIgnore
    private Set<AlertSubscription> subscriptionsCreated;


    /**
     * One to many mapping with AlertSubscription. This is the set of alert subscriptions that this user has modified.
     *
     * Uni-directional (ignoring from user_profile side)
     */
    @OneToMany(mappedBy = "modifiedBy",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @Column(nullable = true)
    @JsonIgnore
    private Set<AlertSubscription> subscriptionsModified;


    /**
     * One to many mapping with Context entity. This is the set of metric contexts that this user has created.
     *
     * Uni-directional (ignoring from user_profile side)
     */
    @OneToMany(mappedBy = "createdBy",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @Column(nullable = true)
    @JsonIgnore
    private Set<Context> contextsCreated;


    /**
     * One to many mapping with Context entity. This is the set of metric contexts that this user has modified.
     *
     * Uni-directional (ignoring from user_profile side)
     */
    @OneToMany(mappedBy = "modifiedBy",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @Column(nullable = true)
    @JsonIgnore
    private Set<Context> contextsModified;


    /**
     * One to many mapping with ModelVersion entity. This is the set of model versions created by this user.
     *
     * Uni-directional (ignoring from user_profile side)
     */
    @OneToMany(mappedBy = "createdBy",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @Column(nullable = true)
    @JsonIgnore
    private Set<ModelVersion> modelVersionsCreated;


    /**
     * One to many mapping with ModelVersion entity. This is the set of model versions modified by this user.
     *
     * Uni-directional (ignoring from user_profile side)
     */
    @OneToMany(mappedBy = "modifiedBy",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @Column(nullable = true)
    @JsonIgnore
    private Set<ModelVersion> modelVersionsModified;


    /**
     * One to many mapping with Model entity. This is the set of models created by this user.
     *
     * Uni-directional (ignoring from user_profile side)
     */
    @OneToMany(mappedBy = "createdBy",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @Column(nullable = true)
    @JsonIgnore
    private Set<Model> modelsCreated;


    /**
     * One to many mapping with Model entity. This is the set of models modified by this user.
     *
     * Uni-directional (ignoring from user_profile side)
     */
    @OneToMany(mappedBy = "modifiedBy",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @Column(nullable = true)
    @JsonIgnore
    private Set<Model> modelsModified;


    /**
     * One to many mapping with Topic entity. This is the set of topics created in metastore by this user.
     *
     * Uni-directional (ignoring from user_profile side)
     */
    @OneToMany(mappedBy = "createdBy",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @Column(nullable = true)
    @JsonIgnore
    private Set<Topic> topicsCreated;


    /**
     * One to many mapping with Topic entity. This is the set of models modified by this user.
     *
     * Uni-directional (ignoring from user_profile side)
     */
    @OneToMany(mappedBy = "modifiedBy",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @Column(nullable = true)
    @JsonIgnore
    private Set<Topic> topicsModified;
}
