package es.etg.dawes.java.rest.client;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;

public class Gui extends JFrame {

    private static final String TITULO_VENTANA = "Cliente REST en Java";
    private static final String TEXTO_BOTON = "Ejecutar Servicio REST";
    private static final String ETIQUETA_SALIDA = "Salida del Servicio:";
    private static final int ANCHO_VENTANA = 750;
    private static final int ALTO_VENTANA = 450;
    private static final String ETIQUETA_RUTA = "Ruta:";

    private JButton btnEjecutar;
    private JScrollPane scrollPaneSalida;
    private JTextArea txtSalida;
    private JLabel lblTitulo;
    private JLabel lblSalida;
    private JPanel panelPrincipal;
    private JTextField txtRuta;

    public Gui() {
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(TITULO_VENTANA);
        setSize(ANCHO_VENTANA, ALTO_VENTANA);
        setLocationRelativeTo(null);

        panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout(15, 15));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título superior
        lblTitulo = new JLabel(TITULO_VENTANA, JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // Panel central
        JPanel panelCentro = new JPanel(new BorderLayout(10, 10));

        btnEjecutar = new JButton(TEXTO_BOTON);
        btnEjecutar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEjecutar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEjecutar.addActionListener(this::btnEjecutarActionPerformed);

        txtRuta = new JTextField(30);
        txtRuta.addActionListener(this::btnEjecutarActionPerformed);
        txtRuta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtRuta.putClientProperty("JTextField.placeholderText", Cliente.RUTA_EJEMPLO);

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.add(new JLabel(ETIQUETA_RUTA));
        panelBoton.add(txtRuta);
        panelBoton.add(btnEjecutar);
        panelCentro.add(panelBoton, BorderLayout.NORTH);

        // Área de texto y scroll
        JPanel panelAreaSalida = new JPanel(new BorderLayout(5, 5));
        lblSalida = new JLabel(ETIQUETA_SALIDA);
        lblSalida.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        txtSalida = new JTextArea();
        txtSalida.setFont(new Font("Consolas", Font.PLAIN, 13));
        txtSalida.setEditable(false);
        txtSalida.setLineWrap(true);
        txtSalida.setWrapStyleWord(true);

        scrollPaneSalida = new JScrollPane(txtSalida);
        scrollPaneSalida.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        panelAreaSalida.add(lblSalida, BorderLayout.NORTH);
        panelAreaSalida.add(scrollPaneSalida, BorderLayout.CENTER);

        panelCentro.add(panelAreaSalida, BorderLayout.CENTER);
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);

        add(panelPrincipal);
    }

    private void btnEjecutarActionPerformed(java.awt.event.ActionEvent evt) {
        String ruta = txtRuta.getText().trim();

        if (ruta.isEmpty()) {
            ruta = Cliente.RUTA_EJEMPLO;
        }

        Cliente cliente = new Cliente(ruta);
        String salida = cliente.ejecutar();
        txtSalida.setText(salida);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        EventQueue.invokeLater(() -> new Gui().setVisible(true));
    }
}