package config;

import dao.ClientRepositoryImpl;
import dao.ItemRepository;
import dao.ItemRepositoryImpl;
import dao.OrderRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.Transactional;

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
    private ClientRepositoryImpl clientRepository;

    @Autowired
    private OrderRepositoryImpl orderRepository;

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
    @Transactional
    public  void init(){
        ItemRepository calc = context.getBean(ItemRepository.class);
        System.out.print(calc.getAll());
//        Client client = clientRepository.getByEmail("q@q");
//        Bucket v = new Bucket();
//        v.setClient(client);
//        v.setItem(repository.getItem(10008));
//        v.setQuantity(3);
//        orderRepository.saveB(v);
    }
}
