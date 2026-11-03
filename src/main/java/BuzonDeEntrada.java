import java.util.LinkedList;
import java.util.Queue;

public class BuzonDeEntrada {

    private final Queue<Solicitud> cola;
    private final int capacidad;
    private int usuariosActivos;
    private long solicitudesDepositadas;
    private long solicitudesRetiradas;

    public BuzonDeEntrada(int capacidad, int usuariosActivos) {
        this.cola = new LinkedList<>();
        this.capacidad = capacidad;
        this.usuariosActivos = usuariosActivos;
    }

    public synchronized boolean depositar(Solicitud s) {
        while (cola.size() >= capacidad) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        cola.add(s);
        solicitudesDepositadas++;
        notifyAll();
        return true;
    }

    public synchronized Solicitud retirar() {
        if (cola.isEmpty()) {
            return null;
        }
        Solicitud solicitud = cola.remove();
        solicitudesRetiradas++;
        notifyAll();
        return solicitud;
    }

    public synchronized void reportarTerminacion() {
        usuariosActivos--;
        /*
         * Un gestor puede estar comprobando el estado del buzón mientras el
         * último usuario termina. Lo despertamos para que evalúe de nuevo la
         * condición de terminación sin depender únicamente de yield().
         */
        notifyAll();
    }

    public synchronized boolean hayUsuariosActivos() {
        return usuariosActivos > 0;
    }

    public synchronized boolean terminado() {
        return cola.isEmpty() && usuariosActivos == 0;
    }

    public synchronized boolean estaVacio() {
        return cola.isEmpty();
    }

    public synchronized long getSolicitudesDepositadas() {
        return solicitudesDepositadas;
    }

    public synchronized long getSolicitudesRetiradas() {
        return solicitudesRetiradas;
    }
}
