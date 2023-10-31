package etiqueta;

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
    
}
