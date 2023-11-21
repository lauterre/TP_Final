package columna;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Exceptions.ConversionDeTiposException;
import Exceptions.ValorInvalidoException;
import celda.Celda;
import celda.CeldaBoolean;
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
        for (CeldaNum celdaNum : celdas) {
            if (!(celdaNum.isNA())) {
                suma += celdaNum.getValor().doubleValue();
                cantCeldasNoNA += 1;
            }
        }
        return suma / cantCeldasNoNA;
    }

    @Override
    public double mediana() {
        List<Double> valores = new ArrayList<>();
        for (CeldaNum celdaNum : celdas) {
            if (!celdaNum.isNA()) {
                valores.add(celdaNum.getValor().doubleValue());
            }
        }
        Collections.sort(valores);
        int size = valores.size();
        if (size % 2 != 0) {
            return valores.get(size / 2);
        } else {
            return (valores.get((size - 1) / 2) + valores.get(size / 2)) / 2.0;
        }
    }

    @Override
    public double suma() {
        double suma = 0;
        for (CeldaNum celdaNum : celdas) {
            if (!(celdaNum.isNA())) {
                suma += celdaNum.getValor().doubleValue();
            }
        }
        return suma;
    }

    @Override
    public int count(Object valor) {
        int contador = 0;
        for (CeldaNum celda : celdas) {
            if (celda.getValor() == valor || celda.getValor().equals(valor)) {
                contador += 1;
            }
        }
        return contador;
    }

    @Override
    public Map<CeldaNum, Integer> count() {
        Map<CeldaNum, Integer> contador = new HashMap<>();
        for (CeldaNum celda : celdas) {
            contador.put(celda, contador.getOrDefault(celda, 0) + 1);
        }
        return ordenarPorValor(contador);
    }

    @Override
    public List<CeldaNum> unique() {
        List<CeldaNum> unicos = new ArrayList<>();
        for (CeldaNum celda : celdas) {
            if (!(unicos.contains(celda))) {
                unicos.add(celda);
            }
        }
        return unicos;
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

    @Override
    public ColumnaBoolean convertirABoolean() {
        List<CeldaBoolean> booleanas = new ArrayList<>();
        for (CeldaNum celdaNum : celdas) {
            CeldaBoolean bool = celdaNum.transformarABoolean();
            booleanas.add(bool);
        }
        return new ColumnaBoolean(booleanas);
    }

    @Override
    public ColumnaNum convertirANum() {
        return this;
    }

    public double max() {
        double maximo = celdas.stream()
                .filter(celda -> !celda.isNA())
                .map(celda -> Double.valueOf((double) celda.getValor()))
                .max(Comparator.comparing(Double::doubleValue)).get();
        return maximo;
    }

    public double min() {
        double minimo = celdas.stream()
                .filter(celda -> !celda.isNA())
                .map(celda -> Double.valueOf((double) celda.getValor()))
                .filter(valor -> !(valor == null))
                .max(Comparator.comparing(Double::doubleValue)).get();
        return minimo;
    }

    public double varianza() {
        double promedio = promedio();
        double sumaDiferenciasCuadradas = celdas.stream()
                .filter(celda -> !celda.isNA())
                .map(celda -> Double.valueOf((double) celda.getValor()))
                .mapToDouble(valor -> Math.pow(valor - promedio, 2))
                .sum();

        return sumaDiferenciasCuadradas / celdas.size();
    }

    public double desvioEstandar() {
        double varianza = varianza();
        return Math.sqrt(varianza);
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
}
