package com.german.deck;

import com.german.deck.config.ApplicationConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EntityScan(basePackages = "com.german.deck.entities")
@Import({ApplicationConfig.class})
public class GermanDeckApplication {

    public static void main(String[] args) {
        SpringApplication.run(GermanDeckApplication.class, args);
    }

}
