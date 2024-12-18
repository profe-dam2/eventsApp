package org.dam.dao;

import org.dam.database.Conexion;
import org.dam.database.SQLDatabaseManager;
import org.dam.models.EventModel;
import org.postgresql.util.PSQLException;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class RegistroDAO extends Conexion {
    private Connection connection;

    public boolean createRegistro(int id_evento, int id_usuario) throws SQLException {
        if(!initDBConnection()){
            throw new SQLException("Error al conectar con la bse de datos");
        }
        try{
            String insertQuery = "INSERT INTO registro (id_evento," +
                    "id_usuario, fecha_registro, hora_registro)\n" +
                    "VALUES (?,?,?,?);";
            PreparedStatement insertStatement =
                    connection.prepareStatement(insertQuery);
            insertStatement.setInt(1, id_evento);
            insertStatement.setInt(2, id_usuario);
            insertStatement.setDate(3, Date.valueOf(LocalDate.now()));
            insertStatement.setTime(4, Time.valueOf(LocalTime.now()));

            int rowsAffected = insertStatement.executeUpdate();
            return rowsAffected > 0;

        }catch (PSQLException e){
            throw new SQLException("Error al crear el registro");

        }finally{
            closeDBConnection();
        }
    }
}
