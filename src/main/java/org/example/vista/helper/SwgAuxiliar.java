package org.example.vista.helper;

import org.example.modelo.Libro;
import org.example.modelo.dao.helper.Sql;
import org.example.vista.FormMain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

/**
 * Clase Auxiliar pensada para gestionar los mensajes mas comunes en una aplicación implementada en swing
 * @author AGE
 * @version 2
 */
public class SwgAuxiliar {
    /**
     * Muestra el mensaje de una excepción en un panel option
     * @param e excepción que contiene el mensaje
     */
    public static void msgExcepcion(Exception e){
        Component parent = FormMain.getInstance();
        JOptionPane.showMessageDialog(parent,e.getMessage(),"Error: ",JOptionPane.ERROR_MESSAGE);
    }
    /**
     * Muestra el mensaje en un panel option
     * @param msg  contiene el mensaje
     */
    public static void msgError(String msg){
        Component parent = FormMain.getInstance();
        JOptionPane.showMessageDialog(parent,msg,"Error: ",JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Para asignar a un panel que los objetos que reciben el foco puedan responder al enter y al tab
     * @param panel objeto panel donde actuara las teclas enter y tab
     */
    public static void AsignaTeclaEnterTab(JPanel panel){
        // Configuro el panel central para que permita pulsar teclas enter
        // y tab para pasar de campo
        Set<AWTKeyStroke> teclas = new HashSet<AWTKeyStroke>();
        teclas.add(AWTKeyStroke.getAWTKeyStroke(
                KeyEvent.VK_ENTER, 0));
        teclas.add(AWTKeyStroke.getAWTKeyStroke(
                KeyEvent.VK_TAB, 0));
        panel.setFocusTraversalKeys(
                KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
                teclas);
    }

    /**
     * Permitirá grabar el contenido de una tabla en un fichero csv
     * dentro de la carpeta ficheros/ListaDeTABLA.csv
     * @param tabla nombre de la tabla a grabar el contenido
     * @param delimiter caracter delimitador de campos
     */
    public static void grabarCSV(String tabla,char delimiter) throws Exception {
        Path path = Paths.get("ficheros/ListaDe"+tabla.toUpperCase()+".csv");
        Sql.importCSV(Libro.class,tabla,delimiter, path);
    }
}
