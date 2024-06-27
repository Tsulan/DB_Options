package org.example;

import org.example.Entity.User;
import org.example.Service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner demo(UserService userService) {
        return (args) -> {
            System.out.println("\nShow All Users using Spring JPA:");
            userService.getAllUsers().forEach(System.out::println);

            // Add user
            User newUser = new User();
            newUser.setName("AliceJPA");
            newUser.setEmail("alice.jpa@example.com");
            userService.addUser(newUser);

            System.out.println("\nAll users after ADD:");
            userService.getAllUsers().forEach(System.out::println);

            // Update user
            newUser.setName("Alice Johnson JPA UPDATE");
            newUser.setEmail("alice.johnson@example.com");
            userService.updateUser(newUser);

            System.out.println("\nAll users after UPDATE:");
            userService.getAllUsers().forEach(System.out::println);

            // Delete user
            userService.deleteUser(newUser.getId());
            System.out.println("\nAll users after DELETE:");
            userService.getAllUsers().forEach(System.out::println);
        };
    }
}
