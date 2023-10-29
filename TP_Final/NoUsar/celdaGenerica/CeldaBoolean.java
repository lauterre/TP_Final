package NoUsar.celdaGenerica;

public class CeldaBoolean extends Celda<Boolean> {
    
    public CeldaBoolean(Boolean valor) {
        this.valor = valor;
    }

    // no se esto:
    public CeldaBoolean(Integer valor) {
        if (valor == null) {
            this.valor = null;
        }else if (valor == 0) {
            this.valor = false;
        } else {
            this.valor = true;
        }
    }

    @Override
    public boolean isNA() {
        return valor == null;
    }

    @Override
    public int compareTo(Celda<Boolean> otraCelda) {
        if (this.isNA()) {
            return -1;
        } else if (otraCelda.isNA()) {
            return 1;
        }

        int valorThis = this.valor ? 1 : 0;
        int valorOtraCelda = otraCelda.getValor() ? 1 : 0;
        if (valorThis > valorOtraCelda) {
            return 1;
        } else if (valorThis < valorOtraCelda) {
            return -1;
        } else return 0;
    }
}
