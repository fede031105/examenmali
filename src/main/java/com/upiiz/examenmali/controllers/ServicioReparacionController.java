package com.upiiz.examenmali.controllers;

import com.upiiz.examenmali.entities.ServicioReparacion;
import com.upiiz.examenmali.services.ServicioReparacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/servicios")
public class ServicioReparacionController {
    @Autowired private ServicioReparacionService servicioService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("servicios", servicioService.obtenerTodos());
        return "servicios/listado";
    }

    @GetMapping("/nuevo")
    public String formulario(Model model) {
        model.addAttribute("servicio", new ServicioReparacion());
        return "servicios/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute ServicioReparacion servicio) {
        servicioService.guardarServicio(servicio);
        return "redirect:/servicios";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        ServicioReparacion servicio = servicioService.obtenerPorId(id).orElseThrow();
        model.addAttribute("servicio", servicio);
        return "servicios/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        servicioService.eliminarServicio(id);
        return "redirect:/servicios";
    }
}