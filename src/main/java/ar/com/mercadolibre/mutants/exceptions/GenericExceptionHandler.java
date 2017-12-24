package ar.com.mercadolibre.mutants.exceptions;

import spark.ExceptionHandler;
import spark.Request;
import spark.Response;

public class GenericExceptionHandler implements ExceptionHandler {

    private final int status;
    private final String message;

    public GenericExceptionHandler(int status, String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * Invoked when an exception that is mapped to this handler occurs during routing
     *
     * @param exception The exception that was thrown during routing
     * @param request   The request object providing information about the HTTP request
     * @param response  The response object providing functionality for modifying the response
     */
    @Override
    public void handle(Exception exception, Request request, Response response) {
        response.status(status);
        response.body(message);
    }
}
