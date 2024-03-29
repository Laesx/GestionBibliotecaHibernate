package org.example.vista;

import com.formdev.flatlaf.FlatDarculaLaf;
import org.example.excepciones.CampoVacioExcepcion;
import org.example.modelo.Categoria;
import org.example.modelo.Libro;
import org.example.modelo.Prestamo;
import org.example.modelo.Usuario;
import org.example.modelo.dao.CategoriaDAOImpl;
import org.example.modelo.dao.LibroDAOImpl;
import org.example.modelo.dao.PrestamoDAOImpl;
import org.example.modelo.dao.UsuarioDAOImpl;
import org.example.modelo.dao.helper.Entidades;
import org.example.modelo.dao.helper.LogFile;
import org.example.observer.Observer;
import org.example.observer.Subject;
import org.example.vista.componentes.MiBarraDeEstado;
import org.example.vista.helper.Categorias;
import org.example.vista.helper.Libros;
import org.example.vista.helper.SwgAuxiliar;
import org.example.vista.helper.Usuarios;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Formulario principal de la aplicación, en el se implementarán las opciones
 * de menú necesarias para poder utilizar la aplicación de BIBLIOTECA
 *
 * @author AGE
 * @version 2
 */
public class FormMain extends JFrame implements Observer, ActionListener, FocusListener, WindowListener, KeyListener {
    private static FormMain main = null;
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 756;
    private JDesktopPane desktopPane = new JDesktopPane();

    private static JTextArea textArea;
    private  ArrayList<String> listaComandos;



    private JMenu mArchivo;

    {
        mArchivo = new JMenu("Archivo");
        mArchivo.setMnemonic('A');
    }

    private JMenuItem miAbrir;

    {
        miAbrir = new JMenuItem("Abrir..");
        miAbrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
        miAbrir.setMnemonic('A');
        miAbrir.setFocusable(true);
        miAbrir.addActionListener(this);
        miAbrir.addFocusListener(this);
        //mArchivo.add(miAbrir); TODO pendiente de implementar
    }

    private JMenuItem miGuardarLibro;

