package com.api.Ubank.Controller;

import com.api.Ubank.DTO.LoginDTO;
import com.api.Ubank.DTO.UserDTO;
import com.api.Ubank.Entity.User;
import com.api.Ubank.Service.UserCardService;
import com.api.Ubank.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:4200/")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserCardService userCardService;

    @PostMapping("/login")
    public ResponseEntity<LoginDTO> login(@RequestBody LoginDTO loginDTO) {
        Optional<User> userOptional = userService.getUserByEmail(loginDTO.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(loginDTO.getPassword())) {
                // La contraseña coincide, permite el inicio de sesión exitoso
                return ResponseEntity.ok(loginDTO);
            } else {
                // La contraseña no coincide, devuelve una respuesta de error
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        } else {
            // El correo electrónico no existe en la base de datos, devuelve una respuesta de error
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO) {
        Optional<User> existingUser = userService.getUserByEmail(userDTO.getEmail());
        if (existingUser.isPresent()) {
            // El correo electrónico ya está en uso, devuelve una respuesta de error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
            try {
                User createdUser = userCardService.createUserWithRandomCard(userDTO);
                System.out.println("EL USUARIO FUE CREADO");
                return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("ERROR AL CREAR EL USUARIO");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }}
}
