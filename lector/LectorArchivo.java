package lector;

import java.util.List;

import columna.Columna;
import etiqueta.Etiqueta;
import lector.exceptions.ArchivoNoEncontradoException;
import lector.exceptions.CSVParserException;

public abstract class LectorArchivo {
    private Etiqueta[] encabezados;

    public abstract List<String> leer(String ruta) throws ArchivoNoEncontradoException;

    public abstract List<Columna> parserColumnas(List<String> lineas, boolean tieneEncabezados) throws CSVParserException;

    protected static boolean esNum(String cadena) {
        return cadena.matches("-?\\d+(\\.\\d+)?");
    }

    protected static boolean esBool(String cadena) {
        return cadena.equalsIgnoreCase("true") || cadena.equalsIgnoreCase("false");
    }

    protected static boolean esBool(int entero) {
        if (entero==0) {
            return false;
        } else {
            return true;
        }
    }

    protected Etiqueta[] getEncabezados() {
        return this.encabezados;
    } 

}