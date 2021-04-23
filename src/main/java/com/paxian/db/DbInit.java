package com.paxian.db;

import com.paxian.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class DbInit implements CommandLineRunner {

    @Autowired
    public DataRepository dataRepository;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Create users
        UserModel server = new UserModel("Server", "server.controller@paxian.solutions.com", passwordEncoder.encode("server21"), false, "08093530000", "ADMIN", "ACCESS_ALL");


        // Drop all users
        this.dataRepository.deleteAll();

        // Save all to db
        List<UserModel> users = Collections.singletonList(server);
        this.dataRepository.saveAll(users);
    }
}
