package ar.com.mercadolibre.mutants;

import ar.com.mercadolibre.mutants.handler.Answer;
import ar.com.mercadolibre.mutants.dto.Stats;
import ar.com.mercadolibre.mutants.exceptions.HandlerException;
import ar.com.mercadolibre.mutants.exceptions.MutantDbException;
import ar.com.mercadolibre.mutants.handler.StatsHandler;
import ar.com.mercadolibre.mutants.model.Dna;
import ar.com.mercadolibre.mutants.request.EmptyRequest;
import ar.com.mercadolibre.mutants.services.impl.StatsServiceImpl;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatsHandlerTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(StatsHandlerTest.class);

    @Test
    public void statisticsRetrivedSuccessfully() {

        StatsHandler statsHandler = new StatsHandler(new StatsServiceImpl(db));
        Stats stats = new Stats(1, 2, 0.33);
        insertData();

        try {
            Answer resp = statsHandler.process(new EmptyRequest(), null);

            logger.debug(resp.toString());
            logger.debug("Status code: " + resp.getCode());
            logger.debug("Body: " + resp.getBody());

            assertEquals(new Answer(200, new Gson().toJson(stats)), resp);
        } catch (HandlerException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void errorRetrivingCountOfDnaAnalized() {

        StatsHandler statsHandler = new StatsHandler(new StatsServiceImpl(db));
        shutDownDatabase();

        try {
            Answer resp = statsHandler.process(new EmptyRequest(), null);

        } catch (HandlerException e) {
            assertEquals(Boolean.TRUE, e.getMessage().contains("Error on stats service trying to get statistics"));
        }
    }

    private void insertData() {
        String[] dna1 = {"AAAA", "CCCC", "TTTT", "GGGG"};
        String[] dna2 = {"AAAC", "CCCA", "TTTG", "GGGT"};
        String[] dna3 = {"CAAA", "ACCC", "GTTT", "TGGG"};

        try {
            db.addDNASequence(new Dna(dna1, Boolean.TRUE));
            db.addDNASequence(new Dna(dna2, Boolean.FALSE));
            db.addDNASequence(new Dna(dna3, Boolean.FALSE));
        } catch (MutantDbException e) {
            e.printStackTrace();
        }
    }
}
