package etiqueta;

public abstract class Etiqueta {
    public abstract Object getNombre();

    
    
    public static EtiquetaNum crear(int e) {
        return new EtiquetaNum(e);
    }

    public static EtiquetaString crear(String e) {
        return new EtiquetaString(e);
    }


}
