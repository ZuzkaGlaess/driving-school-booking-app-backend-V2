package at.technikum.drivingschool.bookingappbackend.security;

import at.technikum.drivingschool.bookingappbackend.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import at.technikum.drivingschool.bookingappbackend.security.jwt.AuthEntryPointJwt;
import at.technikum.drivingschool.bookingappbackend.security.jwt.AuthTokenFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class WebSecurityConfig {
  @Autowired
  UserDetailsServiceImpl userDetailsService;

  @Autowired
  private AuthEntryPointJwt unauthorizedHandler;

  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {
    return new AuthTokenFilter();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
       
      authProvider.setUserDetailsService(userDetailsService);
      authProvider.setPasswordEncoder(passwordEncoder());
   
      return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  /**
   * Encoder for user password
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Filer to configure paths that need authentication and paths that are free to access
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    //http.cors(cors -> cors.disable())
      http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            // set handler for unauthorized access
        .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
            // set session policy
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // set path permissions
        .authorizeHttpRequests(auth -> 
          auth.requestMatchers("/api/auth/**").permitAll() // allow authentication and registration api
//              .requestMatchers("/api/test/**").permitAll() // allow test api - just for now
                  .requestMatchers("/swagger.html").permitAll() // allow access to swagger
                  .requestMatchers("/swagger-ui/**").permitAll() // allow access to swagger
                  .requestMatchers("/api/docs/**").permitAll() // allow access to swagger
                  .requestMatchers("/api/docs/swagger-config/**").permitAll() // allow access to swagger
              .anyRequest().authenticated() // all other need to get authenticated
        );
    
    http.authenticationProvider(authenticationProvider());

    // add filter to existing filter list before username password filter
    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    
    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    /*List<String> origins = new ArrayList<String>();
    origins.add("http://localhost:8080");
    origins.add("http://localhost:63342");
    config.setAllowedOrigins(origins);*/
    List<String> originPatterns = new ArrayList<String>();
    originPatterns.add("*");
    config.setAllowedOriginPatterns(originPatterns);
    config.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT"));
    config.addAllowedHeader("*");
    config.setAllowCredentials(true);
    List<String> headers = new ArrayList<String>();
    headers.add("Authorization");
    config.setExposedHeaders(headers);
    source.registerCorsConfiguration("/**", config);
    return source;
  }
}
