package celdaGenerica;

public class CeldaNum extends Celda<Number> {
    
    public CeldaNum(Number valor) {
        this.valor = valor;
    }

    @Override
    public int compareTo(Celda<Number> otraCelda) {
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

    @Override
    public boolean isNA(){
        if (valor == null) {
            return true;
        } else {
            return false;
        }
    }
}
