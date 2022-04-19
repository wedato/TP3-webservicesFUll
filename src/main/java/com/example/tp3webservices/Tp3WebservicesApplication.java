package com.example.tp3webservices;

import com.example.tp3webservices.facade.FacadeApplication;
import com.example.tp3webservices.facade.FacadeUtilisateurs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Tp3WebservicesApplication {
    private static final Logger logger = LoggerFactory.getLogger(Tp3WebservicesApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(Tp3WebservicesApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(FacadeUtilisateurs facadeUtilisateurs, FacadeApplication facadeApplication) {
        return args -> {
            int id = facadeUtilisateurs.inscrireUtilisateur("yohan.boichut@univ-orleans.fr",
                    "monMotDePasse");
            facadeUtilisateurs.inscrireUtilisateur("gerard.menvussaa@etu.univ-orleans.fr",
                    "sonMotDePasse");
            facadeApplication.ajouterUneQuestion(id, "Quel est le meilleur design pattern ?");
        };
    }
}
