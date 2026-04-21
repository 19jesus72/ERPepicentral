package epicentral;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/contabilidad")
public class ContabilidadController {

    @Autowired private AccountRepository accountRepository;
    @Autowired private JournalEntryRepository journalRepository;
    @Autowired private ClientRepository clientRepository;
    @Autowired private InvoiceRepository invoiceRepository;
    @Autowired private SupplierRepository supplierRepository;
    @Autowired private ExpenseRecordRepository expenseRepository;
    @Autowired private PayrollRepository payrollRepository;
    @Autowired private UserRepository userRepository;
    // 1. PANTALLA PRINCIPAL DEL LIBRO DIARIO
    @GetMapping("/diario")
    public String libroDiario(Model model) {

        // AUTO-SEMILLADO DE DATOS: Si no hay cuentas, creamos el catálogo base de Master Control
        if (accountRepository.count() == 0) {
            accountRepository.save(new Account("1.1.1.01", "Caja General", "ACTIVO", "Dinero en efectivo"));
            accountRepository.save(new Account("1.1.2.01", "Banco Pichincha Cuenta Corriente", "ACTIVO", "Cuenta principal"));
            accountRepository.save(new Account("1.1.3.01", "Cuentas por Cobrar Clientes", "ACTIVO", "Facturas pendientes de cobro"));
            accountRepository.save(new Account("2.1.1.01", "Cuentas por Pagar Proveedores", "PASIVO", "Deudas a proveedores"));
            accountRepository.save(new Account("2.1.2.01", "IESS por Pagar", "PASIVO", "Retenciones laborales"));
            accountRepository.save(new Account("3.1.1.01", "Capital Social", "PATRIMONIO", "Aporte de accionistas"));
            accountRepository.save(new Account("4.1.1.01", "Ingresos por Servicios TICs", "INGRESO", "Ventas de redes y cámaras"));
            accountRepository.save(new Account("4.1.1.02", "Ingresos por Proyectos GLP", "INGRESO", "Instalaciones industriales"));
            accountRepository.save(new Account("5.1.1.01", "Gastos de Nómina", "GASTO", "Sueldos y salarios"));
            accountRepository.save(new Account("5.1.2.01", "Gastos de Materiales e Insumos", "GASTO", "Cables, tuberías, etc."));
        }

        model.addAttribute("cuentas", accountRepository.findAll());
        model.addAttribute("asientos", journalRepository.findAllByOrderByEntryDateDesc());

        return "contabilidad-diario";
    }

    // 2. GUARDAR UN NUEVO ASIENTO CONTABLE
    @PostMapping("/diario/guardar")
    public String guardarAsiento(@RequestParam("entryDate") LocalDate entryDate,
                                 @RequestParam("description") String description,
                                 @RequestParam("referenceDocument") String referenceDocument,
                                 @RequestParam("accountIds") List<Long> accountIds,
                                 @RequestParam("debits") List<Double> debits,
                                 @RequestParam("credits") List<Double> credits) {

        JournalEntry asiento = new JournalEntry();
        asiento.setEntryDate(entryDate);
        asiento.setDescription(description);
        asiento.setReferenceDocument(referenceDocument);
        asiento.setStatus("REGISTRADO");

        // Recorremos las listas de líneas que envía el formulario HTML
        for (int i = 0; i < accountIds.size(); i++) {
            Double deb = debits.get(i);
            Double cred = credits.get(i);

            // Solo guardamos la línea si tiene algún valor en Debe o Haber
            if (deb > 0 || cred > 0) {
                JournalLine linea = new JournalLine();
                linea.setAccount(accountRepository.findById(accountIds.get(i)).get());
                linea.setDebit(deb != null ? deb : 0.0);
                linea.setCredit(cred != null ? cred : 0.0);

                asiento.addLine(linea); // Este método conecta la línea con el asiento maestro
            }
        }

        journalRepository.save(asiento);
        return "redirect:/contabilidad/diario?exito";
    }
    // ---------------------------------------------------------
    // MÓDULO DE FACTURACIÓN Y CUENTAS POR COBRAR
    // ---------------------------------------------------------

    @GetMapping("/facturas")
    public String panelFacturas(Model model) {
        // Auto-semillado de un cliente de prueba si no existen
        if (clientRepository.count() == 0) {
            clientRepository.save(new Client("1790000000001", "Granja Palestina S.A.", "pagos@palestina.com", "0999999999", "Valle de los Chillos"));
            clientRepository.save(new Client("1790000000002", "Grupo Oro", "admin@gruporo.com", "0988888888", "Quito Sur"));
        }

        model.addAttribute("clientes", clientRepository.findAll());
        model.addAttribute("facturas", invoiceRepository.findAllByOrderByIssueDateDesc());
        return "contabilidad-facturas";
    }

