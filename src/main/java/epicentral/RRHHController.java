package epicentral;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
// 👇 ESTA LÍNEA CON EL ASTERISCO SOLUCIONA EL ERROR 👇
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/rrhh")
public class RRHHController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    // Panel General
    @GetMapping("/panel")
    public String mostrarPanelRRHH() {
        return "rrhh-panel";
    }

    // Listado de Personal (RRHH)
    @GetMapping("/personal")
    public String listarPersonal(Model model, @RequestParam(value = "keyword", required = false) String keyword) {
        List<User> personal;

        // Buscador inteligente
        if (keyword != null && !keyword.trim().isEmpty()) {
            personal = userRepository.searchUsers(keyword);
            model.addAttribute("keyword", keyword);
        } else {
            personal = userRepository.findAll();
        }

        model.addAttribute("personal", personal);
        return "rrhh-personal-lista";
    }

    // Mostrar formulario de creación (Mega-Ficha)
    @GetMapping("/personal/crear")
    public String mostrarFormularioPersonal(Model model) {
        model.addAttribute("user", new User());
        return "rrhh-personal-form";
    }

    // Guardar los datos del colaborador
    @PostMapping("/personal/guardar")
    public String guardarPersonal(@ModelAttribute("user") User user, Model model) {
        // Validación de correo único
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("errorCorreo", "Este correo ya está asignado a otro colaborador.");
            return "rrhh-personal-form";
        }

        // Lógica de Acceso al Sistema
        if (user.getHasSystemAccess() != null && !user.getHasSystemAccess()) {
            // Generamos una clave interna temporal para cumplir con SQLite
            user.setPassword(java.util.UUID.randomUUID().toString().substring(0, 16));
        } else if (user.getPassword() == null || user.getPassword().length() < 8) {
            // Validamos que la clave sea segura
            model.addAttribute("errorPassword", "La contraseña de acceso debe tener al menos 8 caracteres.");
            return "rrhh-personal-form";
        }

        userService.registrarUsuario(user);
        return "redirect:/rrhh/personal?exito";
    }
}
