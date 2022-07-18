package project.userFeaturePortal.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import project.userFeaturePortal.security.service.UserDetailsService;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService();
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return null;
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return false;
            }
        };
    };

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        // {noop} => No operation for password encoder	(no password encoding needed)
        //auth.inMemoryAuthentication().withUser("devs").password("{noop}Test").authorities("ADMIN");
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.GET).hasRole("ADMIN").anyRequest().authenticated().and().httpBasic();
    }
}