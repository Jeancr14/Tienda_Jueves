package com.tienda.service;

// Una clase service es una clase que va estar en todo momento atenta a llamados o consultas

import com.tienda.domain.Categoria;
import com.tienda.repository.CategoriaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriaService {
    
    @Autowired // Para acceder automaticamente sin necesidad de hacer un new
    private CategoriaRepository categoriaRepository;
    
    
    // Método que hace consulta a la base de datos y recupera los registros, se le llama transacción
    // findAll() = SELECT * FROM [Tabla];
    
    @Transactional(readOnly=true) // readOnly=true para solo consultas
    public List<Categoria> getCategorias(boolean activos){
        var lista = categoriaRepository.findAll();
        
        if(activos){
            // Si solo quiere activos
            lista.removeIf(e -> !e.isActivo());
        }
        return lista;
    }
    
    // Obtener solo un objeto categoría de la BD en la tabla categorias
    @Transactional(readOnly=true)
    public Categoria getCategoria(Categoria categoria){
        return categoriaRepository.findById(categoria.getIdCategoria()).orElse(null);
    }
    
    
    // Doble función, si viene el id nulo se crea un objeto categoría, si viene el id entonces solo actualiza
    
    // Cuando se apreta el botón agregar categoría viene sin id entonces el método se da cuenta y crea
    // Cuando se apreta el de actualizar viene el id entonces el método se da cuenta y solo actualiza
    
    
    @Transactional // sin readOnly=true porque si modifica la base de datos
    public void save(Categoria categoria){
        categoriaRepository.save(categoria);
    }
    
    
    @Transactional // sin readOnly=true porque si modifica la base de datos
    public void delete(Categoria categoria){
        categoriaRepository.delete(categoria);
    }
}
