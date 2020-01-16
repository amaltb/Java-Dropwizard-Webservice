package com.ab.example.metastore.service.dao;

import com.expedia.www.doppler.metastore.commons.entities.User;
import com.expedia.www.doppler.metastore.commons.entities.UserProfile;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author _amal
 *
 * Dao layer for User entity
 */
public class UserDao extends AbstractDAO<User> {
    public UserDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public User create(final User user) {
        return persist(user);
    }

    public void update(final User user) {
        currentSession().saveOrUpdate(user);
    }

    public User find(final long id) {
        return currentSession().get(User.class, id);
    }

    public User findByName(final String userName) {

        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<User> criteria = builder.createQuery(User.class);
        final Root<User> root = criteria.from(User.class);

        final Predicate nameCheck = builder.equal(root.get("userName"), userName);
        criteria.where(nameCheck);
        criteria.select(root);
        return currentSession().createQuery(criteria).getSingleResult();
    }

    public void delete(final long id) {
        final User user = currentSession().get(User.class, id);
        currentSession().delete(user);
    }

    public List<User> findAll() {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<User> criteria = builder.createQuery(User.class);
        final Root<User> root = criteria.from(User.class);
        criteria.select(root);
        return currentSession().createQuery(criteria).getResultList();
    }

    public UserProfile findActiveProfileForUser(String userName) {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<User> criteria = builder.createQuery(User.class);
        final Root<User> root = criteria.from(User.class);

        final Predicate nameCheck = builder.equal(root.get("userName"), userName);
        criteria.where(nameCheck);

        final User user =  currentSession().createQuery(criteria.select(root)).getSingleResult();

        for (final UserProfile profile: user.getUserProfileSet()) {
            if(profile.isEnabled())
            {
                return profile;
            }
        }

        throw new RuntimeException(String.format("Unable to fetch an active profile for given user: %s", userName));
    }
}
