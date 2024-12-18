package org.dam.controllers;

import org.dam.dao.*;
import org.dam.views.MainFrame;

import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainFrameController implements ActionListener,
        WindowListener, ItemListener {

    public static final String GET_ARTITS_EVENTS = "GET_ARTITS_EVENTS";
    public static final String GET_USER_EVENTS = "GET_USER_EVENTS";
    public static final String APARTADO3_INSERT = "APARTADO3_INSERT";


    public static final String UPDATE2 = "UPDATE2";
    public static final String UPDATE3_1 = "UPDATE3_1";
    public static final String UPDATE3_2 = "UPDATE3_2";
    public static final String UPDATE4 = "UPDATE4";
    public static final String UPDATE4_E = "UPDATE4_E";
    public static final String DELETE1 = "DELETE1";
    public static final String DELETE2 = "DELETE2";
    public static final String DELETE3 = "DELETE3";

    private final MainFrame mainFrame;
    private final GeneroDAO generoDAO;
    private final UsuariosDAO usuariosDAO;
    private final ArtistaDAO artistaDAO;
    private final EventosDAO eventosDAO;

    private final UbicacionDAO ubicacionDAO;

    private final ComentarioDAO comentarioDAO;

    public MainFrameController(MainFrame mainFrame, GeneroDAO generoDAO, UsuariosDAO usuariosDAO,
                               ArtistaDAO artistaDAO, EventosDAO eventosDAO, UbicacionDAO ubicacionDAO,
                               ComentarioDAO comentarioDAO) {
        this.mainFrame = mainFrame;
        this.generoDAO = generoDAO;
        this.usuariosDAO = usuariosDAO;
        this.artistaDAO = artistaDAO;
        this.eventosDAO = eventosDAO;
        this.ubicacionDAO = ubicacionDAO;
        this.comentarioDAO = comentarioDAO;
    }


    private void handleUpdate2(){
        try {
            ArrayList<String[]> listaComentarios = usuariosDAO.censurarComentarios();
            String[] encabezado = new String[]{"COMENTARIO","FECHA","USUARIO"};
            mainFrame.loadTable(listaComentarios, encabezado);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleUpdate3_1(){
        try {
            ArrayList<String[]> listaUsuarios = usuariosDAO.establecerPremioMalComentario();
            String[] encabezado = new String[]{"ID USUARIO","NOMBRE USUARIO","EMAIL", "ACTIVO", "PREMIOS"};
            mainFrame.loadTable(listaUsuarios, encabezado);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleUpdate3_2(){
        try {
            ArrayList<String[]> listaUsuarios = usuariosDAO.establecerPremioBuenComentario();
            String[] encabezado = new String[]{"ID USUARIO","NOMBRE USUARIO","EMAIL", "ACTIVO", "PREMIOS"};
            mainFrame.loadTable(listaUsuarios, encabezado);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleUpdate4(){
        try {
            ArrayList<String[]> listaUsuarios = usuariosDAO.deshabilitarUsuariosInactivos();
            String[] encabezado = new String[]{"ID USUARIO","NOMBRE USUARIO","EMAIL", "ACTIVO", "PREMIOS"};
            mainFrame.loadTable(listaUsuarios, encabezado);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleUpdate4_E(){
        try {
            ArrayList<String[]> listaUsuarios = usuariosDAO.deshabilitarYHabilitarUsuarios(mainFrame.getDate1(), mainFrame.getIgnorePremio());
            String[] encabezado = new String[]{"ID USUARIO","NOMBRE USUARIO","EMAIL", "ACTIVO", "PREMIOS"};
            mainFrame.loadTable(listaUsuarios, encabezado);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    private void handleDelete1(){
        try {
            boolean okDelete =  comentarioDAO.eliminarComentario(Integer.parseInt(mainFrame.getIdDelet1()));
            if (okDelete){
                JOptionPane.showMessageDialog(null, "Mensaje eliminado con éxito");
            }else{
                JOptionPane.showMessageDialog(null, "El mensaje indicado no existe");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleDelete2(){
        try {
            boolean okDelete =  ubicacionDAO.eliminarUbicacion(mainFrame.getDelete2Id());
            if (okDelete){
                JOptionPane.showMessageDialog(null, "Ubicacion eliminada con éxito");
            }else{
                JOptionPane.showMessageDialog(null, "La id no existe");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void handleDelete3(){
        try {
            boolean okDelete =  artistaDAO.eliminarArtista(mainFrame.getDelete3Id());
            if (okDelete){
                JOptionPane.showMessageDialog(null, "Artista eliminado con éxito");
            }else{
                JOptionPane.showMessageDialog(null, "La id "+ mainFrame.getDelete3Id() +" no existe");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
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

    private void handleApartado3_Insert(){

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
            case APARTADO3_INSERT:
                handleApartado3_Insert();
                break;
            case UPDATE2:
                handleUpdate2();
                break;
            case UPDATE3_1:
                handleUpdate3_1();
                break;
            case UPDATE3_2:
                handleUpdate3_2();
                break;
            case UPDATE4:
                handleUpdate4();
                break;
            case UPDATE4_E:
                handleUpdate4_E();
                break;
            case DELETE1:
                handleDelete1();
                break;
            case DELETE2:
                handleDelete2();
                break;
            case DELETE3:
                handleDelete3();
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
