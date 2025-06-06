package com.ccc.jo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableJpaRepositories("com.ccc.jo.*")
@ComponentScan(basePackages = { "com.ccc.jo.*"})
@EntityScan("com.ccc.jo.*")
public class JOApplication {

  public static void main(String[] args) {
    SpringApplication.run(JOApplication.class, args);
  }

}