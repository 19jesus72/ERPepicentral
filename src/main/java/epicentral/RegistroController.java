package epicentral;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistroController {

    @Autowired
    private UserService userService;

    @GetMapping("/registro")
    public String mostrarFormulario(User user) {
        return "registro"; // Buscaremos registro.html
    }

    @PostMapping("/registro")
    public String guardarUsuario(User user) {
        userService.registrarUsuario(user);
        return "redirect:/login?exito"; // Al terminar, nos manda al login
    }
}
