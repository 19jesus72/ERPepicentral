package epicentral;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // Cambiamos a @Controller para servir archivos HTML (Thymeleaf)
public class HomeController {

    @GetMapping("/login")
    public String login() {
        return "login"; // Busca el archivo login.html en src/main/resources/templates
    }

    @GetMapping("/")
    public String dashboard() {
        return "dashboard"; // Este será nuestro siguiente paso: la bienvenida
    }
}