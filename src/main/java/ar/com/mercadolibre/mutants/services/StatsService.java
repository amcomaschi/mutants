package ar.com.mercadolibre.mutants.services;

import ar.com.mercadolibre.mutants.dto.Stats;
import ar.com.mercadolibre.mutants.exceptions.StatsServiceException;


public interface StatsService extends Service {

    Stats getStats() throws StatsServiceException;
}
