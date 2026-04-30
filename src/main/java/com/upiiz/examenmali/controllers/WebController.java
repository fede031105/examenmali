package com.upiiz.examenmali.controllers;

import com.upiiz.examenmali.entities.*;
import com.upiiz.examenmali.repositories.*;
import com.upiiz.examenmali.services.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class WebController {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private ProductoRepository productoRepository;
    @Autowired private ServicioReparacionRepository servicioReparacionRepository;
    @Autowired private FacturaRepository facturaRepository;
    @Autowired private EmailService emailService;

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    // ==========================================
    // AUTENTICACIÓN
    // ==========================================
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUsuario(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
        Optional<Usuario> user = usuarioRepository.findByEmail(email);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            session.setAttribute("nombreUsuario", user.get().getNombre());
            return "redirect:/dashboard";
        }
        model.addAttribute("error", "Credenciales incorrectas");
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String registrarUsuario(@RequestParam String nombre, @RequestParam String email, @RequestParam String password, Model model) {
        if (usuarioRepository.findByEmail(email).isPresent()) {
            model.addAttribute("error", "El correo ya está registrado.");
            return "register";
        }
        Usuario user = new Usuario();
        user.setNombre(nombre);
        user.setEmail(email);
        user.setPassword(password);
        usuarioRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // ==========================================
    // RECUPERACIÓN CON MANEJO DE ERRORES
    // ==========================================
    @GetMapping("/recuperar")
    public String mostrarRecuperar() {
        return "recuperar";
    }

    @PostMapping("/recuperar")
    public String procesarRecuperar(@RequestParam String email, Model model) {
        Optional<Usuario> user = usuarioRepository.findByEmail(email);
        if (user.isPresent()) {
            try {
                emailService.enviarRecuperacion(user.get().getEmail(), user.get().getPassword(), user.get().getNombre());
                model.addAttribute("exito", "Contraseña enviada a tu correo.");
            } catch (Exception e) {
                // Si el puerto 465 o 587 falla, el usuario verá este mensaje en lugar de una página cargando
                model.addAttribute("error", "Error al conectar con Gmail. Intenta de nuevo en unos minutos.");
                System.out.println("ERROR MAIL: " + e.getMessage());
            }
        } else {
            model.addAttribute("error", "El correo ingresado no pertenece a ningún usuario.");
        }
        return "recuperar";
    }

    // ==========================================
    // DASHBOARD TECHREPAIR
    // ==========================================
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (session.getAttribute("nombreUsuario") == null) {
            return "redirect:/login";
        }

        model.addAttribute("numClientes", clienteRepository.count());
        model.addAttribute("numProductos", productoRepository.count());
        model.addAttribute("numServicios", servicioReparacionRepository.count());

        double ingresos = 0.0;
        for (Factura f : facturaRepository.findAll()) {
            // isPagada() es boolean primitivo (no nulo)
            if (f.isPagada()) {
                if (f.getTotal() != null) {
                    ingresos += f.getTotal();
                }
            }
        }
        model.addAttribute("ingresos", ingresos);

        return "dashboard";
    }
}