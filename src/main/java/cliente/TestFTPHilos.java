package cliente;

import java.util.Scanner;

/**
 * Test que crea tantos hilos de conexión como el usuario prefiera,
 * y que además espera hasta que cada uno ha finalizado para comprobarlo.
 *
 * Esto puede no ser lo que querríamos en última instancia, pero se puede
 * modificar deshaciéndonos del join(), y es mucho más cómodo para probar
 * el correcto funcionamiento del cliente.
 *
 * @author Lunkberjack
 */

public class TestFTPHilos {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("¿Cuántos hilos quieres?: ");
        int hilos = scan.nextInt();

        // Creamos tantos hilos de conexión como quiera el usuario.
        for(int i = 0; i < hilos; i++) {
            Thread hilo = new Thread(new TestFTPRunnable());
            hilo.start();
            // Los hilos se esperarán: no tendremos una petición de varios
            // datos seguidos. Cada conexión se realizará por bloque.
            try {
                hilo.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

/**
 * Cada hilo (o conexión con servers), que se puede realizar de forma dependiente,
 * es decir, podremos conectarnos hasta a 5 servidores desde este cliente.
 *
 * @author Lunkberjack
 */

class TestFTPRunnable implements Runnable {
    public void run() {
        // En vez de reescribir el código, podemos llamar al método main de la
        // clase TestFTP de manera estática: pasamos un nuevo array de Strings
        // que, como ya sabemos, es el parámetro que toma main().
        TestFTP.main(new String[]{});
    }
}
