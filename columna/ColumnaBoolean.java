package columna;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Exceptions.ConversionDeTiposException;
import Exceptions.ValorInvalidoException;
import celda.Celda;
import celda.CeldaBoolean;
import celda.CeldaNum;
import celda.CeldaString;

public class ColumnaBoolean extends Columna<CeldaBoolean> {
    private List<CeldaBoolean> celdas;

    public ColumnaBoolean(List<CeldaBoolean> celdas) {
        this.celdas = celdas;
    }

    @Override
    public ColumnaBoolean convertirABoolean() {
        return this;
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
            throw new ValorInvalidoException("Indice de fila fuera de rango");
        }
    }

    @Override
    public void fijarValor(Integer indiceFila, Object valor) {
        if (!(valor instanceof Boolean))
            throw new ValorInvalidoException("Tipo de valor invalido");
        if (indiceFila >= 0 && indiceFila < celdas.size()) {
            celdas.get(indiceFila).setValor((Boolean) valor);
        } else {
            throw new ValorInvalidoException("Índice de fila fuera de rango");
        }
    }

    @Override
    public List<Integer> indicesNA() {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < celdas.size(); i++) {
            if (celdas.get(i).isNA()) {
                indices.add(i);
            }
        }
        return indices;
    }

    @Override
    public double promedio() {
        double suma = 0;
        int cantCeldasNoNA = 0;
        for (CeldaBoolean celda : celdas) {
            if (!(celda.isNA())) {
                int valor;
                if (celda.getValor() == false) {
                    valor = 0;
                } else {
                    valor = 1;
                }
                suma += valor;
                cantCeldasNoNA += 1;
            }
        }
        return suma / cantCeldasNoNA;
    }

    @Override
    public double suma() {
        double suma = 0;
        for (CeldaBoolean celda : celdas) {
            if (!(celda.isNA())) {
                int valor;
                if (celda.getValor() == false) {
                    valor = 0;
                } else {
                    valor = 1;
                }
                suma += valor;
            }
        }
        return suma;
    }

    @Override
    public double mediana() {
        throw new UnsupportedOperationException("No puede calcularse la mediana sobre valores de tipo boolean");
    }

    @Override
    public int count(Object valor) {
        int contador = 0;
        for (CeldaBoolean celda : celdas) {
            if (celda.getValor() == valor || celda.getValor().equals(valor)) {
                contador += 1;
            }
        }
        return contador;
    }

    @Override
    public Map<CeldaBoolean, Integer> count() {
        Map<CeldaBoolean, Integer> contador = new HashMap<>();
        for (CeldaBoolean celda : celdas) {
            contador.put(celda, contador.getOrDefault(celda, 0) + 1);
        }
        return ordenarPorValor(contador);
    }

    @Override
    public List<CeldaBoolean> unique() {
        List<CeldaBoolean> unicos = new ArrayList<>();
        for (CeldaBoolean celda : celdas) {
            if (!(unicos.contains(celda))) { // OJO
                unicos.add(celda);
            }
        }
        return unicos;
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
        try {
            this.celdas.add((CeldaBoolean) celda);
        } catch (ConversionDeTiposException e) {
            System.out.println("Error de conversión de tipos: " + e.getMessage());
        }
    }

    @Override
    public ColumnaNum convertirANum() {
        List<CeldaNum> numeros = new ArrayList<>();
        for (CeldaBoolean celdaBoolean : celdas) {
            Number numero;
            if (celdaBoolean.getValor() == false) {
                numero = 0;
            } else {
                numero = 1;
            }
            CeldaNum celdaNum = new CeldaNum(numero);
            numeros.add(celdaNum);
        }
        return new ColumnaNum(numeros);
    }

    @Override
    public String toString() {
        String out = "[";
        for (int i = 0; i < celdas.size(); i++) {
            out += celdas.get(i).toString();
            if (!(i == celdas.size() - 1))
                out += ", ";
        }
        out += "]";
        return out;
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

        // col.ordenar("descendente");

        // System.out.println("desc: " + col);

        // col.ordenar(null);

        // System.out.println("asc: " + col);

    }
}
