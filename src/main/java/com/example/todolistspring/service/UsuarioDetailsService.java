package com.example.todolistspring.service;

import com.example.todolistspring.models.Usuario;
import com.example.todolistspring.repository.UsuarioRepository;
import com.example.todolistspring.security.JwtUtil;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService { // Implementa la interfaz
    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = (Usuario)this.usuarioRepository.findByUsername(username).orElseThrow(() -> {
            return new UsernameNotFoundException("Usuario no encontrado: " + username);
        });
        return User.withUsername(usuario.getUsername()).password(usuario.getPassword()).roles(new String[]{"USER"}).build();
    }
}
