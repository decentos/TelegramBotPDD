package me.decentos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(Main.class, args);
    }
}
