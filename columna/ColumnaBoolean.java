package columna;

import java.util.List;

import celda.Celda;
import celda.CeldaBoolean;
import etiqueta.Etiqueta;

public class ColumnaBoolean extends Columna {
    private Etiqueta etiqueta;
    private List<CeldaBoolean> celdas;

    public ColumnaBoolean(Etiqueta etiqueta, List<CeldaBoolean> celdas) {
        this.etiqueta = etiqueta;
        this.celdas = celdas;
    }

    @Override
    public void ordenar(String orden){
        //TO DO
    }

    @Override
    public Celda obtenerValor(Integer indiceFila){
        // Asegúrate de que el índice de fila sea válido
        if (indiceFila >= 0 && indiceFila < celdas.size()) {
            return celdas.get(indiceFila); //celdas.get(indiceFila).getValor()?
        } else {
            throw new IllegalArgumentException("Índice de fila fuera de rango");
        }
    }

    @Override
    public void fijarValor(Integer indiceFIla, Celda valor) {
        // Asegúrate de que el índice de fila sea válido
        if (indiceFIla >= 0 && indiceFIla < celdas.size()) {
            celdas.set(indiceFIla, (CeldaBoolean) valor);
        } else {
            throw new IllegalArgumentException("Índice de fila fuera de rango");
        }
    }

    @Override
    public String tipoDato() {
        return "Booleana".toString();
    }

    public Etiqueta getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(Etiqueta etiqueta) {
        this.etiqueta = etiqueta;
    }

    public List<CeldaBoolean> getCeldas() {
        return celdas;
    }

    public void setCeldas(List<CeldaBoolean> celdas) {
        this.celdas = celdas;
    }

    
}
