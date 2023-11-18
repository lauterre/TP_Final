package columna;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Exceptions.ConversionDeTiposException;
import Exceptions.ValorInvalidoException;
import celda.Celda;
import celda.CeldaNum;

public class ColumnaNum extends Columna<CeldaNum> {
    private List<CeldaNum> celdas;

    public ColumnaNum(List<CeldaNum> celdas) {
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
            throw new ValorInvalidoException("Índice de fila fuera de rango");
        }
    }

    @Override
    public void fijarValor(Integer indiceFila, Object valor) {
        if (!(valor instanceof Number))
            throw new ValorInvalidoException("Tipo de valor invalido");
        if (indiceFila >= 0 && indiceFila < celdas.size()) {
            celdas.get(indiceFila).setValor((Number) valor);

        } else {
            throw new ValorInvalidoException("Índice de fila fuera de rango");
        }
    }

    @Override
    public String tipoDato() {
        return "Number";
    }

    @Override
    public int size() {
        return celdas.size();
    }

    public List<CeldaNum> getCeldas() {
        return celdas;
    }

    public void setCeldas(List<CeldaNum> celdas) {
        this.celdas = celdas;
    }

    @Override
    public void agregarCelda(Celda celda) {
        try {
            this.celdas.add((CeldaNum) celda);
        } catch (ConversionDeTiposException e) {
            System.out.println("Error de conversión de tipos: " + e.getMessage());
        }
    }

    // para probar cosas:
    public String toString() {
        String inicial = "";
        for (CeldaNum celda : celdas) {
            inicial += celda;
            inicial += ", ";
        }
        return inicial;
    }

    public static void main(String[] args) {
        CeldaNum celda1 = new CeldaNum(3);
        CeldaNum celda2 = new CeldaNum(1);
        CeldaNum celda3 = new CeldaNum(null);
        CeldaNum celda4 = new CeldaNum(2);

        List<CeldaNum> listaCeldas = new ArrayList<>();
        listaCeldas.add(celda1);
        listaCeldas.add(celda2);
        listaCeldas.add(celda3);
        listaCeldas.add(celda4);

        ColumnaNum col = new ColumnaNum(listaCeldas);

        System.out.println("original: " + col);

        col.ordenar("descendente");

        System.out.println("desc: " + col);

        col.ordenar(null);

        System.out.println("asc: " + col);

    }
}
