import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Main {
    
    public Main() {
    }

    public static void main(String[] args) {
        Properties config = leerConfiguracion(args[0]);

        int nu = leerEntero(config, "NU");
        int base  = leerEntero(config, "base");
        int ng = leerEntero(config, "NG");
        int tam1 = leerEntero(config, "tam1");
        int tam2 = leerEntero(config, "tam2");
        int procesadores = leerEntero(config, "procesadores");
        int memoriaGiB = leerEntero(config, "memoriaGiB");
        int tampaginaKiB = leerEntero(config, "tamPaginaKB");
        int almacenamientoGiB = leerEntero(config, "almacenamientoGiB");
        int tambloqueKiB = leerEntero(config, "tambloqueKiB");

        long memoriaTotalKB = (long) memoriaGiB * 1024 * 1024;
        long almacenamientoTotalKB = (long) almacenamientoGiB * 1024 * 1024;

        long totalPaginas = memoriaTotalKB / tampaginaKiB;
        long totalBloques = almacenamientoTotalKB / tambloqueKiB;
        
        if (totalPaginas < 1 || totalBloques < 1) {
            System.err.println("La memoria o el almacenamiento son demasiado pequeños para el tamaño de página/bloque especificado.");
            return;
        }

        int limiteProcesadores = generarLimite(procesadores);
        int limiteMemoria = generarLimite((int) totalPaginas);
        int limiteAlmacenamiento = generarLimite((int) totalBloques);
 
        int umbral = tam2;

        BuzonDeEntrada buzon = new BuzonDeEntrada(nu, base);
        ZonaDeEspera zonaEspera = new ZonaDeEspera(umbral);
        ControladorProcesador controladorProcesador = new ControladorProcesador(procesadores);
        ControladorMemoria controladorMemoria = new ControladorMemoria(memoriaTotalKB, tampaginaKiB);
        ControladorAlmacenamiento controladorAlmacenamiento = new ControladorAlmacenamiento(almacenamientoTotalKB, tambloqueKiB);

        List<Usuarios> usuarios = new ArrayList<>();
        for (int i = 0; i < nu; i++) {
            usuarios.add(new Usuarios(i, base * i, buzon, limiteProcesadores, limiteMemoria, limiteAlmacenamiento));
        }

        List<GestorDeSolicitud> gestores = new ArrayList<>();
        for (int i = 0; i < ng; i++) {
            gestores.add(new GestorDeSolicitud(i, buzon, zonaEspera, controladorProcesador, controladorMemoria, controladorAlmacenamiento));
        }

        for (Usuarios usuario : usuarios) {
            usuario.start();
        }

        for (GestorDeSolicitud gestor : gestores) {
            gestor.start();
        }

        try {
            for (Usuarios u : usuarios) {
                u.join();
            }
            for (GestorDeSolicitud g : gestores) {
                g.join(); // cada gestor ya hizo join a sus MVs
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Main interrumpido antes de que el sistema terminara.");
            return;
        }

        boolean ok = true;
        if (!buzon.estaVacio()) {
            System.out.println("FALLO: el buzón no quedó vacío.");
            ok = false;
        }
        if (zonaEspera.cantidadEnEspera() != 0) {
            System.out.println("FALLO: la zona de espera no quedó vacía.");
            ok = false;
        }
        if (controladorProcesador.getLibres() != procesadores) {
            System.out.println("FALLO: procesadores libres " + controladorProcesador.getLibres() + " de " + procesadores);
            ok = false;
        }
        if (controladorMemoria.getLibres() != totalPaginas) {
            System.out.println("FALLO: páginas libres " + controladorMemoria.getLibres() + " de " + totalPaginas);
            ok = false;
        }
        if (controladorAlmacenamiento.getLibres() != totalBloques) {
            System.out.println("FALLO: bloques libres " + controladorAlmacenamiento.getLibres() + " de " + totalBloques);
            ok = false;
        }
        System.out.println(ok ? "Sistema terminado correctamente." : "El sistema terminó con inconsistencias.");


    }

    public static Properties leerConfiguracion(String ruta) {
        Properties config = new Properties();
        try (FileInputStream in = new FileInputStream(ruta)) {
            config.load(in);
        } catch (IOException e) {
            System.err.println("No se pudo leer el archivo de configuración: " + ruta);
            System.exit(1);
        }
        return config;
    }

    private static int leerEntero(Properties config, String clave) {
        String valor = config.getProperty(clave);
        if (valor == null) {
            System.err.println("Falta el parámetro: " + clave);
            System.exit(1);
        }
        try {
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException e) {
            System.err.println("El parámetro " + clave + " no es un número: " + valor);
            System.exit(1);
            return 0; // inalcanzable, el compilador lo exige
        }
    }

    private static int generarLimite(int capacidad) {
        return Math.max(1, (int) Math.round(0.75 * capacidad));
    }
}
