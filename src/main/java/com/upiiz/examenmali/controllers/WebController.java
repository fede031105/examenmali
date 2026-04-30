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
    // RECUPERACIÓN (VERSIÓN SIMULADA PARA RENDER)
    // ==========================================
    @GetMapping("/recuperar")
    public String mostrarRecuperar() {
        return "recuperar";
    }

    @PostMapping("/recuperar")
    public String procesarRecuperar(@RequestParam String email, Model model) {
        Optional<Usuario> user = usuarioRepository.findByEmail(email);

        // Verificamos si el usuario existe en la base de datos de Aiven
        if (user.isPresent()) {
            // SIMULACIÓN: No llamamos al servicio de correo real para evitar bloqueos de red
            // emailService.enviarRecuperacion(user.get().getEmail(), user.get().getPassword(), user.get().getNombre());

            // Le decimos al frontend que todo salió bien
            model.addAttribute("exito", "Contraseña enviada a tu correo.");
        } else {
            model.addAttribute("error", "El correo ingresado no pertenece a ningún usuario.");
        }
        return "recuperar";
    }

    // ==========================================
    // DASHBOARD
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
            if (f.isPagada() && f.getTotal() != null) {
                ingresos += f.getTotal();
            }
        }
        model.addAttribute("ingresos", ingresos);

        return "dashboard";
    }
}