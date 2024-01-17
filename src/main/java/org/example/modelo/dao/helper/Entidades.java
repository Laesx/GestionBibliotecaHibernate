package org.example.modelo.dao.helper;

import org.example.modeloJPA.Categoria;
import org.example.modeloJPA.Libro;
import org.example.modeloJPA.Prestamos;
import org.example.modeloJPA.Usuario;
import org.example.modelo.dao.CategoriaDAOImpl;
import org.example.modelo.dao.LibroDAOImpl;
import org.example.modelo.dao.PrestamoDAOImpl;
import org.example.modelo.dao.UsuarioDAOImpl;

import java.util.List;

public class Entidades {
    public static Categoria categoria(int id){
        try{
            return new CategoriaDAOImpl().categoria(id);
        }
        catch (Exception e) {
            return null;
        }
    }
    public static List<Categoria> leerAllCategorias(){
        try{
            return new CategoriaDAOImpl().leerAllCategorias();
        }
        catch (Exception e) {
            return null;
        }
    }

    public static Libro libro(int id){
        try{
            return new LibroDAOImpl().getLibro(id);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static List<Libro> leerLibrosOR(int id, String titulo, String autor, String editorial, int categoria) {
        try{
            return new LibroDAOImpl().leerLibrosOR(id,titulo,autor,editorial,categoria);
        }
        catch (Exception e) {
            return null;
        }
    }
    public static List<Libro> leerAllLibros(){
        try{
            return new LibroDAOImpl().leerAllLibros();
        }
        catch (Exception e) {
            return null;
        }
    }

    public static Usuario usuario(int id){
        try {
            return new UsuarioDAOImpl().getUsuario(id);
        } catch (Exception e)  {
            return null;
        }
    }
    public static List<Usuario> leerAllUsuarios() {
        try {
            return new UsuarioDAOImpl().leerAllUsuarios();
        } catch (Exception e)  {
            return null;
        }
    }
    public static List<Usuario> leerUsuariosOR(int id,String nombre,String apellidos){
        try {
            return new UsuarioDAOImpl().leerUsuariosOR(id,nombre,apellidos);
        } catch (Exception e)  {
            return null;
        }
    }

    public static Prestamos prestamo(int id){
        try {
            return new PrestamoDAOImpl().getPrestamo(id);
        } catch (Exception e)  {
            return null;
        }
    }
    public static List<Prestamos> leerAllPrestamos(){
        try {
            return new PrestamoDAOImpl().leerAllPrestamos();
        } catch (Exception e)  {
            return null;
        }
    }
}
