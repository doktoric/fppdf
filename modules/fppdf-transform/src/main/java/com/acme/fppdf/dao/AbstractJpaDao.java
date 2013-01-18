package com.acme.fppdf.dao;

import java.util.List;

/**
 * abstract class to simple dao
 * 
 * @author Richard_Doktorics
 * 
 * @param <T>
 *            generic type
 */
public interface AbstractJpaDao<T> {

    /**
     * Setup the class to generic
     * 
     * @param clazzToSet
     *            the selected class
     */
    void setClazz(final Class<T> clazzToSet);

    /**
     * Find object by id.
     * 
     * @param id
     *            database id
     * @return the found object
     */
    T findOne(final Long id);

    /**
     * Find all object.
     * 
     * @return the found objects
     */
    List<T> findAll();

    /**
     * Persist paramter entity
     * 
     * @param entity
     *            the selected entity
     */
    void save(T entity);

    /**
     * update the parameter entity
     * 
     * @param entity
     *            the selected entity
     */
    void update(T entity);

    /**
     * Delete the paramter entity
     * 
     * @param entity
     *            the selected entity
     */
    void delete(T entity);

}
