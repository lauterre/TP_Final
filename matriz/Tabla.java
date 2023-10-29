package matriz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lector.LectorCSV;
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
import etiqueta.EtiquetaNum;
import etiqueta.EtiquetaString;

public class Tabla {
    List<Columna> columnas;
    Map <Etiqueta, Integer> colLabels;
    Map <Etiqueta, Integer> rowLabels;

    public Tabla(int cantidadColumnas){
        // TODO : Exceptions
        columnas = new ArrayList<>();
        colLabels = new HashMap<>();
        rowLabels = new HashMap<>();
    }

    public Tabla(int cantidadColumnas, Etiqueta[] etiquetas) {
        this(cantidadColumnas);
        if (cantidadColumnas != etiquetas.length)
            throw new IllegalArgumentException("La longitud de etiquetas no coincide.");
        setEtiquetasColumnas(etiquetas);
    }

    // toda del chatgpt
    public Tabla(Object[][] matriz, boolean tieneEncabezadosColumnas, boolean tieneEncabezadosFilas) {
        int cantidadColumnas = matriz[0].length;
        this.columnas = new ArrayList<>();
        this.colLabels = new HashMap<>();
        this.rowLabels = new HashMap<>();

        int inicioFila = tieneEncabezadosFilas ? 1 : 0;
        int inicioColumna = tieneEncabezadosColumnas ? 1 : 0;

        for (int i = inicioColumna; i < cantidadColumnas; i++) {
            List<Celda> celdas = new ArrayList<>();
            for (int j = inicioFila; j < matriz.length; j++) {
                Celda celda;
                if (matriz[j][i] instanceof Boolean) {
                    celda = new CeldaBoolean((Boolean) matriz[j][i]);
                } else if (matriz[j][i] instanceof Number) {
                    celda = new CeldaNum((Number) matriz[j][i]);
                } else if (matriz[j][i] instanceof String) {
                    celda = new CeldaString((String) matriz[j][i]);
                } else {
                    throw new IllegalArgumentException("Tipo de datos no compatible en la matriz");
                }
                celdas.add(celda);
            }
            Columna columna = crearColumna(celdas);
            this.columnas.add(columna);
            if (tieneEncabezadosColumnas) {
                Etiqueta etiqueta = matriz[0][i] instanceof String
                        ? new EtiquetaString((String) matriz[0][i])
                        : new EtiquetaNum((int) matriz[0][i]);
                this.colLabels.put(etiqueta, i - inicioColumna);
            }
        }

        for (int i = inicioFila; i < matriz.length; i++) {
            Etiqueta etiqueta = matriz[i][0] instanceof String
                    ? new EtiquetaString((String) matriz[i][0])
                    : new EtiquetaNum((int) matriz[i][0]);
            this.rowLabels.put(etiqueta, i - inicioFila);
        }
    }

    //TODO etiquetas de filas
    public Tabla(String rutaArchivo, boolean tieneEncabezados) {
        LectorCSV lector = new LectorCSV();
        try {
            List<String> lineas = lector.leer(rutaArchivo);
            List<Columna> cols = lector.parserColumnas(lineas, tieneEncabezados);
            this.columnas = cols;
            if (tieneEncabezados) setEtiquetasColumnas(lector.getEncabezados());
            
        } catch (ArchivoNoEncontradoException | CSVParserException e) {
            // TODO Auto-generated catch block
        }
        
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

    private Columna crearColumna(List<Celda> celdas) {
        // Identificar el tipo de columna
        if (celdas.get(0) instanceof CeldaBoolean) {
            List<CeldaBoolean> booleanCeldas = new ArrayList<>();
            for (Celda celda : celdas) {
                booleanCeldas.add((CeldaBoolean) celda);
            }
            return new ColumnaBoolean(booleanCeldas);
        } else if (celdas.get(0) instanceof CeldaNum) {
            List<CeldaNum> numCeldas = new ArrayList<>();
            for (Celda celda : celdas) {
                numCeldas.add((CeldaNum) celda);
            }
            return new ColumnaNum(numCeldas);
        } else if (celdas.get(0) instanceof CeldaString) {
            List<CeldaString> stringCeldas = new ArrayList<>();
            for (Celda celda : celdas) {
                stringCeldas.add((CeldaString) celda);
            }
            return new ColumnaString(stringCeldas);
        } else {
            throw new IllegalArgumentException("Tipo de celda desconocido");
        }
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

}
