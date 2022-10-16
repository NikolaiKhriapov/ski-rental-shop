package my.project.skirentalshop.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.theme.SessionThemeResolver;
import org.springframework.web.servlet.theme.ThemeChangeInterceptor;

@Configuration
public class ApplicationConfigTheme implements WebMvcConfigurer {

    //    ----- default locale of the application -----
    @Bean
    public ThemeResolver themeResolver() {
        SessionThemeResolver sessionThemeResolver = new SessionThemeResolver();
        sessionThemeResolver.setDefaultThemeName("theme_1");
        return sessionThemeResolver;
    }

    //    ----- change to a different theme based on the value of the parameter added to the request -----
    @Bean
    public ThemeChangeInterceptor themeChangeInterceptor() {
        ThemeChangeInterceptor themeChangeInterceptor = new ThemeChangeInterceptor();
        themeChangeInterceptor.setParamName("theme");
        return themeChangeInterceptor;
    }

    //    ----- add the ThemeChangeInterceptor into the application's registry interceptor -----
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(themeChangeInterceptor());
    }
}