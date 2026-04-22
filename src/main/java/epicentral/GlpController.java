package epicentral;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/operaciones/glp")
public class GlpController {

    @Autowired private GlpInstallationRepository glpRepository;
    @Autowired private ClientRepository clientRepository;

    @GetMapping("/instalaciones")
    public String panelInstalaciones(Model model) {

        // Auto-semillado de instalaciones industriales
        if (glpRepository.count() == 0) {
            // Aseguramos que existan clientes base
            Client granja = clientRepository.findByBusinessName("Granja Palestina S.A.").stream().findFirst().orElse(null);
            if (granja == null) {
                granja = new Client("1790000000001", "Granja Palestina S.A.", "mantenimiento@palestina.com", "0999999999", "Valle de los Chillos");
                clientRepository.save(granja);
            }

            Client panaderia = clientRepository.findByBusinessName("Panadería La Unión").stream().findFirst().orElse(null);
            if(panaderia == null) {
                panaderia = new Client("1790000000005", "Panadería La Unión", "info@launion.com", "0977777777", "Quito");
                clientRepository.save(panaderia);
            }

            // Creamos una instalación operativa
            glpRepository.save(new GlpInstallation(granja, "Matriz de Secado GLP", "Galpón 3", 2000.0, "Regulación 2 Etapas", true,
                    LocalDate.of(2024, 1, 15), LocalDate.of(2025, 12, 10), LocalDate.now().plusMonths(6)));

            // Creamos una instalación que requiere mantenimiento urgente (Fecha vencida)
            glpRepository.save(new GlpInstallation(panaderia, "Tren de Regulación Hornos", "Planta Baja", 500.0, "Alta Presión", false,
                    LocalDate.of(2025, 5, 20), LocalDate.of(2025, 11, 10), LocalDate.now().minusDays(5)));
        }

        // Motor de evaluación de estados en tiempo real
        List<GlpInstallation> instalaciones = glpRepository.findAllByOrderByNextMaintenanceDateAsc();
        for (GlpInstallation inst : instalaciones) {
            if (inst.getNextMaintenanceDate() != null && inst.getNextMaintenanceDate().isBefore(LocalDate.now())) {
                inst.setStatus("REQUIERE_INSPECCION");
                glpRepository.save(inst); // Actualizamos estado automáticamente
            } else if (inst.getNextMaintenanceDate() != null && inst.getNextMaintenanceDate().isAfter(LocalDate.now())) {
                inst.setStatus("OPERATIVO");
                glpRepository.save(inst);
            }
        }

        model.addAttribute("instalaciones", instalaciones);
        model.addAttribute("clientes", clientRepository.findAll());
        model.addAttribute("nuevaInstalacion", new GlpInstallation());

        return "glp-instalaciones";
    }

    @PostMapping("/instalaciones/guardar")
    public String guardarInstalacion(@ModelAttribute GlpInstallation instalacion, @RequestParam("clientId") Long clientId) {
        instalacion.setClient(clientRepository.findById(clientId).get());
        glpRepository.save(instalacion);
        return "redirect:/operaciones/glp/instalaciones?exito";
    }
}