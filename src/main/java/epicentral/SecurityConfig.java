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
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/registro").permitAll() // Puerta abierta para estilos y registro
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true) // Esto te manda directo al inicio tras el login
                        .permitAll()
                )
                .rememberMe(remember -> remember
                        .key("EpicentralSecretKey_2026") // Llave única para cifrar la cookie
                        .tokenValiditySeconds(2592000)  // Válido por 30 días (60*60*24*30)
                        .userDetailsService(userDetailsService)
                )
                .logout(logout -> logout.permitAll());
        return http.build();
    }
}
