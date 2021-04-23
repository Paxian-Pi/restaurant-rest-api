package com.paxian.controller;

import com.paxian.db.ConfirmLoginRepo;
import com.paxian.db.DataRepository;
import com.paxian.model.UserModel;
import com.paxian.payload.MessageResponse;
import com.paxian.security.JwtConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PublicApiController {

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    public DataRepository dataRepository;

    @Autowired
    public ConfirmLoginRepo confirmLoginRepo;

    @GetMapping("/home")
    public List<UserModel> getHome() {
        return this.dataRepository.findAll();
    }

    @GetMapping("/riders")
    public List<UserModel> getRiders() {
        return this.dataRepository.findAll();
    }

    @GetMapping("/team")
    public List<UserModel> getTeam() {
        return this.dataRepository.findAll();
    }

    @GetMapping("/user/data")
    public List<UserModel> getUserData() {
        return this.dataRepository.findAll();
    }

    @PostMapping("/team/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createTeam(@RequestBody UserModel userModel) {

        userModel.setUsername(userModel.getUsername());
        userModel.setEmail(userModel.getEmail());
        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        userModel.setLoggedIn(userModel.isLoggedIn());
        userModel.setPhone(userModel.getPhone());
        userModel.setRoles(userModel.getRoles());
        userModel.setPermissions(userModel.getPermissions());

        this.dataRepository.save(userModel);
        return new ResponseEntity<>(userModel, HttpStatus.CREATED);
    }

    @DeleteMapping("/team/delete/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserModel> delete(@PathVariable("id") UserModel id) {
        this.dataRepository.delete(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PostMapping("/rider/validate")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> validateRider(@RequestBody UserModel userModel) {
        this.dataRepository.save(userModel);
        return new ResponseEntity<>("Rider validated!", HttpStatus.CREATED);
    }

    @PostMapping("/rider/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createRider(@RequestBody UserModel userModel) {
        this.dataRepository.save(userModel);
        return new ResponseEntity<>("Rider validated!", HttpStatus.CREATED);
    }
}
