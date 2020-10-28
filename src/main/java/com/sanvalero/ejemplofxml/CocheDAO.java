package com.sanvalero.ejemplofxml;

import com.sanvalero.ejemplofxml.domain.Coche;
import javafx.scene.control.Alert;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CocheDAO {

    private Connection conexion;
    private final String USUARIO = JOptionPane.showInputDialog("Escriba su clave de Usuario");
    private final String PASSWORD = JOptionPane.showInputDialog("Escriba su contraseña");

    public void conectar() {

        int eligeMotor = Integer.parseInt(JOptionPane.showInputDialog("Elige motor de BBDD: (1)MySQL, (2)Postgre"));

        if(eligeMotor == 1) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver"); /* <--- Busco en MVNrepository (en internet),
                                                        copio el xml de mysql connector y lo pego en el POM*/
                conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/taller?serverTimezone=UTC",
                        USUARIO, PASSWORD);
            } catch (ClassNotFoundException cnfe) {
                cnfe.printStackTrace();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }

        } /*else {
            try {
                Class.forName("org.postgresql.Driver").newInstance();
                conexion = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/basededatos",
                        "usuario", "contraseña");
            } catch (ClassNotFoundException cnfe) {
                cnfe.prinStackTrace();
            } catch (SQLException sqle) {
                sqle.prinStackTrace();
            } catch (InstantiationException ie) {
                ie.prinStackTrace();
            } catch (IllegalAccessException iae) {
                iae.prinStackTrace();
            }
        }*/

    }

    public void desconectar() {

    }

    public void guardarCoche(Coche coche) {
        String sql = "INSERT INTO coches (matricula, marca, modelo, tipo) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement sentencia = conexion.prepareStatement(sql);
            sentencia.setString(1, coche.getMatricula());
            sentencia.setString(2, coche.getMarca());
            sentencia.setString(3, coche.getModelo());
            sentencia.setString(4, coche.getTipo());
            sentencia.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Guardar Coche");
            alert.setContentText("El coche se ha guardado con éxito");
            alert.show();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public void eliminarCoche(Coche coche) {
        String sql = "DELETE FROM COCHES WHERE matricula = ?";

        try {
            PreparedStatement sentencia = conexion.prepareStatement(sql);
            sentencia.setString(1, coche.getMatricula());
            sentencia.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Coche Eliminado");
            alert.setContentText("El coche se ha sido borrado de la BBDD");
            alert.show();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    }

    public void modificarCoche(Coche coche) {
        String sql = "UPDATE coches SET matricula = ?, marca = ?, modelo = ?, tipo = ? WHERE matricula = ?";

        try {
            PreparedStatement sentencia = conexion.prepareStatement(sql);

            sentencia.setString(1, coche.getMatricula());
            sentencia.setString(2, coche.getMarca());
            sentencia.setString(3, coche.getModelo());
            sentencia.setString(4, coche.getTipo());
            sentencia.setString(5, JOptionPane.showInputDialog("Introduzca la matrícula del coche que desea modificar"));
            sentencia.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Coche Modificado");
            alert.setContentText("Su coche ha sido modificado");
            alert.show();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public List<Coche> listarCoches() {
        String sql = "SELECT * FROM coches";

        try {
            Statement sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery(sql);

            List<Coche> lista = new ArrayList<Coche>();

            while(resultado.next()) {
                Coche coche = new Coche(
                        resultado.getInt(0),
                        resultado.getString(1),
                        resultado.getString(2),
                        resultado.getString(3),
                        resultado.getString(4)
                );
                lista.add(coche);

            }

            return lista;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

}
