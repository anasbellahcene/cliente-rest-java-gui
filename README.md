# Cliente REST en Java con interfaz gráfica

Práctica de **Entorno Servidor**: cliente REST en Java que consume la API de pruebas
[JSONPlaceholder](https://jsonplaceholder.typicode.com) usando la API nativa `HttpClient`
(Java 11+) y una interfaz gráfica en **Swing** con el tema **FlatLaf**.

## Tecnologías

| Tecnología | Uso |
|---|---|
| Java 17 | Lenguaje del proyecto |
| Maven | Gestión del proyecto y dependencias |
| `java.net.http.HttpClient` | Peticiones HTTP (sin librerías externas) |
| Swing | Interfaz gráfica |
| FlatLaf 3.8 | Tema oscuro (*look and feel*) para Swing |

## Estructura del proyecto

```
client/
├── pom.xml
├── README.md
└── src/main/java/es/etg/dawes/java/rest/client/
    ├── Cliente.java            → Lógica de la petición REST
    ├── ClienteRestBasico.java  → Versión por consola
    └── Gui.java                → Interfaz gráfica (Swing + FlatLaf)
```

## Práctica base

### `Cliente.java`
Clase que encapsula la petición REST siguiendo el patrón visto en clase:

1. Crea un `HttpClient` con un *timeout* de conexión de 10 segundos.
2. Construye una `HttpRequest` de tipo **GET** con la cabecera `Accept: application/json`.
3. La envía con `client.send()` y recibe la respuesta como `String`.
4. Devuelve un texto con el **código de estado** y el **cuerpo de la respuesta**.

La ruta se recibe en el constructor, así la misma clase sirve para cualquier endpoint.
La ruta de ejemplo está en la constante `RUTA_EJEMPLO`
(`https://jsonplaceholder.typicode.com/posts/1`).

### `ClienteRestBasico.java`
Versión por consola: crea un `Cliente` con la ruta de ejemplo y muestra el resultado
por pantalla.

### `Gui.java`
Ventana Swing con:
- Un título.
- Un botón **"Ejecutar Servicio REST"**.
- Un área de texto con *scroll* donde se muestra la respuesta del servicio.

El tema oscuro se aplica en el `main` con `UIManager.setLookAndFeel(new FlatDarkLaf())`.

## Mejoras añadidas

### 1. Campo para introducir la ruta
Se ha añadido un campo de texto (`JTextField txtRuta`) junto al botón, con la etiqueta
**"Ruta:"**, para que el usuario pueda consultar **cualquier endpoint** y no solo el de ejemplo.

```java
txtRuta = new JTextField(30);
txtRuta.setFont(new Font("Segoe UI", Font.PLAIN, 14));

JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
panelBoton.add(new JLabel(ETIQUETA_RUTA));
panelBoton.add(txtRuta);
panelBoton.add(btnEjecutar);
```

Como el panel usa `FlowLayout`, los componentes se colocan en fila:
`Ruta: [______________] [Ejecutar Servicio REST]`.

### 2. Ruta por defecto si el campo está vacío
Al pulsar el botón se lee el campo y, si está vacío (o solo tiene espacios), se usa la ruta
de ejemplo:

```java
String ruta = txtRuta.getText().trim();

if (ruta.isEmpty()) {
    ruta = Cliente.RUTA_EJEMPLO;
}

Cliente cliente = new Cliente(ruta);
```

### 3. Texto de ayuda (*placeholder*)
Con una propiedad de FlatLaf, el campo muestra en gris la ruta de ejemplo mientras está vacío.
Así el usuario sabe qué ruta se usará si no escribe nada:

```java
txtRuta.putClientProperty("JTextField.placeholderText", Cliente.RUTA_EJEMPLO);
```

### 4. Ejecutar pulsando Enter
El campo de texto tiene el mismo *listener* que el botón, así que se puede lanzar la petición
pulsando **Enter** sin usar el ratón:

```java
txtRuta.addActionListener(this::btnEjecutarActionPerformed);
```

### 5. Mensaje de error más detallado
Antes, si la petición fallaba, solo se mostraba *"Error en la ejecución del servicio"*.
Ahora también se muestra la excepción, lo que permite saber **por qué** ha fallado:

```java
} catch (Exception e) {
    salida = MENSAJE_ERROR + ": " + e;
}
```

## Ejemplos de uso

| Ruta | Resultado |
|---|---|
| *(vacío)* | Usa `https://jsonplaceholder.typicode.com/posts/1` |
| `https://jsonplaceholder.typicode.com/posts/2` | Devuelve la publicación 2 |
| `https://jsonplaceholder.typicode.com/users/1` | Devuelve el usuario 1 |
| `https://jsonplaceholder.typicode.com/posts` | Devuelve la lista completa de publicaciones |

Salida esperada:

```
Código de estado: 200
Cuerpo de la respuesta:
{
  "userId": 1,
  "id": 1,
  "title": "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
  "body": "quia et suscipit..."
}
```

## Problema encontrado y solución

Al ejecutar la aplicación por primera vez aparecía este error (visible gracias a la mejora 5):

```
javax.net.ssl.SSLHandshakeException: PKIX path building failed:
unable to find valid certification path to requested target
```

**Causa:** la red utilizada interceptaba las conexiones HTTPS y las firmaba con un certificado
propio en lugar del certificado real del servidor. Java no confía en ese certificado y rechaza
la conexión. Ocurría igual en Windows y en Linux (máquina virtual), lo que confirmó que el
problema era de la red y no del código.

**Solución:** ejecutar la aplicación conectado a otra red. No se ha desactivado la validación
de certificados en el código, porque eso eliminaría la seguridad de HTTPS.

## Cómo ejecutarlo

Requisitos: **Java 17** y **Maven**.

- **Desde VS Code:** abrir el proyecto, ir a `Gui.java` y pulsar **Run** sobre el método `main`.
- **Versión por consola:** ejecutar `ClienteRestBasico.java` de la misma forma.
