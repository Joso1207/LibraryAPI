package ChasAcademy.LibraryAPI.service;

import ChasAcademy.LibraryAPI.persistence.model.AppUser;
import ChasAcademy.LibraryAPI.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/*
For smaller systems I actually hate the implementation of UserDetailsService and Authmanager.
Outside of needing multiple plug-in auth providers and systems its often more efficient with
a simplified and conventional password matching.

Here UserDetailsService's only purpose is to tell the authmanager where to find the userdetails so it can authenticate.
 */
@Service
public class Authservice implements UserDetailsService {

    private final UserService service;

    public Authservice(UserService service) {
        this.service = service;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = service.findByUsername(username).orElseThrow(()->new UsernameNotFoundException(username));

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }




}
