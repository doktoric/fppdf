package com.acme.fppdf.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Abstract dao to simple persist methods
 * 
 * @author Richard_Doktorics
 * 
 * @param <T>
 *            generic parameter
 */
public class SimpleAbstractJpaDao<T> implements AbstractJpaDao<T> {
    /**
     * the entitymanager to persisting.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * the generic class.
     */
    private Class<T> clazz;

    /*
     * (non-Javadoc)
     * 
     * @see com.acme.doktoric.presenter.dal.dao.IAbstractJpaDAO#setClazz(java.lang .Class)
     */

    @Override
    public void setClazz(final Class<T> clazzToSet) {
        this.clazz = clazzToSet;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.acme.doktoric.presenter.dal.dao.IAbstractJpaDAO#findOne(java.lang .Long)
     */

    @Override
    public T findOne(final Long id) {
        return entityManager.find(clazz, id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.acme.doktoric.presenter.dal.dao.IAbstractJpaDAO#findAll()
     */

    @Override
    public List<T> findAll() {
        return entityManager.createQuery("from " + clazz.getName()).getResultList();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.acme.doktoric.presenter.dal.dao.IAbstractJpaDAO#save(T)
     */

    @Override
    public void save(T entity) {
        entityManager.persist(entity);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.acme.doktoric.presenter.dal.dao.IAbstractJpaDAO#update(T)
     */
    @Override
    public void update(T entity) {
        entityManager.merge(entity);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.acme.doktoric.presenter.dal.dao.IAbstractJpaDAO#delete(T)
     */

    @Override
    public void delete(T entity) {
        entityManager.remove(entity);
    }
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    protected void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}

