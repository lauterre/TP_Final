package columna;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Exceptions.ConversionDeTiposException;
import Exceptions.ValorInvalidoException;
import celda.Celda;
import celda.CeldaString;
import celda.CeldaNum;

public class ColumnaString extends Columna<CeldaString> {
    private List<CeldaString> celdas;

    public ColumnaString(List<CeldaString> celdas) {
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
        if (indiceFila >= 0 && indiceFila < celdas.size()) {
            return celdas.get(indiceFila);
        } else {
            throw new ValorInvalidoException("Índice de fila fuera de rango");
        }
    }

    @Override
    public void fijarValor(Integer indiceFila, Object valor) {
        if (!(valor instanceof String))
            throw new ValorInvalidoException("Tipo de valor invalido");
        if (indiceFila >= 0 && indiceFila < celdas.size()) {
            celdas.get(indiceFila).setValor((String) valor);
        } else {
            throw new ValorInvalidoException("Índice de fila fuera de rango");
        }
    }

    @Override
    public ColumnaNum convertirANum() {
        List<CeldaNum> numeros = new ArrayList<>();
        for (CeldaString celdaString : celdas) {
            Number numero;
            if (celdaString.getValor() == null || celdaString.getValor() == "") {
                numero = null;
            } else {
                numero = Double.parseDouble(celdaString.getValor());
            }
            CeldaNum celdaNum = new CeldaNum(numero);
            numeros.add(celdaNum);
        }
        return new ColumnaNum(numeros);
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
        throw new UnsupportedOperationException("No puede calcularse el promedio sobre valores de tipo cadena");
    }

    @Override
    public double mediana() {
        throw new UnsupportedOperationException("No puede calcularse la mediana sobre valores de tipo cadena");
    }

    @Override
    public double suma() {
        throw new UnsupportedOperationException("No puede calcularse la suma sobre valores de tipo cadena");
    }

    @Override
    public int count(Object valor) {
        int contador = 0;
        for (CeldaString celda : celdas) {
            if (celda.getValor().equals(valor)) {
                contador += 1;
            }
        }
        return contador;
    }

    @Override
    public Map<CeldaString, Integer> count() {
        Map<CeldaString, Integer> contador = new HashMap<>();
        for (CeldaString celda : celdas) {
            contador.put(celda, contador.getOrDefault(celda, 0) + 1);
        }
        return ordenarPorValor(contador);
    }

    @Override
    public List<CeldaString> unique() {
        List<CeldaString> unicos = new ArrayList<>();
        for (CeldaString celda : celdas) {
            if (!(unicos.contains(celda))) {
                unicos.add(celda);
            }
        }
        return unicos;
    }

    @Override
    public String tipoDato() {
        return "String";
    }

    @Override
    public int size() {
        return celdas.size();
    }

    public List<CeldaString> getCeldas() {
        return celdas;
    }

    public void setCeldas(List<CeldaString> celdas) {
        this.celdas = celdas;
    }

    @Override
    public void agregarCelda(Celda celda) {
        try {
            this.celdas.add((CeldaString) celda);
        } catch (ConversionDeTiposException e) {
            System.out.println("Error de conversión de tipos: " + e.getMessage());
        }
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

    @Override
    public ColumnaBoolean convertirABoolean() {
        throw new UnsupportedOperationException("No es posible convertir una columna de cadenas a una de booleanos");
    }

}
