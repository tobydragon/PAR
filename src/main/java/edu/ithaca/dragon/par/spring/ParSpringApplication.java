package edu.ithaca.dragon.par.spring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class ParSpringApplication {

    public static void main(String[] args){
        SpringApplication.run(ParSpringApplication.class, args);
    }
}
