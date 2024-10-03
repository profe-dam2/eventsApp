package org.dam.dao;

import org.dam.database.SQLDatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UsuariosDAO {

    private Connection connection;

    private boolean initDBConnection(){
        try {
            connection = SQLDatabaseManager.connect();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos");
        }
        return false;
    }

    private boolean closeDBConnection(){
        try {
            SQLDatabaseManager.disconnect(connection);
            return true;
        } catch (SQLException e) {
            System.err.println("Error al desconectar con la base de datos");
        }
        return false;
    }

    public ArrayList<String[]> getUsuarios() throws SQLException {
        ArrayList<String[]> listaUsuarios = new ArrayList<>();

        if(!initDBConnection()){
            throw new SQLException("Error al conectar con la bse de datos");
        }

        try {
            String query = "SELECT id_usuario, username, email, activo\n" +
                    "FROM Usuario";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                String[] usuario = new String[]{resultSet.getString("id_usuario"),
                        resultSet.getString("username"),
                        resultSet.getString("email"),
                        String.valueOf(resultSet.getBoolean("activo"))};
                listaUsuarios.add(usuario);
            }

        }catch (Exception e){
            throw new SQLException("Error al consultar los datos");
        }finally {
            closeDBConnection();
        }

        return listaUsuarios;
    }

    public ArrayList<String[]> getUsuariosByRol(String rol) throws SQLException {
        ArrayList<String[]> listaUsuarios = new ArrayList<>();

        if(!initDBConnection()){
            throw new SQLException("Error al conectar con la bse de datos");
        }

        try {
            String query = "SELECT id_usuario, username, email, activo\n" +
                           "FROM Usuario\n" +
                           "WHERE rol_usuario = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, rol);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                String[] usuario = new String[]{resultSet.getString("id_usuario"),
                        resultSet.getString("username"),
                        resultSet.getString("email"),
                        String.valueOf(resultSet.getBoolean("activo"))};
                listaUsuarios.add(usuario);
            }

        }catch (Exception e){
            throw new SQLException("Error al consultar los datos");
        }finally {
            closeDBConnection();
        }

        return listaUsuarios;
    }


}
