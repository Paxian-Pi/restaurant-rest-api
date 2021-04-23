package com.paxian.security;

import com.auth0.jwt.JWT;
import com.paxian.db.DataRepository;
import com.paxian.model.UserModel;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    public DataRepository dataRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, DataRepository dataRepository) {
        super(authenticationManager);
        this.dataRepository = dataRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Read the Authorization, where the token should be
        String header  = request.getHeader(JwtConstants.HEADER);

        // If header does not contain 'Bearer' or is null, delegate to Spring impl and exit
        if(header == null || !header.startsWith(JwtConstants.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        // If header is present, grab user principal from database and perform authorization
        Authentication authentication = getUsernamePasswordAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Continue filter execution
        chain.doFilter(request, response);
    }

    private Authentication getUsernamePasswordAuthentication(HttpServletRequest request) {
        String token = request.getHeader(JwtConstants.HEADER).replace(JwtConstants.TOKEN_PREFIX, "");

        JwtConstants.LOGGER.trace(token);

        // Parse the token and validate it
        String username = JWT.require(HMAC512(JwtConstants.SECRET.getBytes()))
                .build()
                .verify(token)
                .getSubject();

        // Search in the DB to find the user by token subject (username)
        // If so, then grab user details and create Spring Auth Token; using 'username', 'password' and 'authorization/roles'
        if(username != null) {
            UserModel userModel = dataRepository.findByUsername(username);
            UserPrincipal principal = new UserPrincipal(userModel);

            return new UsernamePasswordAuthenticationToken(username, null, principal.getAuthorities());
        }
        return null;
    }
}
