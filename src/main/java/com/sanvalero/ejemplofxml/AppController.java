package com.sanvalero.ejemplofxml;

import com.sanvalero.ejemplofxml.domain.Coche;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {

    public TextField tfMatricula;
    public TextField tfMarca;
    public TextField tfModelo;
    public ComboBox<String> cbTipo;

    public TableView<Coche> tvCoches;
    //public TableColumn<Coche, Integer> tcId; ???
    public TableColumn<Coche, String> tcMatricula;
    public TableColumn<Coche, String> tcMarca;
    public TableColumn<Coche, String> tcModelo;
    public TableColumn<Coche, String> tcTipo;
    ObservableList<Coche> listaCoches;

    public MenuItem mfQuit;

    private CocheDAO cocheDAO;

    public AppController() {
        cocheDAO = new CocheDAO();
        cocheDAO.conectar();
    }

    @FXML
    public void desconectar() {
        cocheDAO.desconectar();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> list = FXCollections.observableArrayList("Turismo", "Monovolumen", "4x4", "Furgoneta");
        cbTipo.setItems(list);

        listaCoches = FXCollections.observableArrayList(cocheDAO.listarCoches());
        cocheDAO.listarCoches();
        tvCoches.setItems(listaCoches);

        //tcId.setCellValueFactory(new PropertyValueFactory<Coche, Integer>("id")); ???
        tcMatricula.setCellValueFactory(new PropertyValueFactory<Coche, String>("matricula"));
        tcMarca.setCellValueFactory(new PropertyValueFactory<Coche, String>("marca"));
        tcModelo.setCellValueFactory(new PropertyValueFactory<Coche, String>("modelo"));
        tcTipo.setCellValueFactory(new PropertyValueFactory<Coche, String>("tipo"));
    }

    /*@FXML
    public void desplegable(Event event) {

    }*/

    @FXML
    public void nuevoCoche(Event event) {
        tfMatricula.setText("");
        tfMarca.setText("");
        tfModelo.setText("");
        cbTipo.setValue(null);
        //cb.getSelectionModel().clearSelection();
    }

    @FXML
    public void modificarCoche(Event event) {
        String matricula = tfMatricula.getText();
        if (matricula.equals("")) {
            // TODO Error de que falta indicar la matricula como campo obligatorio
        }
        String marca = tfMarca.getText();
        String modelo = tfModelo.getText();
        String tipo = cbTipo.getSelectionModel().getSelectedItem();

        Coche coche = new Coche(matricula, marca, modelo, tipo);
        cocheDAO.modificarCoche(coche);
    }

    @FXML
    public void eliminarCoche(Event event) {
        String matricula = tfMatricula.getText();
        if (matricula.equals("")) {
            // TODO Error de que falta indicar la matricula como campo obligatorio
        }
        Coche coche = new Coche(matricula);
        cocheDAO.eliminarCoche(coche);
    }

    @FXML
    public void guardarCoche(Event event) {
        String matricula = tfMatricula.getText();
        if (matricula.equals("")) {
            // TODO Error de que falta indicar la matricula como campo obligatorio
        }
        String marca = tfMarca.getText();
        String modelo = tfModelo.getText();
        String tipo = cbTipo.getSelectionModel().getSelectedItem();

        Coche coche = new Coche(matricula, marca, modelo, tipo);
        cocheDAO.guardarCoche(coche);
    }

   /* @FXML
    public void listarCoches() {

        listaCoches = FXCollections.observableArrayList();
        cocheDAO.listarCoches();
        tvCoches.setItems(listaCoches);

        tcId.setCellValueFactory(new PropertyValueFactory<Coche, Integer>("id"));
        tcMatricula.setCellValueFactory(new PropertyValueFactory<Coche, String>("matricula"));
        tcMarca.setCellValueFactory(new PropertyValueFactory<Coche, String>("marca"));
        tcModelo.setCellValueFactory(new PropertyValueFactory<Coche, String>("modelo"));
        tcTipo.setCellValueFactory(new PropertyValueFactory<Coche, String>("tipo"));
    }*/


}
