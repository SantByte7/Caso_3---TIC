import java.util.LinkedList;
import java.util.Queue;

public class BuzonDeEntrada {

    private final Queue<Solicitud> cola;
    private final int capacidad;
    private int usuariosActivos;

    public BuzonDeEntrada(int capacidad, int usuariosActivos) {
        this.cola = new LinkedList<>();
        this.capacidad = capacidad;
        this.usuariosActivos = usuariosActivos;
    }

    public synchronized void depositar(Solicitud s) {
        while (cola.size() >= capacidad) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        cola.add(s);
        
    }

    public synchronized Solicitud retirar() {
        if (cola.isEmpty()) {
            return null;
        }
        Solicitud solicitud = cola.remove();
        notifyAll();
        return solicitud;
    }

    public synchronized void reportarTerminacion() {
        usuariosActivos--;
    }

    public synchronized boolean hayUsuariosActivos() {
        return usuariosActivos > 0;
    }

    public synchronized boolean estaVacio() {
        return cola.isEmpty();
    }
}
