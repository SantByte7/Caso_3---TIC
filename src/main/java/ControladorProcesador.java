public class ControladorProcesador {
    private MaquinaVirtual[] duenos;
    private int libres;

    public ControladorProcesador(int totalprocesadores) {
        this.duenos = new MaquinaVirtual[totalprocesadores];
        this.libres = totalprocesadores;
    }

    public synchronized int getLibres() {
        return libres;
    }

    public synchronized boolean reservar(int cantidad, MaquinaVirtual mv) {
        if (libres < cantidad){
            return false;
        }

        int asignados = 0;
        for (int i = 0; i < duenos.length && asignados < cantidad; i++) {
            if (duenos[i] == null) {
                duenos[i] = mv;
                asignados++;
            }
        }
        libres -= asignados;
        return true;
    }

    public synchronized void liberar(MaquinaVirtual mv) {
        
        for (int i = 0; i < duenos.length; i++) {
            if (duenos[i] == mv) {
                duenos[i] = null;
                libres++;
            }
        }
    }
}
