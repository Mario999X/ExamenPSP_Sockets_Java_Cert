package client;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Nave;
import models.mensajes.Request;
import models.mensajes.Response;
import models.mensajes.TypeRequest;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.InetAddress;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Cliente {
    private static Request<Nave> request;

    // Datos que usaremos en la conexion
    private final static int PUERTO = 6969;

    public static void main(String[] args) {
        InetAddress direccion;
        SSLSocket servidor;
        SSLSocketFactory clientFactory;

        boolean salida = false;

        // Bucle cliente
        while (!salida) {
            System.out.println("1. Piloto\n2. BB8\n3. Luke Skywalker\n4. Salir");

            // Preguntamos por terminal
            Scanner input = new Scanner(System.in);
            int opcion = input.nextInt();

            // Preparamos el request segun la opcion seleccionada
            switch (opcion) {
                case 1 -> {
                    System.out.println("Conectado como piloto");
                    Nave nave = new CreadorNavesRandom().creadorNavesRandom();
                    System.out.println("\tNave preparada: " + nave);

                    request = new Request<>(nave, TypeRequest.ADD);
                    System.out.println("\t--" + request + " enviada, esperando respuesta...");
                }

                case 2 -> {
                    System.out.println("Conectado como BB8");

                    request = new Request<>(new Nave(), TypeRequest.GETBB8); // Se envia una nave sin nada, luego ni se usara
                    System.out.println("\t--" + request + " enviada, esperando respuesta...");
                }

                case 3 -> {
                    System.out.println("Conectado como Luke Skywalker");

                    request = new Request<>(new Nave(), TypeRequest.GETLUKE); // Se envia una nave sin nada, luego ni se usara
                    System.out.println("\t--" + request + " enviada, esperando respuesta...");
                }

                case 4 -> {
                    System.out.println("Saliendo del programa...");
                    input.close(); // Cerramos el Scanner
                    salida = true;
                }

                default -> {
                    System.out.println("Opcion desconocida");
                    //System.exit(1); // Como no se puede trabajar con null
                    opcion = 4; // Puede que sea una mejor solucion, asi el programa no se cierra solo.
                }
            }

            // Evitamos la conexion con el servidor si la opcion no lo necesita
            try {
                if (opcion == 4) {
                    System.out.println("---");
                } else {
                    // -- Conectamos con el servidor --

                    // Fichero de donde se sacan los datos
                    String userDir = System.getProperty("user.dir");
                    Path pathFile = Paths.get(userDir + File.separator + "certClient" + File.separator + "truststore.jks");

                    // Para depurar y ver el dialogo y handshake
                    System.setProperty("javax.net.debug", "ssl, keymanager, handshake");

                    // Certificados
                    System.setProperty("javax.net.ssl.trustStore", pathFile.toString());
                    System.setProperty("javax.net.ssl.trustStorePassword", "654321");

                    direccion = InetAddress.getLocalHost();
                    clientFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
                    servidor = (SSLSocket) clientFactory.createSocket(direccion, PUERTO);

                    // Canales de entrada-salida
                    DataInputStream receiveResponse = new DataInputStream(servidor.getInputStream());
                    DataOutputStream sendRequest = new DataOutputStream(servidor.getOutputStream());

                    // Enviamos el dato al servidor en formato json usando Jackson
                    ObjectMapper obj = new ObjectMapper();
                    String jsonRequest = obj.writeValueAsString(request);
                    sendRequest.writeUTF(jsonRequest);

                    // Esperamos el resultado = Response<String>
                    JavaType type = obj.getTypeFactory().constructParametricType(Response.class, String.class);

                    Response<String> response = obj.readValue(receiveResponse.readUTF(), type);
                    System.out.println("Resultado: " + response.getContent());

                }
            } catch (Exception e) {
                e.printStackTrace();
                salida = true; // En caso de que el servidor no este operativo, salimos del bucle
            }
        }
    }
}
