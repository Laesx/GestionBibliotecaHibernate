package org.example.modelo.dao.helper;

import org.example.vista.FormMain;

import javax.swing.*;
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
public class LogFile {
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

    public static  ArrayList<String> getListaComandosLogs() {
        return listaComandosLogs;
    }

    public static  ArrayList<String> agregaLineaListaLog(String msqLog){
       if(listaComandosLogs!=null){
           for(String lineas: listaComandosLogs){
               System.out.println(lineas);
           }
           msqLog=LocalDateTime.now().format(DateTimeFormatter.ofPattern("(HH:mm:ss)"))+msqLog;
           listaComandosLogs.add(msqLog);
           FormMain.actualizarLogSession(listaComandosLogs);
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

}
