package cliente;

import java.util.Scanner;

/**
 * No puedo firmar el código porque me han ayudado muchísimo compañeros
 * como Álvaro, Ernesto y Alejandro, y el código es una mezcla de fuentes
 * de internet y su ayuda. He "mejorado" algunas cosas y he conseguido
 * entenderlo al completo, sin embargo.
 */
public class TestFTP {
    public static void main(String[] args) {
        final int CONEXION = 1, LISTADO = 2, BUSCAR = 3,
                DESCARGAR = 4, SUBIR = 5, DESCONEXION = 6, SALIR = 7;
        Scanner scan = new Scanner(System.in);
        String usuario, pass, server;
        int puerto, num;

        System.out.println("Usuario:");
        usuario = scan.next();
        System.out.println("Contraseña:");
        pass = scan.next();
        System.out.println("Servidor:");
        server = scan.next();
        System.out.println("Puerto:");
        puerto = scan.nextInt();
        ClienteFTP ftp = new ClienteFTP(usuario, pass, server, puerto);

        do {
            System.out.println("Lista de acciones:");
            System.out.println("1. Conexión");
            System.out.println("2. Listado");
            System.out.println("3. Buscar");
            System.out.println("4. Descargar archivo");
            System.out.println("5. Subir archivo");
            System.out.println("6. Desconexión");
            System.out.println("7. Salir");

            num = scan.nextInt();
            // Con el enhanced switch no hace falta poner break tras cada caso.
            switch (num) {
                case CONEXION -> ftp.conectar();
                case LISTADO -> {
                    System.out.println("Ruta: ");
                    String ruta = scan.next();
                    ftp.listar(ruta);
                }
                case BUSCAR -> {
                    System.out.println("Ruta: ");
                    String rutaBuscar = scan.next();
                    System.out.println("Fichero: ");
                    String tb = scan.next();
                    ftp.buscar(tb, rutaBuscar);
                }
                case DESCARGAR -> {
                    System.out.println("Ruta: ");
                    String rutaDescargar = scan.next();
                    System.out.println("Ruta donde guardar: ");
                    String rutaGuardar = scan.next();
                    ftp.descargarArchivo(rutaDescargar, rutaGuardar + "/" + rutaDescargar);
                }
                case SUBIR -> {
                    System.out.println("Ruta: ");
                    String rutaSubir = scan.next();
                    System.out.println("Ruta donde guardar: ");
                    String rutaGuardar2 = scan.next();
                    ftp.subirArchivo(rutaGuardar2, rutaSubir);
                }
                case DESCONEXION -> ftp.desconectar();
                case SALIR-> System.out.println("Saliendo...");
                default -> System.out.println("Esa opción no existe.");
            }
        } while(num != 7);
        scan.close();
    }
}
