public class ControladorMemoria {
    private MaquinaVirtual[] duenos;
    private int tamPaginaKB;
    private int libres;

    public ControladorMemoria(long memoriaTotalKB, int tamPaginaKB) {
        this.tamPaginaKB = tamPaginaKB;
        int totalpaginas = (int) (memoriaTotalKB / tamPaginaKB);

        this.duenos = new MaquinaVirtual[totalpaginas];
        this.libres = totalpaginas;
    }

    public synchronized int getLibres() {
        return libres;
    }

    public synchronized boolean reservar(int memoriaGIB, MaquinaVirtual mv) {
        int paginas = paginasNecesarias(memoriaGIB);

        if (libres < paginas){
            return false;
        }

        int asignados = 0;
        for(int i = 0; i < duenos.length && asignados < paginas; i++) {
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


    public int paginasNecesarias(int memoriaGIB) {
        long memoriaKB = (long) memoriaGIB * 1024 * 1024;
        long paginas = memoriaKB / tamPaginaKB;

        if (memoriaKB % tamPaginaKB != 0) {
            paginas++;
        }

        return (int) paginas;
    }
}
