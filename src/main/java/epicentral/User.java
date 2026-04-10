package epicentral;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "users_erp")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- IDENTIFICACIÓN ---
    @Column(name = "primer_nombre")
    private String firstName;

    @Column(name = "segundo_nombre")
    private String secondName;

    @Column(name = "primer_apellido")
    private String firstSurname;

    @Column(name = "segundo_apellido")
    private String secondSurname;

    private String documentType;   // Cédula, RUC, Pasaporte
    private String documentNumber;
    private String gender;
    private String birthDate;      // Usamos String para compatibilidad directa con HTML date
    private String nationality;
    private String city;
    private String homeAddress;

    // --- CUENTA Y ACCESO ---

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    // --- CONTACTO ---
    private String primaryPhone;
    private String secondaryPhone;
    private String emergencyKinship; // Parentesco del contacto de emergencia

    // --- INFORMACIÓN DE SALUD ---
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean hasDisability;
    private String disabilityDescription;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean hasChronicIllness;
    private String chronicIllnessDescription;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean hasAllergy;
    private String allergyDescription;

    // --- DATOS DE RECURSOS HUMANOS (NUEVO) ---

    @Column(nullable = false, columnDefinition = "boolean default true")
    private Boolean hasSystemAccess = true; // ¿Se le permite iniciar sesión?

    private String jobTitle; // Cargo (Ej: Técnico Instalador, Contador)
    private String department; // Departamento (Ej: Operaciones, Administración)
    private String hireDate; // Fecha de contratación

    @Column(columnDefinition = "TEXT")
    private String workHistory; // Trayectoria laboral previa

    @Column(columnDefinition = "TEXT")
    private String educationHistory; // Trayectoria educativa/títulos

    @Column(columnDefinition = "TEXT")
    private String skills; // Habilidades (Ej: Soldadura, Redes, Liderazgo)

    @Column(columnDefinition = "TEXT")
    private String weaknesses; // Áreas de mejora / Debilidades


    // --- SEGURIDAD Y PERMISOS (Lo que ya tenías) ---
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    @ElementCollection
    private List<String> functionalities;

    // --- CONSTRUCTOR VACÍO (Obligatorio para JPA) ---
    public User() {}

    // --- GETTERS Y SETTERS ---
    // Identificación
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getSecondName() { return secondName; }
    public void setSecondName(String secondName) { this.secondName = secondName; }
    public String getFirstSurname() { return firstSurname; }
    public void setFirstSurname(String firstSurname) { this.firstSurname = firstSurname; }
    public String getSecondSurname() { return secondSurname; }
    public void setSecondSurname(String secondSurname) { this.secondSurname = secondSurname; }
    public String getDocumentType() { return documentType; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }
    public String getDocumentNumber() { return documentNumber; }
    public void setDocumentNumber(String documentNumber) { this.documentNumber = documentNumber; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }
    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getHomeAddress() { return homeAddress; }
    public void setHomeAddress(String homeAddress) { this.homeAddress = homeAddress; }

    // Cuenta y Contacto
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPrimaryPhone() { return primaryPhone; }
    public void setPrimaryPhone(String primaryPhone) { this.primaryPhone = primaryPhone; }
    public String getSecondaryPhone() { return secondaryPhone; }
    public void setSecondaryPhone(String secondaryPhone) { this.secondaryPhone = secondaryPhone; }
    public String getEmergencyKinship() { return emergencyKinship; }
    public void setEmergencyKinship(String emergencyKinship) { this.emergencyKinship = emergencyKinship; }

    // Salud
    public boolean isHasDisability() { return hasDisability; }
    public void setHasDisability(boolean hasDisability) { this.hasDisability = hasDisability; }
    public String getDisabilityDescription() { return disabilityDescription; }
    public void setDisabilityDescription(String disabilityDescription) { this.disabilityDescription = disabilityDescription; }
    public boolean isHasChronicIllness() { return hasChronicIllness; }
    public void setHasChronicIllness(boolean hasChronicIllness) { this.hasChronicIllness = hasChronicIllness; }
    public String getChronicIllnessDescription() { return chronicIllnessDescription; }
    public void setChronicIllnessDescription(String chronicIllnessDescription) { this.chronicIllnessDescription = chronicIllnessDescription; }
    public boolean isHasAllergy() { return hasAllergy; }
    public void setHasAllergy(boolean hasAllergy) { this.hasAllergy = hasAllergy; }
    public String getAllergyDescription() { return allergyDescription; }
    public void setAllergyDescription(String allergyDescription) { this.allergyDescription = allergyDescription; }

    // Listas
    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }
    public List<String> getFunctionalities() { return functionalities; }
    public void setFunctionalities(List<String> functionalities) { this.functionalities = functionalities; }

    public Boolean getHasSystemAccess() { return hasSystemAccess; }
    public void setHasSystemAccess(Boolean hasSystemAccess) { this.hasSystemAccess = hasSystemAccess; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getHireDate() { return hireDate; }
    public void setHireDate(String hireDate) { this.hireDate = hireDate; }

    public String getWorkHistory() { return workHistory; }
    public void setWorkHistory(String workHistory) { this.workHistory = workHistory; }

    public String getEducationHistory() { return educationHistory; }
    public void setEducationHistory(String educationHistory) { this.educationHistory = educationHistory; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public String getWeaknesses() { return weaknesses; }
    public void setWeaknesses(String weaknesses) { this.weaknesses = weaknesses; }
}
