package epicentral;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/proyectos")
public class ProyectoController {

    @Autowired private ProjectRepository projectRepository;
    @Autowired private ProjectTaskRepository taskRepository;
    @Autowired private ClientRepository clientRepository;
    @Autowired private UserRepository userRepository;

    // 1. PORTAFOLIO GLOBAL DE PROYECTOS
    @GetMapping("/portafolio")
    public String verPortafolio(Model model) {
        model.addAttribute("proyectos", projectRepository.findAll());
        model.addAttribute("clientes", clientRepository.findAll());
        model.addAttribute("usuarios", userRepository.findAll()); // Para asignar el Jefe de Obra
        model.addAttribute("nuevoProyecto", new Project());
        return "proyectos-portafolio";
    }

    @PostMapping("/guardar")
    public String guardarProyecto(@ModelAttribute Project proyecto,
                                  @RequestParam("clientId") Long clientId,
                                  @RequestParam("managerId") Long managerId) {
        proyecto.setClient(clientRepository.findById(clientId).get());
        proyecto.setManager(userRepository.findById(managerId).get());
        projectRepository.save(proyecto);
        return "redirect:/proyectos/portafolio?exito";
    }

    // 2. TABLERO KANBAN (Detalle de un proyecto específico)
    @GetMapping("/tablero/{id}")
    public String verTableroKanban(@PathVariable("id") Long id, Model model) {
        Project proyecto = projectRepository.findById(id).orElseThrow();
        model.addAttribute("proyecto", proyecto);
        model.addAttribute("usuarios", userRepository.findAll()); // Para asignar tareas
        model.addAttribute("nuevaTarea", new ProjectTask());
        return "proyectos-tablero";
    }

    // 3. GESTIÓN DE TAREAS
    @PostMapping("/tareas/guardar")
    public String crearTarea(@ModelAttribute ProjectTask tarea,
                             @RequestParam("projectId") Long projectId,
                             @RequestParam("assignedToId") Long assignedToId) {

        tarea.setProject(projectRepository.findById(projectId).get());
        tarea.setAssignedTo(userRepository.findById(assignedToId).get());
        taskRepository.save(tarea);
        return "redirect:/proyectos/tablero/" + projectId;
    }

    @PostMapping("/tareas/estado")
    public String actualizarEstadoTarea(@RequestParam("taskId") Long taskId,
                                        @RequestParam("nuevoEstado") String nuevoEstado,
                                        @RequestParam("projectId") Long projectId) {
        ProjectTask tarea = taskRepository.findById(taskId).get();
        tarea.setStatus(nuevoEstado);
        taskRepository.save(tarea);
        return "redirect:/proyectos/tablero/" + projectId;
    }
}