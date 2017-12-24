package  ar.com.mercadolibre.mutants;

import ar.com.mercadolibre.mutants.controller.MutantController;
import ar.com.mercadolibre.mutants.controller.StatsController;
import ar.com.mercadolibre.mutants.exceptions.GenericExceptionHandler;
import ar.com.mercadolibre.mutants.response.ErrorResponse;
import ar.com.mercadolibre.mutants.services.impl.MutantServiceImpl;
import ar.com.mercadolibre.mutants.services.impl.StatsServiceImpl;
import com.google.gson.Gson;
import spark.Spark;

import static spark.Spark.exception;

public class MutantsApp {

    public static void main(String[] args) {
        new MutantController(new MutantServiceImpl());
        new StatsController(new StatsServiceImpl());

        Spark.exception(NullPointerException.class, new GenericExceptionHandler(500, "server.error.nullpointer1"));

        exception(IllegalArgumentException.class, (e, req, res) -> {
            res.status(400);
            res.body(new Gson().toJson((new ErrorResponse(e))));
        });
    }
}