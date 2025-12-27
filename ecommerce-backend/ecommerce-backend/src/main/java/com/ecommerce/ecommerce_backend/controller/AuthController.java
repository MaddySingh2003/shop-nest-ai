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
public ResponseEntity<User> register(@RequestBody User user) {
    user.setRole(User.Role.USER);
    return ResponseEntity.ok(userService.register(user));
}


//                test mapings    ///////


@GetMapping("/secure")
public ResponseEntity<String> secured(){
    return ResponseEntity.ok("You are authorized 🎉");
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

    var userOpt = userRepository.findByEmail(request.getEmail());

    if(userOpt.isEmpty()){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("User not found");
    }

    var user = userOpt.get();

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Invalid Credentials");
    }

    String token = jwtService.generateToken(user.getEmail());
    var expiry = jwtService.getExpiry(token);

    return ResponseEntity.ok(Map.of(
            "token", token,
            "email", user.getEmail(),
            "role", user.getRole().name(),
            "expiresAt", expiry.getTime()
    ));
}


}
