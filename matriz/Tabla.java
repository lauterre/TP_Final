package matriz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import Exceptions.ColumnaNoAgregableException;
import Exceptions.EtiquetaExistenteException;
import Exceptions.EtiquetaInvalidaException;
import Exceptions.TablasNoConcatenablesException;
import celda.Celda;
import celda.CeldaBoolean;
import celda.CeldaNum;
import celda.CeldaString;
import columna.Columna;
import columna.ColumnaBoolean;
import columna.ColumnaNum;
import columna.ColumnaString;
import escritor.EscritorCSV;
import etiqueta.Etiqueta;
import etiqueta.EtiquetaNum;
import etiqueta.EtiquetaString;
import fila.Fila;
import lector.LectorCSV;
import groupby.GroupBy;
import lector.exceptions.ArchivoNoEncontradoException;
import lector.exceptions.CSVParserException;

public class Tabla {
    List<Columna<? extends Celda>> columnas;
    Map<Etiqueta, Integer> colLabels;
    public Map<Etiqueta, Integer> rowLabels;
    boolean tieneEtiquetaCol = false;
    boolean tieneEtiquetaFila = false;

    public Tabla() {
        columnas = new ArrayList<>();
        colLabels = new LinkedHashMap<>();
        rowLabels = new LinkedHashMap<>();
    }

    public Tabla(int cantidadColumnas, String[] etiquetas) {
        this();
        if (cantidadColumnas <= 0)
            throw new IllegalArgumentException("Debe indicar un numero positivo.");
        if (cantidadColumnas != etiquetas.length)
            throw new IllegalArgumentException("La longitud de etiquetas no coincide.");
        setEtiquetasColumnas(etiquetas);
    }

    public Tabla(Object[][] matriz, boolean tieneEncabezadosColumnas, boolean tieneEncabezadosFilas) {
        this.tieneEtiquetaCol = tieneEncabezadosColumnas;
        this.tieneEtiquetaFila = tieneEncabezadosFilas;
        int cantidadColumnas = matriz[0].length;
        this.columnas = new ArrayList<>();
        colLabels = new LinkedHashMap<>();
        rowLabels = new LinkedHashMap<>();

        int inicioFila = tieneEncabezadosFilas ? 1 : 0;
        int inicioColumna = tieneEncabezadosColumnas ? 1 : 0;

        try {
            for (int i = inicioFila; i < cantidadColumnas; i++) {
                List<Object> celdas = new ArrayList<>();
                for (int j = inicioColumna; j < matriz.length; j++) {
                    celdas.add(matriz[j][i]);
                }
                Columna<? extends Celda> columna = crearColumna(celdas);
                this.columnas.add(columna);
                if (tieneEncabezadosColumnas) {
                    Etiqueta etiqueta = Etiqueta.crear(matriz[0][i].toString());
                    this.colLabels.put(etiqueta, i - inicioFila);
                } else {
                    Etiqueta etiqueta = Etiqueta.crear(i - inicioFila);
                    this.colLabels.put(etiqueta, i - inicioFila);
                }
            }

            for (int i = inicioColumna; i < matriz.length; i++) {
                Etiqueta etiqueta;
                if (tieneEncabezadosFilas) {
                    etiqueta = Etiqueta.crear(matriz[i][0].toString());
                } else {
                    etiqueta = Etiqueta.crear(i - inicioColumna);
                }
                this.rowLabels.put(etiqueta, i - inicioColumna);
            }
        } catch (EtiquetaInvalidaException e) {
            e.printStackTrace();
        }
    }

    public Tabla(int[][] matriz, boolean tieneEncabezadosColumnas, boolean tieneEncabezadosFilas) {
        this(convertirMatrizANumber(matriz), tieneEncabezadosColumnas, tieneEncabezadosFilas);
    }

    public Tabla(float[][] matriz, boolean tieneEncabezadosColumnas, boolean tieneEncabezadosFilas) {
        this(convertirMatrizANumber(matriz), tieneEncabezadosColumnas, tieneEncabezadosFilas);
    }

    public Tabla(double[][] matriz, boolean tieneEncabezadosColumnas, boolean tieneEncabezadosFilas) {
        this(convertirMatrizANumber(matriz), tieneEncabezadosColumnas, tieneEncabezadosFilas);
    }

