package lector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import celda.CeldaNum;
import columna.Columna;
import columna.ColumnaNum;
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

    public static List<List<String>> parserColumnas(List<String> lineas) throws CSVParserException {
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

        return columnas;
    }


    public static List<Columna> leerCSV(List<List<String>> columnas) {
        List<Columna> cols = new ArrayList<>();
        for (List<String> columna : columnas) {
            if (esNum(columna.get(1))) {
                List<CeldaNum> colNum = new ArrayList<>();
                for(String celda : columna){
                    int numero = Integer.parseInt(celda);
                    CeldaNum celdaNum = new CeldaNum(numero);
                    colNum.add(celdaNum);
                }
                ColumnaNum col = new ColumnaNum(colNum);
                cols.add(col);
            }
            //TODO: demas tipos de columnas
        }
        return cols;
    }

    private static boolean esNum(String cadena) {
        return cadena.matches("-?\\d+(\\.\\d+)?");
    }

    private static boolean esBool(String cadena) {
        //TODO: tambien con 0 y 1
        return cadena.equalsIgnoreCase("true") || cadena.equalsIgnoreCase("false");
    }
}

