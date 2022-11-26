package MS_Quokka.spring;

import MS_Quokka.Utils.Role;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() //uncomment to disable csrf
                .authorizeRequests()
                .antMatchers("/viewAllUsers.jsp").hasRole(Role.ADMIN.toString())
                .antMatchers("/admin").hasRole(Role.ADMIN.toString())
                .antMatchers("/userProfile.jsp").hasRole(Role.USER.toString())
                .antMatchers("/user").access("hasRole('ROLE_USER') or hasRole('ROLE_SELLER')")
                .antMatchers("/sellerGroup.jsp").hasRole(Role.ADMIN.toString())
                .antMatchers("/sellerGroup").hasRole(Role.ADMIN.toString())
                .antMatchers("/listing").authenticated()
                .antMatchers("/purchases").authenticated()
                .antMatchers("/purchases.jsp").authenticated()
                .antMatchers("/adminDashboard.jsp").hasRole(Role.ADMIN.toString())
                .and()
                .formLogin()
                .loginPage("/login.jsp")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login.jsp?alertMsg=error")
                .and()
                .logout()
                .logoutSuccessUrl("/login.jsp");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder();
        encoder.setEncodeHashAsBase64(false);
        auth.userDetailsService(new UserDetailServiceImpl()).passwordEncoder(encoder);
    }
}

