package ar.com.mercadolibre.mutants.services;

import ar.com.mercadolibre.mutants.exceptions.MutantServiceException;

public interface MutantService extends Service {

    boolean isMutant(String[] dna) throws MutantServiceException;

}
