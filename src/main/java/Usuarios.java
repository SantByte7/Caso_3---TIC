import java.util.Random;

public class Usuarios extends Thread {
    private int id;
    private int numSolicitudes;
    private BuzonDeEntrada buzon;
    private final int maxProcesadores;
    private final int maxMemoriaGIB;
    private final int maxAlmacenamientoGIB;
    private final Random random = new Random();

    public Usuarios(int id, int numSolicitudes, BuzonDeEntrada buzon,
            int maxProcesadores, int maxMemoriaGIB, int maxAlmacenamientoGIB) {

        this.id = id;
        this.numSolicitudes = numSolicitudes;
        this.buzon = buzon;
        this.maxProcesadores = maxProcesadores;
        this.maxMemoriaGIB = maxMemoriaGIB;
        this.maxAlmacenamientoGIB = maxAlmacenamientoGIB;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < numSolicitudes; i++) {
                Solicitud s = generarSolicitud(i);
                if (!buzon.depositar(s)) {
                    return;
                }
            }
        } finally {
            // El buzón siempre debe conocer que este productor terminó.
            buzon.reportarTerminacion();
        }
    }

    public Solicitud generarSolicitud(int secuencial) {
        int procesadores = valorAleatorio(maxProcesadores);
        int memoriaGIB = potenciaDeDosHasta(valorAleatorio(maxMemoriaGIB));
        int almacenamientoGIB = parHasta(valorAleatorio(maxAlmacenamientoGIB));

        String idSolicitud = id + "-" + secuencial;
        return new Solicitud(idSolicitud, procesadores, memoriaGIB, almacenamientoGIB);
    }

    // Entero entre 1 y limite (ambos inclusivos)
    private int valorAleatorio(int limite) {
        return random.nextInt(limite) + 1;
    }

    // Mayor potencia de dos menor o igual a v (v >= 1)
    private int potenciaDeDosHasta(int x) {
        return Integer.highestOneBit(x);
    }

    // Mayor múltiplo de dos menor o igual a v; si da 0, usa 1
    private int parHasta(int x) {
        int par = x - (x % 2);
        return par == 0 ? 1 : par;
    }
}
