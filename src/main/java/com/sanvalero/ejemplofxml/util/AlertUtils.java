package com.sanvalero.ejemplofxml.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Creado por @ author: Pedro Orós
 * el 07/11/2020
 */
public class AlertUtils {
    public static void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(mensaje);
        alert.show();
    }

    public static void mostrarConfirmacion(String titulo) {
        Alert confirmacion = new Alert((Alert.AlertType.CONFIRMATION));
        confirmacion.setTitle(titulo);
        confirmacion.setContentText("¿Estás seguro?");
        Optional<ButtonType> respuesta = confirmacion.showAndWait();
        if (respuesta.get().getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE) //Si pulsa boton Cancelar se cancela la eliminación
            return;
    }
}
