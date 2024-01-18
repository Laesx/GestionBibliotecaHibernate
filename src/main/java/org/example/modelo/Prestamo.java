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
    @Column(name = "idLibro", nullable = true, insertable = false, updatable = false)
    private Integer idLibro;
    @Basic
    @Column(name = "idUsuario", nullable = true, insertable = false, updatable = false)
    private Integer idUsuario;
    @Basic
    @Column(name = "fechaPrestamo", nullable = true)
    private LocalDateTime fechaPrestamo = LocalDateTime.now();
    @ManyToOne
    @JoinColumn(name = "idLibro", referencedColumnName = "id")
    private Libro libro;
    @ManyToOne
    @JoinColumn(name = "idUsuario", referencedColumnName = "id")
    private Usuario usuario;

    public int getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(int idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public Integer getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(Integer idLibro) {
        this.idLibro = idLibro;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
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
        if (idLibro != null ? !idLibro.equals(prestamo.idLibro) : prestamo.idLibro != null) return false;
        if (idUsuario != null ? !idUsuario.equals(prestamo.idUsuario) : prestamo.idUsuario != null) return false;
        if (fechaPrestamo != null ? !fechaPrestamo.equals(prestamo.fechaPrestamo) : prestamo.fechaPrestamo != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idPrestamo;
        result = 31 * result + (idLibro != null ? idLibro.hashCode() : 0);
        result = 31 * result + (idUsuario != null ? idUsuario.hashCode() : 0);
        result = 31 * result + (fechaPrestamo != null ? fechaPrestamo.hashCode() : 0);
        return result;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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
