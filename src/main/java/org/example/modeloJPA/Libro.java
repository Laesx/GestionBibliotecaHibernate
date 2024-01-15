package org.example.modeloJPA;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Libro {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "nombre", nullable = true, length = -1)
    private String nombre;
    @Basic
    @Column(name = "autor", nullable = true, length = -1)
    private String autor;
    @Basic
    @Column(name = "editorial", nullable = true, length = -1)
    private String editorial;
    @Basic
    @Column(name = "categoria", nullable = true)
    private Integer categoria;
    @OneToMany(mappedBy = "libro")
    private Collection<Prestamos> prestamos;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public Integer getCategoria() {
        return categoria;
    }

    public void setCategoria(Integer categoria) {
        this.categoria = categoria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Libro libro = (Libro) o;

        if (id != libro.id) return false;
        if (nombre != null ? !nombre.equals(libro.nombre) : libro.nombre != null) return false;
        if (autor != null ? !autor.equals(libro.autor) : libro.autor != null) return false;
        if (editorial != null ? !editorial.equals(libro.editorial) : libro.editorial != null) return false;
        if (categoria != null ? !categoria.equals(libro.categoria) : libro.categoria != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        result = 31 * result + (autor != null ? autor.hashCode() : 0);
        result = 31 * result + (editorial != null ? editorial.hashCode() : 0);
        result = 31 * result + (categoria != null ? categoria.hashCode() : 0);
        return result;
    }

    public Collection<Prestamos> getPrestamos() {
        return prestamos;
    }

    public void setPrestamos(Collection<Prestamos> prestamos) {
        this.prestamos = prestamos;
    }
}
