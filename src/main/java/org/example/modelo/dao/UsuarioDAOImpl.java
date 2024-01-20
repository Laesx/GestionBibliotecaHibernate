package org.example.modelo.dao;

import org.example.modelo.Usuario;
import org.example.modelo.dao.helper.LogFile;
import org.example.modelo.dao.helper.Sql;
import org.example.observer.Observer;
import org.example.observer.Subject;
import org.example.singleton.HibernateUtilJPA;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Aquí implementaremos las reglas definidas
 * en la interfaz para trabajar con usuario y
 * base de datos en MySQL
 */
public class UsuarioDAOImpl implements UsuarioDAO, Subject {
    private static final String sqlINSERT="INSERT INTO usuario (nombre,apellidos) VALUES (?,?)";
    private static final String sqlUPDATE="UPDATE usuario SET nombre = ?, apellidos = ? WHERE id = ?";
    private static final String sqlDELETE="DELETE usuario WHERE id = ?";

    public UsuarioDAOImpl() {
    }
    @Override
    public boolean insertar(Usuario usuario) throws Exception {
        boolean insertado = false;
        EntityTransaction transaction = null;
        EntityManager em = HibernateUtilJPA.getEntityManager();

        try{
            transaction = em.getTransaction();
            transaction.begin();
            em.persist(usuario);
            transaction.commit();
            insertado = true;
        }catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            throw e;
        } finally {
            em.close();
        }
        grabaEnLogIns(usuario,sqlINSERT);
        notifyObservers();
        return insertado;
    }

    private void grabaEnLogIns(Usuario usuario,String sql) throws Exception {
        sql = sql.replaceFirst("\\?",usuario.getNombre());
        sql = sql.replaceFirst("\\?",usuario.getApellidos());
        LogFile.saveLOG(sql);
    }
    @Override
    public boolean modificar(Usuario usuario) throws Exception {
        boolean modificado = false;
        EntityManager em = HibernateUtilJPA.getEntityManager();
        EntityTransaction transaction = em.getTransaction();


        try {
            transaction.begin();
            Usuario usuario1 = em.find(Usuario.class, usuario.getId());

            usuario1.setId(usuario.getId());
            usuario1.setNombre(usuario.getNombre());
            usuario1.setApellidos(usuario.getApellidos());
            em.merge(usuario1);
            transaction.commit();
            modificado = true;

        }catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            throw e;
        } finally {
            em.close();
        }
        grabaEnLogUpd(usuario,sqlUPDATE);
        notifyObservers();
        return modificado;
    }
    private void grabaEnLogUpd(Usuario usuario,String sql) throws Exception {
        sql = sql.replaceFirst("\\?",usuario.getNombre());
        sql = sql.replaceFirst("\\?",usuario.getApellidos());
        sql = sql.replaceFirst("\\?",String.valueOf(usuario.getId()));
        LogFile.saveLOG(sql);
    }


    @Override
    public boolean borrar(int id) throws Exception {
        boolean borrado = false;

        EntityManager em = HibernateUtilJPA.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try{
            transaction.begin();
            Usuario usuario = em.find(Usuario.class, id);

            if(usuario != null){
                em.remove(usuario);
                transaction.commit();
                borrado = true;
            }
        }catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            throw e;
        } finally {
            em.close();
        }
        grabaEnLogDel(id,sqlDELETE);
        notifyObservers();
        return borrado;
    }
    private void grabaEnLogDel(int id,String sql) throws Exception {
        sql = sql.replaceFirst("\\?",String.valueOf(id));
        LogFile.saveLOG(sql);
    }

    @Override
    public List<Usuario> leerAllUsuarios() throws Exception {
        return (List<Usuario>) MetodosGenerales.obtenerLista("FROM Usuario");
    }

    @Override
    public List<Usuario> leerUsuariosOR(int id, String nombre, String apellidos) throws Exception {

        String sql="SELECT u FROM Usuario u";
        String where="";
        List<Usuario> lista = null;
        EntityManager em = HibernateUtilJPA.getEntityManager();

        String wId="";
        if (id != 0) {
            wId = "u.id = :idUsuario";
            where = Sql.rellenaWhereOR(where, wId);
        }
        String wNombre="";
        if (!nombre.trim().isEmpty()) {
            wNombre = "u.nombre LIKE :nombreUsuario";
            where = Sql.rellenaWhereOR(where, wNombre);
        }
        String wApellidos="";
        if (!apellidos.trim().isEmpty()) {
            wApellidos = "u.apellidos LIKE :apellidoUsuario";
            where = Sql.rellenaWhereOR(where, wApellidos);
        }

        if (where.isEmpty())
            return leerAllUsuarios();
        else {
            sql = sql + " WHERE "+where;
            TypedQuery<Usuario> typedQuery = em.createQuery(sql, Usuario.class);

            if (!wId.isEmpty())
                typedQuery.setParameter("idUsuario", id);
            if (!wNombre.isEmpty())
                typedQuery.setParameter("nombreUsuario", nombre);
            if (!wApellidos.isEmpty())
                typedQuery.setParameter("apellidoUsuario", apellidos);

            lista = typedQuery.getResultList();
        }
        LogFile.saveLOG(sql);
        return lista;
    }



    @Override
    public Usuario getUsuario(int id) throws Exception {
        EntityManager em = HibernateUtilJPA.getEntityManager();
        return em.find(Usuario.class, id);
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
}
