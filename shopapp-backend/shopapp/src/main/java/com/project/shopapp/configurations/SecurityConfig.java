package com.project.shopapp.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Value("${api.prefix}")
    private String apiPrefix;
    private final CustomJwtDecoder customJwtDecoder;
    private  final  CustomAuthenticationConverter customAuthenticationConverter;
    private  final  JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    public SecurityConfig(CustomJwtDecoder customJwtDecoder, CustomAuthenticationConverter customAuthenticationConverter,JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.customJwtDecoder = customJwtDecoder;
        this.customAuthenticationConverter = customAuthenticationConverter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    private static final String[] PUBLIC_ENDPOINTS_ALL = {"/auth/**"};


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> {
                    request
                            .requestMatchers(PUBLIC_ENDPOINTS_ALL).permitAll()
                            .requestMatchers(GET, String.format("%s/public/**", apiPrefix)).permitAll()
                            .requestMatchers(String.format("%s/size", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(String.format("%s/banner", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(String.format("%s/categories", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(POST, String.format("%s/order", apiPrefix)).hasAnyRole("ADMIN", "USER")
                            .requestMatchers(DELETE, String.format("%s/size/delete", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(DELETE, String.format("%s/products/**", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(GET, String.format("%s/users/all", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(PUT, String.format("%s/products", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(PUT, String.format("%s/users/ban", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(PUT, String.format("%s/users/unban", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(POST, String.format("%s/products/**", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(DELETE, String.format("%s/categories/**", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(PUT, String.format("%s/categories", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(POST, String.format("%s/categories/**", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(POST, String.format("%s/orders", apiPrefix)).hasAnyRole("USER", "ADMIN")
                            .requestMatchers(GET, String.format("%s/orders", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(PUT, String.format("%s/order", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(DELETE, String.format("%s/orders/**", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(POST, String.format("%s/order-details/**", apiPrefix)).hasAnyRole("USER")
                            .requestMatchers(GET, String.format("%s/order-details/**", apiPrefix)).hasAnyRole("ADMIN", "USER")
                            .requestMatchers(PUT, String.format("%s/order-details/**", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(DELETE, String.format("%s/order-details/**", apiPrefix)).hasRole("ADMIN")
                            .anyRequest().permitAll();
                });
        http.oauth2ResourceServer(oauth -> oauth.jwt(jwtConfigurer -> jwtConfigurer
                        .decoder(customJwtDecoder)
                        .jwtAuthenticationConverter(customAuthenticationConverter))
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
        );
        http.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("*"));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
                configuration.setExposedHeaders(List.of("x-auth-token"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                httpSecurityCorsConfigurer.configurationSource(source);
            }
        });
        return http.build();
    }

//    @Bean
//    JwtAuthenticationConverter jwtAuthenticationConverter() {
//        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
//        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
//        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
//        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
//        return new JwtAuthenticationConverter();
//    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
