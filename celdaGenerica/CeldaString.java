package celdaGenerica;

public class CeldaString extends Celda<String> {

    public CeldaString(String valor) {
        this.valor = valor;
    }

    @Override
    public boolean isNA() {
        if (this.valor == null || this.valor.equalsIgnoreCase("NA")) {
            return true;
        } else return false;
    }

    @Override
    public int compareTo(Celda<String> otraCelda) {
        if (this.isNA()) {
            return 1;
        } else if (otraCelda.isNA()) {
            return -1;
        }

        return this.valor.compareTo(otraCelda.getValor());
    }
    
}