    @PostMapping("/facturas/generar")
    public String generarFactura(@RequestParam("clientId") Long clientId,
                                 @RequestParam("invoiceNumber") String invoiceNumber,
                                 @RequestParam("projectDescription") String projectDescription,
                                 @RequestParam("issueDate") LocalDate issueDate,
                                 @RequestParam("dueDate") LocalDate dueDate,
                                 @RequestParam("subtotal") Double subtotal,
                                 @RequestParam("ivaPercentage") Double ivaPercentage) {

        Client cliente = clientRepository.findById(clientId).orElseThrow();

        Invoice factura = new Invoice();
        factura.setClient(cliente);
        factura.setInvoiceNumber(invoiceNumber);
        factura.setProjectDescription(projectDescription);
        factura.setIssueDate(issueDate);
        factura.setDueDate(dueDate);
        factura.setSubtotal(subtotal);

        // Calculamos IVA y Total
        factura.calculateTotal(ivaPercentage);

        invoiceRepository.save(factura);
        return "redirect:/contabilidad/facturas?generada";
    }

    @PostMapping("/facturas/cobrar")
    public String marcarCobrada(@RequestParam("invoiceId") Long invoiceId) {
        Invoice factura = invoiceRepository.findById(invoiceId).orElseThrow();
        factura.setStatus("PAGADA");
        invoiceRepository.save(factura);
        return "redirect:/contabilidad/facturas?cobrada";
    }
    // ---------------------------------------------------------
    // MÓDULO DE GASTOS Y CUENTAS POR PAGAR
    // ---------------------------------------------------------

    @GetMapping("/gastos")
    public String panelGastos(Model model) {
        // Auto-semillado de proveedores base
        if (supplierRepository.count() == 0) {
            supplierRepository.save(new Supplier("1790000000003", "TecnoCables S.A.", "ventas@tecnocables.com", "022222222", "Equipos TICs"));
            supplierRepository.save(new Supplier("1790000000004", "Acero e Insumos", "info@aceros.com", "023333333", "Tuberías GLP"));
        }

        model.addAttribute("proveedores", supplierRepository.findAll());
        model.addAttribute("gastos", expenseRepository.findAllByOrderByIssueDateDesc());
        return "contabilidad-gastos";
    }

    @PostMapping("/gastos/registrar")
    public String registrarGasto(@RequestParam("supplierId") Long supplierId,
                                 @RequestParam("invoiceNumber") String invoiceNumber,
                                 @RequestParam("description") String description,
                                 @RequestParam("issueDate") LocalDate issueDate,
                                 @RequestParam("dueDate") LocalDate dueDate,
                                 @RequestParam("totalAmount") Double totalAmount) {

        Supplier proveedor = supplierRepository.findById(supplierId).orElseThrow();

        ExpenseRecord gasto = new ExpenseRecord();
        gasto.setSupplier(proveedor);
        gasto.setInvoiceNumber(invoiceNumber);
        gasto.setDescription(description);
        gasto.setIssueDate(issueDate);
        gasto.setDueDate(dueDate);
        gasto.setTotalAmount(totalAmount);

        expenseRepository.save(gasto);
        return "redirect:/contabilidad/gastos?registrado";
    }

    @PostMapping("/gastos/pagar")
    public String marcarGastoPagado(@RequestParam("expenseId") Long expenseId) {
        ExpenseRecord gasto = expenseRepository.findById(expenseId).orElseThrow();
        gasto.setStatus("PAGADO");
        expenseRepository.save(gasto);
        return "redirect:/contabilidad/gastos?pagado";
    }
    // ---------------------------------------------------------
    // MÓDULO DE REPORTES: ESTADO DE RESULTADOS
    // ---------------------------------------------------------

