package ar.com.mercadolibre.mutants;

import ar.com.mercadolibre.mutants.exceptions.MutantDbException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DbServiceTest extends BaseTest {

    @Test
    public void errorGettingCountOfMutantsDna() {
        shutDownDatabase();

        try {
            db.getCountMutantsDNA();
        } catch (MutantDbException e) {
            assertEquals(Boolean.TRUE, e.getMessage().contains("Error while getting count of mutants dna"));
        }
    }
}
