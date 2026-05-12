package epicentral;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/bodega")
public class BodegaController {

    @Autowired private ProductRepository productRepository;
    @Autowired private InventoryMovementRepository movementRepository;
    @Autowired private UserRepository userRepository;

    // INVENTARIO CON BUSCADOR
    @GetMapping("/inventario")
    public String panelInventario(Model model, @RequestParam(value = "keyword", required = false) String keyword) {
        List<Product> productos;
        if (keyword != null && !keyword.isEmpty()) {
            productos = productRepository.searchProducts(keyword);
            model.addAttribute("keyword", keyword);
        } else {
            productos = productRepository.findAllByOrderByCategoryAscNameAsc();
        }
        model.addAttribute("productos", productos);
        model.addAttribute("nuevoProducto", new Product());
        return "bodega-inventario";
    }

    // MOVIMIENTOS CON FILTROS
    @GetMapping("/movimientos")
    public String panelMovimientos(Model model,
                                   @RequestParam(required = false) String type,
                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {

        List<InventoryMovement> movimientos;

        if (start != null && end != null) {
            LocalDateTime startDateTime = start.atStartOfDay();
            LocalDateTime endDateTime = end.atTime(23, 59, 59);

            if (type != null && !type.isEmpty() && !type.equals("TODOS")) {
                movimientos = movementRepository.findByMovementTypeAndMovementDateBetween(type, startDateTime, endDateTime);
            } else {
                movimientos = movementRepository.findByMovementDateBetween(startDateTime, endDateTime);
            }
        } else {
            movimientos = movementRepository.findAllByOrderByMovementDateDesc();
        }

        model.addAttribute("movimientos", movimientos);
        model.addAttribute("productos", productRepository.findAll());
        return "bodega-movimientos";
    }

    // EXPORTAR A CSV
    @GetMapping("/movimientos/exportar")
    public void exportarCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; file=Kardex_Epicentral.csv");

        List<InventoryMovement> movimientos = movementRepository.findAllByOrderByMovementDateDesc();

        StringBuilder csvContent = new StringBuilder();
        csvContent.append("Fecha,Usuario,Producto,Tipo,Cantidad,Referencia\n");

        for (InventoryMovement m : movimientos) {
            csvContent.append(m.getMovementDate()).append(",")
                    .append(m.getCreatedBy().getFirstName()).append(",")
                    .append(m.getProduct().getName()).append(",")
                    .append(m.getMovementType()).append(",")
                    .append(m.getQuantity()).append(",")
                    .append(m.getReference().replace(",", " ")).append("\n");
        }

        response.getWriter().write(csvContent.toString());
    }

    // Rutas de guardado y eliminado permanecen igual...
}

    @PostMapping("/movimientos/registrar")
    public String registrarMovimiento(@RequestParam("productId") Long productId,
                                      @RequestParam("movementType") String movementType,
                                      @RequestParam("quantity") Double quantity,
                                      @RequestParam("reference") String reference,
                                      Principal principal) {

        Optional<Product> prodOpt = productRepository.findById(productId);
        if (prodOpt.isEmpty()) return "redirect:/bodega/movimientos?error";

        Product producto = prodOpt.get();

        // 1. Validar que no haya stock negativo en las SALIDAS
        if (movementType.equals("SALIDA") && producto.getCurrentStock() < quantity) {
            return "redirect:/bodega/movimientos?error_stock";
        }

        // 2. Crear el registro del Kardex
        InventoryMovement mov = new InventoryMovement();
        mov.setProduct(producto);
        mov.setMovementType(movementType);
        mov.setQuantity(quantity);
        mov.setReference(reference);
        mov.setMovementDate(LocalDateTime.now());

        // Registrar al usuario conectado
        User creador = userRepository.findByEmail(principal.getName()).get();
        mov.setCreatedBy(creador);

        movementRepository.save(mov);

        // 3. Actualizar el Stock Físico del producto
        if (movementType.equals("ENTRADA")) {
            producto.setCurrentStock(producto.getCurrentStock() + quantity);
        } else {
            producto.setCurrentStock(producto.getCurrentStock() - quantity);
        }
        productRepository.save(producto);

        return "redirect:/bodega/movimientos?exito";
    }
}