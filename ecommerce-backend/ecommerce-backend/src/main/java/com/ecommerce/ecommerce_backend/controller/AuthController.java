package com.ecommerce.ecommerce_backend.controller;

import com.ecommerce.ecommerce_backend.dto.LoginRequest;
import com.ecommerce.ecommerce_backend.model.User;
import com.ecommerce.ecommerce_backend.repository.UserRepository;
import com.ecommerce.ecommerce_backend.service.JwtService;
import com.ecommerce.ecommerce_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

   

    // ---------------- REGISTER ----------------
    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.register(user));
    }


//                test mapings    ///////


@GetMapping("/secure")
public String secure() {
    return "You are authorized 🎉";
}



    @GetMapping("/test")
public String test(){
    return "OK";
}

@GetMapping("/users")
public Object testUsers() {
    return userRepository.findAll();
}

//             _____________________-------------------  
@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest request){

    System.out.println("Login attempt: " + request.getEmail());

    var userOpt = userRepository.findByEmail(request.getEmail());

    if(userOpt.isEmpty()){
        System.out.println("USER NOT FOUND");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("User not found");
    }

    var user = userOpt.get();

    System.out.println("DB Password: " + user.getPassword());
    System.out.println("Raw Password: " + request.getPassword());
    System.out.println("Password match: "
            + passwordEncoder.matches(request.getPassword(), user.getPassword())
    );

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Invalid Credentials");
    }

    String token = jwtService.generateToken(user.getEmail());
    return ResponseEntity.ok(Map.of("token", token));
}


}
