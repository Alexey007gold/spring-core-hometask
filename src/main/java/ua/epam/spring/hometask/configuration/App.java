package ua.epam.spring.hometask.configuration;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.epam.spring.hometask.service.interf.DiscountService;

/**
 * Created by Oleksii_Kovetskyi on 4/6/2018.
 */
public class App {

    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfiguration.class);
        ctx.getBean(DiscountService.class);
    }
}
