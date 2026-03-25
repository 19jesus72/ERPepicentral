package epicentral;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "users_erp")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Asignado secuencialmente por la BD

    @Column(name = "primer_nombre")
    private String firstName;

    @Column(name = "segundo_nombre")
    private String secondName;

    @Column(name = "primer_apellido")
    private String firstSurname;

    @Column(name = "segundo_apellido")
    private String secondSurname;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password; // Aquí guardaremos el hash (BCrypt)

    // Lista de roles/cargos (ej: ADMIN, VENDEDOR)
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    // Lista de funcionalidades permitidas
    @ElementCollection
    private List<String> functionalities;

    // Getters y Setters...
}