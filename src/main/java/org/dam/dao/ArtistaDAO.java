package org.dam.dao;

import org.dam.database.SQLDatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ArtistaDAO {
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

    public ArrayList<String[]> getArtistEvents(int id_artista) throws SQLException {
        ArrayList<String[]> eventList = new ArrayList<>();

        if(!initDBConnection()){
            throw new SQLException("Error al conectar con la bse de datos");
        }

        try {
            String query = "SELECT a.nombre_artista, e.nombre_evento, e.fecha_evento, u.nombre_lugar\n" +
                    "FROM artista a\n" +
                    "INNER JOIN eventoartista ea\n" +
                    "ON ea.id_artista = a.id_artista\n" +
                    "INNER JOIN evento e\n" +
                    "ON ea.id_evento = e.id_evento\n" +
                    "INNER JOIN ubicacion u\n" +
                    "ON e.id_ubicacion = u.id_ubicacion\n" +
                    "WHERE a.id_artista = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id_artista);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                String[] event = new String[]{resultSet.getString("nombre_artista"),
                        resultSet.getString("nombre_evento"),
                        String.valueOf(resultSet.getDate("fecha_evento")),
                        resultSet.getString("nombre_lugar")};

                eventList.add(event);
            }

        }catch (Exception e){
            throw new SQLException("Error al consultar los datos");
        }finally {
            closeDBConnection();
        }

        return eventList;
    }

}
