package org.example.modelo.dao.helper;

import org.example.observer.Observer;
import org.example.observer.Subject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Para registrar todas las operaciones realizadas en la base de datos
 * en ficheros logs locales
 * @author AGE
 * @version 2
 */
public class LogFile implements Subject {
    private static String file="ficheros/historial"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("_yyyyMMdd"))+".log";

    private static ArrayList<String> listaComandosLogs;
    private static String nomFile;
    /**
     * Graba en el fichero log para el día actual el mensaje recibido
     * el mensaje tambien es grabado en la tabla historico de la BD
     * @param msgLog texto a guarda en el fichero log
     * @throws IOException en el caso de que no pueda accederse adecuadamente al fichero
     */
    public static void saveLOG(String msgLog) throws Exception {
        saveLOGsinBD(msgLog);
        //HistoricoDAOImpl.mensaje(msgLog);
    }


    public static ArrayList<String> getListaComandosLogs() {
        return listaComandosLogs;
    }

    public static ArrayList<String> agregaLineaListaLog(String msqLog){
       if(listaComandosLogs!=null){
           listaComandosLogs.add(msqLog);
       }else{
           listaComandosLogs=new ArrayList<>();
           listaComandosLogs.add(msqLog);
       }
       return listaComandosLogs;
    }


    /**
     * Graba en el fichero log para el día actual el mensaje recibido
     * @param msgLog texto a guarda en el fichero log
     * @throws IOException en el caso de que no pueda accederse adecuadamente al fichero
     */
    public static void saveLOGsinBD(String msgLog) throws IOException {
        agregaLineaListaLog(msgLog);
        Path path = Paths.get(file);
        if (path.toFile().exists())
            Files.writeString(path,msgLog+"\n",StandardCharsets.UTF_8,StandardOpenOption.APPEND);
        else Files.writeString(path,msgLog+"\n",StandardCharsets.UTF_8,StandardOpenOption.CREATE);
    }


    private Observer observer;

    @Override
    public void register(Observer obj){
        if (obj == null) throw new NullPointerException("Null Observer");
        observer=obj;
    }

    @Override
    public void unregister(Observer obj) {
        observer=null;
    }

    @Override
    public void notifyObservers() throws Exception {
        if (observer!=null){
            observer.update(this);
        }
    }

    @Override
    public Object getUpdate(Observer obj) {
        return null;
    }
}
