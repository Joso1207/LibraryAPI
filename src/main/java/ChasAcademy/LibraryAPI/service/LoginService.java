package ChasAcademy.LibraryAPI.service;

import ChasAcademy.LibraryAPI.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil util;

    public LoginService(AuthenticationManager authenticationManager, JwtUtil util) {
        this.authenticationManager = authenticationManager;
        this.util = util;
    }

    public String login(String username, String password){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
        UserDetails user = (UserDetails) authentication.getPrincipal();
        return util.generateToken(user.getUsername());
    }
}
