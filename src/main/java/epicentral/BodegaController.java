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
import java.util.Optional;

@Controller
@RequestMapping("/bodega")
public class BodegaController {

    @Autowired private ProductRepository productRepository;
    @Autowired private InventoryMovementRepository movementRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CostCenterRepository costCenterRepository; // NUEVO

    // 1. INVENTARIO (Queda igual, con su buscador)
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

    @PostMapping("/inventario/guardar")
    public String guardarProducto(@ModelAttribute Product producto) {
        productRepository.save(producto);
        return "redirect:/bodega/inventario?exito";
    }

    @PostMapping("/inventario/eliminar")
    public String eliminarProducto(@RequestParam("productoId") Long productoId) {
        productRepository.deleteById(productoId);
        return "redirect:/bodega/inventario?eliminado";
    }

    // 2. KARDEX: DESPACHOS, DEVOLUCIONES Y COMPRAS
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
        model.addAttribute("productos", productRepository.findAllByOrderByCategoryAscNameAsc());
        model.addAttribute("centrosCosto", costCenterRepository.findAll()); // Enviamos los centros al formulario
        return "bodega-movimientos";
    }

    @PostMapping("/movimientos/registrar")
    public String registrarMovimiento(@RequestParam("productId") Long productId,
                                      @RequestParam("movementType") String movementType,
                                      @RequestParam("quantity") Double quantity,
                                      @RequestParam("reference") String reference,
                                      @RequestParam(value = "costCenterId", required = false) Long costCenterId,
                                      Principal principal) {

        Product producto = productRepository.findById(productId).orElseThrow();

        // Evitar sacar más de lo que hay en un DESPACHO
        if (movementType.equals("DESPACHO") && producto.getCurrentStock() < quantity) {
            return "redirect:/bodega/movimientos?error_stock";
        }

        InventoryMovement mov = new InventoryMovement();
        mov.setProduct(producto);
        mov.setMovementType(movementType); // COMPRA, DESPACHO, DEVOLUCION
        mov.setQuantity(quantity);
        mov.setReference(reference);
        mov.setMovementDate(LocalDateTime.now());

        // Vincular Centro de Costo si se seleccionó
        if (costCenterId != null) {
            mov.setCostCenter(costCenterRepository.findById(costCenterId).orElse(null));
        }

        mov.setCreatedBy(userRepository.findByEmail(principal.getName()).get());
        movementRepository.save(mov);

        // Lógica de Stock: Compras y Devoluciones suman, Despachos restan.
        if (movementType.equals("DESPACHO")) {
            producto.setCurrentStock(producto.getCurrentStock() - quantity);
        } else {
            producto.setCurrentStock(producto.getCurrentStock() + quantity);
        }
        productRepository.save(producto);

        return "redirect:/bodega/movimientos?exito";
    }

    // 3. EXPORTAR A CSV
    @GetMapping("/movimientos/exportar")
    public void exportarCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; file=Kardex_Epicentral.csv");
        response.getOutputStream().write(0xEF); response.getOutputStream().write(0xBB); response.getOutputStream().write(0xBF);

        List<InventoryMovement> movimientos = movementRepository.findAllByOrderByMovementDateDesc();
        StringBuilder csvContent = new StringBuilder();
        csvContent.append("Fecha,Usuario,Producto,Tipo,Cantidad,Centro de Costo,Referencia\n");

        for (InventoryMovement m : movimientos) {
            String cCosto = m.getCostCenter() != null ? m.getCostCenter().getCode() : "Bodega Central";
            csvContent.append(m.getMovementDate()).append(",")
                    .append(m.getCreatedBy().getFirstName()).append(",")
                    .append(m.getProduct().getName()).append(",")
                    .append(m.getMovementType()).append(",")
                    .append(m.getQuantity()).append(",")
                    .append(cCosto).append(",")
                    .append(m.getReference().replace(",", " ")).append("\n");
        }
        response.getWriter().write(csvContent.toString());
    }
}