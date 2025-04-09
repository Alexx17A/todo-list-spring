package com.example.todolistspring.service;

import com.example.todolistspring.models.Usuario;
import com.example.todolistspring.repository.UsuarioRepository;
import com.example.todolistspring.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public String login(String username, String password) {
        try {
            Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return this.jwtUtil.generateToken(authentication.getName());
        } catch (BadCredentialsException var4) {
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }
    }

    public String register(Usuario usuario) {
        if (this.usuarioRepository.findByUsername(usuario.getUsername()).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        } else {
            usuario.setPassword(this.passwordEncoder.encode(usuario.getPassword()));
            this.usuarioRepository.save(usuario);
            return this.jwtUtil.generateToken(usuario.getUsername());
        }
    }
}
