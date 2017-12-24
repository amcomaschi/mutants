package ar.com.mercadolibre.mutants.response;

public class ErrorResponse {

    private String message;

    public ErrorResponse(String message, String... args) {
        this.message = String.format(message, args);
    }

    public ErrorResponse(Exception e) {
        this.message = e.getMessage();
    }

    public String getMessage() {
        return this.message;
    }
}
