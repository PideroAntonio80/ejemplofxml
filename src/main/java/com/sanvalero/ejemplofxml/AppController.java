package com.sanvalero.ejemplofxml;

import com.sanvalero.ejemplofxml.domain.Coche;
import com.sanvalero.ejemplofxml.util.AlertUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class AppController implements Initializable {

    public TextField tfMatricula;
    public TextField tfMarca;
    public TextField tfModelo;
    public ComboBox<String> cbTipo;
    public Label lAvisos;

    public Button btNuevo;
    public Button btGuardar;
    public Button btEliminar;
    public Button btModificar;
    public Button btCancelar;

    public TableView<Coche> tvCoches;
    public TableColumn<Coche, Integer> tcId;
    public TableColumn<Coche, String> tcMatricula;
    public TableColumn<Coche, String> tcMarca;
    public TableColumn<Coche, String> tcModelo;
    public TableColumn<Coche, String> tcTipo;
    ObservableList<Coche> listaCoches;

    public MenuItem mfQuit;

    private final CocheDAO COCHEDAO;

    public AppController() {
        COCHEDAO = new CocheDAO();
        try {
            COCHEDAO.conectar();
        } catch (ClassNotFoundException cnfe) {
            AlertUtils.mostrarError("Error al iniciar la aplicación");
        } catch (SQLException sql) {
            AlertUtils.mostrarError("No se ha podido conectar");
        }
    }

    @FXML
    public void desconectar() {
        try {
            COCHEDAO.desconectar();
        } catch (SQLException sql) {
            AlertUtils.mostrarError("No se ha podido desconectar");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> list = FXCollections.observableArrayList("<Selecciona Tipo>", "Turismo", "Berlina", "deportivo", "Monovolumen", "4x4", "Furgoneta");
        cbTipo.setItems(list);
        cbTipo.setValue("Selecciona Tipo");

        try {
            listarCoches();
        } catch (SQLException sql) {
            AlertUtils.mostrarError("Error al cargar los datos");
        }

    }

    @FXML
    public void getDetalleLista(Event event) {

        tfMatricula.setText(tvCoches.getSelectionModel().selectedItemProperty().getValue().getMatricula());
        tfMarca.setText(tvCoches.getSelectionModel().selectedItemProperty().getValue().getMarca());
        tfModelo.setText(tvCoches.getSelectionModel().selectedItemProperty().getValue().getModelo());
        cbTipo.setValue(String.valueOf(tvCoches.getSelectionModel().selectedItemProperty().getValue().getTipo()));

        btNuevo.setDisable(false);
        btGuardar.setDisable(true);
        btEliminar.setDisable(false);
        btModificar.setDisable(false);

        tfMatricula.setEditable(true);
        tfMarca.setEditable(true);
        tfModelo.setEditable(true);
        //modoEdicion(false);

    }

    @FXML
    public void nuevoCoche(Event event) {
        try {
            listarCoches();
        } catch (SQLException throwables) {
            AlertUtils.mostrarError("Error en la tabla de coches");
        }
        limpiaCajas();
        modoEdicion(true);
    }

    @FXML
    public void guardarCoche(Event event) {
        String matricula = tfMatricula.getText();
        if (matricula.equals("")) {
            AlertUtils.mostrarError("El campo matrícula es obligatorio");
            return;
        }
        String marca = tfMarca.getText();
        String modelo = tfModelo.getText();
        String tipo = cbTipo.getSelectionModel().getSelectedItem();

        Coche coche = new Coche(matricula, marca, modelo, tipo);

        try {
            if(COCHEDAO.existeCoche(coche.getMatricula())) {
                AlertUtils.mostrarError("La matrícula ya existe en la BBDD");
                return;
            }
            COCHEDAO.guardarCoche(coche);
            lAvisos.setText("Coche guardado");
            listarCoches();
            modoEdicion(false);
            tfMatricula.setEditable(true);
            tfMarca.setEditable(true);
            tfModelo.setEditable(true);

        } catch (SQLException sql) {
            AlertUtils.mostrarError("Error al guardar coche");
        }

    }

    @FXML
    public void modificarCoche(Event event) {
        try {
            AlertUtils.mostrarConfirmacion("Modificación");

            String matricula = tfMatricula.getText();
            if (matricula.equals("")) {
                AlertUtils.mostrarError("Debes elegir un coche de la lista para modificar");
                return; //return vacio para acabar aquí lo que estabamos haciendo y salir de esta opcion (en este caso de modificar)
            }
            String marca = tfMarca.getText();
            String modelo = tfModelo.getText();
            String tipo = cbTipo.getSelectionModel().getSelectedItem();

            Coche coche = new Coche(matricula, marca, modelo, tipo);
            COCHEDAO.modificarCoche(coche);
            lAvisos.setText("Coche modificado");

            modoEdicion(false);
            listarCoches();

        } catch (SQLException sql) {
            AlertUtils.mostrarError("Error al modificar");
        }

    }

    @FXML
    public void eliminarCoche(Event event) {

        try {
            AlertUtils.mostrarConfirmacion("Eliminación");

            String matricula = tfMatricula.getText();
            if (matricula.equals("")) {
                AlertUtils.mostrarError("Debes elegir un coche de la lista para eliminar");
                return; //return vacio para acabar aquí lo que estabamos haciendo y salir de esta opcion (en este caso de eliminar)
            }
            if(!COCHEDAO.existeCoche(matricula)) {
                AlertUtils.mostrarError("Este coche ya ha sido eliminado.\n Ya no existe en BBDD");
                return;
            }
            Coche coche = new Coche(matricula);
            COCHEDAO.eliminarCoche(coche);
            lAvisos.setText("Coche eliminado");

            modoEdicion(false);
            listarCoches();

        } catch (SQLException sql) {
            AlertUtils.mostrarError("No se ha podido eliminar el coche");
        }

    }

    @FXML
    public void cancelar() {
        AlertUtils.mostrarConfirmacion("Edición");
        modoEdicion(false);

        try {
            listarCoches();
        } catch (SQLException sql) {
            AlertUtils.mostrarError("Error al cancelar");
        }
    }

    @FXML
    public void reset() {
        limpiaCajas();
    }

    public void listarCoches() throws SQLException {
        modoEdicion(false);

        listaCoches = FXCollections.observableArrayList(COCHEDAO.listarCoches());
        tvCoches.setItems(listaCoches);

        //tcId.setCellValueFactory(new PropertyValueFactory<Coche, Integer>("id"));
        tcMatricula.setCellValueFactory(new PropertyValueFactory<Coche, String>("matricula"));
        tcMarca.setCellValueFactory(new PropertyValueFactory<Coche, String>("marca"));
        tcModelo.setCellValueFactory(new PropertyValueFactory<Coche, String>("modelo"));
        tcTipo.setCellValueFactory(new PropertyValueFactory<Coche, String>("tipo"));
    }

    private void limpiaCajas() {
        tfMatricula.setText("");
        tfMarca.setText("");
        tfModelo.setText("");
        cbTipo.setValue("<Selecciona Tipo>");
        //cbTipo.getSelectionModel().clearSelection(); <--- Limpia, resetea el combo box
        lAvisos.setText("");
        tfMatricula.requestFocus(); // <--- Para hacer que el cursor se sitúe donde digas (en matrícula en este caso)
    }

    private void modoEdicion (boolean activar) {
        btNuevo.setDisable(activar);
        btGuardar.setDisable(!activar);
        btEliminar.setDisable(activar);
        btModificar.setDisable(activar);

        tfMatricula.setEditable(activar);
        tfMarca.setEditable(activar);
        tfModelo.setEditable(activar);

        tvCoches.setDisable(activar);
    }

}
