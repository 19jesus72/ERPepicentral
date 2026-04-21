package epicentral;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService; // Necesario para el remember-me

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // PERMITIMOS EL ACCESO A LA PANTALLA DE RECUPERACIÓN SIN LOGIN
                        .requestMatchers("/css/**", "/js/**", "/img/**", "/login", "/recuperar-password", "/recuperar-password/enviar").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                // CONFIGURACIÓN DEL ACCESO POR 30 DÍAS
                .rememberMe(remember -> remember
                        .key("EpicentralMasterControlKey2026") // Llave secreta para encriptar la cookie
                        .tokenValiditySeconds(2592000) // 30 días en segundos (30 * 24 * 60 * 60)
                        .rememberMeParameter("remember-me") // El nombre del checkbox en el HTML
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable()); // Mantener deshabilitado si usas H2/SQLite local para evitar bloqueos

        return http.build();
    }
}
