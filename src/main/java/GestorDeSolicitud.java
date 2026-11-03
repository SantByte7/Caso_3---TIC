import java.util.ArrayList;
import java.util.List;

public class GestorDeSolicitud extends Thread {
    private int id;
    private BuzonDeEntrada buzon;
    private ZonaDeEspera zonaEspera;
    private ControladorProcesador ctrlProcesadores;
    private ControladorMemoria ctrlMemoria;
    private ControladorAlmacenamiento ctrlAlmacenamiento;

    public GestorDeSolicitud(
            int id,
            BuzonDeEntrada buzon,
            ZonaDeEspera zonaEspera,
            ControladorProcesador ctrlProcesadores,
            ControladorMemoria ctrlMemoria,
            ControladorAlmacenamiento ctrlAlmacenamiento) {
        this.id = id;
        this.buzon = buzon;
        this.zonaEspera = zonaEspera;
        this.ctrlProcesadores = ctrlProcesadores;
        this.ctrlMemoria = ctrlMemoria;
        this.ctrlAlmacenamiento = ctrlAlmacenamiento;
    }

    @Override
    public void run() {
        List<MaquinaVirtual> creadas = new ArrayList<>();
        boolean seguir = true; 
        while (seguir){
            if (zonaEspera.estaSaturada()) {
                zonaEspera.esperarQueQuedeVacio();
                continue;
            }

            Solicitud sol = buzon.retirar();

            if (sol == null) {
                if (buzon.terminado()) {
                    seguir = false;
                } else {
                    Thread.yield();
                }
            }

            if (sol != null) {
                MaquinaVirtual mv = crearMV(sol);
                mv.start();
                creadas.add(mv);
            }
        }
        for (MaquinaVirtual mv : creadas) {
            try {
                mv.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

    }

    public MaquinaVirtual crearMV(Solicitud s) {
        return new MaquinaVirtual(s, ctrlProcesadores, ctrlMemoria, ctrlAlmacenamiento, zonaEspera);
    }
}
