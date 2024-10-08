package org.dam;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import org.dam.controllers.MainFrameController;
import org.dam.dao.*;
import org.dam.models.EventModel;
import org.dam.views.MainFrame;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {




        FlatDarculaLaf.setup();

        //Vista
        MainFrame frame = new MainFrame();

        //DAOs
        GeneroDAO generoDAO = new GeneroDAO();
        UsuariosDAO usuariosDAO = new UsuariosDAO();
        EventosDAO eventosDAO = new EventosDAO();
        RegistroDAO registroDAO = new RegistroDAO();
        ArtistaDAO artistaDAO = new ArtistaDAO();

        //Controladores
        MainFrameController mainFrameController = new MainFrameController(frame, generoDAO,
                usuariosDAO, artistaDAO, eventosDAO);

        //Listeners
        frame.addListener(mainFrameController);



        // PRUEBAS
//        try {
//            eventosDAO.createEvent(new EventModel("ErciEvento",
//                    "Un viernes loco en el ercilla",
//                    20,200, true, 100000,
//                    LocalDate.now()));
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        try {
//            registroDAO.createRegistro(1,20);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }

//        ArrayList<Integer> id_artista_lista = new ArrayList<>();
//        id_artista_lista.add(1);
//        id_artista_lista.add(20);
//        id_artista_lista.add(3);
//        id_artista_lista.add(10);
//        try {
//            eventosDAO.addArtistsToEvent(1, id_artista_lista);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }




        frame.showWindow();
    }
}