    @GetMapping("/reportes")
    public String verEstadoResultados(Model model) {
        // 1. Calcular Ingresos Totales (Facturas emiditas)
        Double ingresosTotales = invoiceRepository.findAll().stream()
                .mapToDouble(Invoice::getTotal).sum();

        // 2. Calcular Gastos Operativos (Compras a proveedores)
        Double gastosOperativos = expenseRepository.findAll().stream()
                .mapToDouble(ExpenseRecord::getTotalAmount).sum();

        // 3. Calcular Gastos de Personal (Nómina neta pagada)
        Double gastosNomina = payrollRepository.findAll().stream()
                .mapToDouble(Payroll::getNetSalary).sum();

        Double utilidadNeta = ingresosTotales - (gastosOperativos + gastosNomina);

        model.addAttribute("ingresos", ingresosTotales);
        model.addAttribute("gastosOp", gastosOperativos);
        model.addAttribute("gastosNomina", gastosNomina);
        model.addAttribute("utilidad", utilidadNeta);

        // Enviamos también las listas para detalles en tablas si se desea
        model.addAttribute("facturas", invoiceRepository.findAll());
        model.addAttribute("compras", expenseRepository.findAll());

        return "contabilidad-reportes";
    }
    // ---------------------------------------------------------
    // MÓDULO DE CATÁLOGO DE CUENTAS (INFORMACIÓN CONTABLE)
    // ---------------------------------------------------------

    @GetMapping("/cuentas")
    public String gestionCuentas(Model model) {
        model.addAttribute("cuentas", accountRepository.findAll());
        // Pasamos un objeto vacío para el formulario de creación
        model.addAttribute("nuevaCuenta", new Account());
        return "contabilidad-cuentas";
    }

    @PostMapping("/cuentas/guardar")
    public String guardarCuenta(@ModelAttribute Account cuenta) {
        // Hibernate es inteligente: Si la cuenta NO tiene ID, crea una nueva (INSERT).
        // Si la cuenta SÍ tiene ID (viene del botón editar), la actualiza (UPDATE).
        accountRepository.save(cuenta);
        return "redirect:/contabilidad/cuentas?exito";
    }

    @PostMapping("/cuentas/eliminar")
    public String eliminarCuenta(@RequestParam("cuentaId") Long cuentaId) {
        try {
            accountRepository.deleteById(cuentaId);
            return "redirect:/contabilidad/cuentas?eliminado";
        } catch (Exception e) {
            // Protección de integridad: Si la cuenta ya se usó en un asiento, fallará a propósito
            return "redirect:/contabilidad/cuentas?error_uso";
        }
    }
    // ---------------------------------------------------------
// PANEL GENERAL DE CONTABILIDAD (DASHBOARD FINANCIERO)
// ---------------------------------------------------------

    @GetMapping("/panel")
    public String panelGeneral(Model model) {
        // Obtenemos los últimos 100 movimientos con auditoría
        model.addAttribute("ultimosRegistros", journalRepository.findTop100ByOrderByCreatedAtDesc());

        // Datos rápidos para mini-widgets
        Double ingresos = invoiceRepository.findAll().stream().mapToDouble(Invoice::getTotal).sum();
        Double gastos = expenseRepository.findAll().stream().mapToDouble(ExpenseRecord::getTotalAmount).sum();
        model.addAttribute("balanceRapido", ingresos - gastos);

        return "contabilidad-panel";
    }

    // ACTUALIZACIÓN: Modifica tu método guardarAsiento actual para incluir esto:
    @PostMapping("/diario/guardar")
    public String guardarAsiento(@RequestParam("entryDate") LocalDate entryDate,
                                 @RequestParam("description") String description,
                                 @RequestParam("referenceDocument") String referenceDocument,
                                 @RequestParam("accountIds") List<Long> accountIds,
                                 @RequestParam("debits") List<Double> debits,
                                 @RequestParam("credits") List<Double> credits,
                                 java.security.Principal principal) { // <--- Agregamos Principal

        JournalEntry asiento = new JournalEntry();
        asiento.setEntryDate(entryDate);
        asiento.setDescription(description);
        asiento.setReferenceDocument(referenceDocument);
        asiento.setStatus("REGISTRADO");

        // AUDITORÍA: Capturamos momento real y usuario
        asiento.setCreatedAt(java.time.LocalDateTime.now());
        User userLogueado = userRepository.findByEmail(principal.getName()).get();
        asiento.setCreatedBy(userLogueado);

        for (int i = 0; i < accountIds.size(); i++) {
            Double deb = debits.get(i);
            Double cred = credits.get(i);
            if (deb > 0 || cred > 0) {
                JournalLine linea = new JournalLine();
                linea.setAccount(accountRepository.findById(accountIds.get(i)).get());
                linea.setDebit(deb);
                linea.setCredit(cred);
                asiento.addLine(linea);
            }
        }
        journalRepository.save(asiento);
        return "redirect:/contabilidad/panel?exito"; // Redirigimos al panel ahora
    }
}