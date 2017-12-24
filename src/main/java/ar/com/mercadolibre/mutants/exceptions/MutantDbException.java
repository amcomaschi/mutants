package ar.com.mercadolibre.mutants.exceptions;

public class MutantDbException extends MutantException {

    private static final long serialVersionUID = 2329872367824962542L;

    public MutantDbException(String message, int code) {
        super(message, code);
    }
}
