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

/*
R2D2 crea una seccion critica llamada StarUnix, y se pueden enviar tres tipos de mensajes:
    - Por parte de un piloto, envia un registro de una nave
    - Por parte de BB8, busca el numero de misiles totales y el total de naves
    - Por parte de Luke, el quiere el listado de registros/naves al completo.

Nave: Id, tipo de Nave(X-WIND, T-FIGHTER), salto de hiper espacio (boolean), misiles protonicos (entre 10..20), fecha creacion

-- Aplicacion de certificados y SSLServerSocket (usando .jks)

Lo unico que ha cambiado respecto al proyecto original son las clases:
    - Servidor
    - Cliente

-- GUIA (keytool) --
    1.Creamos el certificado en cuestion; abrimos la terminal:
        keytool -genkey -keyalg RSA -alias <alias> -keystore <keystore.jks> -validity <days> -keysize 2048
    2.Introducimos los datos que se soliciten.
    3.Ese archivo.jks es el que usara el servidor; lo guardamos en la raiz del proyecto (por ejemplo)
    4.Exportamos el certificado; nos desplazamos con cd hasta el directorio donde se encuentre el jks; abrimos terminal
        keytool -export -alias <alias> -storepass <clave> -file <archivo.cer> -keystore <archivo.jks>
    5.Ese certificado lo importamos a un nuevo jks, que sera el que usara el cliente; lo mismo que en el punto 4
        keytool -import -alias <alias> -file <archivo.cer> -keystore <archivo.jks>
    6.Introducimos los datos que se soliciten
    7.FIN

-- PD: Comando util para ver la informacion del keystore
        keytool -list -v -keystore <archivo.jks>

FUENTES USADAS:

https://community.pivotal.io/s/article/Generating-a-self-signed-SSL-certificate-using-the-Java-keytool-command?language=en_US
https://docs.oracle.com/cd/E19798-01/821-1841/gjrgy/
https://www.youtube.com/watch?v=MA4VO5n4RYI&ab_channel=Learner%27sCapital
*/

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
