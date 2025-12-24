package com.example.amsnew.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.example.amsnew.security.JwtAuthenticationFilter;
import org.springframework.security.config.Customizer;


@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Bean
    public org.springframework.security.authentication.dao.DaoAuthenticationProvider daoAuthenticationProvider() {
        var provider = new org.springframework.security.authentication.dao.DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.cors(Customizer.withDefaults());

        http
            .csrf(csrf -> csrf.disable())
<<<<<<< Updated upstream
            .authorizeHttpRequests(auth -> auth
            		.requestMatchers(
                            "/user/register",
                            "/user/login",
                            "/leave/**",
                            "/shift/**",
                            // OpenAPI / Swagger
                            "/v3/api-docs/**",
                            "/v3/api-docs.yaml",
                            "/swagger-ui/**",
                            "/swagger-ui.html",
                            "/swagger-ui/index.html",
                            "/swagger-resources/**",
                            "/webjars/**",
                            // optionally actuator health if you want public
                            "/actuator/health",
                            "/attendance/login",
                            "/attendance/logout/*",
                            "/attendance/*",
                            "/department/**",
                            "/user/getAllEmployees",
                            "/user/delete/**",
                            "/shift/**",
                            "/actuator/health"
                        ).permitAll()
                .anyRequest().authenticated()
=======
            .sessionManagement(sess ->
                sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
>>>>>>> Stashed changes
            )
            .authorizeHttpRequests(auth -> auth

                /* ===== PUBLIC ===== */
                .requestMatchers(
                    "/user/login",
                    "/user/register",
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/swagger-ui/index.html",
                    "/swagger-resources/**",
                    "/webjars/**",
                    "/actuator/health",
                    "/department/add"
                ).permitAll()

                /* ===== DEPARTMENT ===== */
                .requestMatchers(HttpMethod.GET, "/department/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/department/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/department/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/department/**").hasAuthority("ROLE_ADMIN")

                /* ===== USER ===== */
                .requestMatchers("/user/getAllEmployees").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/user/delete/**").hasAuthority("ROLE_ADMIN")

                /* ===== ATTENDANCE ===== */
                .requestMatchers("/attendance/**").authenticated()

                /* ===== LEAVE ===== */
                .requestMatchers(HttpMethod.POST, "/leave/**").hasAuthority("ROLE_EMPLOYEE")
                .requestMatchers(HttpMethod.GET, "/leave/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/leave/**").authenticated()


                /* ===== EVERYTHING ELSE ===== */
                .anyRequest().authenticated()

            )
            .authenticationProvider(daoAuthenticationProvider())
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable());

        return http.build();
    }

    /* ===== CORS ===== */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
