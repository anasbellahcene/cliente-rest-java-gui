package es.etg.dawes.java.rest.client;

public class ClienteRestBasico {
    public static void main(String[] args) {
        Cliente cliente = new Cliente(Cliente.RUTA_EJEMPLO);
        System.out.println(cliente.ejecutar());
    }
}