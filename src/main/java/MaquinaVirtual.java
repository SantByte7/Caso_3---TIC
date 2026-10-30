public class MaquinaVirtual extends Thread {
    
    private Solicitud solicitud;
    private ControladorProcesador ctrlProcesadores;
    private ControladorMemoria ctrlMemoria;
    private ControladorAlmacenamiento ctrlAlmacenamiento;
    private ZonaDeEspera zonaEspera;

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
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean reservarRecursos() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void liberarRecursos() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void ejecutar() {
        throw new UnsupportedOperationException("Not implemented");
    }
}
