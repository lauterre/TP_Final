package celda;

public class CeldaBoolean extends Celda {
    private Boolean valor;

    public CeldaBoolean(Boolean valor) {
        this.valor = valor;
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
        } else return 0;
    }

    public Boolean getValor() {
        return valor;
    }

    public void setValor(Boolean valor) {
        this.valor = valor;
    }
}
