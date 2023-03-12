package cliente;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPReply;
import java.io.*;

/**
 * No puedo firmar el código porque me han ayudado muchísimo compañeros
 * como Álvaro, Ernesto y Alejandro, y el código es una mezcla de fuentes
 * de internet y su ayuda. He "mejorado" algunas cosas y he conseguido
 * entenderlo al completo, sin embargo.
 */
public class ClienteFTP {
    private final String USUARIO, PASS, SERVER;
    private final int PUERTO;
    private final FTPClient FTP_CLIENT = new FTPClient();

    public ClienteFTP(String user, String pass, String server, int port) {
        this.USUARIO = user;
        this.PASS = pass;
        this.SERVER = server;
        this.PUERTO = port;
    }

    /**
     * Muestra todas las respuestas que devuelva el servidor.
     * @param ftpClient
     */
    private static void respuestaServer(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        for (String aReply : replies) {
            System.out.println("Servidor: " + aReply);
        }
    }

    /**
     * Conectamos al servidor con los datos que hemos pasado al constructor.
     */
    public void conectar() {
        try {
            FTP_CLIENT.connect(SERVER, PUERTO);
            respuestaServer(FTP_CLIENT);
            int replyCode = FTP_CLIENT.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.out.println("La operación ha fallado. replyCode: " + replyCode);
                return;
            }
            boolean success = FTP_CLIENT.login(USUARIO, PASS);
            respuestaServer(FTP_CLIENT);
            if (!success) {
                System.out.println("No se ha podido conectar al servidor.");
                return;
            } else {
                System.out.println("Se ha conectado al servidor.");
            }
        } catch (IOException ex) {
            System.out.println("Algo ha salido mal :(");
            ex.printStackTrace();
        }
    }

    /**
     * Primero cerramos sesión en el servidor para después desconectarnos de él.
     */
    public void desconectar() {
        try {
            FTP_CLIENT.logout();
            FTP_CLIENT.disconnect();
            if(!FTP_CLIENT.isConnected()){
                System.out.println("Se ha desconectado del servidor.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Lista todos los archivos en un directorio con nuestro formato especificado.
     * @param path
     */
    public void listar(String path){
        FTPFile[] files;
        try {
            files = FTP_CLIENT.listFiles(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Muestra la información de cada archivo listado (nombre y tamaño).
        for (FTPFile file : files) {
            String details = file.getName() + " " + file.getSize();
            System.out.println(details);
        }
    }

    /**
     * Busca un archivo en un directorio pasando un fragmento a buscar
     * en el nombre de
     * @param fragmentoBuscar
     * @param path
     */
    public void buscar(String fragmentoBuscar, String path) {
        // Cualquier archivo que contenga el fragmento de texto que pasemos.
        FTPFileFilter filter = ftpFile -> (ftpFile.getName().contains(fragmentoBuscar));
        FTPFile[] result;
        try {
            result = FTP_CLIENT.listFiles(path, filter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Mostramos el nombre de cada archivo que se haya encontrado.
        for (FTPFile aFile : result) {
            System.out.println(aFile.getName());
        }
    }

    /**
     * Descargamos un archivo desde el servidor FTP al almacenamiento local.
     * @param remoteFilePath
     * @param savePath
     */
    public void descargarArchivo(String remoteFilePath, String  savePath) {
        try {
            FTP_CLIENT.enterLocalPassiveMode();
            File downloadFile = new File(savePath);
            File parentDir = downloadFile.getParentFile(); // Se obtiene la carpeta inmed. superior al archivo
            if (!parentDir.exists()) { // Si no existe, creamos la carpeta
                parentDir.mkdir();
            }
            // Escribimos en el OutputStream el contenido del archivo que
            // nos traeremos del servidor FTP.
            try (OutputStream outputStream = new BufferedOutputStream(
                    new FileOutputStream(downloadFile))) {
                FTP_CLIENT.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
                System.out.println("El archivo se está descargando");
                // Nos traemos el archivo del dispositivo con el servidor FTP.
                FTP_CLIENT.retrieveFile(remoteFilePath, outputStream);
            } finally {
                System.out.println("Descarga completada.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Subir un archivo al dispositivo con servidor FTP.
     * @param remoteFilePath
     * @param localFilePath
     */
    public void subirArchivo(String remoteFilePath, String localFilePath) {
        try {
            FTP_CLIENT.enterLocalPassiveMode();
            // Seleccionamos un archivo local
            File localFile = new File(localFilePath);
            // Lo "transformamos" a InputStream
            try (InputStream inputStream = new FileInputStream(localFile)) {
                System.out.println("Subiendo archivo");
                if (FTP_CLIENT.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE)) {
                    System.out.println("Subido.");
                }
                // Guardamos el InputStream en nuestro dispositivo con el servidor FTP
                // (en este caso, el móvil).
                FTP_CLIENT.storeFile(remoteFilePath, inputStream);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
