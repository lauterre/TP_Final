package matriz;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lector.exceptions.ArchivoNoEncontradoException;
import lector.exceptions.CSVParserException;
import celda.Celda;
import celda.CeldaBoolean;
import celda.CeldaNum;
import celda.CeldaString;
import columna.Columna;
import columna.ColumnaBoolean;
import columna.ColumnaNum;
import columna.ColumnaString;
import etiqueta.Etiqueta;
import etiqueta.EtiquetaString;

public class Tabla {
    List<Columna> columnas;
    Map <Etiqueta, Integer> colLabels;
    Map <Etiqueta, Integer> rowLabels;

    public Tabla(int cantidadColumnas){
        // TODO : Exceptions
        columnas = new ArrayList<>();
        colLabels = new HashMap<>(cantidadColumnas, cantidadColumnas); //new HashMap<>();
        rowLabels = new HashMap<>(cantidadColumnas, cantidadColumnas);
    }

    public Tabla(int cantidadColumnas, Etiqueta[] etiquetas) {
        this(cantidadColumnas);
        if (cantidadColumnas != etiquetas.length)
            throw new IllegalArgumentException("La longitud de etiquetas no coincide.");
        setEtiquetasColumnas(etiquetas);
    }

    // public Tabla(String ruta, boolean tieneEncabezados) throws ArchivoNoEncontradoException, CSVParserException {
    //     this(parserLineas(leer(ruta)));

    // }

    public Tabla(Object[][] matriz) {
        //TODO
    }

    public void setEtiquetasFilas(Etiqueta[] etiquetas) {
        rowLabels.clear();
        for(int i=0; i < columnas.get(0).size(); i++) {
            rowLabels.put(etiquetas[i], i);
        }
    }

    public void setEtiquetasColumnas(Etiqueta[] etiquetas) {
        colLabels.clear();
        for(int j=0; j < columnas.size(); j++) {
            colLabels.put(etiquetas[j], j);
        }
    }

    public String obtenerEtiquetasColumnas(){
        String etiquetasColumnas = "";
        for (Etiqueta etiqueta : colLabels.keySet()){
            etiquetasColumnas += etiqueta + ", ";
        }
        return etiquetasColumnas;
    }

    public String obtenerEtiquetasFilas(){
        String etiquetasFilas = "";
        for (Etiqueta etiqueta : rowLabels.keySet()){
            etiquetasFilas += etiqueta + ", ";
        }
        return etiquetasFilas;
    }

    public Celda obtenerCelda(Etiqueta etiquetaFila, Etiqueta etiquetaColumna) {
        return columnas.get(colLabels.get(etiquetaColumna)).obtenerValor(rowLabels.get(etiquetaFila)); 
    }

    public Celda cambiarValor(Etiqueta etiquetaFila, Etiqueta etiquetaColumna, Celda valor) {
        Celda celdaBorrada = obtenerCelda(etiquetaFila, etiquetaColumna);
        columnas.get(colLabels.get(etiquetaColumna)).fijarValor(rowLabels.get(etiquetaFila), valor);

        return celdaBorrada; 
    }

    public int obtenerCantidadFilas(){
        int cantidadFilas  = columnas.get(0).size();
        return cantidadFilas;
    }

    public int obtenerCantidadColumnas(){
        int cantidadColumnas = columnas.size();
        return cantidadColumnas;
    }

    public Columna obtenerColumna(Etiqueta etiquetaColumna){
        Columna columnaPedida = columnas.get(colLabels.get(etiquetaColumna));
        return columnaPedida;
    }

    public void ordenar(Etiqueta etiquetaColumna, String orden) {
        Columna columna = columnas.get(colLabels.get(etiquetaColumna));
        columna.ordenar(orden);
    }

    @Override
    public String toString() {
        String out = "  | ";
        String sep = " | ";
        for(Etiqueta label : colLabels.keySet()) {
            out += label + sep;
        }
        out += "\n";
        for(Etiqueta fila : rowLabels.keySet()) {
            out += fila + sep;
            for(Etiqueta columna : colLabels.keySet()) {
                out += obtenerCelda(fila, columna);
                out += sep;
            }
            out += "\n";
        }
        return out;
    }

    public Tabla(String rutaArchivo, boolean tieneEncabezados) throws CSVParserException, ArchivoNoEncontradoException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            List<String> lineas = new ArrayList<>();
            while ((linea = bufferedReader.readLine()) != null) {
                lineas.add(linea);
            }
            int cantidadColumnas = lineas.get(0).split(",").length;
            List<List<String>> columnas = new ArrayList<>();

            for (int i = 0; i < cantidadColumnas; i++) {
                columnas.add(new ArrayList<>());
            }
            for (int l = 0; l < lineas.size(); l++) {
                String[] campos = lineas.get(l).split(",");
                if (l == 0) {
                    Etiqueta[] etiquetas = new Etiqueta[campos.length];
                    for (int campo = 0; campo < campos.length; campo++){
                        EtiquetaString etiqueta = new EtiquetaString(campos[campo]);
                        etiquetas[campo] = etiqueta;
                    }
                }
                if (campos.length != cantidadColumnas) {
                    throw new CSVParserException();
                }

                for (int i = 0; i < cantidadColumnas; i++) {
                    columnas.get(i).add(campos[i]);
                }
            }
            
            List<Columna> cols = new ArrayList<>();
            for (List<String> columna : columnas) {
                if (tieneEncabezados) {
                    columna.remove(0);
                }
                if (esNum(columna.get(1))) {
                    List<CeldaNum> colNum = new ArrayList<>();
                    for(String celda : columna){
                        int numero = Integer.parseInt(celda);
                        CeldaNum celdaNum = new CeldaNum(numero);
                        colNum.add(celdaNum);
                    }
                    ColumnaNum col = new ColumnaNum(colNum);
                    cols.add(col);
                } else if (esBool(columna.get(1))) {
                    List<CeldaBoolean> colBool = new ArrayList<>();
                    for(String celda : columna){
                        boolean booleano = Boolean.parseBoolean(celda);
                        CeldaBoolean celdaBool = new CeldaBoolean(booleano);
                        colBool.add(celdaBool);
                    }
                    ColumnaBoolean col = new ColumnaBoolean(colBool);
                    cols.add(col);
                } else {
                    List<CeldaString> colString = new ArrayList<>();
                    for(String celda : columna){
                        CeldaString celdaString = new CeldaString(celda);
                        colString.add(celdaString);
                    }
                    ColumnaString col = new ColumnaString(colString);
                    cols.add(col);
                }

            this.columnas = cols;
            this.rowLabels = new HashMap<>();
            
        }
        } catch (IOException e) {
            throw new ArchivoNoEncontradoException();
        }

    }

    private static boolean esNum(String cadena) {
        return cadena.matches("-?\\d+(\\.\\d+)?");
    }

    private static boolean esBool(String cadena) {
        //TODO: tambien con 0 y 1
        return cadena.equalsIgnoreCase("true") || cadena.equalsIgnoreCase("false");
    }

}
