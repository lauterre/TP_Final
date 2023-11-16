package escritor;

import matriz.Tabla;

abstract class Escritor {
    
    public Escritor(Tabla tabla, String direccion) {
        escribirDatos(tabla, direccion);
    };

    public abstract void escribirDatos(Tabla tabla, String direccion); 
}