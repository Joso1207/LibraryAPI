package ChasAcademy.LibraryAPI.api.v1.controller;

import ChasAcademy.LibraryAPI.api.core.ApiResponseWrapper;
import ChasAcademy.LibraryAPI.api.core.dto.UserRequest;
import ChasAcademy.LibraryAPI.service.AuthorService;
import ChasAcademy.LibraryAPI.service.Authservice;
import ChasAcademy.LibraryAPI.service.LoginService;
import ChasAcademy.LibraryAPI.service.UserService;
import jakarta.validation.Valid;
import org.h2.engine.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/api/user")
public class UserLoginControllerV1 {

    private final UserService userService;
    private final LoginService loginService;

    public UserLoginControllerV1(UserService userService, LoginService loginService){
        this.userService = userService;
        this.loginService = loginService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRequest newUserRequest){
        userService.createUser(newUserRequest.username(), newUserRequest.password(),"USER" );
        return ResponseEntity.status(HttpStatus.CREATED).body(
                "User " + newUserRequest.username() + " Created");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserRequest loginRequest){
        return ResponseEntity.ok(loginService.login(loginRequest.username(),loginRequest.password()));



    }


}
