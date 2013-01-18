package com.acme.fppdf.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.acme.fppdf.dao.ConversionDao;
import com.acme.fppdf.domain.Conversion;

/**
 * implement the conversionservice interface
 * 
 * @author Richard_Doktorics
 * 
 */
@Component
@Transactional
public class SimpleConversionService implements ConversionService {

    private Logger logger = LoggerFactory.getLogger(ConversionService.class);

    @Autowired
    private ConversionDao conversionDao;

    @Override
    public void saveConversion(Conversion conversion) {
        conversionDao.save(conversion);
    }

    @Override
    public List<Conversion> findAll() {
        return conversionDao.findAll();
    }

    @Override
    public boolean remove(long id) {
        boolean isOk = true;
        try {
            Conversion conversion = conversionDao.findOne(id);
            conversionDao.delete(conversion);
        } catch (Exception e) {
            logger.error("Null the removed object");
            isOk = false;
        }

        return isOk;
    }

    @Override
    public Conversion findObject(long id) {
        Conversion conversion = null;
        conversion = conversionDao.findOne(id);
        return conversion;

    }
}
