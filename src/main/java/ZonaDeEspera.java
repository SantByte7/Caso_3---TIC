import java.util.ArrayList;
import java.util.List;

public class ZonaDeEspera {
    private List<MaquinaVirtual> mvsEnEspera;
    private int umbral;
    private int version;

    public ZonaDeEspera(int umbral) {
        this.mvsEnEspera = new ArrayList<>();
        this.umbral = umbral;
        this.version = 0;
    }

    public synchronized void esperar(MaquinaVirtual mv, int versionActual) {
        mvsEnEspera.add(mv);

        while (version == versionActual) {
            try {
                wait();
            } catch (InterruptedException e) {
                mvsEnEspera.remove(mv);
                Thread.currentThread().interrupt();
                return;
            }
        }

        mvsEnEspera.remove(mv);
        if (mvsEnEspera.isEmpty()) {
            notifyAll();
        }

    }

    public synchronized int getVersion() {
        return version;
    }

    public synchronized void despertar() {
        version++;
        notifyAll();
    }

    public synchronized boolean hayMVsEnEspera() {
        return !mvsEnEspera.isEmpty();
    }

    public synchronized int getUmbral() {
        return umbral;
    }

    public synchronized int cantidadEnEspera() {
        return mvsEnEspera.size();
    }

    public synchronized void esperarQueQuedeVacio() {
        while(!mvsEnEspera.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public synchronized boolean estaSaturada() {
        return mvsEnEspera.size() >= umbral;
    }
}
