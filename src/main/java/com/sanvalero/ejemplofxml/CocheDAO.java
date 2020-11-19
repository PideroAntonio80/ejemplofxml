package com.sanvalero.ejemplofxml;

import com.sanvalero.ejemplofxml.domain.Coche;
import com.sanvalero.ejemplofxml.util.R;
import javafx.scene.control.Alert;

import javax.swing.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CocheDAO {

    private Connection conexion;

    public void conectar() throws ClassNotFoundException, SQLException, IOException {

        Properties configuration = new Properties();
        configuration.load(R.getProperties("database.properties"));
        String host = configuration.getProperty("host");
        String port = configuration.getProperty("port");
        String name = configuration.getProperty("name");
        String username = configuration.getProperty("username");
        String password = configuration.getProperty("password");

        Class.forName("com.mysql.cj.jdbc.Driver"); /* <--- Busco en MVNrepository (en internet),
                                                        copio el xml de mysql connector y lo pego en el POM*/
        conexion = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + name + "?serverTimezone=UTC",
                username, password);
    }

    public void desconectar() throws SQLException {
        conexion.close();
    }

    public void guardarCoche(Coche coche) throws SQLException{
        String sql = "INSERT INTO coches (matricula, marca, modelo, tipo) VALUES (?, ?, ?, ?)";

            PreparedStatement sentencia = conexion.prepareStatement(sql);
            sentencia.setString(1, coche.getMatricula());
            sentencia.setString(2, coche.getMarca());
            sentencia.setString(3, coche.getModelo());
            sentencia.setString(4, coche.getTipo());
            sentencia.executeUpdate();

    }

    public void eliminarCoche(Coche coche) throws SQLException{
        String sql = "DELETE FROM COCHES WHERE matricula = ?";

            PreparedStatement sentencia = conexion.prepareStatement(sql);
            sentencia.setString(1, coche.getMatricula());
            sentencia.executeUpdate();

    }

    public void modificarCoche(Coche coche) throws SQLException {
        String sql = "UPDATE coches SET matricula = ?, marca = ?, modelo = ?, tipo = ? WHERE matricula = ?";

            PreparedStatement sentencia = conexion.prepareStatement(sql);

            sentencia.setString(1, coche.getMatricula());
            sentencia.setString(2, coche.getMarca());
            sentencia.setString(3, coche.getModelo());
            sentencia.setString(4, coche.getTipo());
            sentencia.setString(5, coche.getMatriculaVieja());
            sentencia.executeUpdate();

    }

    public List<Coche> listarCoches() throws SQLException {
        String sql = "SELECT * FROM coches ORDER BY marca";

            Statement sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery(sql);

            List<Coche> lista = new ArrayList<Coche>();

            while(resultado.next()) {
                Coche coche = new Coche(
                        resultado.getInt(1),
                        resultado.getString(2),
                        resultado.getString(3),
                        resultado.getString(4),
                        resultado.getString(5)
                );
                lista.add(coche);
                /*System.out.print(resultado.getString(2) + " - ");  <--- Por si quiero comprobar por consola
                System.out.print(resultado.getString(3) + " - ");
                System.out.print(resultado.getString(4) + " - ");
                System.out.println(resultado.getString(5) + " - ");*/
            }

            return lista;

    }

    public boolean existeCoche(String matricula) throws SQLException {
        String sql = "SELECT * FROM coches WHERE matricula = ?";
        PreparedStatement sentencia = conexion.prepareStatement(sql);
        sentencia.setString(1, matricula);
        ResultSet resultado = sentencia.executeQuery();

        return resultado.next();
    }

}