    {
        miGuardarLibro = new JMenuItem("Guardar libros..");
        miGuardarLibro.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, KeyEvent.CTRL_DOWN_MASK));
        ImageIcon icono = new ImageIcon("imagenes/xlsx.png");
        miGuardarLibro.setIcon(icono);
        miGuardarLibro.setMnemonic('G');
        miGuardarLibro.setFocusable(true);
        miGuardarLibro.addActionListener(this);
        miGuardarLibro.addFocusListener(this);
        mArchivo.add(miGuardarLibro);
    }

    private JMenuItem miConexion;

    {
        miConexion = new JMenuItem("Conectar");
        miConexion.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
        ImageIcon icono = new ImageIcon("imagenes/conexion.png");
        miConexion.setIcon(icono);
        miConexion.setMnemonic('C');
        miConexion.addActionListener(this);
        mArchivo.addSeparator();
        mArchivo.add(miConexion);
    }

    private JMenuItem miSalir;

    {
        miSalir = new JMenuItem("Salir");
        miSalir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
        ImageIcon icono = new ImageIcon("imagenes/salida.png");
        miSalir.setIcon(icono);
        miSalir.setMnemonic('S');
        miSalir.setFocusable(true);
        miSalir.addActionListener(this);
        miSalir.addFocusListener(this);
        mArchivo.addSeparator();
        mArchivo.add(miSalir);
    }

    private JMenu mCategorias;

    {
        mCategorias = new JMenu("Categorias");
        mCategorias.setMnemonic('U');
        mCategorias.setFocusable(true);
        mCategorias.addFocusListener(this);
    }

    private JMenuItem miListaCategorias;

    {
        miListaCategorias = new JMenuItem("Lista");
        miListaCategorias.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, KeyEvent.CTRL_DOWN_MASK));
        ImageIcon icono = new ImageIcon("imagenes/lista.png");
        miListaCategorias.setIcon(icono);
        miListaCategorias.setMnemonic('L');
        miListaCategorias.setFocusable(true);
        miListaCategorias.addActionListener(this);
        miListaCategorias.addFocusListener(this);
        mCategorias.add(miListaCategorias);

    }

    private JMenuItem miNuevaCategoria;

    {
        miNuevaCategoria = new JMenuItem("Nuevo");
        miNuevaCategoria.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_5, KeyEvent.CTRL_DOWN_MASK));
        ImageIcon icono = new ImageIcon("imagenes/nuevo.png");
        miNuevaCategoria.setIcon(icono);
        miNuevaCategoria.setMnemonic('N');
        miNuevaCategoria.setFocusable(true);
        miNuevaCategoria.addActionListener(this);
        miNuevaCategoria.addFocusListener(this);
        mCategorias.add(miNuevaCategoria);
    }

    private JMenu mUsuarios;

    {
        mUsuarios = new JMenu("Usuarios");
        mUsuarios.setMnemonic('U');
        mUsuarios.setFocusable(true);
        mUsuarios.addFocusListener(this);
    }

    private JMenuItem miListaUsuarios;

    {
        miListaUsuarios = new JMenuItem("Lista");
        miListaUsuarios.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, KeyEvent.CTRL_DOWN_MASK));
        ImageIcon icono = new ImageIcon("imagenes/lista.png");
        miListaUsuarios.setIcon(icono);
        miListaUsuarios.setMnemonic('L');
        miListaUsuarios.setFocusable(true);
        miListaUsuarios.addActionListener(this);
        miListaUsuarios.addFocusListener(this);
        mUsuarios.add(miListaUsuarios);

    }


    private JMenuItem miNuevoUsuario;

    {
        miNuevoUsuario = new JMenuItem("Nuevo");
        miNuevoUsuario.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_6, KeyEvent.CTRL_DOWN_MASK));
        ImageIcon icono = new ImageIcon("imagenes/nuevo.png");
        miNuevoUsuario.setIcon(icono);
        miNuevoUsuario.setMnemonic('N');
        miNuevoUsuario.setFocusable(true);
        miNuevoUsuario.addActionListener(this);
        miNuevoUsuario.addFocusListener(this);
        mUsuarios.add(miNuevoUsuario);

    }

    private JMenu mLibros;

    {
        mLibros = new JMenu("Libros");
        mLibros.setMnemonic('L');
        mLibros.setFocusable(true);
        mLibros.addFocusListener(this);
    }

    private JMenuItem miListaLibros;

    {
        miListaLibros = new JMenuItem("Lista");
        miListaLibros.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, KeyEvent.CTRL_DOWN_MASK));
        ImageIcon icono = new ImageIcon("imagenes/lista.png");
        miListaLibros.setIcon(icono);
        miListaLibros.setMnemonic('L');
        miListaLibros.setFocusable(true);
        miListaLibros.addActionListener(this);
        miListaLibros.addFocusListener(this);
        mLibros.add(miListaLibros);
    }

    private JMenuItem miNuevoLibro;

    {
        miNuevoLibro = new JMenuItem("Nuevo");
        miNuevoLibro.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_7, KeyEvent.CTRL_DOWN_MASK));
        ImageIcon icono = new ImageIcon("imagenes/nuevo.png");
        miNuevoLibro.setIcon(icono);
        miNuevoLibro.setMnemonic('N');
        miNuevoLibro.setFocusable(true);
        miNuevoLibro.addActionListener(this);
        miNuevoLibro.addFocusListener(this);
        mLibros.add(miNuevoLibro);
    }


    private JMenu mPrestamos;

    {
        mPrestamos = new JMenu("Préstamos");
        mPrestamos.setMnemonic('P');
        mPrestamos.setFocusable(true);
        mPrestamos.addFocusListener(this);
    }

    private JMenuItem miListaPrestamos;

    {
        miListaPrestamos = new JMenuItem("Lista");
        miListaPrestamos.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, KeyEvent.CTRL_DOWN_MASK));
        ImageIcon icono = new ImageIcon("imagenes/lista.png");
        miListaPrestamos.setIcon(icono);
        miListaPrestamos.setMnemonic('L');
        miListaPrestamos.setFocusable(true);
        miListaPrestamos.addActionListener(this);
        miListaPrestamos.addFocusListener(this);
        mPrestamos.add(miListaPrestamos);
    }

    private JMenuItem miNuevoPrestamo;

    {
        miNuevoPrestamo = new JMenuItem("Nuevo");
        miNuevoPrestamo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_8, KeyEvent.CTRL_DOWN_MASK));
        ImageIcon icono = new ImageIcon("imagenes/nuevo.png");
        miNuevoPrestamo.setIcon(icono);
        miNuevoPrestamo.setMnemonic('N');
        miNuevoPrestamo.setFocusable(true);
        miNuevoPrestamo.addActionListener(this);
        miNuevoPrestamo.addFocusListener(this);
        miNuevoPrestamo.setEnabled(false);
        mPrestamos.add(miNuevoPrestamo);
    }

    private JMenu miHistorial;

    {
        miHistorial = new JMenu("Historial");
        miHistorial.setMnemonic('P');
        miHistorial.setFocusable(true);
        miHistorial.addFocusListener(this);
    }

    private JMenuItem miHistorialVer;
    {
        miHistorialVer = new JMenuItem("Cargar Logs");
        miHistorialVer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK));
        ImageIcon icono = new ImageIcon("imagenes/log.png");
        miHistorialVer.setIcon(icono);
        miHistorialVer.setMnemonic('L');
        miHistorialVer.setFocusable(true);
        miHistorialVer.addActionListener(this);
        miHistorialVer.addFocusListener(this);
        miHistorial.add(miHistorialVer);
    }

    private JMenuItem miHistorialLogActual;

    {
        miHistorialLogActual = new JMenuItem("Log InSesion");
        miHistorialLogActual.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.CTRL_DOWN_MASK));
        ImageIcon icono = new ImageIcon("imagenes/logDin.png");
        miHistorialLogActual.setIcon(icono);
        miHistorialLogActual.setMnemonic('L');
        miHistorialLogActual.setFocusable(true);
        miHistorialLogActual.addActionListener(this);
        miHistorialLogActual.addFocusListener(this);
        miHistorial.add(miHistorialLogActual);
    }

    /**
     * Menú de ayuda donde estarán los créditos de la aplicación
     */
    private JMenu mAyuda;{
        mAyuda = new JMenu("Ayuda");
        mAyuda.setMnemonic('y');
        mAyuda.setFocusable(true);
        mAyuda.addFocusListener(this);
    }

    /**
     * Créditos de la aplicación
     */
    private JMenuItem miCreditos;{
        miCreditos = new JMenuItem("Créditos");
        miCreditos.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK));
        ImageIcon icono = new ImageIcon("imagenes/creditos.png");
        miCreditos.setIcon(icono);
        miCreditos.setMnemonic('C');
        miCreditos.setFocusable(true);
        miCreditos.addActionListener(this);
        miCreditos.addFocusListener(this);
        mAyuda.add(miCreditos);
    }


    private JMenuBar jMenuBar;

    {
        jMenuBar = new JMenuBar();
        jMenuBar.add(mArchivo);
        jMenuBar.add(mCategorias);
        jMenuBar.add(mUsuarios);
        jMenuBar.add(mLibros);
        jMenuBar.add(mPrestamos);
        jMenuBar.add(miHistorial);//Agreamos la nueva pestaña de Historial
        jMenuBar.add(mAyuda);  // Agregamos la nueva pestaña de Ayuda
        jMenuBar.addFocusListener(this);
    }

    private MiBarraDeEstado miBarraDeEstado;

    {
        miBarraDeEstado = MiBarraDeEstado.getInstance();
    }

    private FormMain() {
        setVentana();
        setContenedores();
        actualizaFormulario(false);
        addEventos();
    }

    private void addEventos() {
        addWindowListener(this);
        getContentPane().setFocusable(true);
        getContentPane().addKeyListener(this);
        getContentPane().addFocusListener(this);
    }

    private void setContenedores() {
        setLayout(new BorderLayout());
        add(jMenuBar, BorderLayout.NORTH);
        add(desktopPane, BorderLayout.CENTER);
        add(miBarraDeEstado, BorderLayout.SOUTH);
    }

    private void setVentana() {
        setTitle("Aplicación de gestión de una biblioteca: ");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(0, 0, WIDTH, HEIGHT);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

    }

    public JDesktopPane getDesktopPane() {
        return desktopPane;
    }

    /**
     * Este método habilitará o desactivará las distintas
     * opciones de menú del programa según corresponda
     */
    public void actualizaFormulario(boolean conectado) {
        miConexion.setEnabled(!conectado);
        miAbrir.setEnabled(conectado);
        mCategorias.setEnabled(conectado);
        mUsuarios.setEnabled(conectado);
        mLibros.setEnabled(conectado);
        mPrestamos.setEnabled(conectado);
        miHistorial.setEnabled(conectado);
    }

    /**
     * Método para la implementación del Singleton del formulario principal
     *
     * @return el objeto global donde se instancia el formulario de la aplicación
     */
    public static FormMain getInstance(){
        if (main==null) {
            // Cambia la skin de la aplicación por FlatLaf
            // Se hace aqui ya que tiene que ser antes de que se inicie la Ventana Principal
            FlatDarculaLaf.setup();
            main = new FormMain();
            ImageIcon icono = new ImageIcon("biblioteca.png");
            main.setIconImage(icono.getImage());
            main.loginPassword();
        }
        return main;
    }

    private void muestraCategorias() {
        try {
            desktopPane.add(Categorias.listaCategorias());
            desktopPane.selectFrame(false);
        } catch (Exception e) {
            SwgAuxiliar.msgExcepcion(e);
        }
    }

    private void nuevaCategoria() {
        try {
            desktopPane.add(Categorias.fichaCategoria(new Categoria()));
            desktopPane.selectFrame(false);
        } catch (Exception e) {
            SwgAuxiliar.msgExcepcion(e);
        }
    }

    private void muestraUsuarios() {
        try {
            desktopPane.add(Usuarios.listaUsuarios());
            desktopPane.selectFrame(false);
        } catch (Exception e) {
            SwgAuxiliar.msgExcepcion(e);
        }
    }

    private void cargaHistorial() throws IOException {
        // Crear un JFileChooser
        JFileChooser fileChooser = new JFileChooser();
        // Establecer la carpeta inicial del JFileChooser
        String rutaCarpeta = "ficheros"; // Ruta de la carpeta que deseas
        File carpetaInicial = new File(rutaCarpeta);
        fileChooser.setCurrentDirectory(carpetaInicial);
        // Filtrar solo archivos de texto
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File file) {
                return file.getName().toLowerCase().endsWith(".log") || file.isDirectory();
            }
            public String getDescription() {
                return "Archivos de texto (*.log)";
            }
        });
        // Mostrar el cuadro de diálogo para seleccionar un archivo
        int seleccion = fileChooser.showOpenDialog(null);
        String nombreArchivoLog;
        // Si se selecciona un archivo
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            nombreArchivoLog = archivo.getName();
            // Leer el archivo de texto
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            StringBuilder sb = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) {
                sb.append(linea);
                sb.append("\n");
            }
            br.close();
            // Mostrar el texto en un componente con scroll
            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            // Mostrar el componente en una ventana
            JFrame frame = new JFrame("Historial archivo:" + nombreArchivoLog);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(FormMain.getInstance());
            frame.add(scrollPane);
            frame.pack();
            frame.setVisible(true);
        }
    }

    private void SessionActualLog() {
        textArea= new JTextArea();
        textArea.setEditable(false);
        textArea.setText("");
        listaComandos = LogFile.getListaComandosLogs();
        for (String lineaComandos: listaComandos) {
            textArea.append(lineaComandos+"\n");
        }
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        // Crear el JFrame y agregar el JScrollPane
        JFrame frame = new JFrame("Session Actual");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(FormMain.getInstance());
        frame.add(scrollPane);
        frame.pack();
        frame.setVisible(true);
    }

    public static void actualizarLogSession( ArrayList<String> listaComandos){
        if(textArea!=null){
            StringBuilder sb = new StringBuilder();
            for (String linea : listaComandos) {
                sb.append(linea).append("\n");
            }
            textArea.setText(sb.toString());
        }
    }

    public JTextArea devuelveMiJtextArea(JTextArea textArea) {
        this.textArea=textArea;
        return textArea;
    }




    private void nuevoUsuario() {
        try {
            desktopPane.add(Usuarios.fichaUsuario(new Usuario()));
            desktopPane.selectFrame(false);
        } catch (Exception e) {
            SwgAuxiliar.msgExcepcion(e);
        }
    }

    private void muestraLibros() {
        try {
            desktopPane.add(Libros.listaLibros());
            desktopPane.selectFrame(false);
        } catch (Exception e) {
            SwgAuxiliar.msgExcepcion(e);
        }
    }


    private void nuevoLibro() {
        try {
            desktopPane.add(Libros.fichaLibro(new Libro()));
            desktopPane.selectFrame(false);
        } catch (Exception e) {
            SwgAuxiliar.msgExcepcion(e);
        }
    }

    private void muestraPrestamos() {
        try {
            desktopPane.add(org.example.vista.helper.Prestamos.listaPrestamos());
            desktopPane.selectFrame(false);
        } catch (Exception e) {
            SwgAuxiliar.msgExcepcion(e);
        }
    }

    private void nuevoPrestamo() {
        desktopPane.add(new FichaPrestamo(new Prestamo()));
        desktopPane.selectFrame(false);
    }

    private void loginPassword() {
        new LoginPass(this, "Conectar BD:", true).setVisible(true);
    }

    private void salir() {
        if (JOptionPane.showConfirmDialog(FormMain.getInstance(),
                "¿Seguro que desea SALIR?",
                "Atención:", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            System.exit(0);
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (e.getComponent().equals(mArchivo))
            miBarraDeEstado.setInfo("Opciones para archivos");
        else if (e.getComponent().equals(miAbrir))
            miBarraDeEstado.setInfo("Pulsa para cargar una imagen en el visor");
        else if (e.getComponent().equals(miSalir))
            miBarraDeEstado.setInfo("Cierra la aplicación");

        else
            miBarraDeEstado.setInfo(String.format("Infor: mArchivo: %b, miAbrir: %b, miAbrir: %b", mArchivo.isFocusable(), miAbrir.isFocusable(), miSalir.isFocusable()));
    }

    @Override
    public void focusLost(FocusEvent e) {

    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        salir();
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

    public static int posInterna() {
        return FormMain.getInstance().getDesktopPane().getComponentCount() * 25; // hasta que no se visualiza no se contabiliza
    }

    public static void actualizaListaUsuarios() throws SQLException, CampoVacioExcepcion, IOException {
        List<Usuario> usuarios = Entidades.leerAllUsuarios();
        for (int i = 0; i < FormMain.getInstance().getDesktopPane().getComponentCount(); i++)
            if (FormMain.getInstance().getDesktopPane().getComponent(i) instanceof ListaUsuarios)
                ((ListaUsuarios) FormMain.getInstance().getDesktopPane().getComponent(i)).setUsuarios(usuarios);
    }

    public static void actualizaListaCategorias() throws SQLException, CampoVacioExcepcion, IOException {
        List<Categoria> categorias = Entidades.leerAllCategorias();
        for (int i = 0; i < FormMain.getInstance().getDesktopPane().getComponentCount(); i++)
            if (FormMain.getInstance().getDesktopPane().getComponent(i) instanceof ListaCategorias)
                ((ListaCategorias) FormMain.getInstance().getDesktopPane().getComponent(i)).setCategorias(categorias);
    }

    public static void actualizaListaLibros() throws SQLException, CampoVacioExcepcion, IOException {
        List<Libro> libros = Entidades.leerAllLibros();
        for (int i = 0; i < FormMain.getInstance().getDesktopPane().getComponentCount(); i++)
            if (FormMain.getInstance().getDesktopPane().getComponent(i) instanceof ListaLibros)
                ((ListaLibros) FormMain.getInstance().getDesktopPane().getComponent(i)).setLibros(libros);
    }

    public static void actualizaListaPrestamos() throws SQLException, CampoVacioExcepcion, IOException {
        List<Prestamo> prestamos = Entidades.leerAllPrestamos();
        for (int i = 0; i < FormMain.getInstance().getDesktopPane().getComponentCount(); i++)
            if (FormMain.getInstance().getDesktopPane().getComponent(i) instanceof ListaPrestamos)
                ((ListaPrestamos) FormMain.getInstance().getDesktopPane().getComponent(i)).setPrestamos(prestamos);
    }

    public static void barraEstado(String mensaje) {
        FormMain.getInstance().miBarraDeEstado.setInfo(mensaje);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            salir();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(miSalir))
            salir();
        else if (e.getSource().equals(miConexion))
            loginPassword();
        else if (e.getSource() == miListaUsuarios)
            muestraUsuarios();
        else if (e.getSource() == miNuevoUsuario)
            nuevoUsuario();
        else if (e.getSource() == miListaCategorias)
            muestraCategorias();
        else if (e.getSource() == miNuevaCategoria)
            nuevaCategoria();
        else if (e.getSource() == miListaLibros)
            muestraLibros();
        else if (e.getSource() == miHistorialVer) {
            try {
                cargaHistorial();
            } catch (IOException ex) {
                SwgAuxiliar.msgExcepcion(ex);
            }
        }
        else if (e.getSource() == miHistorialLogActual)
            SessionActualLog();
        else if (e.getSource() == miNuevoLibro)
            nuevoLibro();
        else if (e.getSource() == miListaPrestamos)
            muestraPrestamos();
        else if (e.getSource() == miNuevoPrestamo)
            nuevoPrestamo();
        else if (e.getSource() == miGuardarLibro) {
            try {
                SwgAuxiliar.grabarCSV("Libro", ',');
            } catch (Exception ex) {
                SwgAuxiliar.msgExcepcion(ex);
            }
        } else if (e.getSource() == miCreditos){
            mostrarCreditos();
        }
    }

    private void mostrarCreditos() {
        JOptionPane.showMessageDialog(FormMain.getInstance(),
                "CEO/Manager:\n" +
                        "- Antonio García Expósito\n" +
                        "UMPALUMPAS:\n" +
                        "- Eric\n" +
                        "- Sebastián Olea Castillo\n" +
                        "- Juan Manuel Sújar González\n" +
                        "- José María La Torre Ávila",
                "Créditos", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void update(Subject sub) throws Exception {
        if (sub instanceof UsuarioDAOImpl) {
            actualizaListaUsuarios();
        } else if (sub instanceof CategoriaDAOImpl) {
            actualizaListaCategorias();
        } else if (sub instanceof LibroDAOImpl){
            actualizaListaLibros();
        } else if (sub instanceof PrestamoDAOImpl) {
            actualizaListaPrestamos();
        }
    }
}
