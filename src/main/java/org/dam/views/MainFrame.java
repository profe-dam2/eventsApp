package org.dam.views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import static org.dam.controllers.MainFrameController.GET_ARTITS_EVENTS;
import static org.dam.controllers.MainFrameController.GET_USER_EVENTS;

public class MainFrame extends JFrame implements InterfaceView {
    private JPanel mainPanel;
    private JComboBox cb_generos;
    private JTable tb_consultas;
    private JComboBox cb_rol;
    private JTextField tx_id1;
    private JButton bt_buscar1;
    private JTextField tx_idartista;
    private JButton bt_artistsevents;

    public MainFrame() {
        initWindow();
    }

    @Override
    public void initWindow() {
        setContentPane(mainPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setCommands();
        initComponents();
    }

    public int getIdArtista() {
        return Integer.parseInt(tx_idartista.getText());
    }

    public int getIdUser(){
        return Integer.parseInt(tx_id1.getText());
    }

    public void loadTable(ArrayList<String[]> datos, String[] encabezado) {
        DefaultTableModel model = new DefaultTableModel(null, encabezado);

        for (String[] row : datos) {
            model.addRow(row);
        }
        tb_consultas.setModel(model);
    }

    public void loadUsuarios(ArrayList<String[]> usuarios) {
        String[] encabezado = new String[]{"ID","USERNAME","EMAIL","ACTIVADO"};
        DefaultTableModel model = new DefaultTableModel(null, encabezado);
        for (String[] usuario : usuarios) {
            model.addRow(usuario);
        }
        tb_consultas.setModel(model);

    }

    public String getRole(){
        return (String)cb_rol.getSelectedItem();
    }

    public void loadCbRol(){
        DefaultComboBoxModel modelo = new DefaultComboBoxModel();
        modelo.addElement("Usuario");
        modelo.addElement("Admin");
        cb_rol.setModel(modelo);
    }

    public void setCbGeneros(ArrayList<String> generos) {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for(String genero: generos) {
            model.addElement(genero);
        }

        cb_generos.setModel(model);
    }

    @Override
    public void showWindow() {
        setVisible(true);
    }

    @Override
    public void closeWindow() {
        dispose();
    }

    @Override
    public void setCommands() {
        bt_artistsevents.setActionCommand(GET_ARTITS_EVENTS);
        bt_buscar1.setActionCommand(GET_USER_EVENTS);
    }

    @Override
    public void addListener(ActionListener listener) {
        this.addWindowListener((WindowListener) listener);
        cb_rol.addItemListener((ItemListener) listener);
        bt_artistsevents.addActionListener(listener);
        bt_buscar1.addActionListener(listener);
    }

    @Override
    public void initComponents() {
        cb_rol.setName("cb_rol");
        cb_generos.setName("cb_generos");
    }
}
