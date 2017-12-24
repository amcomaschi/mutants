package ar.com.mercadolibre.mutants.request;

import ar.com.mercadolibre.mutants.handler.Validable;

public class EmptyRequest implements Validable {
    @Override
    public boolean isValid() {
        return true;
    }
}
