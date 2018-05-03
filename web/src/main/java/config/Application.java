package config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
//import org.springframework.boot.builder.SpringApplicationBuilder;
//import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by ilya on 27.01.2018.
 */
//extends SpringBootServletInitializer
@Configuration
@EnableAutoConfiguration
@ComponentScan(value = {"config","controllers","services"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return builder.sources(Application.class);
//    }

    @Bean
    protected MyErrorViewResolver getIt(){
        return new MyErrorViewResolver();
    }

    class MyErrorViewResolver implements ErrorViewResolver {

        public ModelAndView resolveErrorView(HttpServletRequest request,
                                             HttpStatus status, Map<String, Object> model) {
            // Use the request or status to optionally return a ModelAndView
            return status.equals(HttpStatus.NOT_FOUND) ? new ModelAndView("forward:/") : null;
        }

    }


}
