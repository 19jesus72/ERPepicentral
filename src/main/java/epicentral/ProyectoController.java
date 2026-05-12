package epicentral;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/proyectos")
public class ProyectoController {

    @Autowired private ProjectRepository projectRepository;
    @Autowired private ProjectTaskRepository taskRepository;
    @Autowired private ProjectDocumentRepository documentRepository;
    @Autowired private ClientRepository clientRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CostCenterRepository costCenterRepository;
    @Autowired private InventoryMovementRepository movementRepository;

    // 1. PORTAFOLIO (Igual que antes)
    @GetMapping("/portafolio")
    public String verPortafolio(Model model) {
        model.addAttribute("proyectos", projectRepository.findAll());
        model.addAttribute("clientes", clientRepository.findAll());
        model.addAttribute("usuarios", userRepository.findAll());
        model.addAttribute("centros", costCenterRepository.findAll());
        model.addAttribute("nuevoProyecto", new Project());
        return "proyectos-portafolio";
    }

    @PostMapping("/guardar")
    public String guardarProyecto(@ModelAttribute Project proyecto,
                                  @RequestParam("clientId") Long clientId,
                                  @RequestParam("managerId") Long managerId,
                                  @RequestParam(value="costCenterId", required=false) Long costCenterId) {
        proyecto.setClient(clientRepository.findById(clientId).get());
        proyecto.setManager(userRepository.findById(managerId).get());
        if(costCenterId != null) {
            proyecto.setCostCenter(costCenterRepository.findById(costCenterId).get());
        }
        projectRepository.save(proyecto);
        return "redirect:/proyectos/portafolio?exito";
    }

    // 2. GESTIÓN DE CENTROS DE COSTO
    @GetMapping("/centros-costo")
    public String verCentrosCosto(Model model) {
        model.addAttribute("centros", costCenterRepository.findAll());
        model.addAttribute("nuevoCentro", new CostCenter());
        return "proyectos-centros-costo";
    }

    @PostMapping("/centros-costo/guardar")
    public String guardarCentroCosto(@ModelAttribute CostCenter centro) {
        costCenterRepository.save(centro);
        return "redirect:/proyectos/centros-costo?exito";
    }

    // 3. VISTA MAESTRA (SEGUIMIENTO 360°)
    @GetMapping("/seguimiento/{id}")
    public String verSeguimientoProyecto(@PathVariable("id") Long id, Model model) {
        Project proyecto = projectRepository.findById(id).orElseThrow();

        // 1. Actividades (Tareas)
        model.addAttribute("proyecto", proyecto);

        // 2. Materiales (Buscamos en el Kardex por el Centro de Costo del proyecto)
        if(proyecto.getCostCenter() != null) {
            List<InventoryMovement> materiales = movementRepository.findByCostCenterIdOrderByMovementDateDesc(proyecto.getCostCenter().getId());
            model.addAttribute("materiales", materiales);
        }

        // 3. Papeleo (Documentos)
        model.addAttribute("nuevoDoc", new ProjectDocument());

        return "proyectos-seguimiento";
    }

    @PostMapping("/documentos/guardar")
    public String guardarDocumento(@ModelAttribute ProjectDocument doc, @RequestParam("projectId") Long projectId) {
        doc.setProject(projectRepository.findById(projectId).get());
        documentRepository.save(doc);
        return "redirect:/proyectos/seguimiento/" + projectId;
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