package ar.com.mercadolibre.mutants;

import ar.com.mercadolibre.mutants.detector.matrix.MatrixAlgorithm;
import ar.com.mercadolibre.utils.DataGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MatrixAlgorithmTest {

    @Test
    void testIsNotMutant() {

        String[] dna = DataGenerator.generateDna(6, Boolean.FALSE);
        MatrixAlgorithm ma = new MatrixAlgorithm();

        boolean ismutant = ma.analyzeDNA(dna);

        assertFalse(ismutant);
    }

    @Test
    void testIsMutant() {
        String[] dna = DataGenerator.generateDna(6, Boolean.TRUE);
        MatrixAlgorithm ma = new MatrixAlgorithm();

        boolean ismutant =ma.analyzeDNA(dna);

        assertTrue(ismutant);
    }


}
