package org.example.hotelgui.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnIniciar;
    @FXML private ProgressIndicator progress;


    @FXML
    private void iniciarSesion() {
        String usuario = txtUsuario.getText();
        String contrasena = txtPassword.getText();

        // Mostrar progress y ocultar botón
        btnIniciar.setVisible(false);
        progress.setVisible(true);

        // Simular un pequeño retardo como si validara contra un servidor
        new Thread(() -> {
            try {
                Thread.sleep(1500); // Simula procesamiento
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Platform.runLater(() -> {
                // Ocultar el progress y restaurar botón
                progress.setVisible(false);
                btnIniciar.setVisible(true);
                if (usuario.equals("admin") && contrasena.equals("1234")) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/hotelgui/inicio-view.fxml"));
                        Parent root = loader.load();
                        Stage stage = (Stage) txtUsuario.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.setTitle("Pantalla de Inicio");
                    } catch (Exception e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error de sistema");
                        alert.setHeaderText(e.getStackTrace()[0].getClassName());
                        alert.setContentText("No fue posible iniciar sesión, debido a un error de sistema: " + e.getMessage());
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error de autenticación");
                    alert.setHeaderText(null);
                    alert.setContentText("Usuario o contraseña incorrectos.");
                    alert.showAndWait();
                }
            });
        }).start();
    }
}