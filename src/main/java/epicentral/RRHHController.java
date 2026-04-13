package epicentral;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import java.util.List;

@Controller
@RequestMapping("/rrhh")
public class RRHHController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TimeRecordRepository timeRecordRepository;

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

    @GetMapping("/reloj")
    public String mostrarReloj(Model model, Principal principal) {
        // Obtenemos al usuario que tiene la sesión activa
        User currentUser = userRepository.findByEmail(principal.getName()).get();

        // Buscamos si ya tiene un registro creado el día de hoy
        Optional<TimeRecord> recordHoy = timeRecordRepository.findByUserAndWorkDate(currentUser, LocalDate.now());

        if (recordHoy.isPresent()) {
            model.addAttribute("registro", recordHoy.get());
        } else {
            // Si no ha fichado, enviamos un registro vacío para la plantilla
            model.addAttribute("registro", new TimeRecord());
        }

        return "rrhh-reloj";
    }

    @PostMapping("/reloj/accion")
    public String procesarFichaje(@RequestParam("accion") String accion,
                                  @RequestParam(value = "locationMode", required = false) String locationMode,
                                  @RequestParam(value = "assignedProject", required = false) String assignedProject,
                                  @RequestParam(value = "notes", required = false) String notes,
                                  Principal principal) {

        User currentUser = userRepository.findByEmail(principal.getName()).get();
        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now();

        // Recuperamos el registro de hoy, o creamos uno nuevo si es la primera acción (Entrada)
        TimeRecord registro = timeRecordRepository.findByUserAndWorkDate(currentUser, hoy)
                .orElse(new TimeRecord());

        if (registro.getUser() == null) {
            registro.setUser(currentUser);
            registro.setWorkDate(hoy);
        }

        // Máquina de estados para el reloj
        switch (accion) {
            case "ENTRADA":
                registro.setClockInTime(ahora);
                registro.setLocationMode(locationMode);
                registro.setAssignedProject(assignedProject);
                registro.setStatus("TRABAJANDO");
                break;
            case "PAUSA_INICIO":
                registro.setPauseStartTime(ahora);
                registro.setStatus("EN PAUSA");
                break;
            case "PAUSA_FIN":
                registro.setPauseEndTime(ahora);
                registro.setStatus("TRABAJANDO");
                break;
            case "SALIDA":
                registro.setClockOutTime(ahora);
                registro.setNotes(notes);
                registro.setStatus("FINALIZADO");
                break;
        }

        timeRecordRepository.save(registro);
        return "redirect:/rrhh/reloj";
    }

    // ---------------------------------------------------------
    // HOJA DE TIEMPOS (TIMESHEET PARA ADMINISTRADORES)
    // ---------------------------------------------------------

    @GetMapping("/timesheet")
    public String verTimesheet(Model model, @RequestParam(value = "keyword", required = false) String keyword) {
        List<TimeRecord> registros;

        // Si el administrador busca un proyecto específico
        if (keyword != null && !keyword.trim().isEmpty()) {
            registros = timeRecordRepository.findByAssignedProjectContainingIgnoreCase(keyword);
            model.addAttribute("keyword", keyword);
        } else {
            // Mostrar todo el historial ordenado por fecha
            registros = timeRecordRepository.findAllByOrderByWorkDateDesc();
        }

        model.addAttribute("registros", registros);
        return "rrhh-timesheet";
    }


}
