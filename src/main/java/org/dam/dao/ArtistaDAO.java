package org.dam.dao;

import org.dam.database.Conexion;
import org.dam.database.SQLDatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ArtistaDAO extends Conexion {

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

    public boolean eliminarArtista(int idArtista) throws SQLException {
        if (!initDBConnection()) {
            throw new SQLException("Error al conectar con la base de datos");
        }

        try {
            // Verificar si el artista existe
            String queryCheck = "SELECT COUNT(*) > 0 AS existe FROM artista WHERE id_artista = ? AND disponible = true;";
            PreparedStatement preparedStatementCheck = connection.prepareStatement(queryCheck);
            preparedStatementCheck.setInt(1, idArtista);
            ResultSet resultSet = preparedStatementCheck.executeQuery();

            if (resultSet.next() && resultSet.getBoolean("existe")) {
                // Verificar si ha asistido a algún evento
                String queryVerificarAsistencia = "SELECT COUNT(*) > 0 AS asistio FROM eventoartista WHERE id_artista = ?;";
                PreparedStatement preparedStatementAsistencia = connection.prepareStatement(queryVerificarAsistencia);
                preparedStatementAsistencia.setInt(1, idArtista);
                ResultSet resultSetAsistencia = preparedStatementAsistencia.executeQuery();
                resultSetAsistencia.next();

                // Si no ha asistido a ningún evento, eliminar
                if (!resultSetAsistencia.getBoolean("asistio")) {
                    String queryDelete = "DELETE FROM artista WHERE id_artista = ?";
                    PreparedStatement preparedStatementDelete = connection.prepareStatement(queryDelete);
                    preparedStatementDelete.setInt(1, idArtista);
                    int rowsAffected = preparedStatementDelete.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Artista eliminado exitosamente.");
                        return true;
                    }
                } else {
                    // Deshabilitar el artista
                    String queryUpdate = "UPDATE artista SET disponible = false WHERE id_artista = ?;";
                    PreparedStatement preparedStatementUpdate = connection.prepareStatement(queryUpdate);
                    preparedStatementUpdate.setInt(1, idArtista);
                    int rowsAffected = preparedStatementUpdate.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Artista deshabilitado exitosamente.");
                        return true;
                    }
                }
            } else {
                // La ID del artista no existe
                return false;
            }
        } catch (Exception e) {
            throw new SQLException("Error al eliminar artista", e);
        } finally {
            closeDBConnection();
        }

        throw new SQLException("Error al eliminar artista");
    }


}
