package epicentral;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/usuarios") // Todas las rutas empezarán con /usuarios
public class UsuarioController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    // 1. PANTALLA DE CREACIÓN
    @GetMapping("/crear")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("user", new User());
        return "usuarios-crear"; // Crearemos este HTML basado en tu registro
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute("user") User user) {
        userService.registrarUsuario(user);
        return "redirect:/usuarios/actualizar?exito";
    }

    // 2. PANTALLA DE ACTUALIZACIÓN (LISTADO)
    @GetMapping("/actualizar")
    public String listarUsuarios(Model model) {
        List<User> listaUsuarios = userRepository.findAll();
        model.addAttribute("usuarios", listaUsuarios);
        return "usuarios-lista"; // Tabla con todos los usuarios de SQLite
    }

    // 3. ACCESO A PANTALLAS (PERMISOS)
    @GetMapping("/permisos")
    public String gestionarPermisos() {
        return "usuarios-permisos"; // Placeholder para lógica de roles
    }

    // 4. REGISTRO DE ACCESO Y EVENTOS (LOGS)
    @GetMapping("/logs")
    public String verLogs() {
        return "usuarios-logs"; // Aquí irán los mantenimientos y accesos
    }
}