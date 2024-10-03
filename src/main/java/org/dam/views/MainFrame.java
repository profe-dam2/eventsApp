package org.dam.views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.WindowListener;
import java.util.ArrayList;

public class MainFrame extends JFrame implements InterfaceView {
    private JPanel mainPanel;
    private JComboBox cb_generos;
    private JTable tb_consultas;
    private JComboBox cb_rol;

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

    }

    @Override
    public void addListener(ActionListener listener) {
        this.addWindowListener((WindowListener) listener);
        cb_rol.addItemListener((ItemListener) listener);
    }

    @Override
    public void initComponents() {

    }
}
