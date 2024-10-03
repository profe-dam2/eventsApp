package org.dam;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import org.dam.controllers.MainFrameController;
import org.dam.dao.GeneroDAO;
import org.dam.dao.UsuariosDAO;
import org.dam.views.MainFrame;

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
        //Controladores
        MainFrameController mainFrameController = new MainFrameController(frame, generoDAO, usuariosDAO);

        //Listeners
        frame.addListener(mainFrameController);

        frame.showWindow();
    }
}