    public Tabla(String rutaArchivo, boolean tieneEncabezadosColumnas, boolean tieneEncabezadosFilas) {
        this.tieneEtiquetaCol = tieneEncabezadosColumnas;
        this.tieneEtiquetaFila = tieneEncabezadosFilas;
        LectorCSV lector = new LectorCSV();
        this.colLabels = new LinkedHashMap<>();
        this.rowLabels = new LinkedHashMap<>();
        try {
            List<String> lineas = lector.leer(rutaArchivo);
            List<Columna<? extends Celda>> cols = lector.parserColumnas(lineas, tieneEncabezadosColumnas,
                    tieneEncabezadosFilas);
            this.columnas = cols;
            setEtiquetasColumnas(lector.getEncabezados());
            setEtiquetasFilas(lector.getEncabezadosFilas());
        } catch (ArchivoNoEncontradoException | CSVParserException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

    private void borrarEtiqueta(Etiqueta etiqueta, Map<Etiqueta, Integer> map) throws EtiquetaInvalidaException {
        if (!(map.containsKey(etiqueta))) {
            throw new EtiquetaInvalidaException();
        }
        boolean encontrado = false;
        for (Etiqueta label : map.keySet()) {
            if (encontrado == true) {
                int valorActual = map.get(label);
                map.put(label, (valorActual - 1));
            }
            if (label.equals(etiqueta)) {
                encontrado = true;
            }
        }
        map.remove(etiqueta);
    }

    private void borrarEtiquetaColumna(Etiqueta etiqueta) {
        try {
            borrarEtiqueta(etiqueta, colLabels);
        } catch (EtiquetaInvalidaException e) {
            e.printStackTrace();
        }
    }

    private void borrarEtiquetaFila(Etiqueta etiqueta) {
        try {
            borrarEtiqueta(etiqueta, rowLabels);
        } catch (EtiquetaInvalidaException e) {
            e.printStackTrace();
        }
    }

    private void agregarColumna(Columna<? extends Celda> columna, Etiqueta etiqueta) {
        columnas.add(columna);
        colLabels.put(etiqueta, this.columnas.size());
    }

    private void agregarColumna(List<Object> columna, Etiqueta etiqueta)
            throws EtiquetaExistenteException, ColumnaNoAgregableException {
        Columna<? extends Celda> col = Columna.crear(columna);
        if (colLabels.containsKey(etiqueta)) {
            throw new EtiquetaExistenteException();
        }
        if (col.getCeldas().size() == this.obtenerCantidadFilas()) {
            this.colLabels.put(etiqueta, this.columnas.size());
            this.columnas.add(col);
        } else {
            throw new ColumnaNoAgregableException();
        }
    }

    public void agregarColumna(List<Object> columna, String etiqueta) {
        try {
            agregarColumna(columna, convertirAEtiqueta(etiqueta));
        } catch (EtiquetaExistenteException | ColumnaNoAgregableException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void agregarColumna(List<Object> columna, int etiqueta) {
        try {
            agregarColumna(columna, convertirAEtiqueta(etiqueta));
        } catch (EtiquetaExistenteException | ColumnaNoAgregableException e) {
            e.printStackTrace();
        }
    }

    public void agregarColumna(List<Object> columna) {
        try {
            agregarColumna(columna, new EtiquetaNum(this.columnas.size()));
        } catch (EtiquetaExistenteException | ColumnaNoAgregableException e) {
            e.printStackTrace();
        }
    }

    public List<Etiqueta> convertirAEtiqueta(String[] nombres) {
        List<Etiqueta> salida = new ArrayList<>();
        for (int i = 0; i < nombres.length; i++) {
            Etiqueta etiqueta = Etiqueta.crear(nombres[i]);
            salida.add(etiqueta);
        }
        return salida;
    }

    public Etiqueta convertirAEtiqueta(String nombre) {
        Etiqueta etiqueta = Etiqueta.crear(nombre);
        return etiqueta;
    }

    public List<Etiqueta> convertirAEtiqueta(int[] nombres) {
        List<Etiqueta> salida = new ArrayList<>();
        try {
            for (int i = 0; i < nombres.length; i++) {
                Etiqueta etiqueta = Etiqueta.crear(nombres[i]);
                salida.add(etiqueta);
            }
            return salida;
        } catch (EtiquetaInvalidaException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Etiqueta convertirAEtiqueta(int nombre) {
        Etiqueta etiqueta;
        try {
            etiqueta = Etiqueta.crear(nombre);
            return etiqueta;
        } catch (EtiquetaInvalidaException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setEtiquetasFilas(List<Etiqueta> etiquetas) {

        rowLabels.clear();
        for (int i = 0; i < columnas.get(0).size(); i++) {
            rowLabels.put(etiquetas.get(i), i);
        }
    }

    public void setEtiquetasFilas(String[] etiquetas) {
        this.tieneEtiquetaFila = true;
        setEtiquetasFilas(convertirAEtiqueta(etiquetas));
    }

    public void setEtiquetasFilas(int[] etiquetas) {
        this.tieneEtiquetaFila = false;
        setEtiquetasFilas(convertirAEtiqueta(etiquetas));
    }

    private void setEtiquetasColumnas(List<Etiqueta> etiquetas) {
        for (int j = 0; j < columnas.size(); j++) {
            colLabels.put(etiquetas.get(j), j);
        }
    }

    public void setEtiquetasColumnas(String[] etiquetas) {
        setEtiquetasColumnas(convertirAEtiqueta(etiquetas));
        this.tieneEtiquetaCol = true;
    }

    public void setEtiquetasColumnas(int[] etiquetas) {
        this.tieneEtiquetaCol = false;
        setEtiquetasColumnas(convertirAEtiqueta(etiquetas));
    }

    public List<Etiqueta> obtenerEtiquetasColumnas() {
        List<Etiqueta> salida = new ArrayList<>();
        for (Etiqueta etiqueta : colLabels.keySet()) {
            salida.add(etiqueta);
        }
        return salida;
    }

    public List<Etiqueta> obtenerEtiquetasFilas() {
        List<Etiqueta> salida = new ArrayList<>();
        for (Etiqueta etiqueta : rowLabels.keySet()) {
            salida.add(etiqueta);
        }
        return salida;
    }

    public List<Object> obtenerNombreEtiquetasColumnas() {
        List<Object> salida = new ArrayList<>();
        for (Etiqueta etiqueta : colLabels.keySet()) {
            salida.add(etiqueta.getNombre());
        }
        return salida;
    }

    public List<Object> obtenerNombreEtiquetasFilas() {
        List<Object> salida = new ArrayList<>();
        for (Etiqueta etiqueta : rowLabels.keySet()) {
            salida.add(etiqueta.getNombre());
        }
        return salida;
    }

    private Celda obtenerCelda(Etiqueta etiquetaFila, Etiqueta etiquetaColumna) throws EtiquetaInvalidaException {
        if (!rowLabels.containsKey(etiquetaFila)) {
            throw new EtiquetaInvalidaException();
        }
        if (!colLabels.containsKey(etiquetaColumna)) {
            throw new EtiquetaInvalidaException();
        }
        return columnas.get(colLabels.get(etiquetaColumna)).obtenerValor(rowLabels.get(etiquetaFila));
    }

    private Object obtenerValor(Etiqueta etiquetaFila, Etiqueta etiquetaColumna) {
        try {
            return obtenerCelda(etiquetaFila, etiquetaColumna).getValor();
        } catch (EtiquetaInvalidaException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object obtenerValor(String etiquetaFila, String etiquetaColumna) {
        return obtenerValor(convertirAEtiqueta(etiquetaFila), convertirAEtiqueta(etiquetaColumna));
    }

    public Object obtenerValor(int etiquetaFila, int etiquetaColumna) {
        return obtenerValor(convertirAEtiqueta(etiquetaFila), convertirAEtiqueta(etiquetaColumna));
    }

    public Object obtenerValor(String etiquetaFila, int etiquetaColumna) {
        return obtenerValor(convertirAEtiqueta(etiquetaFila), convertirAEtiqueta(etiquetaColumna));
    }

    public Object obtenerValor(int etiquetaFila, String etiquetaColumna) {
        return obtenerValor(convertirAEtiqueta(etiquetaFila), convertirAEtiqueta(etiquetaColumna));
    }

    private void cambiarValor(Etiqueta etiquetaFila, Etiqueta etiquetaColumna, Object valorNuevo, Boolean imprimir) {
        try {
            Celda celdaBorrada = obtenerCelda(etiquetaFila, etiquetaColumna);
            Object valorAnterior = celdaBorrada.getValor();
            columnas.get(colLabels.get(etiquetaColumna)).fijarValor(rowLabels.get(etiquetaFila), valorNuevo);
            if (imprimir)
                System.out.println(
                        "Se cambio el valor de la celda[" + etiquetaFila + ", " + etiquetaColumna
                                + "]\n- Valor anterior: "
                                + valorAnterior + "\n- Valor nuevo: " + valorNuevo);
        } catch (EtiquetaInvalidaException e) {
            e.printStackTrace();
        }
    }

    public void cambiarValor(String etiquetaFila, String etiquetaColumna, Object valor) {
        cambiarValor(convertirAEtiqueta(etiquetaFila), convertirAEtiqueta(etiquetaColumna), valor, true);
    }

    public void cambiarValor(int etiquetaFila, int etiquetaColumna, Object valor) {
        cambiarValor(convertirAEtiqueta(etiquetaFila), convertirAEtiqueta(etiquetaColumna), valor, true);
    }

    public void cambiarValor(String etiquetaFila, int etiquetaColumna, Object valor) {
        cambiarValor(convertirAEtiqueta(etiquetaFila), convertirAEtiqueta(etiquetaColumna), valor, true);
    }

    public void cambiarValor(int etiquetaFila, String etiquetaColumna, Object valor) {
        cambiarValor(convertirAEtiqueta(etiquetaFila), convertirAEtiqueta(etiquetaColumna), valor, true);
    }

    public int obtenerCantidadFilas() {
        if (columnas.isEmpty()) {
            System.out.println("La tabla esta vacia");
            return 0;
        }
        int cantidadFilas = columnas.get(0).size();
        return cantidadFilas;
    }

    public int obtenerCantidadColumnas() {
        if (columnas.isEmpty()) {
            System.out.println("La tabla esta vacia");
            return 0;
        }
        int cantidadColumnas = columnas.size();
        return cantidadColumnas;
    }

    public Columna<? extends Celda> obtenerColumna(Etiqueta etiquetaColumna) {
        Columna<? extends Celda> columnaPedida = columnas.get(colLabels.get(etiquetaColumna));
        return columnaPedida;
    }

    public Columna<? extends Celda> obtenerColumna(String etiquetaColumnaNombre) {
        try {
            Etiqueta etiquetaColumna = getEtiquetaColumna(etiquetaColumnaNombre);
            return obtenerColumna(etiquetaColumna);
        } catch (EtiquetaInvalidaException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Columna<? extends Celda> obtenerColumna(Integer etiquetaColumnaNombre) {
        try {
            Etiqueta etiquetaColumna = getEtiquetaColumna(etiquetaColumnaNombre);
            return obtenerColumna(etiquetaColumna);
        } catch (EtiquetaInvalidaException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Tabla ordenarPorColumnas(List<Etiqueta> columnasOrden, String queOrden) throws EtiquetaInvalidaException {
        for (Etiqueta etiqueta : columnasOrden) {
            if (!(colLabels.keySet().contains(etiqueta)))
                throw new EtiquetaInvalidaException();
        }
        List<Etiqueta> orden = new ArrayList<>(rowLabels.keySet());
        int n = orden.size();
        boolean huboCambio;
        Etiqueta[] etiquetasColumnas = new Etiqueta[columnasOrden.size()];
        for (int i = 0; i < columnasOrden.size(); i++) {
            Etiqueta etiqueta = columnasOrden.get(i);
            etiquetasColumnas[i] = etiqueta;
        }
        do {
            huboCambio = false;
            for (int i = 1; i < n; i++) {
                Etiqueta etiquetaPrevia = orden.get(i - 1);
                Etiqueta etiquetaActual = orden.get(i);

                Fila filaPrevia;
                Fila filaActual;
                try {
                    filaPrevia = getFila(etiquetaPrevia, etiquetasColumnas);
                    filaActual = getFila(etiquetaActual, etiquetasColumnas);
                } catch (EtiquetaInvalidaException e) {
                    e.printStackTrace();
                    return null;
                }

                if (queOrden.equalsIgnoreCase("ascendente")) {
                    if (filaPrevia.compareTo(filaActual) > 0) {
                        orden.set(i - 1, orden.get(i));
                        orden.set(i, etiquetaPrevia);
                        huboCambio = true;
                    }
                } else if (queOrden.equalsIgnoreCase("descendente")) {
                    if (filaPrevia.compareTo(filaActual) < 0) {
                        orden.set(i - 1, orden.get(i));
                        orden.set(i, etiquetaPrevia);
                        huboCambio = true;
                    }
                } else {
                    throw new IllegalArgumentException("Indique un orden : 'ascendente' o 'descendente'");
                }
            }
            n--;
        } while (huboCambio);

        Tabla nuevaTabla = copiarTabla(this);
        nuevaTabla.generarRowLabelsOrdenado(orden);
        return nuevaTabla;
    }

    public Tabla ordenarPorColumnas(String[] columnasOrden, String queOrden) {
        try {
            List<Etiqueta> cols = convertirAEtiqueta(columnasOrden);
            return ordenarPorColumnas(cols, queOrden);
        } catch (EtiquetaInvalidaException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Tabla ordenarPorColumnas(int[] columnasOrden, String queOrden) {
        try {
            List<Etiqueta> cols = convertirAEtiqueta(columnasOrden);
            return ordenarPorColumnas(cols, queOrden);
        } catch (EtiquetaInvalidaException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Fila getFila(Etiqueta etiquetaFila, Etiqueta[] etiquetasColumnas) throws EtiquetaInvalidaException {
        List<Celda> retorno = new ArrayList<>();
        if (!rowLabels.containsKey(etiquetaFila)) {
            throw new EtiquetaInvalidaException();
        }
        for (Etiqueta etiqueta : etiquetasColumnas) {
            if (!colLabels.containsKey(etiqueta)) {
                throw new EtiquetaInvalidaException();
            }
            try {
                retorno.add(obtenerCelda(etiquetaFila, etiqueta));
            } catch (EtiquetaInvalidaException e) {
                e.getMessage();
            }

        }
        return new Fila(retorno);
    }

    public Fila getFila(Etiqueta etiquetaFila) {
        Etiqueta[] etiquetasColumnas = new Etiqueta[colLabels.size()];
        for (Etiqueta etiqueta : colLabels.keySet()) {
            etiquetasColumnas[colLabels.get(etiqueta)] = etiqueta;
        }
        try {
            return getFila(etiquetaFila, etiquetasColumnas);
        } catch (EtiquetaInvalidaException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Fila getFila(int etiquetaFila) {
        try {
            Etiqueta indice = getEtiquetaFila(etiquetaFila);
            Etiqueta[] etiquetasColumnas = new Etiqueta[colLabels.size()];
            for (Etiqueta etiqueta : colLabels.keySet()) {
                etiquetasColumnas[colLabels.get(etiqueta)] = etiqueta;
            }
            return getFila(indice, etiquetasColumnas);
        } catch (EtiquetaInvalidaException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Fila getFila(String etiquetaFila) {
        try {
            Etiqueta indice = getEtiquetaFila(etiquetaFila);
            Etiqueta[] etiquetasColumnas = new Etiqueta[colLabels.size()];
            for (Etiqueta etiqueta : colLabels.keySet()) {
                etiquetasColumnas[colLabels.get(etiqueta)] = etiqueta;
            }
            return getFila(indice, etiquetasColumnas);
        } catch (EtiquetaInvalidaException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void generarRowLabelsOrdenado(List<Etiqueta> orden) {
        for (Etiqueta etiqueta : orden) {
            Integer indice = rowLabels.get(etiqueta);
            rowLabels.remove(etiqueta);
            rowLabels.put(etiqueta, indice);
        }
    }

    private void eliminarColumna(Etiqueta etiquetaNombre) throws EtiquetaInvalidaException {
        if (!(colLabels.containsKey(etiquetaNombre))) {
            throw new EtiquetaInvalidaException();
        }
        Columna<? extends Celda> columna = obtenerColumna(etiquetaNombre);
        this.columnas.remove(columna);
        ;
    }

    public void eliminarColumna(String etiquetaNombre) {
        try {
            eliminarColumna(convertirAEtiqueta(etiquetaNombre));
        } catch (EtiquetaInvalidaException e) {
            e.printStackTrace();
        }
    }

    public void eliminarColumna(Integer etiquetaNombre) {
        try {
            eliminarColumna(convertirAEtiqueta(etiquetaNombre));
        } catch (EtiquetaInvalidaException e) {
            e.printStackTrace();
        }
    }

    public void eliminarColumnaPorIndice(int indice) {
        if (indice < 0 || indice > columnas.size())
            throw new IllegalArgumentException("Indique un indice positivo y ");
        columnas.remove(indice);
        borrarEtiquetaColumna(obtenerEtiquetasColumnas().get(indice));
    }

    private Etiqueta getEtiquetaColumna(String valor) throws EtiquetaInvalidaException {
        for (Etiqueta etiqueta : this.colLabels.keySet()) {
            if (etiqueta.getNombre().equals(valor)) {
                return etiqueta;
            }
        }
        throw new EtiquetaInvalidaException();
    }

    private Etiqueta getEtiquetaColumna(Integer valor) throws EtiquetaInvalidaException {
        for (Etiqueta etiqueta : this.colLabels.keySet()) {
            if (etiqueta.getNombre().equals(valor)) {
                return etiqueta;
            }
        }
        throw new EtiquetaInvalidaException();
    }

    public Etiqueta getEtiquetaFila(String valor) throws EtiquetaInvalidaException {
        for (Etiqueta etiqueta : this.rowLabels.keySet()) {
            if (etiqueta.getNombre().equals(valor)) {
                return etiqueta;
            }
        }
        throw new EtiquetaInvalidaException();
    }

    public Etiqueta getEtiquetaFila(Integer valor) throws EtiquetaInvalidaException {
        for (Etiqueta etiqueta : this.rowLabels.keySet()) {
            if (etiqueta.getNombre().equals(valor)) {
                return etiqueta;
            }
        }
        throw new EtiquetaInvalidaException();
    }

    private Columna<? extends Celda> crearColumna(List<Object> valores) {
        return Columna.crear(valores);
    }

    public boolean tieneEncabezadosColumnas() {
        return tieneEtiquetaCol;
    }

    public boolean tieneEtiquetasFilas() {
        return tieneEtiquetaFila;
    }

    private static Number[][] convertirMatrizANumber(int[][] matriz) {
        int cantidadFilas = matriz.length;
        int cantidadColumnas = matriz[0].length;
        Number[][] matrizNumber = new Number[cantidadFilas][cantidadColumnas];
        for (int i = 0; i < cantidadFilas; i++) {
            for (int j = 0; j < cantidadColumnas; j++) {
                matrizNumber[i][j] = (Number) matriz[i][j];
            }
        }
        return matrizNumber;
    }

    private static Number[][] convertirMatrizANumber(float[][] matriz) {
        int cantidadFilas = matriz.length;
        int cantidadColumnas = matriz[0].length;
        Number[][] matrizNumber = new Number[cantidadFilas][cantidadColumnas];
        for (int i = 0; i < cantidadFilas; i++) {
            for (int j = 0; j < cantidadColumnas; j++) {
                matrizNumber[i][j] = (Number) matriz[i][j];
            }
        }
        return matrizNumber;
    }

    private static Number[][] convertirMatrizANumber(double[][] matriz) {
        int cantidadFilas = matriz.length;
        int cantidadColumnas = matriz[0].length;
        Number[][] matrizNumber = new Number[cantidadFilas][cantidadColumnas];
        for (int i = 0; i < cantidadFilas; i++) {
            for (int j = 0; j < cantidadColumnas; j++) {
                matrizNumber[i][j] = (Number) matriz[i][j];
            }
        }
        return matrizNumber;
    }

    public static Tabla copiarTabla(Tabla origen) {
        Tabla nueva = new Tabla();
        nueva.colLabels.putAll(origen.colLabels);
        nueva.rowLabels.putAll(origen.rowLabels);
        nueva.tieneEtiquetaCol = origen.tieneEtiquetaCol;
        nueva.tieneEtiquetaFila = origen.tieneEtiquetaFila;
        nueva.columnas = new ArrayList<>();
        for (Columna<? extends Celda> columna : origen.columnas) {
            if (columna instanceof ColumnaNum) {
                ColumnaNum columnaNum = (ColumnaNum) columna;
                ColumnaNum columnaNueva = new ColumnaNum(new ArrayList<CeldaNum>());
                for (CeldaNum celda : columnaNum.getCeldas()) {
                    columnaNueva.agregarCelda(celda.copia());
                }
                nueva.columnas.add(columnaNueva);
            } else if (columna instanceof ColumnaString) {
                ColumnaString columnaString = (ColumnaString) columna;
                ColumnaString columnaNueva = new ColumnaString(new ArrayList<CeldaString>());
                for (CeldaString celda : columnaString.getCeldas()) {
                    columnaNueva.agregarCelda(celda.copia());
                }
                nueva.columnas.add(columnaNueva);
            } else {
                ColumnaBoolean columnaBoolean = (ColumnaBoolean) columna;
                ColumnaBoolean columnaNueva = new ColumnaBoolean(new ArrayList<CeldaBoolean>());
                for (CeldaBoolean celda : columnaBoolean.getCeldas()) {
                    columnaNueva.agregarCelda(celda.copia());
                }
                nueva.columnas.add(columnaNueva);
            }
        }
        return nueva;
    }

    public <T extends Celda> Tabla concatenar(Tabla otraTabla, boolean coincideEtiquetaFila) {
        try {
            mismasColumnas(this, otraTabla);
        } catch (TablasNoConcatenablesException e) {
            System.out.println(e.getMessage());
            return this;
        }
        Tabla nuevaTabla = copiarTabla(this);
        if (coincideEtiquetaFila) {
            if (!mismoTipoEtiqueta(this.obtenerEtiquetasFilas(), otraTabla.obtenerEtiquetasFilas())) {
                throw new IllegalArgumentException(
                        "No coinciden las etiquetas, por favor indique falso en el argumento");
            } else {
                for (int i = 0; i < otraTabla.obtenerCantidadFilas(); i++) {
                    if (otraTabla.obtenerEtiquetasFilas().get(i) instanceof EtiquetaString) {
                        nuevaTabla.rowLabels.put(otraTabla.obtenerEtiquetasFilas().get(i),
                                i + nuevaTabla.obtenerCantidadFilas());
                    } else {
                        nuevaTabla.rowLabels.put(new EtiquetaNum(i + nuevaTabla.obtenerCantidadFilas()),
                                i + nuevaTabla.obtenerCantidadFilas());
                    }
                }
            }
        } else {
            List<Etiqueta> etiquetasNuevas = new ArrayList<>();
            for (int i = 0; i < (nuevaTabla.obtenerCantidadFilas()); i++) {
                etiquetasNuevas.add(new EtiquetaNum(i));
            }
            nuevaTabla.setEtiquetasFilas(etiquetasNuevas);
            for (int i = 0; i < otraTabla.obtenerCantidadFilas(); i++) {
                nuevaTabla.rowLabels.put(new EtiquetaNum(i + nuevaTabla.obtenerCantidadFilas()),
                        i + nuevaTabla.obtenerCantidadFilas());
            }
        }
        for (Etiqueta etiqueta : nuevaTabla.obtenerEtiquetasColumnas()) {
            Columna<? extends Celda> columna = nuevaTabla.obtenerColumna(etiqueta);
            Columna<? extends Celda> columnaOtraTabla = otraTabla.obtenerColumna(etiqueta);
            for (Celda celda : columnaOtraTabla.getCeldas()) {
                columna.agregarCelda(celda);
            }
        }
        return nuevaTabla;
    }

    private static boolean mismoTipoEtiqueta(List<Etiqueta> etiqueta1, List<Etiqueta> etiqueta2) {
        return (etiqueta1.get(0).getClass() == etiqueta2.get(0).getClass());
    }

    private static boolean mismasColumnas(Tabla tabla1, Tabla tabla2) throws TablasNoConcatenablesException {
        List<String> columnas1 = tabla1.obtenerNombreEtiquetasColumnas().stream()
                .map(x -> x.toString()).collect(Collectors.toList());
        List<String> columnas2 = tabla2.obtenerNombreEtiquetasColumnas().stream()
                .map(x -> x.toString()).collect(Collectors.toList());

        Collections.sort(columnas1);
        Collections.sort(columnas2);

        if (columnas1.equals(columnas2)) {
            return true;
        } else {
            throw new TablasNoConcatenablesException();
        }
    }

    public Tabla filtrar(String col, char operador, Object valor) {
        try {
            Etiqueta etiquetaCol = getEtiquetaColumna(col);
            Celda celda = Celda.crear(valor);
            return filtrar(etiquetaCol, operador, celda);
        } catch (EtiquetaInvalidaException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private void generarRowLabelsFiltrado(List<Etiqueta> filas) {
        Map<Etiqueta, Integer> nuevas = new LinkedHashMap<>();
        for (Etiqueta fila : filas) {
            nuevas.put(fila, rowLabels.get(fila));
        }
        rowLabels = nuevas;
    }

    private Predicate<Celda> predicateFiltrado(Etiqueta col, char operador, Celda valor) {
        Map<Character, Predicate<Celda>> operadores = new HashMap<>();
        operadores.put('<', e -> e.compareTo(valor) < 0);
        operadores.put('>', e -> e.compareTo(valor) > 0);
        operadores.put('=', e -> e.compareTo(valor) == 0);
        operadores.put('!', e -> e.compareTo(valor) != 0);
        Predicate<Celda> condicion = operadores.get(operador);
        return condicion;
    }

    private Tabla filtrarVariasCondiciones(List<Etiqueta> cols, char[] chars, List<Celda> valores) {
        if (cols.size() != chars.length || cols.size() != valores.size())
            throw new IllegalArgumentException();
        Tabla tablaFiltrada = copiarTabla(this);
        for (int i = 0; i < cols.size(); i++) {
            tablaFiltrada = tablaFiltrada.filtrar(cols.get(i), chars[i], valores.get(i));
        }
        return tablaFiltrada;
    }

    public Tabla filtrarVariasCondiciones(String[] cols, char[] chars, List<Object> valores) {
        List<Celda> celdas = new ArrayList<>();
        for (Object valor : valores) {
            Celda celda = Celda.crear(valor);
            celdas.add(celda);
        }
        return filtrarVariasCondiciones(convertirAEtiqueta(cols), chars, celdas);
    }

    private Tabla filtrar(Etiqueta col, char operador, Celda valor) {
        return filtrar(predicateFiltrado(col, operador, valor), col);
    }

    private Tabla filtrar(Predicate<Celda> condicion, Etiqueta col) {
        List<Etiqueta> salida = new ArrayList<>();
        if (condicion != null) {
            try {
                for (Etiqueta rowLabel : rowLabels.keySet()) {
                    Celda valorAComparar = obtenerCelda(rowLabel, col);
                    if (condicion.test(valorAComparar)) {
                        salida.add(rowLabel);
                    }
                }
            } catch (EtiquetaInvalidaException e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("Indique un operador valido");
        }
        Tabla nueva = copiarTabla(this);
        List<Etiqueta> auxiliar = new ArrayList<>();
        for (Etiqueta fila : nueva.rowLabels.keySet()) {
            if (!(salida.contains(fila))) {
                auxiliar.add(fila);
            }
        }
        for (Etiqueta etiqueta : auxiliar) {
            nueva.eliminarFila(etiqueta);
            nueva.rowLabels.remove(etiqueta);
        }
        nueva.generarRowLabelsFiltrado(salida);
        return nueva;
    }

    public void mostrarTabla() {
        System.out.println(this);
    }

    public Tabla filtrarFilas(List<Etiqueta> etiquetas) {
        Tabla copiaTabla = copiarTabla(this);
        for (Etiqueta etiqueta : copiaTabla.obtenerEtiquetasFilas()) {
            if (!(etiquetas.contains(etiqueta))) {
                copiaTabla.eliminarFila(etiqueta);
            }
        }
        return copiaTabla;
    }

    private void eliminarFila(Etiqueta etiqueta) {
        int indice = rowLabels.get(etiqueta);
        for (Columna<? extends Celda> columna : columnas) {
            columna.getCeldas().remove(indice);
        }
        borrarEtiquetaFila(etiqueta);
        Map<Etiqueta, Integer> auxiliar = new LinkedHashMap<>();
        List<Etiqueta> orden = new ArrayList<>();
        for (Etiqueta eti : rowLabels.keySet()) {
            orden.add(eti);
            auxiliar.put(eti, auxiliar.size());
        }
        rowLabels = auxiliar;
        generarRowLabelsOrdenado(orden);
    }

    public void eliminarFila(String etiqueta) {
        try {
            Etiqueta eti = getEtiquetaFila(etiqueta);
            eliminarFila(eti);
        } catch (EtiquetaInvalidaException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void eliminarFila(int etiqueta) {
        try {
            Etiqueta eti = getEtiquetaFila(etiqueta);
            eliminarFila(eti);
        } catch (EtiquetaInvalidaException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private <T extends Celda> void imputar(Object valor, Etiqueta etiCol) {
        List<Etiqueta> etiquetasFila = getEtiquetasNa(etiCol);
        for (Etiqueta etiqueta : etiquetasFila) {
            cambiarValor(etiqueta, etiCol, valor, false);
        }
    }

    public void imputar(Object valor, String col) {
        try {
            Etiqueta etiqueta = getEtiquetaColumna(col);
            imputar(valor, etiqueta);
        } catch (EtiquetaInvalidaException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void imputar(Object valor, int col) {
        try {
            Etiqueta etiqueta = getEtiquetaColumna(col);
            imputar(valor, etiqueta);
        } catch (EtiquetaInvalidaException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private List<Etiqueta> getEtiquetasNa(Etiqueta etiCol) {
        List<Etiqueta> etiquetasNA = new ArrayList<>();
        Columna<? extends Celda> columna = obtenerColumna(etiCol);
        List<Integer> inicesNA = columna.indicesNA();
        for (Integer i : inicesNA) {
            Etiqueta etiqueta = obtenerClavePorValor(this.rowLabels, i);
            etiquetasNA.add(etiqueta);
        }
        return etiquetasNA;
    }

    private static <K, V> K obtenerClavePorValor(Map<K, V> mapa, V valorBuscado) {
        for (Map.Entry<K, V> entry : mapa.entrySet()) {
            if (entry.getValue().equals(valorBuscado)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private double promedio(Columna<? extends Celda> columna) {
        return columna.promedio();
    }

    public double promedio(String etiquetaCol) {
        Columna<? extends Celda> columna = obtenerColumna(etiquetaCol);
        return promedio(columna);
    }

    public double promedio(int etiquetaCol) {
        Columna<? extends Celda> columna = obtenerColumna(etiquetaCol);
        return promedio(columna);
    }

    public double mediana(String etiquetaCol) {
        Columna<? extends Celda> columna = obtenerColumna(etiquetaCol);
        return mediana(columna);
    }

    public double mediana(int etiquetaCol) {
        Columna<? extends Celda> columna = obtenerColumna(etiquetaCol);
        return mediana(columna);
    }

    private double mediana(Columna<? extends Celda> columna) {
        return columna.mediana();
    }

    private double suma(Columna<? extends Celda> columna) {
        return columna.suma();
    }

    public double suma(String etiquetaCol) {
        Columna<? extends Celda> columna = obtenerColumna(etiquetaCol);
        return suma(columna);
    }

    public double suma(int etiquetaCol) {
        Columna<? extends Celda> columna = obtenerColumna(etiquetaCol);
        return suma(columna);
    }

    public int count(String valor, String etiquetaCol) {
        Columna<? extends Celda> columna = obtenerColumna(etiquetaCol);
        return columna.count(valor);
    }

    public int count(boolean valor, String etiquetaCol) {
        Columna<? extends Celda> columna = obtenerColumna(etiquetaCol);
        return columna.count(valor);
    }

    public int count(Number valor, String etiquetaCol) {
        Columna<? extends Celda> columna = obtenerColumna(etiquetaCol);
        return columna.count(valor);
    }

    public int count(String valor, int etiquetaCol) {
        Columna<? extends Celda> columna = obtenerColumna(etiquetaCol);
        return columna.count(valor);
    }

    public int count(boolean valor, int etiquetaCol) {
        Columna<? extends Celda> columna = obtenerColumna(etiquetaCol);
        return columna.count(valor);
    }

    public int count(Number valor, int etiquetaCol) {
        Columna<? extends Celda> columna = obtenerColumna(etiquetaCol);
        return columna.count(valor);
    }

    public Map<? extends Celda, Integer> count(String etiquetaCol) {
        Columna<? extends Celda> columna = obtenerColumna(etiquetaCol);
        return columna.count();
    }

    public Map<? extends Celda, Integer> count(int etiquetaCol) {
        Columna<? extends Celda> columna = obtenerColumna(etiquetaCol);
        return columna.count();
    }

    public List<? extends Celda> unique(String etiquetaCol) {
        Columna<? extends Celda> columna = obtenerColumna(etiquetaCol);
        return columna.unique();
    }

    public List<? extends Celda> unique(int etiquetaCol) {
        Columna<? extends Celda> columna = obtenerColumna(etiquetaCol);
        return columna.unique();
    }

    private static List<Integer> generarNumeros(int cantidad, int rangoFinal) {
        Set<Integer> numerosUnicos = new HashSet<>();
        Random random = new Random();

        while (numerosUnicos.size() < cantidad) {
            int numeroAleatorio = random.nextInt(rangoFinal);
            numerosUnicos.add(numeroAleatorio);
        }
        return new ArrayList<>(numerosUnicos);
    }

    public Tabla muestreo(int porcentaje) {
        if (porcentaje <= 0 || porcentaje > 100)
            throw new IllegalArgumentException("El porcentaje debe estar entre 0 y 100");
        Tabla muestra = copiarTabla(this);
        int filasParaBorrar = (int) Math.ceil((this.obtenerCantidadFilas() * (100 - porcentaje)) / 100);
        List<Integer> indices = generarNumeros(filasParaBorrar, obtenerCantidadFilas());
        for (Integer indice : indices) {
            muestra.eliminarFila(indice);
        }
        return muestra;
    }

    private Tabla vista(List<Etiqueta> etiquetasFilas, List<Etiqueta> etiquetasColumnas)
            throws EtiquetaInvalidaException {
        Tabla vista = new Tabla();
        vista.rowLabels = this.rowLabels;
        vista.tieneEtiquetaCol = this.tieneEtiquetaCol;
        vista.tieneEtiquetaFila = this.tieneEtiquetaFila;
        for (Etiqueta etiqueta : etiquetasColumnas) {
            if (!(colLabels.containsKey(etiqueta)))
                throw new EtiquetaInvalidaException();
            vista.agregarColumna(this.obtenerColumna(etiqueta), etiqueta);
        }
        vista.setEtiquetasColumnas(etiquetasColumnas);

        List<Etiqueta> etiquetasABorrar = new ArrayList<>();
        for (Etiqueta etiqueta : this.rowLabels.keySet()) {
            if (!(etiquetasFilas.contains(etiqueta)))
                etiquetasABorrar.add(etiqueta);
        }
        for (Etiqueta etiqueta : etiquetasABorrar) {
            vista.eliminarFila(etiqueta);
        }
        return vista;
    }

    public Tabla vista(String[] etiquetasFilas, String[] etiquetasColumnas) {
        try {
            return vista(convertirAEtiqueta(etiquetasFilas), convertirAEtiqueta(etiquetasColumnas));
        } catch (EtiquetaInvalidaException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Tabla vista(int[] etiquetasFilas, int[] etiquetasColumnas) {
        try {
            return vista(convertirAEtiqueta(etiquetasFilas), convertirAEtiqueta(etiquetasColumnas));
        } catch (EtiquetaInvalidaException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Tabla vista(String[] etiquetasFilas, int[] etiquetasColumnas) {
        try {
            return vista(convertirAEtiqueta(etiquetasFilas), convertirAEtiqueta(etiquetasColumnas));
        } catch (EtiquetaInvalidaException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Tabla vista(int[] etiquetasFilas, String[] etiquetasColumnas) {
        try {
            return vista(convertirAEtiqueta(etiquetasFilas), convertirAEtiqueta(etiquetasColumnas));
        } catch (EtiquetaInvalidaException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void verFila(int etiquetasFilas) {
        try {
            Tabla auxiliar = copiarTabla(this);
            int[] etiqueta = { etiquetasFilas };
            List<Etiqueta> x = convertirAEtiqueta(etiqueta);
            if (!(x == null))
                System.out.println(auxiliar.vista(x, obtenerEtiquetasColumnas()));
        } catch (EtiquetaInvalidaException e) {
            e.printStackTrace();
        }
    }

    public Tabla head(int x) {
        if (x < 0 || x > obtenerCantidadFilas())
            throw new IllegalArgumentException(
                    "La cantidad de filas indicada debe ser mayor a 0 y menor a la cantidad de filas en la tabla");
        List<Etiqueta> etiquetas = new ArrayList<>();
        int i = 0;
        List<Etiqueta> etiquetasfila = new ArrayList<>(rowLabels.keySet());
        do {
            etiquetas.add(etiquetasfila.get(i));
            i++;
        } while (i < x);
        List<Etiqueta> etiquetasColumna = new ArrayList<>(colLabels.keySet());
        try {
            return vista(etiquetas, etiquetasColumna);
        } catch (EtiquetaInvalidaException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Tabla tail(int x) {
        if (x < 0 || x > obtenerCantidadFilas())
            throw new IllegalArgumentException(
                    "La cantidad de filas indicada debe ser mayor a 0 y menor a la cantidad de filas en la tabla");
        List<Etiqueta> etiquetas = new ArrayList<>();
        int i = 0;
        List<Etiqueta> etiquetasfila = new ArrayList<>(rowLabels.keySet());
        do {
            etiquetas.add(etiquetasfila.get(obtenerCantidadFilas() - 1 - i));
            i++;
        } while (i < x);
        generarRowLabelsOrdenado(etiquetasfila);
        List<Etiqueta> etiquetasColumna = new ArrayList<>(colLabels.keySet());
        try {
            return vista(etiquetas, etiquetasColumna);
        } catch (EtiquetaInvalidaException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String obtenerTipoDeDato(String etiquetaColumna) {
        Columna<? extends Celda> columna = obtenerColumna(etiquetaColumna);
        return columna.tipoDato();
    }

    public String obtenerTipoDeDato(int etiquetaColumna) {
        Columna<? extends Celda> columna = obtenerColumna(etiquetaColumna);

        return columna.tipoDato();
    }

    public List<String> obtenerTipoDeDato() {
        List<String> tiposDeDato = new ArrayList<>();
        for (Columna<? extends Celda> columna : columnas) {
            tiposDeDato.add(columna.tipoDato());
        }
        return tiposDeDato;
    }

    public void guardarCSV(String direccion) {
        EscritorCSV escritorCSV = new EscritorCSV(this, direccion);
        escritorCSV.escribirDatos();
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        int anchoEtiquetaFila = obtenerAnchoEtiquetasFilas();
        String sep = " | ";
        int anchoTotal = 0;

        if (tieneEtiquetaFila) {
            out.append(" ".repeat(anchoEtiquetaFila)).append(sep);
        }

        if (tieneEtiquetaCol) {
            for (Etiqueta encabezado : colLabels.keySet()) {
                int anchoColumna = obtenerAnchoColumna(encabezado);
                anchoTotal += anchoColumna + sep.length();
                out.append(String.format("%-" + anchoColumna + "s", encabezado)).append(sep);
            }
        }
        out.append("\n");
        out.append("-".repeat(anchoTotal + anchoEtiquetaFila));
        out.append("\n");

        for (Etiqueta fila : rowLabels.keySet()) {
            if (tieneEtiquetaFila) {
                out.append(String.format("%-" + anchoEtiquetaFila + "s", fila)).append(sep);
            }
            for (Etiqueta columna : colLabels.keySet()) {
                try {
                    Celda valorCelda = obtenerCelda(fila, columna);
                    int anchoColumna = obtenerAnchoColumna(columna);
                    if (valorCelda == null) {
                        out.append(String.format("%-" + anchoColumna + "s", "NA")).append(sep);
                    } else {
                        out.append(String.format("%-" + anchoColumna + "s", valorCelda)).append(sep);
                    }
                } catch (EtiquetaInvalidaException e) {
                    e.getMessage();
                }
            }
            out.append("\n");
        }

        return out.toString();
    }

    private int obtenerAnchoColumna(Etiqueta etiquetaColumna) {
        int anchoMaximo = etiquetaColumna.toString().length();

        for (Etiqueta etiquetaFila : rowLabels.keySet()) {
            try {
                Celda valorCelda = obtenerCelda(etiquetaFila, etiquetaColumna);
                if (valorCelda != null) {
                    int longitudValor = valorCelda.toString().length();
                    if (longitudValor > anchoMaximo) {
                        anchoMaximo = longitudValor;
                    }
                }
            } catch (EtiquetaInvalidaException e) {
                e.printStackTrace();
            }
        }

        return anchoMaximo;
    }

    private int obtenerAnchoEtiquetasFilas() {
        int anchoMaximo = 0;

        if (rowLabels != null) {
            for (Etiqueta etiquetaFila : rowLabels.keySet()) {
                int longitudEtiqueta = etiquetaFila.toString().length();
                if (longitudEtiqueta > anchoMaximo) {
                    anchoMaximo = longitudEtiqueta;
                }
            }
        }

        return anchoMaximo;
    }

    public void convertirABool(String etiquetaCol) {
        Columna<? extends Celda> columna = obtenerColumna(etiquetaCol);
        ColumnaBoolean colBoolean = columna.convertirABoolean();
        int indice = columnas.indexOf(columna);
        columnas.remove(indice);
        columnas.add(indice, colBoolean);
    }

    public void convertirABool(int etiquetaCol) {
        Columna<? extends Celda> columna = obtenerColumna(etiquetaCol);
        ColumnaBoolean colBoolean = columna.convertirABoolean();
        int indice = columnas.indexOf(columna);
        columnas.remove(indice);
        columnas.add(indice, colBoolean);
    }

    public void convertirANum(String etiquetaCol) {
        Columna<? extends Celda> columna = obtenerColumna(etiquetaCol);
        ColumnaNum colBoolean = columna.convertirANum();
        int indice = columnas.indexOf(columna);
        columnas.remove(indice);
        columnas.add(indice, colBoolean);
    }

    public void convertirANum(int etiquetaCol) {
        Columna<? extends Celda> columna = obtenerColumna(etiquetaCol);
        ColumnaNum colBoolean = columna.convertirANum();
        int indice = columnas.indexOf(columna);
        columnas.remove(indice);
        columnas.add(indice, colBoolean);
    }

    public GroupBy groupBy(List<String> etiquetasParaAgrupar) {
        GroupBy agrupador = new GroupBy(this, etiquetasParaAgrupar);
        return agrupador;
    }
}
