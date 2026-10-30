import java.util.Queue;

public class BuzonDeEntrada {

    private Queue<Solicitud> cola;
    private int capacidad;
    private int usuariosActivos;

    public BuzonDeEntrada(Queue<Solicitud> cola, int capacidad, int usuariosActivos) {
        this.cola = cola;
        this.capacidad = capacidad;
        this.usuariosActivos = usuariosActivos;
    }

    public synchronized void depositar(Solicitud s) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public synchronized Solicitud retirar() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public synchronized void reportarTerminacion() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public synchronized boolean hayUsuariosActivos() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public synchronized boolean estaVacio() {
        throw new UnsupportedOperationException("Not implemented");
    }
}
