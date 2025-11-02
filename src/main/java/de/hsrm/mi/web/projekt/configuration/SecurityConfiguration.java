package de.hsrm.mi.web.projekt.configuration;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    PasswordEncoder passwordEncoder() { // @Bean -> Encoder woanders per @Autowired abrufbar
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChainApp(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(autherize -> autherize
                .requestMatchers(toH2Console()).permitAll()
                .requestMatchers("/benutzer/**").hasRole("ADMIN")
                .requestMatchers("/login", "/logout", "/register").permitAll()
                .requestMatchers("/doener/**").authenticated()
                .requestMatchers("/api/**", "/stompbroker/**", "/images/**", "/css/**", "/style.css").permitAll())
                .csrf(csrf -> csrf.ignoringRequestMatchers(toH2Console(),
                        AntPathRequestMatcher.antMatcher("/benutzer/*/hx/feld/*"))) // H2 Console CSRF-Schutz
                                                                                    // deaktivieren
                .headers(hdrs -> hdrs.frameOptions(fo -> fo.sameOrigin()))
                .formLogin(form -> form
                        .defaultSuccessUrl("/doener", true))
                .build();
    }

}
