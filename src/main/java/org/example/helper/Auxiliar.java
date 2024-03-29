package org.example.helper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Auxiliar {
    public static String dameFechaActual() {
        // Obtener la fecha actual
        LocalDate fechaActual = LocalDate.now();
        // Formatear la fecha en un formato específico, por ejemplo, "dd/MM/yyyy"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaFormateada = fechaActual.format(formatter);
        return fechaFormateada;
    }
}