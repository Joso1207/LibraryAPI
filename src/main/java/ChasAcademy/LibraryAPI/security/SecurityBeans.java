package ChasAcademy.LibraryAPI.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SecurityBeans {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /*
For smaller systems I actually hate the implementation of UserDetailsService and AuthManager.
Outside of needing multiple plug-in auth providers and systems its often more efficient with
a simplified and conventional password matching.

Here AuthenticationManager is called for its DaoAuthenticationProvider... which litterary just does password matching.

 */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

}
