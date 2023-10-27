package etiqueta;

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
}
