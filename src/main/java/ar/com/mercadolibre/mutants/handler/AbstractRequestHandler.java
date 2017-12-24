package ar.com.mercadolibre.mutants.handler;

import ar.com.mercadolibre.mutants.exceptions.HandlerException;
import ar.com.mercadolibre.mutants.services.Service;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Map;

public abstract class AbstractRequestHandler<V extends Validable> implements RequestHandler<V>, Route {

    private Class<V> valueClass;
    protected Service service;

    private static final int HTTP_BAD_REQUEST = 400;

    public AbstractRequestHandler(Class<V> valueClass, Service service) {
        this.valueClass = valueClass;
        this.service = service;
    }


    public final Answer process(V value, Map<String, String> queryParams) throws HandlerException {
        if (value != null && !value.isValid()) {
            return new Answer(HTTP_BAD_REQUEST);
        } else {
            return processImpl(value, queryParams);
        }
    }

    protected abstract Answer processImpl(V value, Map<String, String> queryParams) throws HandlerException;


    @Override
    public Object handle(Request request, Response response) throws Exception {

        V value = new Gson().fromJson(request.body(), valueClass);

        Answer answer = process(value, null);

        response.status(answer.getCode());
        response.type("application/json");
        response.body(answer.getBody());

        return answer.getBody();
    }

}