package epicentral;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.security.Principal;

@ControllerAdvice
public class GlobalControllerAdvice {

    // Este metodo se ejecuta automáticamente en TODAS las pantallas de tu ERP
    @ModelAttribute("nombreUsuarioActual")
    public String obtenerUsuarioConectado(Principal principal) {
        if (principal != null) {
            return principal.getName(); // Devuelve el correo de quien inició sesión
        }
        return "Master Control"; // Valor de respaldo por seguridad
    }
}
