package matriz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.naming.LinkRef;

import Exceptions.EtiquetaInvalidaException;
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
    boolean tieneEtiquetaCol = false;
    boolean tieneEtiquetaFila = false;

    public Tabla(int cantidadColumnas){
        // TODO : Exceptions
        columnas = new ArrayList<>();
        colLabels = new LinkedHashMap<>();
        rowLabels = new LinkedHashMap<>();
    }

    public Tabla(int cantidadColumnas, Etiqueta[] etiquetas) {
        this(cantidadColumnas);
        if (cantidadColumnas != etiquetas.length)
            throw new IllegalArgumentException("La longitud de etiquetas no coincide.");
        setEtiquetasColumnas(etiquetas);
    }

    public Tabla (Tabla m){
        columnas = m.columnas;
        colLabels = new LinkedHashMap<Etiqueta, Integer>();
        colLabels.putAll(m.colLabels);
        rowLabels = new LinkedHashMap<Etiqueta, Integer>();
        rowLabels.putAll(m.rowLabels);
    }

    public Tabla copia(Tabla origen){
        Tabla nueva = new Tabla(this);
        nueva.columnas = new ArrayList<>();
        for (Columna columna : columnas){
            if (columna instanceof ColumnaNum){ //TODO: demas clases
                ColumnaNum columnaNum = (ColumnaNum) columna;
                Columna columnaNueva = new ColumnaNum(new ArrayList<CeldaNum>());
                for (CeldaNum celda : columnaNum.getCeldas()){
                    columnaNueva.agregarCelda(celda.copia());
                }
            }
        }
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

        for (int i = inicioFila; i < cantidadColumnas; i++) {
            List<Celda> celdas = new ArrayList<>();
            for (int j = inicioColumna; j < matriz.length; j++) {
                Celda celda = crearCelda(matriz[j][i]);
                celdas.add(celda);
            }
            Columna columna = crearColumna(celdas);
            this.columnas.add(columna);
            if (tieneEncabezadosColumnas) {
                Etiqueta etiqueta = new EtiquetaString(matriz[0][i].toString());
                this.colLabels.put(etiqueta, i - inicioFila);
            } else {
                Etiqueta etiqueta = new EtiquetaNum(i - inicioFila);
                this.colLabels.put(etiqueta, i - inicioFila);
            }
        }

        for (int i = inicioColumna; i < matriz.length; i++) {
            Etiqueta etiqueta;
            if (tieneEncabezadosFilas) {
                etiqueta = new EtiquetaString(matriz[i][0].toString()); 
            } else {
                etiqueta = new EtiquetaNum(i - inicioColumna);
            }
            this.rowLabels.put(etiqueta, i - inicioColumna);
        }
    }

    public Tabla(int[][] matriz, boolean tieneEncabezadosColumnas, boolean tieneEncabezadosFilas) {
        this(convertirMatrizANumber(matriz),tieneEncabezadosColumnas, tieneEncabezadosFilas);
    }

    public Tabla(float[][] matriz, boolean tieneEncabezadosColumnas, boolean tieneEncabezadosFilas) {
        this(convertirMatrizANumber(matriz),tieneEncabezadosColumnas, tieneEncabezadosFilas);
    }

    public Tabla(double[][] matriz, boolean tieneEncabezadosColumnas, boolean tieneEncabezadosFilas) {
        this(convertirMatrizANumber(matriz),tieneEncabezadosColumnas, tieneEncabezadosFilas);
    }

    public Tabla(String rutaArchivo, boolean tieneEncabezadosColumnas, boolean tieneEncabezadosFilas) {
        this.tieneEtiquetaCol = tieneEncabezadosColumnas;
        this.tieneEtiquetaFila = tieneEncabezadosFilas;
        LectorCSV lector = new LectorCSV();
        try {
            List<String> lineas = lector.leer(rutaArchivo);
            List<Columna> cols = lector.parserColumnas(lineas, tieneEncabezadosColumnas);
            this.columnas = cols;
            if (tieneEncabezadosColumnas) setEtiquetasColumnas(lector.getEncabezados());
            Etiqueta[] etiquetasFilas = new Etiqueta[this.columnas.get(0).size()];
            for (int i = 0; i < this.columnas.get(0).size(); i ++) {
                if (tieneEncabezadosFilas) {
                    EtiquetaString etiqueta = new EtiquetaString(this.columnas.get(0).obtenerValor(i).getValor().toString());
                    etiquetasFilas[i] = etiqueta;
                    this.columnas.remove(0);
                } else {
                    EtiquetaNum etiqueta = new EtiquetaNum(i);
                    etiquetasFilas[i] = etiqueta;
                }
            }
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

    public List<Etiqueta> getEtiquetasColumnas() {
        List<Etiqueta> etiquetasColumnas = new ArrayList<>();
        for (Etiqueta etiqueta : colLabels.keySet()) {
            etiquetasColumnas.add(etiqueta);
        }
        return etiquetasColumnas;
    }

    public List<Etiqueta> getEtiquetasFilas() {
        List<Etiqueta> etiquetasFilas = new ArrayList<>();
        for (Etiqueta etiqueta : rowLabels.keySet()) {
            etiquetasFilas.add(etiqueta);
        }
        return etiquetasFilas;
    }

    // sería mejor que devuelva un iterable --> Lo incluí en los métodos anteriores, dejé estos por las dudas
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

    public Columna obtenerColumna(String etiquetaColumnaNombre){
        try {
            Etiqueta etiquetaColumna = getEtiquetaColumna(etiquetaColumnaNombre);
            Columna columnaPedida = columnas.get(colLabels.get(etiquetaColumna));
            return columnaPedida;
        } catch (EtiquetaInvalidaException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    //en caso de que la etiqueta sea numerica
    public Columna obtenerColumna(Integer etiquetaColumnaNombre){
        try {
            Etiqueta etiquetaColumna = getEtiquetaColumna(etiquetaColumnaNombre);
            Columna columnaPedida = columnas.get(colLabels.get(etiquetaColumna));
            return columnaPedida;
        } catch (EtiquetaInvalidaException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<Celda> obtenerFila(Etiqueta etiqueta) {
        List<Celda> filaPedida = new ArrayList<>();
        
        for (Etiqueta etiquetaColumna : colLabels.keySet()) {
            filaPedida.add(obtenerCelda(etiqueta, etiquetaColumna));
        }

        return filaPedida;
    }

    private void generarRowLabelsOrdenado(List<Etiqueta> orden){
        for (Etiqueta etiqueta : orden){
            Integer indice = rowLabels.get(etiqueta);
            rowLabels.remove(etiqueta);
            rowLabels.put(etiqueta, indice);
        }
    }


    public void ordenar(Etiqueta etiquetaColumna, String orden) {
        Columna columna = columnas.get(colLabels.get(etiquetaColumna));
        columna.ordenar(orden);
    }

    //TODO: parece que ningun metodo funciona por lo de abajo, :)
//tener que pasarle una instancia de etiqueta es incomodo para trabajar, no es mejor que reciba un string o un int? (en los demás métodos también)
    public void eliminarColumna(String etiquetaNombre) {
        try {
            Etiqueta etiqueta = getEtiquetaColumna(etiquetaNombre);
            Columna columna = obtenerColumna(etiquetaNombre);
            this.columnas.remove(columna);
            this.colLabels.remove(etiqueta);
        } catch (EtiquetaInvalidaException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    //en caso de que la etiqueta sea numerica
    public void eliminarColumna(Integer etiquetaNombre) {
        try {
            Etiqueta etiqueta = getEtiquetaColumna(etiquetaNombre);
            Columna columna = obtenerColumna(etiquetaNombre);
            this.columnas.remove(columna);
            this.colLabels.remove(etiqueta);
        } catch (EtiquetaInvalidaException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    //TODO: metodos que usen el indice de la etiqueta, eliminarColumnaPorIndice(int indice), etc.

    public void eliminarFila(Etiqueta etiqueta) {
        int index = rowLabels.get(etiqueta);
        for (Columna columna : columnas) {
            columna.eliminarCelda(index);
        }
        this.rowLabels.remove(etiqueta);
    }

    public Tabla filtrarColumnas(List<Etiqueta> etiquetas) {
        Tabla copiaTabla = copia(this);
        for (Etiqueta etiqueta : etiquetas) {
            copiaTabla.eliminarColumna((String) etiqueta.getNombre());
        }
        return copiaTabla;
    }

    public Tabla filtrarFilas(List<Etiqueta> etiquetas) {
        Tabla copiaTabla = copia(this);
        for (Etiqueta etiqueta : copiaTabla.getEtiquetasFilas()) {
            if (!(etiquetas.contains(etiqueta))) {
                copiaTabla.eliminarFila(etiqueta);
            }
        }
        return copiaTabla;
    }

    private Etiqueta getEtiquetaColumna(String valor) throws EtiquetaInvalidaException {
        for (Etiqueta etiqueta : this.colLabels.keySet()) {
            if(etiqueta.getNombre().equals(valor)) {
                return etiqueta;
            }
        }
        throw new EtiquetaInvalidaException();
    }

    private Etiqueta getEtiquetaColumna(Integer valor) throws EtiquetaInvalidaException {
        for (Etiqueta etiqueta : this.colLabels.keySet()) {
            if(etiqueta.getNombre().equals(valor)) {
                return etiqueta;
            }
        }
        throw new EtiquetaInvalidaException();
    }

    private Etiqueta getEtiquetaFila(String valor) throws EtiquetaInvalidaException {
        for (Etiqueta etiqueta : this.rowLabels.keySet()) {
            if(etiqueta.getNombre().equals(valor)) {
                return etiqueta;
            }
        }
        throw new EtiquetaInvalidaException();
    }

    private Etiqueta getEtiquetaFila(Integer valor) throws EtiquetaInvalidaException {
        for (Etiqueta etiqueta : this.rowLabels.keySet()) {
            if(etiqueta.getNombre().equals(valor)) {
                return etiqueta;
            }
        }
        throw new EtiquetaInvalidaException();
    }

    private Celda crearCelda(Object valor) {
        Celda celda;
        if (valor instanceof Boolean) {
            celda = new CeldaBoolean((Boolean) valor);
        } else if (valor instanceof Number) {
            celda = new CeldaNum((Number) valor);
        } else if (valor instanceof String) {
            celda = new CeldaString((String) valor);
        } else {
            //TODO: excepcion propia
            throw new IllegalArgumentException("Tipo de datos no compatible en la matriz");
        }
        return celda;
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

    //TODO: esto hay que mejorarlo
    @Override
    public String toString() {
        String out;
        if (tieneEtiquetaFila) {
            out = "      | ";
        } else {
            out = "";
        }
        String sep = " | ";
        if (tieneEtiquetaCol){
            for(Etiqueta label : colLabels.keySet()) {
                out += label + sep;
            }
        }
        out += "\n";
        for(Etiqueta fila : rowLabels.keySet()) {
            if (tieneEtiquetaFila) out += fila + sep;
            for(Etiqueta columna : colLabels.keySet()) {
                out += obtenerCelda(fila, columna);
                out += sep;
            }
            out += "\n";
        }
        return out;
    }

    public Tabla concatenarTabla(Tabla otraTabla) {
        List<Etiqueta> etiquetasDistintas = new ArrayList<>();
        int numeroFilasActuales = obtenerCantidadFilas();
        int numeroFilasNuevas = otraTabla.obtenerCantidadFilas();

        List<Etiqueta> etiquetasActuales = this.getEtiquetasColumnas();
        List<Etiqueta> etiquetasOtraTabla = otraTabla.getEtiquetasColumnas();
        for (Etiqueta etiqueta : etiquetasOtraTabla) {
            if (!(etiquetasActuales.contains(etiqueta))) {
                etiquetasDistintas.add(etiqueta);
            }
        }
        Tabla otraTablaFiltrada = otraTabla.filtrarColumnas(etiquetasDistintas);

        int numeroFilasTotales = numeroFilasActuales + numeroFilasNuevas;

        Etiqueta[] etiquetasNuevaTabla = new Etiqueta[obtenerCantidadColumnas()];
        for (int i=0; i < etiquetasActuales.size(); i++) {
            etiquetasNuevaTabla[i] = etiquetasActuales.get(i);
        }

        Tabla nuevaTabla = new Tabla(obtenerCantidadColumnas(), etiquetasNuevaTabla);
        nuevaTabla.colLabels = this.colLabels;
        nuevaTabla.tieneEtiquetaCol = this.tieneEtiquetaCol;
        if (this.tieneEtiquetaFila | otraTabla.tieneEtiquetaFila) {
            nuevaTabla.tieneEtiquetaFila = true;
            List<Etiqueta> etiquetasNuevas = otraTabla.getEtiquetasFilas();
            for (int i=0; i < otraTabla.obtenerCantidadFilas(); i++) {
                nuevaTabla.rowLabels.put(etiquetasNuevas.get(i), numeroFilasActuales-1+i);
            }
        } else {
            for (int i=0; i < numeroFilasTotales; i++) {
                Etiqueta etiqueta = new EtiquetaNum(i);
                nuevaTabla.rowLabels = new LinkedHashMap<>();
                nuevaTabla.rowLabels.put(etiqueta, i);
            }
        }
        for (Etiqueta etiquetaColumna : nuevaTabla.getEtiquetasColumnas()) {
            Columna columnaActual = nuevaTabla.obtenerColumna((String) etiquetaColumna.getNombre());
            String tipoDato = columnaActual.tipoDato();
            Columna nuevaColumna;
            
            if (!etiquetasOtraTabla.contains(etiquetaColumna)) {
                if (tipoDato == "String") {
                    List<CeldaString> nuevaLista = new ArrayList<>();
                    for (int i=0; i<numeroFilasNuevas;i++){
                        nuevaLista.add(null);
                    }
                    nuevaColumna = new ColumnaString(nuevaLista);
                } else if (tipoDato == "Numerica") {
                    List<CeldaNum> nuevaLista = new ArrayList<>();
                    for (int i=0; i<numeroFilasNuevas;i++){
                        nuevaLista.add(null);
                    }
                    nuevaColumna = new ColumnaNum(nuevaLista);
                } else {
                    List<CeldaBoolean> nuevaLista = new ArrayList<>();
                    for (int i=0; i<numeroFilasNuevas;i++){
                        nuevaLista.add(null);
                    }
                    nuevaColumna = new ColumnaBoolean(nuevaLista);
                }
            } else {
                Columna nuevosValores = otraTabla.obtenerColumna((String) etiquetaColumna.getNombre());

                if (tipoDato == "String") {
                    List<CeldaString> nuevaLista = new ArrayList<>();
                    for (Object celdaVieja : columnaActual.getCeldas()) {
                        CeldaString celdaVieja2 = (CeldaString) celdaVieja;
                        nuevaLista.add(celdaVieja2);
                    }
                    for (Object celdaNueva : nuevosValores.getCeldas()) {
                        CeldaString celdaNueva2 = (CeldaString) celdaNueva;
                        nuevaLista.add(celdaNueva2);
                    }
                    nuevaColumna = new ColumnaString(nuevaLista);
                } else if (tipoDato == "Numerica") {
                    List<CeldaNum> nuevaLista = new ArrayList<>();
                    for (Object celdaVieja : columnaActual.getCeldas()) {
                        CeldaNum celdaVieja2 = (CeldaNum) celdaVieja;
                        nuevaLista.add(celdaVieja2);
                    }
                    for (Object celdaNueva : nuevosValores.getCeldas()) {
                        CeldaNum celdaNueva2 = (CeldaNum) celdaNueva;
                        nuevaLista.add(celdaNueva2);
                    }
                    nuevaColumna = new ColumnaNum(nuevaLista);
                } else {
                    List<CeldaBoolean> nuevaLista = new ArrayList<>();
                    for (Object celdaVieja : columnaActual.getCeldas()) {
                        CeldaBoolean celdaVieja2 = (CeldaBoolean) celdaVieja;
                        nuevaLista.add(celdaVieja2);
                    }
                    for (Object celdaNueva : nuevosValores.getCeldas()) {
                        CeldaBoolean celdaNueva2 = (CeldaBoolean) celdaNueva;
                        nuevaLista.add(celdaNueva2);
                    }
                    nuevaColumna = new ColumnaBoolean(nuevaLista);
                }   
            }
            nuevaTabla.columnas.add(nuevaColumna);
        } 
        return nuevaTabla;
    }    

    public <T> Map<T, Tabla> groupByColumna(Etiqueta etiqueta) {
        if (getEtiquetasColumnas().contains(columna)){
            Columna laColumnaIndicada = obtenerColumna((String) etiqueta.getNombre());
            List<Celda> celdasColumna = laColumnaIndicada.getCeldas();
            List<Etiqueta> etiquetasFilas = getEtiquetasFilas();

            Map<Etiqueta, Celda> valores = new HashMap<>();
            Map mapaAgrupado;
            Map<Object, Tabla> resultadoAgrupado;
            
            for (int i=0; i< celdasColumna.size(); i++) {
                valores.put(etiquetasFilas.get(i), celdasColumna.get(i));
            }
    
            mapaAgrupado = valores.entrySet()
                .stream()
                .collect(Collectors.groupingBy(
                    entry -> entry.getValue().getValor(),
                    Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
                ));

            if (laColumnaIndicada.tipoDato() == "String") {
                for (String key : mapaAgrupado.keySet()) {
                    Set filasGrupo = mapaAgrupado.get(key).keySet();
                    List<Etiqueta> filasGrupoLista = (List<Etiqueta>) filasGrupo;
                    Tabla subsetAgrupado = filtrarFilas(filasGrupoLista);
                    resultadoAgrupado.put(key, subsetAgrupado);
                }
            } else if (laColumnaIndicada.tipoDato() == "Numerica") {
                for (int key : mapaAgrupado.keySet()) {
                    Set filasGrupo = mapaAgrupado.get(key).keySet();
                    List<Etiqueta> filasGrupoLista = (List<Etiqueta>) filasGrupo;
                    Tabla subsetAgrupado = filtrarFilas(filasGrupoLista);
                    resultadoAgrupado.put(key, subsetAgrupado);
                }
            }
            return resultadoAgrupado;
        } else {
            // TODO: Algun tipo de IndexError propio
            throw new IllegalArgumentException("La etiqueta no se encuentra en las columnas.");
        }
    }

    public <T> Map<Map<Etiqueta, T>, Tabla> groupByRecursivo(List<Etiqueta> etiquetas) {
        if (etiquetas.size() == 1) {
            Etiqueta ultimaEtiqueta = (Etiqueta) etiquetas.get(0);
            Map<T, Tabla> agrupadoUnaColumna = groupByColumna(ultimaEtiqueta);
            Map<Map<Etiqueta, T>, Tabla> resultadoProvisorio = new HashMap<>();
            for (T key : agrupadoUnaColumna.keySet()) {
                Map<Etiqueta, T> mapaEtiquetaValor = new HashMap<>();
                mapaEtiquetaValor.put(ultimaEtiqueta, key);
                resultadoProvisorio.put(mapaEtiquetaValor, agrupadoUnaColumna.get(key));
            }
            return resultadoProvisorio;
        } else {
            Map<Map<Etiqueta, T>, Tabla> resultadoProvisorio = new HashMap<>();
            Etiqueta etiquetaActual = etiquetas.get(0);
            etiquetas.remove(0);
            resultadoProvisorio = groupByRecursivo(etiquetas);

            Map<Map<Etiqueta, T>, Tabla> nuevoResultadoProvisorio = new HashMap<>();
            for (Map.Entry entry : resultadoProvisorio.entrySet()) {
                Tabla valoresAgrupados = (Tabla) entry.getValue();
                Map<Etiqueta, T> mapaEtiquetaValor = (Map<Etiqueta, T>) entry.getKey();
                Map<T, Tabla> agrupadoNuevaColumna = valoresAgrupados.groupByColumna(etiquetaActual);

                for (Map.Entry entryInterior : agrupadoNuevaColumna.entrySet()) {
                    Tabla valoresAgrupadosInterno = (Tabla) entryInterior.getValue();
                    T valorEspecifico = (T) entryInterior.getKey();

                    Map<Etiqueta, T> nuevoMapaEtiquetaValor = new HashMap();
                    for (Etiqueta etiquetaInterior : mapaEtiquetaValor.keySet()) {
                        nuevoMapaEtiquetaValor.put(etiquetaInterior, mapaEtiquetaValor.get(etiquetaInterior));
                    }
                    nuevoMapaEtiquetaValor.put(etiquetaActual, valorEspecifico);
                    nuevoResultadoProvisorio.put(nuevoMapaEtiquetaValor, valoresAgrupadosInterno);
                }
            }
            return nuevoResultadoProvisorio;
        }
    } 

    public <T> Map<Map<Etiqueta, T>, Tabla> groupByMultiple(List<Etiqueta> etiquetas) {
        if (getEtiquetasColumnas().containsAll(columnas)) {
            Map<Map<Etiqueta, T>, Tabla> resultadoAgrupado = groupByRecursivo(etiquetas); 
            return resultadoAgrupado;
        } else {
            // TODO: Algún tipo de IndexError
            throw new IllegalArgumentException("No todas las etiquetas se encuentran en las columnas.");
        }
    }

    public Tabla filtrar ( Etiqueta col, char operador, Celda valor){
        Map<Character, Predicate<Celda>> operadores = new HashMap<>();
        operadores.put('<', e -> e.compareTo(valor) < 0);
        operadores.put('>', e -> e.compareTo(valor) > 0);
        operadores.put('=', e -> e.compareTo(valor) == 0);
        operadores.put('!', e -> e.compareTo(valor) != 0);

        Predicate<Celda> condicion = operadores.get(operador);
        List<Etiqueta> salida = new ArrayList<>();
        
        if (condicion != null){
            for(Etiqueta rowLabel : rowLabels.keySet()){
                Celda valorAComparar = obtenerCelda(rowLabel, col);
                if (condicion.test(valorAComparar)){
                    salida.add(rowLabel);
                }                
            }
        }else{
            throw new IllegalArgumentException();
        }
        Tabla nueva = new Tabla(this);
        nueva.generarRowLabelsOrdenado(salida);
        return nueva; 

    }

    public static void main(String[] args) {
        String[][] matriz = new String[3][3];

        matriz[0][0] = "Nombre";
        matriz[0][1] = "Apellido";
        matriz[0][2] = "Edad";
        matriz[1][0] = "Martín";
        matriz[1][1] = "Gutierrez";
        matriz[1][2] = "23";
        matriz[2][0] = "Javier";
        matriz[2][1] = "Moreno";
        matriz[2][2] = "34";

        
        // matriz[0][0] = 0;
        // matriz[0][1] = 1;
        // matriz[0][2] = 2;
        // matriz[1][0] = 3;
        // matriz[1][1] = 4;
        // matriz[1][2] = 5;
        // matriz[2][0] = 6;
        // matriz[2][1] = 7;
        // matriz[2][2] = 8;

        Tabla tabla = new Tabla(matriz, true, false);

        System.out.println(tabla.toString());
        System.out.println("etiquetas de columna");
        System.out.println(tabla.colLabels.keySet());
        System.out.println(tabla.colLabels.values());

        
        System.out.println("etiquetas de fila");
        System.out.println(tabla.rowLabels.keySet());
        System.out.println(tabla.rowLabels.values());

        System.out.println("Borro la columna edad");
        tabla.eliminarColumna("Edad");
        System.out.println(tabla.toString());

        System.out.println("etiquetas de columna");
        System.out.println(tabla.colLabels.keySet());
        System.out.println(tabla.colLabels.values());

        
        System.out.println("etiquetas de fila");
        System.out.println(tabla.rowLabels.keySet());
        System.out.println(tabla.rowLabels.values());

        System.out.println("columna Apellido: " + tabla.obtenerColumna("Apellido"));
    }
}
