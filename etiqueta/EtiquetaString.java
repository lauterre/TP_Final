package etiqueta;

import java.util.Objects;

public class EtiquetaString extends Etiqueta {
    private String nombre;

    public EtiquetaString(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public boolean equals(Object otro){
        if (otro == this) return true;
        if (otro == null || otro.getClass() != this.getClass()) return false;

        EtiquetaString otro2 = (EtiquetaString) otro;
        return this.nombre.equals(otro2.getNombre());
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }
}
