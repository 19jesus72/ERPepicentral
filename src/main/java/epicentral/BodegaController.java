package epicentral;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/bodega")
public class BodegaController {

    @Autowired private ProductRepository productRepository;
    @Autowired private InventoryMovementRepository movementRepository;
    @Autowired private UserRepository userRepository; // Para la auditoría

    // ---------------------------------------------------------
    // FASE 1: CATÁLOGO Y STOCK
    // ---------------------------------------------------------

    @GetMapping("/inventario")
    public String panelInventario(Model model) {
        if (productRepository.count() == 0) {
            productRepository.save(new Product("TIC-001", "Cable UTP Cat 6 Exterior", "TICs", "Rollo (305m)", 5.0, 2.0));
            productRepository.save(new Product("TIC-002", "Cámara IP VIGI 4MP Tipo Bala", "TICs", "Unidad", 12.0, 4.0));
            productRepository.save(new Product("TIC-003", "Switch TP-Link Omada 8 Puertos", "TICs", "Unidad", 3.0, 2.0));
            productRepository.save(new Product("GLP-001", "Válvula Reguladora de Alta Presión", "GLP", "Unidad", 8.0, 3.0));
            productRepository.save(new Product("GLP-002", "Tubería de Cobre Tipo L 1/2", "GLP", "Metro", 120.0, 30.0));
            productRepository.save(new Product("EPP-001", "Casco de Seguridad Dieléctrico", "Seguridad Industrial", "Unidad", 15.0, 5.0));
        }

        model.addAttribute("productos", productRepository.findAllByOrderByCategoryAscNameAsc());
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

    // ---------------------------------------------------------
    // FASE 2: MOVIMIENTOS (ENTRADAS Y SALIDAS)
    // ---------------------------------------------------------

    @GetMapping("/movimientos")
    public String panelMovimientos(Model model) {
        model.addAttribute("productos", productRepository.findAllByOrderByCategoryAscNameAsc());
        model.addAttribute("movimientos", movementRepository.findAllByOrderByMovementDateDesc());
        return "bodega-movimientos";
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