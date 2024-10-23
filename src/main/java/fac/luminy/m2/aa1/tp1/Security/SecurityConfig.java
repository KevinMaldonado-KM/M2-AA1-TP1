package fac.luminy.m2.aa1.tp1.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        String[] publicEndpoints = {"/login", "/graphql", "/&continue" };
        String[] adminEndpoints = { "/for-admin/**" };

        http.authorizeHttpRequests(config -> {
            config.requestMatchers(publicEndpoints).permitAll();
            config.requestMatchers(adminEndpoints).hasAuthority("ADMIN");
            config.anyRequest().authenticated();
        });

        http.formLogin(form -> form.permitAll());
        http.logout(logout -> logout.permitAll().logoutSuccessUrl("/"));
        http.csrf(csrf -> csrf.ignoringRequestMatchers(publicEndpoints));

        return http.build();
    }

    @Bean
    public AuthenticationProvider authProvider(UserDetailsService userDetailsService, PasswordEncoder encoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder);
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
