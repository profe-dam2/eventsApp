package org.dam.dao;

import org.dam.database.Conexion;
import org.dam.database.SQLDatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UbicacionDAO extends Conexion {

    public boolean eliminarComentario(int idComentario) throws SQLException {

        if (!initDBConnection()) {

            throw new SQLException("Error al conectar con la base de datos");

        }

        try {

            String query = "DELETE FROM Comentarios WHERE id_comentario = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, idComentario);

            int rowsAffected = preparedStatement.executeUpdate();


            if (rowsAffected > 0) {

                System.out.println("Comentario eliminado exitosamente.");
                return true;

            } else {

                System.out.println("No se encontró el comentario con ID: " + idComentario);
                return false;
            }

        } catch (Exception e) {

            throw new SQLException("Error al eliminar el comentario: " + e.getMessage());

        } finally {

            closeDBConnection();

        }

    }


}
