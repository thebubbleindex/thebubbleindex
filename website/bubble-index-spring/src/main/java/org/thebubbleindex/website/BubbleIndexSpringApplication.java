package org.thebubbleindex.website;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages={"org.thebubbleindex.controller", "org.thebubbleindex.repository"})
@EntityScan("org.thebubbleindex.model")
@EnableJpaRepositories("org.thebubbleindex.repository")
public class BubbleIndexSpringApplication {

	public static void main(final String[] args) {
		SpringApplication.run(BubbleIndexSpringApplication.class, args);
	}
	
	@Bean
    public CommandLineRunner commandLineRunner(final ApplicationContext ctx) {
        return args -> {

            System.out.println("Let's inspect the beans provided by Spring Boot:");

            final String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (final String beanName : beanNames) {
                System.out.println(beanName);
            }

        };
    }
}
