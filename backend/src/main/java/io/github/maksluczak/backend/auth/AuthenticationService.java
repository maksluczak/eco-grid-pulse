package io.github.maksluczak.backend.auth;

import io.github.maksluczak.backend.auth.dto.AuthenticationRequest;
import io.github.maksluczak.backend.auth.dto.AuthenticationResponse;
import io.github.maksluczak.backend.auth.dto.RegisterRequest;
import io.github.maksluczak.backend.exception.EmailAlreadyTakenException;
import io.github.maksluczak.backend.exception.InvalidTokenException;
import io.github.maksluczak.backend.user.User;
import io.github.maksluczak.backend.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            JwtService jwtService,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        String email = normalize(request.getEmail());
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyTakenException(email);
        }

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        String email = normalize(request.getEmail());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email, request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(email));

        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse refresh(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new InvalidTokenException("No token found in Authorization header");
        }

        String token = authHeader.substring(7);
        String email = jwtService.extractEmailForRefresh(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidTokenException("User from token does not exist"));

        String newToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(newToken)
                .build();
    }

    private String normalize(String email) {
        return email.trim().toLowerCase();
    }
}
