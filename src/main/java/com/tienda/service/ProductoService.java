package com.tienda.service;

// Una clase service es una clase que va estar en todo momento atenta a llamados o consultas

import com.tienda.domain.Producto;
import com.tienda.repository.ProductoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductoService {
    
    @Autowired // Para acceder automaticamente sin necesidad de hacer un new
    private ProductoRepository productoRepository;
    
    
    // Método que hace consulta a la base de datos y recupera los registros, se le llama transacción
    // findAll() = SELECT * FROM [Tabla];
    
    @Transactional(readOnly=true) // readOnly=true para solo consultas
    public List<Producto> getProductos(boolean activos){
        var lista = productoRepository.findAll();
        
        if(activos){
            // Si solo quiere activos
            lista.removeIf(e -> !e.isActivo());
        }
        return lista;
    }
    
    // Obtener solo un objeto categoría de la BD en la tabla productos
    @Transactional(readOnly=true)
    public Producto getProducto(Producto producto){
        return productoRepository.findById(producto.getIdProducto()).orElse(null);
    }
    
    
    // Doble función, si viene el id nulo se crea un objeto categoría, si viene el id entonces solo actualiza
    
    // Cuando se apreta el botón agregar categoría viene sin id entonces el método se da cuenta y crea
    // Cuando se apreta el de actualizar viene el id entonces el método se da cuenta y solo actualiza
    
    
    @Transactional // sin readOnly=true porque si modifica la base de datos
    public void save(Producto producto){
        productoRepository.save(producto);
    }
    
    
    @Transactional // sin readOnly=true porque si modifica la base de datos
    public void delete(Producto producto){
        productoRepository.delete(producto);
    }
    
    
    @Transactional(readOnly=true)
    public List<Producto> consultaAmpliada(double precioInf, double precioSup){
        return productoRepository.findByPrecioBetweenOrderByPrecio(precioInf, precioSup);
    }
    
    @Transactional(readOnly=true)
    public List<Producto> consultaJPQL(double precioInf, double precioSup){
        return productoRepository.consultaJPQL(precioInf, precioSup);
    }
    
    @Transactional(readOnly=true)
    public List<Producto> consultaSQL(double precioInf, double precioSup){
        return productoRepository.consultaSQL(precioInf, precioSup);
    }
 
    
    // Método para consulta ampliada de práctica #4 que filtra por nombre de producto
    
    @Transactional(readOnly=true)
    public List<Producto> consultaAmpliadaNombre(String descripcion){
        return productoRepository.findByDescripcion(descripcion);
    }
    
}
