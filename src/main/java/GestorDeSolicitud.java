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
        throw new UnsupportedOperationException("Not implemented");
    }

    public MaquinaVirtual crearMV(Solicitud s) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
