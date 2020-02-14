package com.clphub.clpapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {



    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.csrf().ignoringAntMatchers("/h2console/**");
        http.headers().frameOptions().sameOrigin();

        http
                .authorizeRequests()
                .antMatchers("/loginError","/registration").permitAll()
                .antMatchers("/users").hasAnyRole("ADMIN", "USER")
                .antMatchers("/getuserbyid/**").permitAll()/*hasRole("ADMIN")*/
                .antMatchers("/user-delete/**").permitAll()/*hasRole("ADMIN")*/
                .antMatchers("/swagger-ui.html/**").permitAll()/*hasRole("ADMIN")*/
                .antMatchers("/edit/**").permitAll()/*hasRole("ADMIN")*/
                .antMatchers("/delete/**").permitAll()/*hasRole("ADMIN")*/
                .antMatchers("/createuser").permitAll()/*hasRole("ADMIN")*/
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(new CustomAuthenticationSuccess())
                .failureHandler(new CustomAuthenticationFailure())
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/403");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }
}

