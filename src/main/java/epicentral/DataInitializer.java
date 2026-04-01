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
        // Verificamos si ya existe el admin para no duplicarlo cada vez que inicies
        if (userRepository.findByEmail("jesus-abo@hotmail.com").isEmpty()) {

            User admin = new User();
            admin.setFirstName("Jesús");
            admin.setFirstSurname("Álvarez");
            admin.setEmail("jesus-abo@hotmail.com");

            // Encriptamos la contraseña "admin123" (Cámbiala luego)
            admin.setPassword(passwordEncoder.encode("19728523"));

            // Le asignamos el rol de ADMIN que usaremos después
            admin.setRoles(Collections.singletonList("ADMIN"));

            userRepository.save(admin);

            System.out.println("--------------------------------------");
            System.out.println("SISTEMA: Usuario Admin creado con éxito");
            System.out.println("Email: jesus-abo@hotmail.com");
            System.out.println("Password: 19728523");
            System.out.println("--------------------------------------");
        }
    }
}