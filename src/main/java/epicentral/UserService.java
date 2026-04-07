package epicentral;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // En UserService.java
    public void registrarUsuario(User user) {
        // 1. Validar si el email ya existe
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("El correo electrónico ya está registrado en el sistema.");
        }

        // 2. Si es nuevo, encriptar y guardar
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}