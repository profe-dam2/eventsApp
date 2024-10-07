package org.dam.dao;

import org.dam.database.SQLDatabaseManager;
import org.dam.models.EventModel;
import org.postgresql.util.PSQLException;

import java.sql.*;
import java.util.ArrayList;

public class EventosDAO {
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

    public boolean createEvent(EventModel event) throws SQLException {
        if(!initDBConnection()){
            throw new SQLException("Error al conectar con la bse de datos");
        }

        try{
            String insertQuery = "INSERT INTO evento (nombre_evento," +
                                 "descripcion, id_ubicacion, capacidad," +
                                 "estado, precio, fecha_evento) " +
                                 "VALUES (?,?,?,?,?,?,?);";
            PreparedStatement insertStatement =
                    connection.prepareStatement(insertQuery);
            insertStatement.setString(1, event.getNombre_evento());
            insertStatement.setString(2, event.getDescripcion_evento());
            insertStatement.setInt(3,event.getId_ubicacion());
            insertStatement.setInt(4, event.getCapacidad());
            insertStatement.setBoolean(5, event.isEstado());
            insertStatement.setDouble(6,event.getPrecio());
            insertStatement.setDate(7, Date.valueOf(event.getFecha_evento()));

            int rowsAffected = insertStatement.executeUpdate();
            return rowsAffected > 0;

        }catch (PSQLException e){
            throw new SQLException("Error al crear el evento");

        }finally{
            closeDBConnection();
        }
    }


    public boolean addArtistsToEvent(int id_evento, ArrayList<Integer> id_artista_list) throws SQLException {
        if(!initDBConnection()){
            throw new SQLException("Error al conectar con la bse de datos");
        }

        try{
            for(int i = 0; i < id_artista_list.size(); i++){
                String insertQuery = "INSERT INTO eventoartista (id_evento, id_artista)\n" +
                        "VALUES (?,?);";
                PreparedStatement insertStatement =
                        connection.prepareStatement(insertQuery);

                int id_artista = id_artista_list.get(i); // Obtener el id_artista
                insertStatement.setInt(1,id_evento);
                insertStatement.setInt(2,id_artista);
                int rowsAffected = insertStatement.executeUpdate();
//                if( rowsAffected > 0){
//                    return true;
//                }
            }
            return true;

        }catch (PSQLException e){
            throw new SQLException("Error al crear el evento");

        }finally{
            closeDBConnection();
        }
    }

    public ArrayList<String[]> getEventsByUserID(int id) throws SQLException {
        ArrayList<String[]> eventList = new ArrayList<>();

        if(!initDBConnection()){
            throw new SQLException("Error al conectar con la bse de datos");
        }

        try {
            String query = "SELECT r.id_usuario, e.nombre_evento, e.descripcion, e.fecha_evento \n" +
                           "FROM registro r\n" +
                           "INNER JOIN evento e\n" +
                           "ON r.id_evento = e.id_evento\n" +
                           "WHERE r.id_usuario = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                String[] event = new String[]{String.valueOf(resultSet.getInt("id_usuario")),
                                              resultSet.getString("nombre_evento"),
                                              resultSet.getString("descripcion"),
                                              String.valueOf(resultSet.getDate("fecha_evento"))};

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
