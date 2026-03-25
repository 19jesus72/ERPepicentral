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

    public User() {}

    // --- GETTERS Y SETTERS (Sin duplicar variables) ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getFirstSurname() { return firstSurname; }
    public void setFirstSurname(String firstSurname) { this.firstSurname = firstSurname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }

    public List<String> getFunctionalities() { return functionalities; }
    public void setFunctionalities(List<String> functionalities) { this.functionalities = functionalities; }
}

