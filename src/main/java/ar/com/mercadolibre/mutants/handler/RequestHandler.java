package ar.com.mercadolibre.mutants.handler;

import ar.com.mercadolibre.mutants.exceptions.HandlerException;

import java.util.Map;

public interface RequestHandler<V extends Validable> {

    Answer process(V value, Map<String, String> urlParams) throws HandlerException;
}
