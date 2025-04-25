package com.tienda.controller;

// Clases que tienen anotación controller van a estar guardadas en un indice para cuando llegue un url (petición de usuario) responda con algo

// import com.tienda.service.CategoriaService;

import com.tienda.domain.Categoria;
import com.tienda.service.CategoriaService;
import com.tienda.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/pruebas")
public class PruebasController {

    @Autowired
    private CategoriaService categoriaService; // Recupero el servicio
    
    @Autowired
    private ProductoService productoService;
    
    @GetMapping("/listado")
    public String listado(Model model){
        var categorias=categoriaService.getCategorias(false); // Recuperando el array list
        model.addAttribute("categorias",categorias); // inyectando el array list para que se vea en el html
        
        var productos=productoService.getProductos(false);
        model.addAttribute("productos",productos);
                    
        return"/pruebas/listado";
    }
    
    @GetMapping("/listado/{idCategoria}")
    public String listado(Model model, Categoria categoria){
        var categorias=categoriaService.getCategorias(false); // Recuperando el array list
        model.addAttribute("categorias",categorias); // inyectando el array list para que se vea en el html
        
        categoria = categoriaService.getCategoria(categoria);
        
        var productos=categoria.getProductos();
        model.addAttribute("productos",productos);
                    
        return"/pruebas/listado";
    }
    
    
    @GetMapping("/listado2")
    public String listado2(Model model){
        var productos=productoService.getProductos(false);
        var Allproductos=productoService.getProductos(false);
        
        model.addAttribute("Allproductos",Allproductos);
        model.addAttribute("productos",productos);
                    
        return"/pruebas/listado2";
    }
    
    
    @PostMapping("/query1")
    public String consulta1(@RequestParam("precioInf") double precioInf,
                            @RequestParam("precioSub") double precioSub,
                            Model model){
        
        var productos=productoService.consultaAmpliada(precioInf, precioSub);
        
        model.addAttribute("productos",productos);
        model.addAttribute("precioInf",precioInf);
        model.addAttribute("precioSub",precioSub);
        
        return"/pruebas/listado2";
    }
 
    
    
    @PostMapping("/query2")
    public String consulta2(@RequestParam("precioInf") double precioInf,
                            @RequestParam("precioSub") double precioSub,
                            Model model){
        
        var productos=productoService.consultaJPQL(precioInf, precioSub);
        
        model.addAttribute("productos",productos);
        model.addAttribute("precioInf",precioInf);
        model.addAttribute("precioSub",precioSub);
        
        return"/pruebas/listado2";
    }

    
    @PostMapping("/query3")
    public String consulta3(@RequestParam("precioInf") double precioInf,
                            @RequestParam("precioSub") double precioSub,
                            Model model){
        
        var productos=productoService.consultaSQL(precioInf, precioSub);
        
        model.addAttribute("productos",productos);
        model.addAttribute("precioInf",precioInf);
        model.addAttribute("precioSub",precioSub);
        
        return"/pruebas/listado2";
    }
}
