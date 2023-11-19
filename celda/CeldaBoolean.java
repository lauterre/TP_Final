package celda;

public class CeldaBoolean extends Celda {
    private Boolean valor;

    public CeldaBoolean(Boolean valor) {
        this.valor = valor;
    }

    // no se esto:
    public CeldaBoolean(Integer valor) {
        if (valor == null) {
            this.valor = null;
        } else if (valor == 0) {
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
    public int compareTo(Celda otraCelda) {
        CeldaBoolean otraCeldaBoolean = (CeldaBoolean) otraCelda;
        if (this.isNA()) {
            return -1;
        } else if (otraCelda.isNA()) {
            return 1;
        }

        int valorThis = this.valor ? 1 : 0;
        int valorOtraCelda = otraCeldaBoolean.getValor() ? 1 : 0;
        if (valorThis > valorOtraCelda) {
            return 1;
        } else if (valorThis < valorOtraCelda) {
            return -1;
        } else
            return 0;
    }

    @Override
    public Boolean getValor() {
        return valor;
    }

    public void setValor(Boolean valor) {
        this.valor = valor;
    }

    @Override
    public CeldaBoolean copia() {
        valor = this.getValor();
        return new CeldaBoolean(valor);
    }

    @Override
    public String toString() {
        if (!(valor == null)) {
            return valor.toString();
        } else {
            return "NA"; // return "NA".toString()
        }
    }

}
