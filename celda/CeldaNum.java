package celda;

public class CeldaNum extends Celda implements Comparable<CeldaNum>{
    private Number valor;

    public CeldaNum(Number valor) {
        this.valor = valor;
    }

    public int compareTo(CeldaNum otraCelda) {
        if (this.isNA()) {
            return -1;
        } else if (otraCelda.isNA()) {
            return 1;
        }
        
        double diferencia = this.valor.doubleValue() - otraCelda.valor.doubleValue();
        if (diferencia < 0) {
            return -1;
        } else if (diferencia > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    public Number getValor() {
        return valor;
    }

    public void setValor(Number valor) {
        this.valor = valor;
    }

    @Override
    public boolean isNA(){
        if (valor == null) {
            return true;
        } else {
            return false;
        }
    }
}
