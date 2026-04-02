package epicentral;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    // 1. PANTALLA DE CREACIÓN
    @GetMapping("/crear")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("user", new User());
        return "usuarios-crear";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute("user") User user) {
        userService.registrarUsuario(user);
        return "redirect:/usuarios/actualizar?exito";
    }

    // 2. PANTALLA DE LISTADO Y ACTUALIZACIÓN
    @GetMapping("/actualizar")
    public String listarUsuarios(Model model) {
        List<User> listaUsuarios = userRepository.findAll();
        model.addAttribute("usuarios", listaUsuarios);
        return "usuarios-lista";
    }

    // 3. FUNCIONALIDAD DE EDICIÓN
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de usuario inválido:" + id));
        model.addAttribute("user", user);
        return "usuarios-editar";
    }

    @PostMapping("/actualizar")
    public String actualizarUsuario(@ModelAttribute("user") User user) {
        // Mantenemos la contraseña original si no se gestiona cambio de clave aquí
        User userOriginal = userRepository.findById(user.getId()).get();
        user.setPassword(userOriginal.getPassword());

        userRepository.save(user);
        return "redirect:/usuarios/actualizar?editado";
    }

    // 4. FUNCIONALIDAD DE ELIMINACIÓN
    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable("id") Long id) {
        User u = userRepository.findById(id).get();
        logRepository.save(new LogEntry("admin@epicentral.com", "ELIMINACION", "Se eliminó al usuario: " + u.getEmail()));
        userRepository.deleteById(id);
        return "redirect:/usuarios/actualizar?eliminado";
    }

    // 5. OTROS APARTADOS
    // En UsuarioController.java

    // 1. Mostrar pantalla de permisos para un usuario específico
    @GetMapping("/permisos/{id}")
    public String gestionarPermisos(@PathVariable("id") Long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido:" + id));

        // Lista de módulos disponibles en el ERP Epicentral
        List<String> todosLosModulos = List.of("DASHBOARD", "CAMARAS", "MANTENIMIENTO", "INVENTARIO", "USUARIOS");

        model.addAttribute("user", user);
        model.addAttribute("todosLosModulos", todosLosModulos);
        return "usuarios-permisos";
    }

    // 2. Guardar los permisos seleccionados
    @PostMapping("/permisos/guardar")
    public String guardarPermisos(@RequestParam("userId") Long userId,
                                  @RequestParam(value = "modulos", required = false) List<String> modulos) {
        User user = userRepository.findById(userId).get();

        // Si no se selecciona nada, enviamos lista vacía para evitar errores
        user.setFunctionalities(modulos != null ? modulos : new java.util.ArrayList<>());

        userRepository.save(user);
        return "redirect:/usuarios/actualizar?permisos_actualizados";
    }

    @Autowired
    private LogRepository logRepository;

    @GetMapping("/logs")
    public String verLogs(Model model) {
        // Obtenemos todos los eventos, del más reciente al más antiguo
        model.addAttribute("logs", logRepository.findAll());
        return "usuarios-logs";
    }

    // Ejemplo de cómo registrar una eliminación (haz algo similar en guardar y actualizar)

}