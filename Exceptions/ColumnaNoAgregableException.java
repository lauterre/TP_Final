package Exceptions;

public class ColumnaNoAgregableException extends Exception {
    public ColumnaNoAgregableException() {
        super("La cantidad de celdas de la columna no coincide con el de la tabla");
    }
}
