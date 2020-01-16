package com.ab.example.metastore.service.dao;

import com.expedia.www.doppler.metastore.commons.entities.UserProfile;
import com.expedia.www.doppler.metastore.commons.list_entities.UserProfileLight;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @author _amal
 *
 * Dao layer for UserProfile entity
 */
public class UserProfileDao extends AbstractDAO<UserProfile> {
    public UserProfileDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public UserProfile create(final UserProfile userProfile) {
        return persist(userProfile);
    }

    public void update(final UserProfile userProfile) {
        currentSession().saveOrUpdate(userProfile);
    }

    public UserProfile find(final long id) {
        return currentSession().get(UserProfile.class, id);
    }

    public void delete(final long id) {
        final UserProfile userProfile = currentSession().get(UserProfile.class, id);
        currentSession().delete(userProfile);
    }

    public List<UserProfileLight> findAll() {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<UserProfile> criteria = builder.createQuery(UserProfile.class);
        final Root<UserProfile> root = criteria.from(UserProfile.class);
        criteria.select(root);
        final List<UserProfile> userProfiles =  currentSession().createQuery(criteria).getResultList();
        final List<UserProfileLight> userProfileLights = new ArrayList<>();

        for (final UserProfile profile: userProfiles) {
            userProfileLights.add(new UserProfileLight(profile));
        }
        return userProfileLights;
    }
}
