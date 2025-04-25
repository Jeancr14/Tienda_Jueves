package com.tienda;

import com.tienda.domain.Ruta;
import com.tienda.service.RutaPermitService;
import com.tienda.service.RutaService;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Configuration
public class ProjectConfig implements WebMvcConfigurer{

    
    @Bean
    public LocaleResolver localeResolver(){
        var slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.getDefault());
        slr.setLocaleAttributeName("session.current.locale");
        slr.setTimeZoneAttributeName("session.current.timezone");
        return slr;
    }
    
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor(){
        var lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registro){
        registro.addInterceptor(localeChangeInterceptor());
    }
    
    
    /* El siguiente método se utilizar para publicar en la nube, independientemente  */
    
    @Bean
    public SpringResourceTemplateResolver templateResolver_0() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setPrefix("classpath:/templates");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setOrder(0);
        resolver.setCheckExistence(true);
        return resolver;
    }
    
    
    // Método para editar el uso de un controller y que pase directo
    
    @Override
    public void addViewControllers(ViewControllerRegistry registro){
        registro.addViewController("/").setViewName("index");
        registro.addViewController("/login").setViewName("login");
        registro.addViewController("/index").setViewName("index");
        registro.addViewController("/registro/nuevo").setViewName("/registro/nuevo");
    }
    
    
    @Autowired
    private RutaPermitService rutaPermitService;
    @Autowired
    private RutaService rutaService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        String[] rutasPermit = rutaPermitService.getRutaPermitsString();
        List<Ruta> rutas = rutaService.getRutas();

        http.authorizeHttpRequests((request) -> {
            request
                    .requestMatchers(rutasPermit).permitAll();
            for (Ruta ruta : rutas) {
                System.out.println(ruta.getPatron()+":"+ruta.getRolName());
                request
                        .requestMatchers(ruta.getPatron())
                        .hasRole(ruta.getRolName());
            }
        }
        )
                .formLogin((form) -> form
                .loginPage("/login").permitAll())
                .logout((logout) -> logout.permitAll());
        return http.build();
    }
    
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configurerGlobal(AuthenticationManagerBuilder build) throws Exception {
        build.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }
    
    
}