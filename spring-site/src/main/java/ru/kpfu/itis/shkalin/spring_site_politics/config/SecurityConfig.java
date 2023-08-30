package ru.kpfu.itis.shkalin.spring_site_politics.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/books/new", "/books/*/edit", "/books/*/delete").hasRole("ADMIN")
                .antMatchers("/*/new", "/*/*/edit", "/*/*/delete").hasAnyRole("ADMIN", "USER")
                .antMatchers("/profile*").authenticated()
                .anyRequest().permitAll()
        .and()
            .exceptionHandling()
                .accessDeniedPage("/403") // GetMapping("/403") in SecurityController
                /*
                  Using an accessDeniedHandler instead of a page has the advantage
                  that we can define custom logic to be executed before redirecting to the 403 page.
                  https://www.baeldung.com/spring-security-custom-access-denied-page#2-access-denied-handler
                 */
//                .accessDeniedHandler(accessDeniedHandler())
        .and()
            .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/main", false)
                .failureUrl("/login?error=true")
//                .failureHandler(authenticationFailureHandler())
        .and()
            .rememberMe()
                .rememberMeParameter("saveuser")
                .key("uniqueAndSecret")
                .tokenValiditySeconds(3600)
        .and()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/main")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
        .and()
            .csrf()
                .disable();
        return http.build();
    }

//    @Bean
//    public AccessDeniedHandler accessDeniedHandler(){
//        return new CustomAccessDeniedHandler();
//    }

}
