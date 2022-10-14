package my.project.skirentalshop.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.ui.context.ThemeSource;
import org.springframework.ui.context.support.ResourceBundleThemeSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.theme.CookieThemeResolver;
import org.springframework.web.servlet.theme.SessionThemeResolver;
import org.springframework.web.servlet.theme.ThemeChangeInterceptor;

import java.util.Locale;

@Configuration
public class ApplicationConfigTheme implements WebMvcConfigurer {

    //    ----- default locale of the application -----
    @Bean
    public ThemeResolver themeResolver() {
        SessionThemeResolver sessionThemeResolver = new SessionThemeResolver();
        sessionThemeResolver.setDefaultThemeName("theme_2");
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