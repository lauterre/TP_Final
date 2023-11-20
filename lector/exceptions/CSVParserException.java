package lector.exceptions;

public class CSVParserException extends Exception {
    public CSVParserException() {
        super("Error en el archivo CSV");
    }

    public CSVParserException(int l) {
        super("Error en el archivo CSV en la linea " + l);
    }
}
