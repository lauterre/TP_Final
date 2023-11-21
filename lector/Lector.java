package lector;

import java.util.List;

import etiqueta.Etiqueta;
import lector.exceptions.ArchivoNoEncontradoException;

public abstract class Lector {
    private List<Etiqueta> encabezados;
    private List<Etiqueta> encabezadosFilas;

    public abstract List<String> leer(String ruta) throws ArchivoNoEncontradoException;

    public static boolean esNum(String cadena) {
        return cadena.matches("-?\\d+(\\.\\d+)?");
    }

    public static boolean esBool(String cadena) {
        return cadena.equalsIgnoreCase("true") || cadena.equalsIgnoreCase("false");
    }

    public List<Etiqueta> getEncabezados() {
        return encabezados;
    }

    public List<Etiqueta> getEncabezadosFilas() {
        return encabezadosFilas;
    }

}
