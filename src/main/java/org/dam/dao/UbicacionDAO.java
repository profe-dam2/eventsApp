package org.dam.dao;

import org.dam.database.Conexion;
import org.dam.database.SQLDatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UbicacionDAO extends Conexion {

    public boolean eliminarUbicacion(int idUbicacion) throws SQLException {

        if (!initDBConnection()) {
            throw new SQLException("Error al conectar con la base de datos");
        }
        try {

            String query = "DELETE FROM Ubicacion WHERE id_ubicacion = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idUbicacion);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {

                System.out.println("Ubicacion eliminada exitosamente.");
                return true;

            } else {

                System.out.println("No se encontró la ubicacion con ID: " + idUbicacion);
                return false;
            }

        } catch (Exception e) {

            throw new SQLException("La ubicación no puede ser eliminada ya que fue usada");

        } finally {

            closeDBConnection();

        }

    }


}
