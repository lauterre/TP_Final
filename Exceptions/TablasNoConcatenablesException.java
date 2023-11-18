package Exceptions;

public class TablasNoConcatenablesException extends Exception {
    public TablasNoConcatenablesException() {
        super("No es posible concatenar las tablas");
    }
}
