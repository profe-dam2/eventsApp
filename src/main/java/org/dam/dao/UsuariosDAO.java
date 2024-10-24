package org.dam.dao;

import org.dam.database.SQLDatabaseManager;

import java.sql.*;
import java.time.LocalDate;
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

    private String censurarComentario(String comentario){
        String[] listaInsultos = new String[]{"mierda","mierd","mier","puta","put",
                "zorra","zorr","bastardo", "bastard","cabrón","cabr","guarra",
                "guarr","guarro","guarr"};

        for(String insulto : listaInsultos){
            if(comentario.toLowerCase().matches(".*"+ insulto + ".*")){
                String palabraCensurada = insulto.substring(0, 1) + "***";
                comentario = comentario.toLowerCase().replace(insulto, palabraCensurada);
                break;
            }
        }
        return comentario;
    }

    public ArrayList<String[]> establecerPremioBuenComentario() throws SQLException {
        if(!initDBConnection()){
            throw new SQLException("Error al conectar con la base de datos");
        }

        try{
            String obtenerBuenosComentariosquery = "SELECT c.id_usuario, c.comentario " +
                                                   "FROM Comentarios c " +
                                                   "INNER JOIN Usuario u ON c.id_usuario = u.id_usuario " +
                                                   "WHERE LOWER(c.comentario) ~ '(espectacular|espectacula|increíble|increibl|espléndido|espléndid|excelente|excelent|perfecto|perfect|único|únic|agradable|agradabl)';";
            PreparedStatement preparedStatement = connection.prepareStatement(obtenerBuenosComentariosquery);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String establecerPremioBuenComentarioQuery = "UPDATE usuario SET premios = ? WHERE id_usuario = ?";
                int id_usuario = resultSet.getInt("id_usuario");
                PreparedStatement actualizarPreparedStatement = connection.prepareStatement(establecerPremioBuenComentarioQuery);
                actualizarPreparedStatement.setString(1, "Buen usuario");
                actualizarPreparedStatement.setInt(2, id_usuario);
                actualizarPreparedStatement.executeUpdate();
            }

        } catch (Exception e) {
            throw new SQLException("Error al establecer mal premio");
        }finally{
            closeDBConnection();
        }
        return getUsuarios();
    }

    public ArrayList<String[]> establecerPremioMalComentario() throws SQLException {
        if(!initDBConnection()){
            throw new SQLException("Error al conectar con la base de datos");
        }
        try{
            String obtenerComentarioMalos = "SELECT c.id_usuario, c.comentario\n" +
                    "FROM comentarios c\n" +
                    "INNER JOIN usuario u\n" +
                    "ON c.id_usuario = u.id_usuario\n" +
                    "WHERE comentario LIKE CONCAT('%','***','%');\n";
            PreparedStatement preparedStatement = connection.prepareStatement(obtenerComentarioMalos);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String establecerPremioMalComentarioQuery = "UPDATE usuario SET premios = ? WHERE id_usuario = ?";
                int id_usuario = resultSet.getInt("id_usuario");
                PreparedStatement actualizarPreparedStatement = connection.prepareStatement(establecerPremioMalComentarioQuery);
                actualizarPreparedStatement.setString(1, "Mal usuario");
                actualizarPreparedStatement.setInt(2, id_usuario);
                actualizarPreparedStatement.executeUpdate();
            }

        } catch (Exception e) {
            throw new SQLException("Error al establecer mal premio");
        }finally{
            closeDBConnection();
        }
        return getUsuarios();
    }

    public ArrayList<String[]> censurarComentarios() throws SQLException {

        ArrayList<String[]> listaComentarios = new ArrayList<>();

        if(!initDBConnection()){
            throw new SQLException("Error al conectar con la base de datos");
        }

        try{
            String query = "SELECT c.id_comentario, c.comentario\n" +
                           "FROM Comentarios c\n" +
                           "WHERE LOWER(c.comentario) ~ '(mierda|mierd|mier|puta|put|zorra|zorr|bastardo|bastard|cabrón|cabr|guarra|guarr|guarro|guarr)';";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                String comentario = resultSet.getString("comentario");
                int id_comentario = resultSet.getInt("id_comentario");
                String comentarioCensurado = censurarComentario(comentario);
                System.out.println(comentarioCensurado);
                String actualizarComentarioQuery = "UPDATE Comentarios SET comentario = ? WHERE id_comentario = ?";
                PreparedStatement actualizarPreparedStatement = connection.prepareStatement(actualizarComentarioQuery);
                actualizarPreparedStatement.setString(1, comentarioCensurado);
                actualizarPreparedStatement.setInt(2, id_comentario);
                actualizarPreparedStatement.executeUpdate();
            }
            // Obtener los comentarios con el nombre la fecha y el contenido del comentario
            String obtenerComentariosQuery = "SELECT c.comentario, c.fecha_comentario, u.nombre\n" +
                    "FROM comentarios c\n" +
                    "INNER JOIN usuario u\n" +
                    "ON c.id_usuario = u.id_usuario;";
            PreparedStatement obtenerPreparedStatement = connection.prepareStatement(obtenerComentariosQuery);
            ResultSet obtenerResultSet = obtenerPreparedStatement.executeQuery();
            while(obtenerResultSet.next()){
                String[] comentarioArray = new String[]{obtenerResultSet.getString("comentario"),
                        String.valueOf(obtenerResultSet.getDate("fecha_comentario")),
                        obtenerResultSet.getString("nombre")};
                listaComentarios.add(comentarioArray);
            }

        }catch (Exception e){
            throw new SQLException("Error al censurar los comentarios");

        }finally {
            closeDBConnection();
        }
        return listaComentarios;

    }


    public ArrayList<String[]> getUsuarios() throws SQLException {
        ArrayList<String[]> listaUsuarios = new ArrayList<>();

        if(!initDBConnection()){
            throw new SQLException("Error al conectar con la bse de datos");
        }

        try {
            String query = "SELECT id_usuario, username, email, activo, premios\n" +
                    "FROM Usuario";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                String[] usuario = new String[]{resultSet.getString("id_usuario"),
                        resultSet.getString("username"),
                        resultSet.getString("email"),
                        String.valueOf(resultSet.getBoolean("activo")),
                        resultSet.getString("premios")};
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

    public ArrayList<String[]> cambiarEstadoUsuario(int idUsuario) throws SQLException {
        ArrayList<String[]> listaUsuarios = new ArrayList<>();

        // Iniciar la conexión a la base de datos
        if (!initDBConnection()) {
            throw new SQLException("Error al conectar con la base de datos");
        }

        try {
            // Obtener el estado actual del usuario
            String selectQuery = "SELECT username, activo FROM Usuario WHERE id_usuario = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setInt(1, idUsuario);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                // Cambiar el estado
                boolean nuevoEstado = !resultSet.getBoolean("activo");

                // Actualizar el estado en la base de datos
                String updateQuery = "UPDATE Usuario SET activo = ? WHERE id_usuario = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setBoolean(1, nuevoEstado);
                updateStatement.setInt(2, idUsuario);
                updateStatement.executeUpdate();
            }

            // Obtener la lista actualizada de usuarios
            String listQuery = "SELECT id_usuario, username, activo FROM Usuario WHERE id_usuario = ?";
            PreparedStatement listStatement = connection.prepareStatement(listQuery);
            listStatement.setInt(1, idUsuario);
            ResultSet listResultSet = listStatement.executeQuery();

            while (listResultSet.next()) {
                String[] usuarioData = new String[]{
                        String.valueOf(listResultSet.getInt("id_usuario")),
                        listResultSet.getString("username"),
                        String.valueOf(listResultSet.getBoolean("activo"))
                };
                listaUsuarios.add(usuarioData);
            }
        } catch (SQLException e) {
            throw new SQLException("Error al cambiar el estado del usuario: " + e.getMessage());
        } finally {
            closeDBConnection();
        }

        return listaUsuarios; // Retorna la lista de usuarios actualizada
    }


    public ArrayList<String[]> obtenerBuenosComentarios() throws SQLException {
        ArrayList<String[]> niceCommentsList = new ArrayList<>();

        if (!initDBConnection()) {
            throw new SQLException("Error al conectar con la base de datos");
        }

        try {
            String query = "SELECT c.id_usuario, " +
                    "u.nombre AS nombre_usuario, " +
                    "c.fecha_comentario, " +
                    "c.comentario, " +
                    "e.nombre_evento " +
                    "FROM Comentarios c " +
                    "INNER JOIN Usuario u ON c.id_usuario = u.id_usuario " +
                    "INNER JOIN Evento e ON c.id_evento = e.id_evento";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String comentario = resultSet.getString("comentario");
                if (checkNiceWord(comentario)) {
                    String[] comment = new String[]{
                            String.valueOf(resultSet.getInt("id_usuario")),
                            resultSet.getString("nombre_usuario"),
                            String.valueOf(resultSet.getDate("fecha_comentario")),
                            comentario,
                            resultSet.getString("nombre_evento")
                    };

                    niceCommentsList.add(comment);
                }
            }

        } catch (Exception e) {
            throw new SQLException("Error al consultar los comentarios: " + e.getMessage());
        } finally {
            closeDBConnection();
        }

        return niceCommentsList;
    }

    // Método para verificar si el comentario contiene palabras bonitas
    private boolean checkNiceWord(String comentario) {
        String pattern = "(espectacular|espectacula|increíble|increibl|espléndido|espléndid|excelente|excelent|perfecto|perfect|único|únic|agradable|agradabl)";
        return comentario.toLowerCase().matches(".*" + pattern + ".*");
    }

    public ArrayList<String[]> deshabilitarUsuariosInactivos() throws SQLException {
        if (!initDBConnection()) {
            throw new SQLException("Error al conectar con la base de datos");
        }

        // Calcular la fecha de corte (2 meses atrás)
        LocalDate cutOffDate = LocalDate.now().minusMonths(2);

        String updateQuery = "UPDATE Usuario u " +
                             "SET activo = FALSE " +
                             "WHERE u.activo = TRUE AND u.id_usuario NOT IN (" +
                             "    SELECT DISTINCT c.id_usuario " +
                             "    FROM Comentarios c " +
                             "    WHERE c.fecha_comentario >= ? " +
                             ") AND u.premios <> 'Buen usuario'";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setDate(1, Date.valueOf(cutOffDate)); // Establecer la fecha de corte
            int rowsUpdated = preparedStatement.executeUpdate();
            System.out.println(rowsUpdated + " usuarios deshabilitados por inactividad.");
        } catch (Exception e) {
            throw new SQLException("Error al deshabilitar usuarios inactivos: " + e.getMessage());
        } finally {
            closeDBConnection();
        }
        return getUsuarios();
    }


    public ArrayList<String[]> deshabilitarYHabilitarUsuarios(LocalDate fecha, boolean ignorePremio) throws SQLException {
        if (!initDBConnection()) {
            throw new SQLException("Error al conectar con la base de datos");
        }

        String deshabilitarQuery = "UPDATE Usuario u " +
                             "SET activo = FALSE " +
                             "WHERE u.id_usuario NOT IN (" +
                             "    SELECT DISTINCT c.id_usuario " +
                             "    FROM Comentarios c " +
                             "    WHERE c.fecha_comentario >= ? " +
                             ") AND u.premios <> 'Buen usuario';";

        String habilitarQuery = "UPDATE Usuario u " +
                "SET activo = TRUE " +
                "WHERE u.id_usuario IN (" +
                "    SELECT DISTINCT c.id_usuario " +
                "    FROM Comentarios c " +
                "    WHERE c.fecha_comentario >= ? " +
                ") AND u.premios <> 'Buen usuario';";

        if(ignorePremio){
            deshabilitarQuery = "UPDATE Usuario u " +
                          "SET activo = FALSE " +
                          "WHERE u.id_usuario NOT IN (" +
                          "    SELECT DISTINCT c.id_usuario " +
                          "    FROM Comentarios c " +
                          "    WHERE c.fecha_comentario >= ? );";

            habilitarQuery = "UPDATE Usuario u " +
                    "SET activo = TRUE " +
                    "WHERE u.id_usuario IN (" +
                    "    SELECT DISTINCT c.id_usuario " +
                    "    FROM Comentarios c " +
                    "    WHERE c.fecha_comentario >= ? );";
        }

        try{
            PreparedStatement habilitarStmt = connection.prepareStatement(habilitarQuery);
            PreparedStatement deshabilitarStmt = connection.prepareStatement(deshabilitarQuery);
            // Asignar la fecha para ambas consultas
            deshabilitarStmt.setDate(1, Date.valueOf(fecha));
            habilitarStmt.setDate(1, Date.valueOf(fecha));

            // Ejecutar la consulta para deshabilitar
            int deshabilitados = deshabilitarStmt.executeUpdate();
            System.out.println(deshabilitados + " usuarios deshabilitados por inactividad.");

            // Ejecutar la consulta para habilitar
            int habilitados = habilitarStmt.executeUpdate();
            System.out.println(habilitados + " usuarios habilitados por actividad.");

        } catch (SQLException e) {
            throw new SQLException("Error al deshabilitar/habilitar usuarios: " + e.getMessage());
        } finally {
            closeDBConnection();
        }

        return getUsuarios();  // Devuelve la lista de usuarios tras las actualizaciones
    }






}
