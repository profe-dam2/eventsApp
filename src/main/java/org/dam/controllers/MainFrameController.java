package org.dam.controllers;

import org.dam.dao.ArtistaDAO;
import org.dam.dao.EventosDAO;
import org.dam.dao.GeneroDAO;
import org.dam.dao.UsuariosDAO;
import org.dam.views.MainFrame;

import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainFrameController implements ActionListener,
        WindowListener, ItemListener {

    public static final String GET_ARTITS_EVENTS = "GET_ARTITS_EVENTS";
    public static final String GET_USER_EVENTS = "GET_USER_EVENTS";

    private final MainFrame mainFrame;
    private final GeneroDAO generoDAO;
    private final UsuariosDAO usuariosDAO;
    private final ArtistaDAO artistaDAO;
    private final EventosDAO eventosDAO;

    public MainFrameController(MainFrame mainFrame, GeneroDAO generoDAO,
                               UsuariosDAO usuariosDAO,
                               ArtistaDAO artistaDAO, EventosDAO eventosDAO) {
        this.mainFrame = mainFrame;
        this.generoDAO = generoDAO;
        this.usuariosDAO = usuariosDAO;
        this.artistaDAO = artistaDAO;
        this.eventosDAO = eventosDAO;
    }

    private void handleGetArtistEvents(){
        try {
            ArrayList<String[]> eventList = artistaDAO.getArtistEvents(mainFrame.getIdArtista());
            String[] encabezado = new String[]{"ARTISTA","EVENTO","FECHA","LUGAR"};
            mainFrame.loadTable(eventList, encabezado);
        } catch (SQLException e) {
            System.out.println("Error al obtener la lista de eventos de los artistas");
        }
    }

    private void handleGetUserEvents(){
        try {
            ArrayList<String[]> eventList = eventosDAO.getEventsByUserID(mainFrame.getIdUser());
            String[] encabezado = new String[]{"ID","EVENTO","DESCRIPCIÓN","FECHA"};
            mainFrame.loadTable(eventList, encabezado);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void handlerGetGeneros(){
        try {
            mainFrame.setCbGeneros(generoDAO.getGeneros());
        } catch (SQLException e) {
            System.out.println("Error al obtener los generos");
        }
    }

    private void handleGetUsuariosByRole(){
        try {
            mainFrame.loadUsuarios(usuariosDAO.getUsuariosByRol(mainFrame.getRole()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleGetUsuarios(){
        try {
            mainFrame.loadUsuarios(usuariosDAO.getUsuarios());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    private void handleLoadCbRol(){
        mainFrame.loadCbRol();
    }

    @Override
    public void windowOpened(WindowEvent e) {
        handleLoadCbRol();
        handlerGetGeneros();
        handleGetUsuarios();

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case GET_ARTITS_EVENTS:
                handleGetArtistEvents();
                break;
            case GET_USER_EVENTS:
                handleGetUserEvents();
                break;
            default:
                break;
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if(e.getStateChange() == ItemEvent.SELECTED){
            JComboBox combo = (JComboBox) e.getSource();
            String comboName = combo.getName();
            if(comboName.equals("cb_rol")){
                handleGetUsuariosByRole();
            }else if(comboName.equals("cb_generos")){
                // obtener los eventos de los artistas del genero indicado
            }

        }
    }
}
