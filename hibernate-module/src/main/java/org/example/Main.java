package org.example;

import org.example.Config.AppConfig;
import org.example.Entity.User;
import org.example.Service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = context.getBean(UserService.class);

        System.out.println("\nShow All Users using Hibernate:");
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            System.out.println(user);
        }

        // Add user
        User newUser = new User();
        newUser.setName("AliceHibernate");
        newUser.setEmail("alice.hibernate@example.com");
        userService.addUser(newUser);

        System.out.println("\nAll users after ADD:");
        users = userService.getAllUsers();
        for (User user : users) {
            System.out.println(user);
        }

        // Update user
        User existingUser = users.get(0);
        existingUser.setName("Alice Johnson Hibernate UPDATE");
        existingUser.setEmail("alice.johnson@example.com");
        userService.updateUser(existingUser);

        System.out.println("\nAll users after UPDATE:");
        users = userService.getAllUsers();
        for (User user : users) {
            System.out.println(user);
        }

        // Delete user
        userService.deleteUser(existingUser.getId());

        System.out.println("\nAll users after DELETE:");
        users = userService.getAllUsers();
        for (User user : users) {
            System.out.println(user);
        }
    }
}
