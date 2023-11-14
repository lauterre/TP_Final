package etiqueta;

import java.util.Objects;

public class EtiquetaNum extends Etiqueta {
    private int nombre;

    public EtiquetaNum(int nombre) {
        this.nombre = nombre;
    }

    public Integer getNombre() {
        return nombre;
    }

    public void setNombre(int nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return String.valueOf(nombre);
    }

    @Override
    public boolean equals(Object otro){
        if (otro == this) return true;
        if (otro == null || otro.getClass() != this.getClass()) return false;

        EtiquetaNum otro2 = (EtiquetaNum) otro;
        return this.nombre == otro2.getNombre();
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }
    
}
