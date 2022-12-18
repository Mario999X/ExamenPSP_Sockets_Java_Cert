package server;

import db.StarUnix;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor {
    // El puerto que usaremos
    private final static int PUERTO = 6969;

    public static void main(String[] args) {
        SSLServerSocketFactory serverFactory;
        SSLServerSocket servidor;
        SSLSocket cliente;

        StarUnix starUnix = new StarUnix();

        // Necesitamos una pool de hilos para manejar a los clientes
        ExecutorService pool = Executors.newFixedThreadPool(10);

        System.out.println("Arrancando servidor...");
        try {
            // Tratamos de arrancar el servidor

            // Fichero de donde se sacan los datos
            String userDir = System.getProperty("user.dir");
            Path pathFile = Paths.get(userDir + File.separator + "cert" + File.separator + "llaveropsp.jks");

            // Certificados
            System.setProperty("javax.net.ssl.keyStore", pathFile.toString());
            System.setProperty("javax.net.ssl.keyStorePassword", "123456");

            serverFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            servidor = (SSLServerSocket) serverFactory.createServerSocket(PUERTO);

            System.out.println("\tServidor esperando...");
            while (true) {

                cliente = (SSLSocket) servidor.accept();
                System.out.println("Peticion -> " + cliente.getInetAddress() + " --- " + cliente.getPort());
                GestorClientes gc = new GestorClientes(cliente, starUnix);
                pool.execute(gc);

            }
            //pool.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
