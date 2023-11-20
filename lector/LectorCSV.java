package lector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import celda.Celda;
import celda.CeldaBoolean;
import celda.CeldaNum;
import celda.CeldaString;
import columna.Columna;
import columna.ColumnaBoolean;
import columna.ColumnaNum;
import columna.ColumnaString;
import etiqueta.Etiqueta;
import lector.exceptions.ArchivoNoEncontradoException;
import lector.exceptions.CSVParserException;

public class LectorCSV {

    private List<Etiqueta> encabezados;
    private List<Etiqueta> encabezadosFilas;

    public List<String> leer(String ruta) throws ArchivoNoEncontradoException {
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

    // TODO: ver que pasa si el cvs tiene null en la ultima columna
    public List<Columna<? extends Celda>> parserColumnas(List<String> lineas, boolean tieneEncabezados, boolean tieneEncabezadosFila)
            throws CSVParserException {
        int cantidadColumnas = lineas.get(0).split(",").length;
        List<List<String>> columnas = new ArrayList<>();
        this.encabezados = new ArrayList<>();
        this.encabezadosFilas = new ArrayList<>();

        for (int i = 0; i < cantidadColumnas; i++) {
            columnas.add(new ArrayList<>());
        }

        for (int l = 0; l < lineas.size(); l++) {
            String[] campos = lineas.get(l).split(",");
            for (int i = 0; i < campos.length; i++) {
                columnas.get(i).add(campos[i]);
            }
            if (campos.length != cantidadColumnas) {
                throw new CSVParserException();
            }
        }

        if (tieneEncabezados) {
            for (List<String> columna : columnas) {
                Etiqueta etiqueta = Etiqueta.crear(columna.get(0));
                encabezados.add(etiqueta);
                columna.remove(0);
            }
        } else {
            for (int i = 0; i < columnas.size(); i++) {
                Etiqueta etiqueta = Etiqueta.crear(i);
                encabezados.add(etiqueta);
            }
        }

        if (tieneEncabezadosFila) {
            encabezados.remove(0);
            for (String celda : columnas.get(0)) {
                Etiqueta etiqueta = Etiqueta.crear(celda);
                encabezadosFilas.add(etiqueta);
            }
            columnas.remove(0);
        } else {
            for (int i = 0; i < columnas.get(0).size(); i++) {
                Etiqueta etiqueta = Etiqueta.crear(i);
                encabezadosFilas.add(etiqueta);
            }
        }

        List<Columna<? extends Celda>> cols = new ArrayList<>();
        for (List<String> columna : columnas) {
            if (esNum(columna.get(0))) {
                List<CeldaNum> colNum = new ArrayList<>();
                for (String celda : columna) {
                    Double numero;
                    if (celda.equals("")) {
                        numero = null;
                    } else {
                        numero = Double.parseDouble(celda);
                    }
                    CeldaNum celdaNum = new CeldaNum(numero);
                    colNum.add(celdaNum);
                }
                ColumnaNum col = new ColumnaNum(colNum);
                cols.add(col);
            } else if (esBool(columna.get(0))) {
                List<CeldaBoolean> colBool = new ArrayList<>();
                for (String celda : columna) {
                    Boolean booleano;
                    if (celda.equals("")) {
                        booleano = null;
                    } else {
                        booleano = Boolean.parseBoolean(celda);
                    }
                    CeldaBoolean celdaBool = new CeldaBoolean(booleano);
                    colBool.add(celdaBool);
                }
                ColumnaBoolean col = new ColumnaBoolean(colBool);
                cols.add(col);
            } else {
                List<CeldaString> colString = new ArrayList<>();
                for (String celda : columna) {
                    CeldaString celdaString = new CeldaString(celda);
                    colString.add(celdaString);
                }
                ColumnaString col = new ColumnaString(colString);
                cols.add(col);
            }
        }

        // if (tieneEncabezadosFila) {
        //     for (Celda celda : cols.get(0)) {
        //         Etiqueta etiqueta = Etiqueta.crear(celda.getValor());
        //         encabezadosFilas.add(etiqueta);
        //     }
        //     cols.remove(0);
        // }

        // if (tieneEncabezados) {
        //     for (Columna col : cols) {
        //         Celda celda = col.getCeldas().get(0);
        //         Etiqueta etiqueta = Etiqueta.crear(col.getCeldas().get(0).getValor());
        //     }
        // }
        return cols;
    }

    private static boolean esNum(String cadena) {
        return cadena.matches("-?\\d+(\\.\\d+)?");
    }

    private static boolean esBool(String cadena) {
        // TODO: tambien con 0 y 1
        return cadena.equalsIgnoreCase("true") || cadena.equalsIgnoreCase("false");
    }

    public List<Etiqueta> getEncabezados() {
        return this.encabezados;
    }

    public List<Etiqueta> getEncabezadosFilas() {
        return this.encabezadosFilas;
    }
}