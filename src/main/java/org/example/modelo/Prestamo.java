package org.example.modelo;

import org.example.modelo.dao.helper.Entidades;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "prestamos", schema = "BIBLIOTECA")
public class Prestamo extends Entidad{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idPrestamo", nullable = false)
    private int idPrestamo;
    @Basic
    @Column(name = "idLibro", nullable = false)
    private int idLibro;
    @Basic
    @Column(name = "idUsuario", nullable = false)
    private int idUsuario;
    @Basic
    @Column(name = "fechaPrestamo", nullable = true)
    private LocalDateTime fechaPrestamo = LocalDateTime.now();

    public int getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(int idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    //TODO revisar formateo de las fechas en los DAO
    public LocalDateTime getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDateTime fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Prestamo prestamo = (Prestamo) o;

        if (idPrestamo != prestamo.idPrestamo) return false;
        if (fechaPrestamo != null ? !fechaPrestamo.equals(prestamo.fechaPrestamo) : prestamo.fechaPrestamo != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idPrestamo;
        result = 31 * result + (fechaPrestamo != null ? fechaPrestamo.hashCode() : 0);
        return result;
    }

    @Override
    public int getId() {
        return idPrestamo;
    }
    public String getFecha(){
        return fechaPrestamo.toString();
    }
    public Libro getObjLibro(){
        return Entidades.libro(idLibro);
    }

    public Usuario getObjUsuario(){
        return  Entidades.usuario(idUsuario);
    }
}
