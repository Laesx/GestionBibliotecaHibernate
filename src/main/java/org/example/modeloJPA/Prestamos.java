package org.example.modeloJPA;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Prestamos {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idPrestamo", nullable = false)
    private int idPrestamo;
    //TODO Decidir si usar la id de libro o el objeto libro como tal y lo mismo con el usuario
    @Basic
    @Column(name = "idLibro", nullable = true, insertable = false, updatable = false)
    private Integer idLibro;
    @Basic
    @Column(name = "idUsuario", nullable = true, insertable = false, updatable = false)
    private Integer idUsuario;
    @Basic
    @Column(name = "fechaPrestamo", nullable = true)
    private Timestamp fechaPrestamo;
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

    public Timestamp getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(Timestamp fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Prestamos prestamos = (Prestamos) o;

        if (idPrestamo != prestamos.idPrestamo) return false;
        if (idLibro != null ? !idLibro.equals(prestamos.idLibro) : prestamos.idLibro != null) return false;
        if (idUsuario != null ? !idUsuario.equals(prestamos.idUsuario) : prestamos.idUsuario != null) return false;
        if (fechaPrestamo != null ? !fechaPrestamo.equals(prestamos.fechaPrestamo) : prestamos.fechaPrestamo != null)
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
}
