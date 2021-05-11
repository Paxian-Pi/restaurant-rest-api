package com.paxian.security;

import com.paxian.db.DataRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    public UserPrincipalDetailsService userPrincipalDetailsService;
    public DataRepository dataRepository;

    public SecurityConfiguration(UserPrincipalDetailsService userPrincipalDetailsService, DataRepository dataRepository) {
        this.userPrincipalDetailsService = userPrincipalDetailsService;
        this.dataRepository = dataRepository;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.authenticationProvider(getAuthenticationProvider());
        auth.userDetailsService(userPrincipalDetailsService).passwordEncoder(passwordEncoder());
    }

    /*@Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/api/token/**");
    }*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //remove csrf and state in session because in jwt, we do not need them
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()

                // add jwt filters (1. authentication, 2. authorization)
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), this.dataRepository))
                .authorizeRequests()

                // configure access rules

                //.antMatchers("/api/some_other_route_with_role").hasRole("MANAGER")
                .antMatchers("/api/token/**").permitAll()
                .antMatchers("/api/team/**").permitAll()

                .antMatchers(HttpMethod.POST, "/login").permitAll()

                .anyRequest().authenticated();

        http.cors();
    }

    @Bean
    public DaoAuthenticationProvider getAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(this.userPrincipalDetailsService);

        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
