package org.example.hotelgui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Parent;
import org.example.hotelgui.logica.ClienteLogica;
import org.example.hotelgui.model.Cliente;
import java.sql.SQLException;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.time.LocalDate;
import javafx.beans.property.ReadOnlyStringWrapper;

public class InicioController implements Initializable {
    // Controller de la tabla
    @FXML private TableView<Cliente> tblClientes;
    @FXML private TableColumn<Cliente, String> colNombreCliente;
    @FXML private TableColumn<Cliente, String> colIdentificacionCliente;
    @FXML private TableColumn<Cliente, String> colIdCliente;

    // Controllers de la busqueda
    @FXML private TextField txtBuscarCliente;

    // Alamacenamiento de datos
    private final ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();

    private final ClienteLogica clienteLogica = new ClienteLogica();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            colIdCliente.setCellValueFactory(cd ->
                    new ReadOnlyStringWrapper(String.valueOf(cd.getValue().getId()))
            );

            colIdentificacionCliente.setCellValueFactory(cd ->
                    new ReadOnlyStringWrapper(cd.getValue().getIdentificacion())
            );

            colNombreCliente.setCellValueFactory(cd ->
                    new ReadOnlyStringWrapper(
                            cd.getValue().getNombre() + " " + cd.getValue().getPrimerApellido()
                    )
            );

            listaClientes.addAll(clienteLogica.findAll());

            tblClientes.setItems(listaClientes);
        }
        catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void agregarCliente() throws SQLException {
        Cliente nuevo = mostrarFormulario(null, false);
        if(nuevo != null) {

            clienteLogica.create(nuevo);

            listaClientes.add(nuevo);
        }
    }

    private Cliente mostrarFormulario(Cliente cliente, boolean editar) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/hotelgui/formulario-cliente-view.fxml"));
            Parent root = loader.load();

            //LLamar a la clase de FormularioClienteController
            FormularioClienteController controller = loader.getController();
            //Vamos a setear la información del cliente que vamos a agregar
            //Pero para eso necesitamos el metodo respectivo en FormularioClienteController
            controller.setCliente(cliente, editar);

            Stage stage = new Stage();
            stage.setTitle(editar ? "Modificar Cliente" : "Agregar Cliente");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            return (Cliente) stage.getUserData();
        }
        catch (IOException error)
        {
            mostrarAlerta("Error al abrir el formulario", error.getMessage());
            return null;
        }
    }

    @FXML
    private void abrirEstadisticas() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/hotelgui/estadisticas-cliente.fxml")
            );
            Parent root = loader.load();

            // Si quieres pasarle la ruta XML manualmente:
            // EstadisticasClientesController c = loader.getController();
            // c.setRutaXml(RUTA_CLIENTES);

            Stage stage = new Stage();
            stage.setTitle("Estadísticas de Clientes");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(tblClientes.getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            mostrarAlerta("Error al abrir estadísticas", e.getMessage());
        }
    }


    @FXML
    private void modificarCliente() {
        Cliente seleccionado = tblClientes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Seleccione un cliente", "Por favor, seleccione un cliente de la tabla para modificar.");
            return;
        }

        Cliente modificado = mostrarFormulario(seleccionado, true);
        if (modificado != null) {
            tblClientes.refresh();
        }
    }

    @FXML
    private void eliminarCliente() {
        try {
            Cliente seleccionado = tblClientes.getSelectionModel().getSelectedItem();
            if (seleccionado == null) {
                mostrarAlerta("Seleccione un cliente", "Por favor, seleccione un cliente de la tabla para eliminar.");
                return;
            }

            listaClientes.remove(seleccionado);
            clienteLogica.deleteById(seleccionado.getId());
        }
        catch (Exception error)
        {
            mostrarAlerta("Error al eliminar el cliente", error.getMessage());
        }
    }

    @FXML
    private void buscarCliente() {
        try {
            String criterio = txtBuscarCliente.getText().trim().toLowerCase();
            if(criterio.isEmpty())
            {
                tblClientes.setItems(listaClientes);
                return;
            }

            ObservableList<Cliente> filtrados =
                    FXCollections.observableArrayList(
                            listaClientes.stream()
                                    .filter(c -> c.getIdentificacion().toLowerCase().contains(criterio)
                                    || c.getNombre().toLowerCase().contains(criterio)
                                    || c.getPrimerApellido().toLowerCase().contains(criterio))
                    .collect(Collectors.toList())
                    );

            tblClientes.setItems(filtrados);
        }
        catch (Exception error)
        {
            mostrarAlerta("Error al buscar el cliente", error.getMessage());
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

}
