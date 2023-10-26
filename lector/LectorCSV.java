package lector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import columna.ColumnaBoolean;
import celda.CeldaBoolean;
import celda.CeldaNum;
import celda.CeldaString;
import columna.Columna;
import columna.ColumnaNum;
import columna.ColumnaString;
import etiqueta.Etiqueta;
import etiqueta.EtiquetaString;
import lector.exceptions.ArchivoNoEncontradoException;
import lector.exceptions.CSVParserException;

public class LectorCSV {
    public static List<String> leer(String ruta) throws ArchivoNoEncontradoException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(ruta))) {
            String linea;
            List<String> lineas = new ArrayList<>();
            while ((linea = bufferedReader.readLine()) != null) {
                lineas.add(linea);
            }
            return lineas;
        } catch (IOException e) {
            throw new ArchivoNoEncontradoException();
        }
    }
    public static List<Columna> parserColumnas(List<String> lineas, boolean encabezados) throws CSVParserException {
        int cantidadColumnas = lineas.get(0).split(",").length;
        List<List<String>> columnas = new ArrayList<>();

        for (int i = 0; i < cantidadColumnas; i++) {
            columnas.add(new ArrayList<>());
        }

        for (String linea : lineas) {
            String[] campos = linea.split(",");
            if (campos.length != cantidadColumnas) {
                throw new CSVParserException();
            }

            for (int i = 0; i < cantidadColumnas; i++) {
                columnas.get(i).add(campos[i]);
            }
        }
        
        List<Columna> cols = new ArrayList<>();
        for (List<String> columna : columnas) {
            if (encabezados) {
                EtiquetaString etiqueta = new EtiquetaString(columna.get(0));
                columna.remove(0);
                if (esNum(columna.get(1))) {
                    List<CeldaNum> colNum = new ArrayList<>();
                    for(String celda : columna){
                        int numero = Integer.parseInt(celda);
                        CeldaNum celdaNum = new CeldaNum(numero);
                        colNum.add(celdaNum);
                    }
                    ColumnaNum col = new ColumnaNum(etiqueta,colNum);
                    cols.add(col);
                } else if (esBool(columna.get(1))) {
                    List<CeldaBoolean> colBool = new ArrayList<>();
                    for(String celda : columna){
                        boolean booleano = Boolean.parseBoolean(celda);
                        CeldaBoolean celdaBool = new CeldaBoolean(booleano);
                        colBool.add(celdaBool);
                    }
                    ColumnaBoolean col = new ColumnaBoolean(etiqueta,colBool);
                    cols.add(col);
                } else {
                    List<CeldaString> colString = new ArrayList<>();
                    for(String celda : columna){
                        CeldaString celdaString = new CeldaString(celda);
                        colString.add(celdaString);
                    }
                    ColumnaString col = new ColumnaString(etiqueta,colString);
                    cols.add(col);
                }
            } else {
                //TODO
            }
            
        }
        return cols;
    }

    // public static Etiqueta[] headers(String linea) {
    //     String[] encabezados = linea.split(",");
    //     Etiqueta[] etiquetas = new Etiqueta[encabezados.length];
        
    //     for (int i = 0; i < encabezados.length; i++) {
    //         etiquetas[i] = new EtiquetaString(encabezados[i]);
    //     }

    //     return etiquetas;
    // }


    // public static List<Columna> leerCSV(List<List<String>> columnas) {
    //     List<Columna> cols = new ArrayList<>();
    //     for (List<String> columna : columnas) {
    //         if (esNum(columna.get(1))) {
    //             List<CeldaNum> colNum = new ArrayList<>();
    //             for(String celda : columna){
    //                 int numero = Integer.parseInt(celda);
    //                 CeldaNum celdaNum = new CeldaNum(numero);
    //                 colNum.add(celdaNum);
    //             }
    //             ColumnaNum col = new ColumnaNum(colNum);
    //             cols.add(col);
    //         } else if (esBool(columna.get(1))) {
    //             List<CeldaBoolean> colBool = new ArrayList<>();
    //             for(String celda : columna){
    //                 boolean booleano = Boolean.parseBoolean(celda);
    //                 CeldaBoolean celdaBool = new CeldaBoolean(booleano);
    //                 colBool.add(celdaBool);
    //             }
    //             ColumnaBoolean col = new ColumnaBoolean(colBool);
    //             cols.add(col);
    //         } else {
    //             List<CeldaString> colString = new ArrayList<>();
    //             for(String celda : columna){
    //                 CeldaString celdaString = new CeldaString(celda);
    //                 colString.add(celdaString);
    //             }
    //             ColumnaString col = new ColumnaString(colString);
    //             cols.add(col);
    //         }
            
    //     }
    //     return cols;
    // }

    private static boolean esNum(String cadena) {
        return cadena.matches("-?\\d+(\\.\\d+)?");
    }

    private static boolean esBool(String cadena) {
        //TODO: tambien con 0 y 1
        return cadena.equalsIgnoreCase("true") || cadena.equalsIgnoreCase("false");
    }
}

