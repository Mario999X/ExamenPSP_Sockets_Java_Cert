package server;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import db.StarUnix;
import models.Nave;
import models.mensajes.Request;
import models.mensajes.Response;
import models.mensajes.TypeResponse;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class GestorClientes implements Runnable {

    private final Socket cliente;
    private final StarUnix starUnix;

    public GestorClientes(Socket cliente, StarUnix starUnix) {
        this.cliente = cliente;
        this.starUnix = starUnix;
    }

    @Override
    public void run() {
        // Generamos canales de entrada-salida
        try {
            DataInputStream receiveRequest = new DataInputStream(cliente.getInputStream());
            DataOutputStream sendResponse = new DataOutputStream(cliente.getOutputStream());

            // Leemos el dato del cliente = Request<Nave>
            ObjectMapper obj = new ObjectMapper();
            // Es necesario hacer esto debido a la naturaleza generica de Request<T>
            JavaType type = obj.getTypeFactory().constructParametricType(Request.class, Nave.class);

            Request<Nave> request = obj.readValue(receiveRequest.readUTF(), type);
            System.out.println(request);

            Response<String> response; // Preparamos el response

            switch (request.getRequestType()) {
                case ADD -> {
                    Nave nave = request.getContent();
                    System.out.println("\tNave recibida, agregando...");

                    starUnix.add(nave);
                    response = new Response<>(nave + " agregado", TypeResponse.OK);
                }

                case GETLUKE -> {
                    System.out.println("\tLuke recibido");
                    List<Nave> listado = starUnix.getAll();
                    response = new Response<>(listado.toString(), TypeResponse.OK);
                }

                case GETBB8 -> {
                    System.out.println("\tBB8 recibido");
                    response = new Response<>(starUnix.getInfoDetallada(), TypeResponse.OK);
                }

                default -> {
                    System.out.println("Tipo no reconocido");
                    response = new Response<>("Error", TypeResponse.ERROR);
                }
            }
            // Enviamos el response
            String jsonResponse = obj.writeValueAsString(response);
            sendResponse.writeUTF(jsonResponse);

            // Cerramos la conexion con el cliente
            cliente.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
