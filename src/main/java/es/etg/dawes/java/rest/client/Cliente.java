package es.etg.dawes.java.rest.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class Cliente {

    public static final String RUTA_EJEMPLO = "https://jsonplaceholder.typicode.com/posts/1";
    private static final String MENSAJE_FORMATO = "Código de estado: %s%nCuerpo de la respuesta:%n%s";
    private static final String HEADER_ACCEPT_NAME = "Accept";
    private static final String HEADER_ACCEPT_VALUE = "application/json";
    private static final String MENSAJE_ERROR = "Error en la ejecución del servicio";
    private static final int TIMEOUT_SEGUNDOS = 10;

    private final String ruta;

    public Cliente(String ruta) {
        this.ruta = ruta;
    }

    public String ejecutar() {
        String salida;
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(TIMEOUT_SEGUNDOS))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ruta))
                .header(HEADER_ACCEPT_NAME, HEADER_ACCEPT_VALUE)
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            salida = String.format(MENSAJE_FORMATO, response.statusCode(), response.body());
        } catch (Exception e) {
            salida = MENSAJE_ERROR + ": " + e;
        }

        return salida;
    }
}