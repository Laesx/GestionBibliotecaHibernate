package org.example.singleton;

import org.example.helper.EncriptacionDesencriptacion;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import static org.example.helper.Auxiliar.dameFechaActual;

/**
 * Esta clase solo permitira la instanciación de un único objeto
 * para definir y mantener la configuración de una aplicación,
 * teniendo esta implementada en un fichero de propiedades
 * ubicado en la carpeta ficheros y con nombre de archivo biblioteca.config
 * @author AGE
 * @version 2
 */
public class Configuracion {
    public static final String FILE_CONF="ficheros/biblioteca.config";
    private static final String claveSecreta="asdf234fsdva%l9asdnklfa@f4f_adfafaAAaad;";
    private static Configuracion conf=null;
    private static final String DRIVER_DEFAULT="com.mysql.cj.jdbc.Driver";

    private String driver;
    private String url;
    private String user;
    private String password;

    private Configuracion() throws Exception {
        Properties p = new Properties();
        p.load(new FileReader(FILE_CONF));
        driver = p.getProperty("driver");
        url = p.getProperty("url");
        user = p.getProperty("user");
        password = p.getProperty("password");
        //modificarConfigHibernate();
    }

    /**
     * para modificar las propiedades del fichero de configuración xml de JPA/Hibernate
     * TODO Borrar, no sirve para nada al final xd
     */
    private void modificarConfigHibernate() throws Exception{
        File inputFile = new File("src/main/resources/META-INF/persistence.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName("property");

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element elemento = (Element) nNode;
                if (elemento.getAttribute("name").equals("hibernate.connection.url")){
                    elemento.setAttribute("value", url);
                }
                if (elemento.getAttribute("name").equals("hibernate.connection.username")){
                    elemento.setAttribute("value", user);
                }
                if (elemento.getAttribute("name").equals("hibernate.connection.password")){
                    // Guarda la contraseña desencriptada para que hibernate pueda acceder a ella
                    elemento.setAttribute("value", getPassword());
                }
                if (elemento.getAttribute("name").equals("hibernate.connection.driver_class")){
                    elemento.setAttribute("value", driver);
                }
            }
        }

        // Escribir toda la configuracion al archivo XML
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File("src/main/resources/META-INF/persistence.xml"));
        transformer.transform(source, result);
    }

    /**
     * para obtener el driver que necesita el contralador JDBC para la conexión
     * @return la clase del driver JDBC
     */
    public String getDriver() {
        if (driver.equals(""))
            return DRIVER_DEFAULT;
        else return driver;
    }

    /**
     * actualiza el driver en la configuración
     * @param driver la clase del driver JDBC
     */
    public void setDriver(String driver) {
        this.driver = driver;
    }
    /**
     * para obtener la configuracíón del servidor/puerto/esquema de la BD
     * @return cadena de conexión a la bd
     */
    public String getUrl() {
        if (url.equals(""))
            return "Indica la url en el fichero de configuración";
        return url;
    }
    /**
     * actualiza la cadena de conexión
     * @param url cadena de conexión a la bd
     */
    public void setUrl(String url) {
        this.url = url;
    }
    /**
     * para obtener el usuario de la bd
     * @return usuario de la bd
     */
    public String getUser() {
        return user;
    }
    /**
     * actualiza el usuario de conexión
     * @param user usuario de conexión a la bd
     */
    public void setUser(String user) {
        this.user = user;
    }
    /**
     * para obtener la contraseña de la bd asociada a un usario
     * @return contraseña de la bd
     */
    public String getPassword() throws Exception {
        return EncriptacionDesencriptacion.desencriptar(password,claveSecreta);
    }
    /**
     * actualiza la contraseña de la bd asociada a un usario
     * @param password contraseña de la bd
     */
    public void setPassword(String password) throws Exception {
        this.password = EncriptacionDesencriptacion.encriptar(password,claveSecreta);
        /*
        try {
            modificarConfigHibernate();
        } catch (Exception e) {
            // TODO Provisional
            throw new RuntimeException(e);
        }*/
    }
    /**
     * implentación del patrón de diseño Singleton para esta clase
     * @return instancia única del objeto de esta clase para la aplicación actual
     * @throws IOException errores de entrada y salida cuando trabajamos con el fichero de configuración de la aplicación
     */
    public static Configuracion getInstance() throws Exception {
        if (conf==null){
            conf=new Configuracion();
            Runtime.getRuntime().addShutdownHook(new MiApagado());
        }
        return conf;
    }
    private static class MiApagado extends Thread {
        @Override
        public void run() {
            super.run();
            if (conf != null) {
                Properties p = new Properties();
                p.setProperty("driver", conf.driver);
                p.setProperty("url", conf.url);
                p.setProperty("user", conf.user);
                try {
                    //p.setProperty("password", EncriptacionDesencriptacion.encriptar(conf.password, claveSecreta));
                    p.setProperty("password", conf.password);
                    p.store(new FileWriter(FILE_CONF), String.format("Actualizado el %s", dameFechaActual()));
                    //conf.modificarConfigHibernate();
                    conf = null;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
