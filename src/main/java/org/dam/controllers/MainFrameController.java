package org.dam.controllers;

import org.dam.dao.GeneroDAO;
import org.dam.dao.UsuariosDAO;
import org.dam.views.MainFrame;

import java.awt.event.*;
import java.sql.SQLException;

public class MainFrameController implements ActionListener,
        WindowListener, ItemListener {

    private final MainFrame mainFrame;
    private final GeneroDAO generoDAO;
    private final UsuariosDAO usuariosDAO;
    public MainFrameController(MainFrame mainFrame, GeneroDAO generoDAO, UsuariosDAO usuariosDAO) {
        this.mainFrame = mainFrame;
        this.generoDAO = generoDAO;
        this.usuariosDAO = usuariosDAO;
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

    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if(e.getStateChange() == ItemEvent.SELECTED){
            handleGetUsuariosByRole();
        }
    }
}
