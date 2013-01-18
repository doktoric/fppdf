package com.acme.fppdf.service;

import java.util.List;

import com.acme.fppdf.domain.Conversion;

/**
 * Service class to dao layer
 * 
 * @author Richard_Doktorics
 * 
 */
public interface ConversionService {

    /**
     * Simple persist method
     * 
     * @param conversion the element that we want to persisting
     */
    void saveConversion(Conversion conversion);

    /**
     * get all converted object
     * 
     * @return list with objects
     */
    List<Conversion> findAll();

    /**
     * find object by id
     * 
     * @param id the selected object
     * @return the object with paramter id
     */
    Conversion findObject(long id);

    /**
     * remove object in database
     * 
     * @param id parameter id in database
     * @return the was success or not
     */
    boolean remove(long id);
}
