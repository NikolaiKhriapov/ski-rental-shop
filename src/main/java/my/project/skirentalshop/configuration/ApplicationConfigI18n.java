package my.project.skirentalshop.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class ApplicationConfigI18n implements WebMvcConfigurer {

    //    ----- default locale of the application -----
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
        sessionLocaleResolver.setDefaultLocale(Locale.US);
        return sessionLocaleResolver;
    }

    //    ----- change to a different locale based on the value of the language parameter added to the request -----
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    //    ----- add the LocaleChangeInterceptor into the application's registry interceptor -----
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    //    ----- add ResourceBundle directories (for localization) -----
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames(
                "classpath:/authentication",
                "classpath:/navigation",
                "classpath:/user_admin",
                "classpath:/user_client",
                "classpath:/booking",
                "classpath:/client",
                "classpath:/rider",
                "classpath:/equipment",
                "classpath:/validation",
                "classpath:/error"
        );
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
