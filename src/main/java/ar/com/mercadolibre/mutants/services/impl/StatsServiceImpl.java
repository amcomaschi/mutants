package ar.com.mercadolibre.mutants.services.impl;

import ar.com.mercadolibre.mutants.dto.Stats;
import ar.com.mercadolibre.mutants.exceptions.MutantDbException;
import ar.com.mercadolibre.mutants.exceptions.StatsServiceException;
import ar.com.mercadolibre.mutants.services.DbService;
import ar.com.mercadolibre.mutants.services.StatsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;

public class StatsServiceImpl implements StatsService {

    private static final Logger logger = LoggerFactory.getLogger(StatsServiceImpl.class);

    private DbService db;

    public StatsServiceImpl() {
        this.db = new DbServiceImpl();
    }

    public StatsServiceImpl(DbService dbService) {
        this.db = dbService;
    }

    @Override
    public Stats getStats() throws StatsServiceException {

        logger.info("Stats service invoked to retrieve statistics");

        long total;
        long mutants;
        double ratio = 0;

        try {
            total = db.getTotal();
            mutants = db.getCountMutantsDNA();

            if(total > 0) {
                DecimalFormat df = new DecimalFormat("#.##");
                String sRatio = df.format((mutants / (double) total));
                ratio = Double.valueOf(sRatio).doubleValue();
            }

            logger.debug("Statistics collected: [Total dna: " + total + ", mutants: " + (total - mutants) + ", ratio: " + ratio + "]");

        } catch (MutantDbException e) {
            logger.info("Error on stats service trying to get statistics");
            logger.error("Error on stats service trying to get statistics: " + e.getMessage());
            e.printStackTrace();
            throw new StatsServiceException("Error on stats service trying to get statistics");
        }


        return new Stats(mutants, (total - mutants), ratio);
    }
}
