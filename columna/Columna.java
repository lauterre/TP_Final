package columna;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import celda.Celda;
import celda.CeldaBoolean;
import celda.CeldaNum;
import celda.CeldaString;

public abstract class Columna<T extends Celda> {

    public abstract void ordenar(String orden);

    public abstract void fijarValor(Integer indiceFila, Object valor);

    public abstract Celda obtenerValor(Integer indiceFila);

    public abstract String tipoDato();

    public abstract int size();

    public abstract List<T> getCeldas();

    public abstract void agregarCelda(Celda celda);

    public abstract List<Integer> indicesNA();

    public abstract double promedio();

    public abstract double mediana();

    public abstract double suma();

    public abstract int count(Object valor);

    public abstract Map<? extends Celda, Integer> count();

    public abstract List<? extends Celda> unique();

    public abstract ColumnaBoolean convertirABoolean();

    public abstract ColumnaNum convertirANum();

    public static Columna<? extends Celda> crear(List<Object> e) {
        if (e.isEmpty()) {
            throw new IllegalArgumentException("La lista de valores no puede estar vacía");
        }
        return construirColumnas(0, e);
    }

    private static Columna<? extends Celda> construirColumnas(int inicio, List<Object> e) {
        if (e.get(inicio) instanceof Boolean) {
            List<CeldaBoolean> celdas = new ArrayList<>();
            for (Object valor : e) {
                CeldaBoolean celda = new CeldaBoolean((Boolean) valor);
                celdas.add(celda);
            }
            ColumnaBoolean columna = new ColumnaBoolean(celdas);
            return columna;
        } else if (e.get(inicio) instanceof Number) {
            List<CeldaNum> celdas = new ArrayList<>();
            for (Object valor : e) {
                CeldaNum celda = new CeldaNum((Number) valor);
                celdas.add(celda);
            }
            ColumnaNum columna = new ColumnaNum(celdas);
            return columna;
        } else if (e.get(inicio) instanceof String) {
            List<CeldaString> celdas = new ArrayList<>();
            for (Object valor : e) {
                CeldaString celda = new CeldaString((String) valor);
                celdas.add(celda);
            }
            ColumnaString columna = new ColumnaString(celdas);
            return columna;
        } else if (e.get(inicio) == null) {
            return construirColumnas(inicio + 1, e);
        } else {
            throw new IllegalArgumentException("Tipo de valor no compatible para crear celda");
        }
    }

    public static ColumnaNum crear(Number[] e) {
        List<CeldaNum> celdas = new ArrayList<>();
        for (Number valor : e) {
            celdas.add(new CeldaNum(valor));
        }
        return new ColumnaNum(celdas);
    }

    public static ColumnaString crear(String[] e) {
        List<CeldaString> celdas = new ArrayList<>();
        for (String valor : e) {
            celdas.add(new CeldaString(valor));
        }
        return new ColumnaString(celdas);
    }

    public static ColumnaBoolean crear(Boolean[] e) {
        List<CeldaBoolean> celdas = new ArrayList<>();
        for (Boolean valor : e) {
            celdas.add(new CeldaBoolean(valor));
        }
        return new ColumnaBoolean(celdas);
    }

    protected static <K, V extends Comparable<? super V>> Map<K, V> ordenarPorValor(Map<K, V> mapa) {
        List<Map.Entry<K, V>> lista = new ArrayList<>(mapa.entrySet());

        // Ordenar la lista usando un Comparator que compara los valores
        Collections.sort(lista, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        // Reconstruir un nuevo Map ordenado por valores
        Map<K, V> mapaOrdenado = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : lista) {
            mapaOrdenado.put(entry.getKey(), entry.getValue());
        }

        return mapaOrdenado;
    }

}
