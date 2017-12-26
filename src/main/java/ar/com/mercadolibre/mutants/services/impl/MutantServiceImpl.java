package ar.com.mercadolibre.mutants.services.impl;

import ar.com.mercadolibre.mutants.detector.Detector;
import ar.com.mercadolibre.mutants.detector.gst.GeneralizedSuffixTree;
import ar.com.mercadolibre.mutants.exceptions.MutantDbException;
import ar.com.mercadolibre.mutants.exceptions.MutantServiceException;
import ar.com.mercadolibre.mutants.model.Dna;
import ar.com.mercadolibre.mutants.services.DbService;
import ar.com.mercadolibre.mutants.services.MutantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MutantServiceImpl implements MutantService {

    private static final Logger logger = LoggerFactory.getLogger(MutantServiceImpl.class);

    private DbService db;

    public MutantServiceImpl(){
        this.db = new DbServiceImpl();
    }

    public MutantServiceImpl(DbService dbService) {
        this.db = dbService;
    }

    public boolean isMutant(String[] dna) throws MutantServiceException {

        logger.info("Checking dna with mutant detector");

        Detector detector = new GeneralizedSuffixTree();
        boolean isMutant = detector.analyzeDNA(dna);

        logger.info("Dna processed, result: " + (isMutant? "mutant": "human"));

        try {

            logger.info("Invoking database service to save dna sequence");
            db.addDNASequence(new Dna(dna, isMutant));
            logger.info("Dna sequence saved successfully");

        } catch (MutantDbException e) {
            logger.error("Error ocurred invoking database service: " + e);

            if(e.getCode() != 11000) {
                throw new MutantServiceException("Error ocurred invoking database service: " + e.getMessage(), e.getCode());
            }
        }

        return isMutant;
    }
}
