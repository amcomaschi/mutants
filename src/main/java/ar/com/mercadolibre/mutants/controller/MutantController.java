package ar.com.mercadolibre.mutants.controller;

import ar.com.mercadolibre.mutants.exceptions.GenericExceptionHandler;
import ar.com.mercadolibre.mutants.exceptions.MutantServiceException;
import ar.com.mercadolibre.mutants.handler.IsMutantHandler;
import ar.com.mercadolibre.mutants.services.impl.MutantServiceImpl;
import spark.Spark;

import static spark.Spark.post;

public class MutantController {

    public MutantController(MutantServiceImpl mutantService) {

        post("/mutants/", new IsMutantHandler(mutantService));

        Spark.exception(MutantServiceException.class, new GenericExceptionHandler(500, "server.error.mutant.service"));
    }
}
