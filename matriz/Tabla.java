package matriz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tabla {
    List<Columna> columnas;
    Map <Etiqueta, Integer> colLabels;
    Map <Etiqueta, Integer> rowLabels;

    public Tabla(int cantidadColumnas){
        // TO DO : Exceptions
        columnas = new ArrayList<>(null);
        colLabels = new HashMap<>(cantidadColumnas, cantidadColumnas);
        rowLabels = new HashMap<>(cantidadColumnas, cantidadColumnas);
    }

    public Tabla(int cantidadColumnas, Etiqueta[] etiquetas) {
        this(cantidadColumnas);
        if (cantidadColumnas != etiquetas.length)
            throw new IllegalArgumentException("La longitud de etiquetas no coincide.");
        setEtiquetasColumnas(etiquetas);
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
