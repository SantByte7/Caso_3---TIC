import java.util.concurrent.ThreadLocalRandom;

public class MaquinaVirtual extends Thread {
    
    private Solicitud solicitud;
    private ControladorProcesador ctrlProcesadores;
    private ControladorMemoria ctrlMemoria;
    private ControladorAlmacenamiento ctrlAlmacenamiento;
    private ZonaDeEspera zonaEspera;
    private static final int Tiempo_min = 4000;
    private static final int Tiempo_max = 40000;

    public MaquinaVirtual(
            Solicitud solicitud,
            ControladorProcesador ctrlProcesadores,
            ControladorMemoria ctrlMemoria,
            ControladorAlmacenamiento ctrlAlmacenamiento,
            ZonaDeEspera zonaEspera) {
        this.solicitud = solicitud;
        this.ctrlProcesadores = ctrlProcesadores;
        this.ctrlMemoria = ctrlMemoria;
        this.ctrlAlmacenamiento = ctrlAlmacenamiento;
        this.zonaEspera = zonaEspera;
    }

    @Override
    public void run() {
        if (!solicitudPosible()) {
            System.err.println(
                    "Solicitud imposible de atender: "
                            + solicitud.getId());
            return;
        }

        boolean recursosReservados = false;

        try {
            while (!recursosReservados && !Thread.currentThread().isInterrupted()) {
                synchronized (zonaEspera) {
                    recursosReservados = reservarRecursos();
                    if (!recursosReservados) {
                        int versionActual = zonaEspera.getVersion();
                        if (!zonaEspera.esperar(this, versionActual)) {
                            return;
                        }
                    }
                }
            }

            if (!recursosReservados) {
                return;
            }
            ejecutar();

        } finally {
            if (recursosReservados) {
                synchronized (zonaEspera) {
                    liberarRecursos();
                    zonaEspera.despertar();
                }
            }
        }
    }

    public boolean reservarRecursos() {
        boolean procesadoresReservados = ctrlProcesadores.reservar(solicitud.getProcesadores(), this);
        if (!procesadoresReservados) {
            return false;
        }

        boolean memoriaReservada = ctrlMemoria.reservar(solicitud.getMemoriaGIB(), this);
        if (!memoriaReservada) {
            ctrlProcesadores.liberar(this);
            return false;
        }

        boolean almacenamientoReservado = ctrlAlmacenamiento.reservar(solicitud.getAlmacenamientoGIB(), this);
        if (!almacenamientoReservado) {
            ctrlProcesadores.liberar(this);
            ctrlMemoria.liberar(this);
            return false;
        }

        return true;
    }

    private boolean solicitudPosible() {
        int paginasNecesarias =
                ctrlMemoria.paginasNecesarias(solicitud.getMemoriaGIB());

        int bloquesNecesarios =
                ctrlAlmacenamiento.bloquesNecesarios(
                        solicitud.getAlmacenamientoGIB());

        return solicitud.getProcesadores()
                    <= ctrlProcesadores.getCapacidadTotal()
                && paginasNecesarias
                    <= ctrlMemoria.getCapacidadTotal()
                && bloquesNecesarios
                    <= ctrlAlmacenamiento.getCapacidadTotal();
    }

    public void liberarRecursos() {
        ctrlProcesadores.liberar(this);
        ctrlMemoria.liberar(this);
        ctrlAlmacenamiento.liberar(this);
    }

    public void ejecutar() {
        int duracion = ThreadLocalRandom.current()
                .nextInt(Tiempo_min, Tiempo_max + 1);
        try {
            Thread.sleep(duracion);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
