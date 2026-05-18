package ChasAcademy.LibraryAPI.service;

import ChasAcademy.LibraryAPI.persistence.model.AppUser;
import ChasAcademy.LibraryAPI.persistence.repository.UserRepository;
import ChasAcademy.LibraryAPI.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class UserService {


    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repo, PasswordEncoder encoder){
        this.repo = repo;
        this.encoder = encoder;
    }

    public AppUser createUser(String username, String password, String role){
        return AppUser.builder()
                .username(username)
                .password(encoder.encode(password))
                .role(role)
                .build();
    }

    public Optional<AppUser> findByUsername(String username){
        return repo.findByUsername(username);
    }




}
