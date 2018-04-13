package config;

import dao.ItemRepository;
import dao.ItemRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;

/**
 * Created by ilya on 28.01.2018.
 */
@Configuration
@SpringBootApplication(scanBasePackages = "dao")
@ComponentScan(basePackages = {"dao","model"})
@EnableAutoConfiguration
@EntityScan(basePackages = "model")
@PropertySource(value={"classpath:persistence.properties"})
public class AppConfig {

    @Autowired
    private  ItemRepositoryImpl repository;

    @Autowired
    private  ApplicationContext context;

//    @Autowired
//    private ItemRepository getRepository(){
//       return repository;
//    }

    public static void main(String[] args) {
        SpringApplication.run(AppConfig.class, args);
    }

    @PostConstruct
    public  void init(){
        ItemRepository calc = context.getBean(ItemRepository.class);
        System.out.print(calc.getAll());
    }
}
