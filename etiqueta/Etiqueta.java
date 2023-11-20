package etiqueta;

import Exceptions.EtiquetaInvalidaException;

public abstract class Etiqueta {
    public abstract Object getNombre();

    public static EtiquetaNum crear(int e) throws EtiquetaInvalidaException {
        if (e < 0)
            throw new EtiquetaInvalidaException();
        return new EtiquetaNum(e);
    }

    public static EtiquetaString crear(String e) {
        return new EtiquetaString(e);
    }

}
