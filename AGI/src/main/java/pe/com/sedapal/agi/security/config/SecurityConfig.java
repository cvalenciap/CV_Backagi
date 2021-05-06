package pe.com.sedapal.agi.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.encoding.PlaintextPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import pe.com.sedapal.agi.security.filters.TokenFilter;
import pe.com.sedapal.agi.util.RedisUtil;
import pe.com.sedapal.seguridad.ws.SeguridadClienteWs;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public PlaintextPasswordEncoder passwordEncoder(){
		PlaintextPasswordEncoder encoder = new PlaintextPasswordEncoder();
		return encoder;
	}
	
	@Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
	
	@Autowired
	private UserDetailsService customUserDetailsService;
	
	@Autowired
	private UserAjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;

	@Autowired
	private RedisUtil redis;

	@Autowired
	private SeguridadClienteWs stub;
	
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().authorizeRequests()
		.antMatchers("/auth/**").permitAll()
		.anyRequest().authenticated()
		.and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.logout().logoutUrl("/auth/logout").invalidateHttpSession(true).logoutSuccessHandler(ajaxLogoutSuccessHandler).clearAuthentication(true)
		.deleteCookies("JSESSIONID")
		.and()
		.addFilterBefore(new TokenFilter(redis, stub), UsernamePasswordAuthenticationFilter.class);
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
	}
}
