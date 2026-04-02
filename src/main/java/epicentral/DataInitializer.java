package epicentral;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Collections;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Se verifica si el usuario administrador ya existe para evitar duplicados
        String adminEmail = "admin@epicentral.com";
        if (userRepository.findByEmail(adminEmail).isEmpty()) {

            User admin = new User();
            admin.setFirstName("Administrador");
            admin.setFirstSurname("Sistema");
            admin.setEmail(adminEmail);

            // Se utiliza el PasswordEncoder definido en SecurityConfig para encriptar la clave
            //
            admin.setPassword(passwordEncoder.encode("admin1234"));

            // Se asignan roles básicos
            admin.setRoles(Collections.singletonList("ADMIN"));

            // Persistencia en la base de datos SQLite definida en application.properties
            //
            userRepository.save(admin);

            System.out.println("SISTEMA: Usuario administrador inicial creado.");
            System.out.println("Email: " + adminEmail);
            System.out.println("Password: admin1234");
        }
    }
}