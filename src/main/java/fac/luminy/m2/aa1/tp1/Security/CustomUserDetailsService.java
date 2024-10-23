package fac.luminy.m2.aa1.tp1.Security;

import fac.luminy.m2.aa1.tp1.model.entity.XUser;
import fac.luminy.m2.aa1.tp1.repository.XUserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private XUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final var appUser = userRepository.findById(username);

        if (appUser.isEmpty()) {
            throw new UsernameNotFoundException("User '" + username + "' not found");
        }

        final var user = appUser.get();
        var authorites = new LinkedList<GrantedAuthority>();
        user.getRoles().forEach((role) -> {
            authorites.add(new SimpleGrantedAuthority(role));
        });
        return org.springframework.security.core.userdetails.User//
                .withUsername(username)//
                .password(user.getPassword())//
                .authorities(authorites)//
                .accountExpired(false)//
                .accountLocked(false)//
                .credentialsExpired(false)//
                .disabled(false)//
                .build();
    }

    @PostConstruct
    public void init() {
        var encoder = new BCryptPasswordEncoder();
        var admin = new XUser("admin", encoder.encode("admin"), Set.of("ADMINISTRATEUR"));
        var proprietaire = new XUser("proprietaire", encoder.encode("proprietaire"), Set.of("PROPRIETAIRE"));
        var locataire = new XUser("locataire", encoder.encode("locataire"), Set.of("LOUEUR"));
        userRepository.save(admin);
        userRepository.save(proprietaire);
        userRepository.save(locataire);
        System.out.println("--- INIT SPRING SECURITY USERS ---");
    }
}
