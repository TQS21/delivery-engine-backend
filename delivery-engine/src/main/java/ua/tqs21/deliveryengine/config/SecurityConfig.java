package ua.tqs21.deliveryengine.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import ua.tqs21.deliveryengine.auth.AuthConsts;
import ua.tqs21.deliveryengine.auth.JWTAuthenticationFilter;
import ua.tqs21.deliveryengine.auth.JWTAuthorizationFilter;
import ua.tqs21.deliveryengine.auth.UserDetailsServiceImpl;
import ua.tqs21.deliveryengine.repositories.UserRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private UserRepository userRepository;

    public SecurityConfig(UserDetailsServiceImpl userDetailsServiceImpl, UserRepository userRepository) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.userRepository = userRepository;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsServiceImpl);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowCredentials(false);
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("X-Auth-Token", "Authorization", "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials"));

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().configurationSource(corsConfigurationSource()).and().csrf().disable()
                .authorizeRequests()

                //PUBLIC
                .antMatchers(HttpMethod.POST, AuthConsts.LOG_IN_URL).permitAll()
                .antMatchers(HttpMethod.GET, "/**").permitAll()
                .antMatchers(HttpMethod.POST, "/**").permitAll()
                .antMatchers(HttpMethod.DELETE, "/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/**").permitAll()

                .anyRequest().authenticated()
                .and()

                .exceptionHandling()
                .authenticationEntryPoint(
                    (req, res, ex) -> {
                        Map<String, Object> body = new HashMap<>();
                        body.put("error", "Access denied");
                        body.put("timestamp", new Date().toString());
                        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);

                        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        new ObjectMapper().writeValue(res.getWriter(), body);
                    } 
                )
                .accessDeniedHandler(
                    (req, res, ex) -> {
                        Map<String, Object> body = new HashMap<>();
                        body.put("error", "Forbidden resource");
                        body.put("timestamp", new Date().toString());
                        body.put("status", HttpServletResponse.SC_FORBIDDEN);
                                        
                        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        new ObjectMapper().writeValue(res.getWriter(), body);
                    }
                )
                .and()

                .addFilterBefore(new JWTAuthenticationFilter(authenticationManager(), this.userRepository), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JWTAuthorizationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }
}
