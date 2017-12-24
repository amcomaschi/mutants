package ar.com.mercadolibre.mutants.controller;


import ar.com.mercadolibre.mutants.exceptions.GenericExceptionHandler;
import ar.com.mercadolibre.mutants.exceptions.StatsServiceException;
import ar.com.mercadolibre.mutants.handler.StatsHandler;
import ar.com.mercadolibre.mutants.services.StatsService;
import spark.Spark;

import static spark.Spark.get;

public class StatsController {

    public StatsController(StatsService statsService) {

        get("/stats", new StatsHandler(statsService));

        Spark.exception(StatsServiceException.class, new GenericExceptionHandler(500, "server.error.statistics.service"));

    }
}