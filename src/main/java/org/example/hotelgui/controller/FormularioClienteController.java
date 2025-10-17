package org.example.hotelgui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.hotelgui.logica.ClienteLogica;
import org.example.hotelgui.model.Cliente;
import org.example.hotelgui.controller.Async;

import java.sql.SQLException;
import java.time.LocalDate;

public class FormularioClienteController {

    //vamos a usar la variable final de la logica
    private final ClienteLogica clienteLogica = new ClienteLogica();

    // Controllers del formulario
    @FXML private TextField txtIdentificacionCliente;
    @FXML private TextField txtNombreCliente;
    @FXML private TextField txtPrimerApellidoCliente;
    @FXML private DatePicker dtpFechaNacimientoCliente;

    private Cliente cliente;
    private boolean modoEdicion = false;


    @FXML
    private TableView<Cliente> tablaClientes;
    @FXML
    private ProgressIndicator progress;

    public void cargarClientesAsync(){
        progress.setVisible(true); //va a durar lo q dure el hilo
        Async.run(() -> { //esta expresion representa el proceso principal
                try{
                    //sobre el proceso principal vamos a ejecutar un hilo con un proceso principal
                    return clienteLogica.findAll();
                }
                catch (SQLException ex){
                    throw new RuntimeException(ex);
                }
        },
        lista -> { //este es el caso del onSuccess
            tablaClientes.getItems().setAll(lista);
            progress.setVisible(false);
        },
        ex -> { //este es el caso del onError
            progress.setVisible(false);
            Alert a = new  Alert(Alert.AlertType.ERROR);
            a.setTitle("Error al cargar la lista de clientes");
            a.setHeaderText(null);
            a.setContentText(ex.getMessage());
            a.showAndWait();
        }
        );
    }

    public void guardarClientesAsync(Cliente c){
        //btnGuardar.setDisable(true); se quita el boton para que no se manden varias al mismo tiempo
        progress.setVisible(true);

        Async.run(
                () -> { //este es el proceso principal
                    try{
                        clienteLogica.create(c);
                        return c;
                    }
                    catch (SQLException ex){
                        throw new RuntimeException(ex);
                    }
                },
                guardado -> { //este es el onSuccess
                    progress.setVisible(false);
                    //btnGuardar.setDisable(false);
                    tablaClientes.getItems().add(guardado);
                    new Alert(Alert.AlertType.INFORMATION, "Cliente Guardado").showAndWait(); //para mostrar que se agrego el cliente
                },
                ex -> { //este es el onError
                    progress.setVisible(false);
                    //btnGuardar.setDisable(false);
                    Alert a = new  Alert(Alert.AlertType.ERROR);
                    a.setTitle("No se pudo guardar el cliente");
                    a.setHeaderText(null);
                    a.setContentText(ex.getMessage());
                    a.showAndWait();
                }
        );
    }





    public void setCliente(Cliente cliente, boolean editar) {
        //Si el cliente es nuevo, es decir, si venimos de un "Agregar Cliente"
        //Entonces solo seteamos el cliente nuevo que vamos a guardar
        //Y el modo edición se mantiene en falso
        this.cliente = cliente;
        this.modoEdicion = editar;

        //Si el cliente no es nuevo, es decir, venimos de un "Modificar Cliente"
        //Entonces modificamos los datos y los guardamos en el cliente ya existente
        //Para esto, en la pantalla del formulario debemos cargar los datos que estaban
        //previamente guardados
        if(editar && cliente != null) {
            txtIdentificacionCliente.setText(cliente.getIdentificacion());
            txtNombreCliente.setText(cliente.getNombre());
            txtPrimerApellidoCliente.setText(cliente.getPrimerApellido());
            dtpFechaNacimientoCliente.setValue(cliente.getFechaNacimiento());
        }
    }

    @FXML
    private void guardarCliente()
    {
        try {
            String identificacion = txtIdentificacionCliente.getText().trim();
            String nombre = txtNombreCliente.getText().trim();
            String primerApellido = txtPrimerApellidoCliente.getText().trim();
            LocalDate fechaNacimiento = dtpFechaNacimientoCliente.getValue();

            // Verificamos que el formulario este completo
            if (identificacion.isEmpty() || nombre.isEmpty() || primerApellido.isEmpty() || fechaNacimiento == null) {
                // Se va a lanzar un error (alert)
                mostrarAlerta("Campos incompletos", "Por favor, complete todos los campos del formulario.");
                return;
            }

            //Vamos a verificar si es un cliente nuevo o si estamos editando un cliente ya existente
            if (!modoEdicion) {
                //Entonces es un cliente nuevo
                cliente = new Cliente(0, nombre, primerApellido, identificacion, fechaNacimiento);
            } else {
                //Entonces estamos modificando un cliente existente
                cliente.setIdentificacion(identificacion);
                cliente.setNombre(nombre);
                cliente.setPrimerApellido(primerApellido);
                cliente.setFechaNacimiento(fechaNacimiento);
            }

            //Aquí vamos a controlar el movimiento de las ventanas
            //Se debe cerrar la ventana del formulario y se debe regresar a la principal
            Stage stage = (Stage) txtNombreCliente.getScene().getWindow();
            stage.setUserData(cliente);
            stage.close();
        }
        catch (Exception error) {
            mostrarAlerta("Error al guardar los datos", error.getMessage());
        }
    }

    @FXML
    private void cancelarCliente()
    {
        try
        {
            Stage stage = (Stage) txtNombreCliente.getScene().getWindow();
            stage.setUserData(null);
            stage.close();
        }
        catch (Exception error) {
            mostrarAlerta("Error al guardar los datos", error.getMessage());
        }
    }

    private void limpiarCampos() {
        txtIdentificacionCliente.clear();
        txtNombreCliente.clear();
        txtPrimerApellidoCliente.clear();
        dtpFechaNacimientoCliente.setValue(null);
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
