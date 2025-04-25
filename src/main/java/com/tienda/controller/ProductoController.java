package com.tienda.controller;

// Clases que tienen anotación controller van a estar guardadas en un indice para cuando llegue un url (petición de usuario) responda con algo

// import com.tienda.service.ProductoService;
import com.google.firebase.FirebaseApp;
import com.tienda.domain.Producto;
import com.tienda.service.CategoriaService;
import com.tienda.service.ProductoService;
import com.tienda.service.FirebaseStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/producto")
public class ProductoController {

    @Autowired
    private ProductoService productoService; // Recupero el servicio
    @Autowired
    private CategoriaService categoriaService; // Recupero el servicio
    
    @GetMapping("/listado")
    public String listado(Model model){
        var productos=productoService.getProductos(false); // Recuperando el array list
        model.addAttribute("productos",productos); // inyectando el array list para que se vea en el html
        var categorias = categoriaService.getCategorias(true);
        model.addAttribute("categorias",categorias);
        return"/producto/listado";
    }
    
    @GetMapping("/eliminar/{idProducto}")  // Cuando se pasa un atributo en la url se crea el objeto categoría y le setea el idProducto solo
    public String eliminar(Producto producto){
        productoService.delete(producto);
        return"redirect:/producto/listado";  // Con esto se le dice a la página que se ejecute el url, en este caso el método y funciona como darle refrescar
    }                                         // Con esto se mostrará la lista de categorías por reutilizar el método de listado
    
    
    @GetMapping("/modificar/{idProducto}") 
    public String modificar(Producto producto, Model model){
        producto = productoService.getProducto(producto);
        
        model.addAttribute("producto", producto);
        var categorias = categoriaService.getCategorias(true);
        model.addAttribute("categorias",categorias);
        return"/producto/modifica";
    }
    
    
    @Autowired
    private FirebaseStorageService firebaseStorageService;
    
    @PostMapping("/guardar") 
    public String guardar(Producto producto, 
           @RequestParam("imagenFile")MultipartFile imagenFile){
        
        if(!imagenFile.isEmpty()){
            productoService.save(producto);
            
            // Sube la imagen al storage y devuelve la ruta
            String ruta = firebaseStorageService.cargaImagen(imagenFile,"producto",producto.getIdProducto());
           
            producto.setRutaImagen(ruta);
        }
        productoService.save(producto);
        
        return"redirect:/producto/listado";
    }
}
