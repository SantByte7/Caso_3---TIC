import java.util.List;

public class ZonaDeEspera {
    private List<MaquinaVirtual> mvsEnEspera;
    private int umbral;

    public ZonaDeEspera(List<MaquinaVirtual> mvsEnEspera, int umbral) {
        this.mvsEnEspera = mvsEnEspera;
        this.umbral = umbral;
    }

    public synchronized void esperar(MaquinaVirtual mv) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public synchronized void despertar() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public synchronized int cantidadEnEspera() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public synchronized void esperarQueQuedeVacio() {
        throw new UnsupportedOperationException("Not implemented");
    }
}
