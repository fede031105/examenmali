package com.upiiz.examenmali.controllers;

import com.upiiz.examenmali.entities.Factura;
import com.upiiz.examenmali.services.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/facturas")
public class FacturaController {

    @Autowired private FacturaService facturaService;
    @Autowired private ClienteService clienteService;
    @Autowired private ProductoService productoService;
    @Autowired private ServicioReparacionService servicioService;
    @Autowired private PdfService pdfService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("facturas", facturaService.obtenerTodas());
        return "facturas/listado";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("factura", new Factura());
        model.addAttribute("clientes", clienteService.obtenerTodos());
        model.addAttribute("productos", productoService.obtenerTodos());
        model.addAttribute("servicios", servicioService.obtenerTodos());
        return "facturas/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Factura factura) {
        factura.setPagada(false);
        facturaService.guardarFactura(factura);
        return "redirect:/facturas";
    }

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model model) {
        Factura factura = facturaService.obtenerPorId(id).orElseThrow();
        model.addAttribute("factura", factura);
        return "invoice";
    }

    @GetMapping("/pagar/{id}")
    public String pagar(@PathVariable Long id) {
        Factura factura = facturaService.obtenerPorId(id).orElseThrow();
        factura.setPagada(true);
        facturaService.guardarFactura(factura);
        return "redirect:/facturas/ver/" + id;
    }

    @GetMapping("/pdf/{id}")
    public ResponseEntity<byte[]> descargarPdf(@PathVariable Long id, HttpSession session) {
        Factura factura = facturaService.obtenerPorId(id).orElseThrow();
        String tecnico = (String) session.getAttribute("nombreUsuario");

        byte[] pdf = pdfService.generarFacturaPdf(factura, tecnico);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "Factura_" + id + ".pdf");

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}