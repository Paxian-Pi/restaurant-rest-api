package com.paxian.security;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paxian.model.LoginModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    public PasswordEncoder passwordEncoder;

    public AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    // Triggered when we make a POST request to '/login'
    // We also need to pass in {"username": <username>, "password": <password>} in the request body

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // Grab credentials and map them to login-view-model
        LoginModel credentials = null;
        try {
            credentials = new ObjectMapper().readValue(request.getInputStream(), LoginModel.class);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // Create login token
        assert credentials != null;
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                credentials.getUsername(),
                credentials.getPassword(),
                new ArrayList<>());

        JwtConstants.LOGGER.trace(String.valueOf(credentials.getPassword()));

        //Authenticate user
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // Grab principal
        UserPrincipal  principal = (UserPrincipal) authResult.getPrincipal();

        // Create JWT
        String token = JWT.create()
                .withSubject(principal.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtConstants.EXPIRATION_TIME))
                .sign(HMAC512(JwtConstants.SECRET.getBytes()));

        JwtConstants.LOGGER.trace(token);

        // Add token to header
        response.addHeader(JwtConstants.HEADER, JwtConstants.TOKEN_PREFIX + token);

        //Render token in response body
        String Jw_Token = "{\"" + JwtConstants.HEADER + "\":\"" + JwtConstants.TOKEN_PREFIX + token + "\"}";
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(Jw_Token);
    }
}
