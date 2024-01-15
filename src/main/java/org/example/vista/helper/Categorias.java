package org.example.vista.helper;

import org.example.modelo.Categoria;
import org.example.modelo.dao.CategoriaDAO;
import org.example.modelo.dao.CategoriaDAOImpl;
import org.example.presentador.PresentadorCategoria;
import org.example.vista.FichaCategoria;
import org.example.vista.ListaCategorias;

public class Categorias {
    public static ListaCategorias listaCategorias() throws Exception {
        CategoriaDAO categoriaDAO=new CategoriaDAOImpl();
        ListaCategorias listaCategorias=new ListaCategorias();
        PresentadorCategoria presentadorCategoria=new PresentadorCategoria(categoriaDAO,listaCategorias);
        listaCategorias.setPresentador(presentadorCategoria);
        listaCategorias.lanzar();
        return listaCategorias;
    }

    public static FichaCategoria fichaCategoria(Categoria categoria) throws Exception {
        CategoriaDAO categoriaDAO=new CategoriaDAOImpl();
        FichaCategoria fichaCategoria=new FichaCategoria(categoria);
        PresentadorCategoria presentadorCategoria=new PresentadorCategoria(categoriaDAO,fichaCategoria);
        fichaCategoria.setPresentador(presentadorCategoria);
        fichaCategoria.lanzar();
        return fichaCategoria;

    }
}
