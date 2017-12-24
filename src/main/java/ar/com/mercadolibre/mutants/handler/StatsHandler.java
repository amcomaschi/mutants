package ar.com.mercadolibre.mutants.handler;

import ar.com.mercadolibre.mutants.dto.Stats;
import ar.com.mercadolibre.mutants.exceptions.HandlerException;
import ar.com.mercadolibre.mutants.exceptions.StatsServiceException;
import ar.com.mercadolibre.mutants.request.EmptyRequest;
import ar.com.mercadolibre.mutants.services.Service;
import ar.com.mercadolibre.mutants.services.StatsService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class StatsHandler extends AbstractRequestHandler<EmptyRequest> {

    private static final Logger logger = LoggerFactory.getLogger(StatsHandler.class);

    public StatsHandler(Service service) {
        super(EmptyRequest.class, service);
    }

    @Override
    protected Answer processImpl(EmptyRequest value, Map<String, String> queryParams) throws HandlerException {

        logger.info("Received request on /stats/ path");
        Stats stats;
        try {
            stats = ((StatsService)service).getStats();
        } catch (StatsServiceException e) {
            logger.error("Error processing stats request ", e);
            throw new HandlerException(e.getMessage());
        }

        logger.info("request processed successfully, statistics: " + stats);

        return new Answer(200, new Gson().toJson(stats, Stats.class));
    }
}
