public class Solicitud {
    private String id;
    private int procesadores;
    private int memoriaGIB;
    private int almacenamientoGIB;

    public Solicitud(String id, int procesadores, int memoriaGIB, int almacenamientoGIB) {
        this.id = id;
        this.procesadores = procesadores;
        this.memoriaGIB = memoriaGIB;
        this.almacenamientoGIB = almacenamientoGIB;
    }

    public String getId() {
        return id;
    }

    public int getProcesadores() {
        return procesadores;
    }

    public int getMemoriaGIB() {
        return memoriaGIB;
    }

    public int getAlmacenamientoGIB() {
        return almacenamientoGIB;
    }
}
