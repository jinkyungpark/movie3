package com.project.movie.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices.RememberMeTokenAlgorithm;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.project.movie.handler.CustomAccessDeniedHandler;

@EnableMethodSecurity
@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                http
                                // .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests((authorize) -> authorize
                                                .requestMatchers("/", "/assets/**", "/css/**", "/js/**", "/upload/**")
                                                .permitAll()
                                                .requestMatchers("/movie/list", "/movie/read").permitAll()
                                                .requestMatchers("/movie/modify").hasRole("ADMIN")
                                                .requestMatchers("/reviews/**", "/upload/display/**").permitAll()
                                                .requestMatchers("/member/register").permitAll()
                                                .anyRequest().authenticated())
                                .formLogin(login -> login
                                                .loginPage("/member/login").permitAll()
                                                .defaultSuccessUrl("/movie/list", true))
                                .sessionManagement(
                                                session -> session
                                                                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                                .logout((logout) -> logout
                                                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                                                .logoutSuccessUrl("/")
                                                .invalidateHttpSession(true))
                                // .exceptionHandling(exception ->
                                // exception.accessDeniedPage("/accessDenied.html"));
                                .exceptionHandling(exception -> exception.accessDeniedHandler(cAccessDeniedHandler()));

                return http.build();
        }

        @Bean // @Bean 이 붙은 상태는 public 임
        PasswordEncoder passwordEncoder() {
                return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }

        @Bean // @Bean 이 붙은 상태는 public 임
        CustomAccessDeniedHandler cAccessDeniedHandler() {
                return new CustomAccessDeniedHandler();
        }

        // .requestMatchers("/js/**", "/css/**").permitAll()
        // .requestMatchers("/member/logout").permitAll()
        // .anyRequest().authenticated())
        // .formLogin(login -> login
        // .loginPage("/member/login").permitAll())
        // .logout((logout) -> logout
        // .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
        // .logoutSuccessUrl("/")
        // .invalidateHttpSession(true));

        // .requestMatchers("/member/register").permitAll()
        // @Bean
        // RememberMeServices rememberMeServices(UserDetailsService userDetailsService)
        // {
        // RememberMeTokenAlgorithm encodingAlgorithm = RememberMeTokenAlgorithm.SHA256;
        // TokenBasedRememberMeServices rememberMe = new
        // TokenBasedRememberMeServices("myKey", userDetailsService,
        // encodingAlgorithm);
        // rememberMe.setMatchingAlgorithm(RememberMeTokenAlgorithm.MD5);
        // rememberMe.setTokenValiditySeconds(60 * 60 * 24 * 7);
        // return rememberMe;
        // }

        // @Bean
        // MemberLoginSuccessHandler successHandler() {
        // return new MemberLoginSuccessHandler();
        // }

        // @Bean
        // UserDetailsService users() {
        // UserDetails user = User.builder()
        // .username("user")
        // // .password("$2a$10$R4dQAJiKh15uLkHh9Qd65eQ224K7CPlkkmzD9arBahs4YL0wrjr7K")
        // .password("{bcrypt}$2a$10$Dg7z5ZNR8VA5mWVTkauNK.69y8N.KwIamfXj.BdEa5cprczSXoEe.")
        // .roles("USER")
        // .build();

        // return new InMemoryUserDetailsManager(user);
        // }
}
