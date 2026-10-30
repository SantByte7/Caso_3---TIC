public class Usuarios {
    private int id;
    private int numSolicitudes;
    private BuzonDeEntrada buzon;
    private int maxProcesadores;
    private int maxMemoriaGIB;
    private int maxAlmacenamientoGIB;

    public Usuarios(
            int id,
            int numSolicitudes,
            BuzonDeEntrada buzon,
            int maxProcesadores,
            int maxMemoriaGIB,
            int maxAlmacenamientoGIB) {
        this.id = id;
        this.numSolicitudes = numSolicitudes;
        this.buzon = buzon;
        this.maxProcesadores = maxProcesadores;
        this.maxMemoriaGIB = maxMemoriaGIB;
        this.maxAlmacenamientoGIB = maxAlmacenamientoGIB;
    }

    public void run() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public Solicitud generarSolicitud(int secuencial) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
