package ar.com.mercadolibre.mutants.services;

import ar.com.mercadolibre.mutants.exceptions.MutantDbException;
import ar.com.mercadolibre.mutants.model.Dna;

public interface DbService {

    public void addDNASequence(Dna dna) throws MutantDbException;

    public long getCountMutantsDNA() throws MutantDbException;

    long getTotal() throws MutantDbException;
}
