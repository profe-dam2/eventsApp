package org.dam.dao;

import org.dam.database.Conexion;
import org.dam.database.SQLDatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GeneroDAO extends Conexion {

    public ArrayList<String> getGeneros() throws SQLException {
        ArrayList<String> listaGeneros = new ArrayList<>();

        if(!initDBConnection()){
            throw new SQLException("Error al conectar con la bse de datos");
        }

        try {
            String query = "SELECT DISTINCT genero\n" +
                           "FROM Artista";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                listaGeneros.add(resultSet.getString("genero"));
            }

        }catch (Exception e){
            throw new SQLException("Error al consultar los datos");
        }finally {
            closeDBConnection();
        }

        return listaGeneros;
    }

}
