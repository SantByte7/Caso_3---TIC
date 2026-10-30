public class ControladorAlmacenamiento {
    private MaquinaVirtual[] duenos;
    private int tamPaginaKB;
    private int libres;

    public ControladorAlmacenamiento(long almacenamientoTotalKB, int tamPaginaKB) {
        this.tamPaginaKB = tamPaginaKB;
        int totalBloques = (int) (almacenamientoTotalKB / tamPaginaKB);

        this.duenos = new MaquinaVirtual[totalBloques];
        this.libres = totalBloques;
    }

    public synchronized int getLibres() {
        return libres;
    }

    public synchronized boolean reservar(int almacenamientoGIB, MaquinaVirtual mv) {
        int bloques = bloquesNecesarios(almacenamientoGIB);

        if (libres < bloques){
            return false;
        }

        int asignados = 0;
        for(int i = 0; i < duenos.length && asignados < bloques; i++) {
            if (duenos[i] == null) {
                duenos[i] = mv;
                asignados++;
            }
        }
        libres -= asignados;
        return true;
    }

    public synchronized void liberar(MaquinaVirtual mv) {
        for (int i = 0; i < duenos.length; i++) {
            if (duenos[i] == mv) {
                duenos[i] = null;
                libres++;
            }
        }
    }


    public synchronized int bloquesNecesarios(int almacenamientoGIB) {
        long almacenamientoKB = (long) almacenamientoGIB * 1024 * 1024;
        long bloques = almacenamientoKB / tamPaginaKB;

        if (almacenamientoKB % tamPaginaKB != 0) {
            bloques++;
        }

        return (int) bloques;
    }
}
