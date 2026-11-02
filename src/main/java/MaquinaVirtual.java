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
        boolean config = false;
        while (!config) {
            int versionActual = zonaEspera.getVersion();
            config = reservarRecursos();
            if (!config) {
                zonaEspera.esperar(this, versionActual);
            }
        }
        ejecutar();
        liberarRecursos();
        zonaEspera.despertar();
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

    public void liberarRecursos() {
        ctrlProcesadores.liberar(this);
        ctrlMemoria.liberar(this);
        ctrlAlmacenamiento.liberar(this);
    }

    public void ejecutar() {
        int duracion = Tiempo_min + new java.util.Random().nextInt(Tiempo_max - Tiempo_min + 1);
        try {
            Thread.sleep(duracion);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
