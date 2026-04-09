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
    public String guardarUsuario(@ModelAttribute("user") User user, Model model) {
        // 1. Validación de Correo Duplicado en SQLite
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("errorCorreo", "Este correo ya pertenece a un usuario registrado.");
            return "usuarios-crear";
        }

        // 2. Validación de Seguridad: Mínimo 8 caracteres
        if (user.getPassword() == null || user.getPassword().length() < 8) {
            model.addAttribute("errorPassword", "La contraseña temporal debe tener al menos 8 caracteres.");
            return "usuarios-crear";
        }

        userService.registrarUsuario(user);
        return "redirect:/usuarios/actualizar?exito";
    }

    // 2. PANTALLA DE LISTADO Y ACTUALIZACIÓN Y BÚSQUEDA
    @GetMapping("/actualizar")
    public String listarUsuarios(Model model, @RequestParam(value = "keyword", required = false) String keyword) {
        List<User> listaUsuarios;

        // Si hay una palabra clave, usamos el buscador
        if (keyword != null && !keyword.trim().isEmpty()) {
            listaUsuarios = userRepository.searchUsers(keyword);
            model.addAttribute("keyword", keyword); // Retornamos la palabra para que se quede en la barra
        } else {
            // Si no hay búsqueda, traemos todos los registros
            listaUsuarios = userRepository.findAll();
        }

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

    // 1. Cargar la pantalla de permisos para un usuario específico
    @GetMapping("/permisos/{id}")
    public String gestionarPermisos(@PathVariable("id") Long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de usuario inválido:" + id));

        // Lista maestra de módulos existentes en tu ERP
        List<String> todosLosModulos = List.of("DASHBOARD", "CAMARAS", "MANTENIMIENTO", "INVENTARIO", "USUARIOS");

        model.addAttribute("user", user);
        model.addAttribute("todosLosModulos", todosLosModulos);
        return "usuarios-permisos";
    }

    // 2. Guardar la selección de módulos
    @PostMapping("/permisos/guardar")
    public String guardarPermisos(@RequestParam("userId") Long userId,
                                  @RequestParam(value = "modulos", required = false) List<String> modulos) {
        User user = userRepository.findById(userId).get();

        // Si no se selecciona ningún módulo, se inicializa una lista vacía
        user.setFunctionalities(modulos != null ? modulos : new java.util.ArrayList<>());

        userRepository.save(user);

        // Registrar la acción en los logs (opcional, si ya integraste LogRepository)
        // logRepository.save(new LogEntry("admin@epicentral.com", "PERMISOS", "Se actualizaron accesos para: " + user.getEmail()));

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
    // En UsuarioController.java

}