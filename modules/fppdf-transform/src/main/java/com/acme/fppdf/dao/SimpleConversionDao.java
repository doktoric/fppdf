package com.acme.fppdf.dao;

import org.springframework.stereotype.Component;

import com.acme.fppdf.domain.Conversion;

/**
 * Conversion class dao layer
 * 
 * @author Richard_Doktorics
 * 
 */
@Component
public class SimpleConversionDao extends SimpleAbstractJpaDao<Conversion>
        implements ConversionDao {
    /**
     * Constructor
     */
    public SimpleConversionDao() {
        setClazz(Conversion.class);
    }

}
