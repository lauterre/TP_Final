package columna;

import java.util.List;

import celda.Celda;
import celda.CeldaNum;
import etiqueta.Etiqueta;

public class ColumnaNum extends Columna {
    private Etiqueta etiqueta;
    private List<CeldaNum> celdas;

    public ColumnaNum(Etiqueta etiqueta, List<CeldaNum> celdas) {
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
            celdas.set(indiceFIla, (CeldaNum) valor);
        } else {
            throw new IllegalArgumentException("Índice de fila fuera de rango");
        }
    }

    @Override
    public String tipoDato() {
        return "Numerica".toString();
    }

    public Etiqueta getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(Etiqueta etiqueta) {
        this.etiqueta = etiqueta;
    }

    public List<CeldaNum> getCeldas() {
        return celdas;
    }

    public void setCeldas(List<CeldaNum> celdas) {
        this.celdas = celdas;
    }

    
}
