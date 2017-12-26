package ar.com.mercadolibre.mutants.handler;


import ar.com.mercadolibre.mutants.exceptions.HandlerException;
import ar.com.mercadolibre.mutants.exceptions.MutantServiceException;
import ar.com.mercadolibre.mutants.request.MutantRequest;
import ar.com.mercadolibre.mutants.services.Service;
import ar.com.mercadolibre.mutants.services.MutantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class IsMutantHandler extends AbstractRequestHandler<MutantRequest> {

    private static final Logger logger = LoggerFactory.getLogger(IsMutantHandler.class);

    public IsMutantHandler(Service service) {
        super(MutantRequest.class, service);
        this.service = service;
    }

    @Override
    protected Answer processImpl(MutantRequest value, Map<String, String> urlParams) throws HandlerException {

        logger.info("Received request on /mutants/ path, dna to process --> " + value);

        boolean isMutant = false;


        try {
            isMutant = ((MutantService) service).isMutant(value.getDna());
        } catch (MutantServiceException e) {
            logger.error("Error processing IsMutant request ", e);
                throw new HandlerException(e.getMessage());
        }


        logger.info("Dna was analized and is " + (isMutant ? "mutant" : "human"));

        if (isMutant) {
            return new Answer(200, "");
        } else {
            return new Answer(403, "");
        }
    }
}
