package columna;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import celda.Celda;
import celda.CeldaBoolean;

public class ColumnaBoolean extends Columna {
    private List<CeldaBoolean> celdas;

    public ColumnaBoolean(List<CeldaBoolean> celdas) {
        this.celdas = celdas;
    }

    @Override
    public void ordenar(String orden) {
        Collections.sort(celdas);

        if ("descendente".equals(orden)) {
            Collections.reverse(celdas);
        }
    }

    @Override
    public Celda obtenerValor(Integer indiceFila) {
        // Asegúrate de que el índice de fila sea válido
        if (indiceFila >= 0 && indiceFila < celdas.size()) {
            return celdas.get(indiceFila); // celdas.get(indiceFila).getValor()?
        } else {
            throw new IllegalArgumentException("Índice de fila fuera de rango");
        }
    }

    @Override
    public void fijarValor(Integer indiceFila, Object valor) {
        //TODO: exception propia
        if (!(valor instanceof Boolean)) throw new IllegalArgumentException("Tipo de valor invalido"); 
        if (indiceFila >= 0 && indiceFila < celdas.size()) {
            celdas.get(indiceFila).setValor((Boolean) valor);
        } else {
            throw new IllegalArgumentException("Índice de fila fuera de rango");
        }
    }

    @Override
    public String tipoDato() {
        return "Boolean";
    }

    @Override
    public int size() {
        return celdas.size();
    }

    public List<CeldaBoolean> getCeldas() {
        return celdas;
    }

    public void setCeldas(List<CeldaBoolean> celdas) {
        this.celdas = celdas;
    }

    @Override
    public void agregarCelda(Celda celda) {
        // TODO: excepcion ClassCast
        this.celdas.add((CeldaBoolean) celda);
    }

    // para probar cosas:
    public String toString() {
        String inicial = "";
        for (CeldaBoolean celda : celdas) {
            inicial += celda.getValor();
            inicial += ", ";
        }
        return inicial;
    }

    public static void main(String[] args) {
        CeldaBoolean celda1 = new CeldaBoolean(true);
        CeldaBoolean celda2 = new CeldaBoolean(false);
        CeldaBoolean celda3 = new CeldaBoolean(1);
        CeldaBoolean celda4 = new CeldaBoolean(0);
        CeldaBoolean celda5 = new CeldaBoolean((Integer) null);
        CeldaBoolean celda6 = new CeldaBoolean((Boolean) null);

        List<CeldaBoolean> listaCeldas = new ArrayList<>();
        listaCeldas.add(celda1);
        listaCeldas.add(celda2);
        listaCeldas.add(celda3);
        listaCeldas.add(celda4);
        listaCeldas.add(celda5);
        listaCeldas.add(celda6);

        ColumnaBoolean col = new ColumnaBoolean(listaCeldas);

        System.out.println("original: " + col);

        col.ordenar("descendente");

        System.out.println("desc: " + col);

        col.ordenar(null);

        System.out.println("asc: " + col);

    }
}
