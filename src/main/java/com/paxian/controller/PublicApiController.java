package com.paxian.controller;

import com.paxian.db.ConfirmLoginRepo;
import com.paxian.db.DataRepository;
import com.paxian.db.TokenRepository;
import com.paxian.model.Token;
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
    public TokenRepository tokenRepository;

    @Autowired
    public ConfirmLoginRepo confirmLoginRepo;

    @GetMapping("/team")
    public List<UserModel> getTeam() {
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
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserModel> delete(@PathVariable("id") UserModel id) {
        this.dataRepository.delete(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @GetMapping("/token")
    public List<Token> getToken() {
        return this.tokenRepository.findAll();
    }

    @PostMapping("/token")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> postToken(@RequestBody Token tokenData) {

        tokenData.setToken(tokenData.getToken());

        this.tokenRepository.save(tokenData);
        return new ResponseEntity<>(tokenData, HttpStatus.CREATED);
    }

    @DeleteMapping("/token/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Token> deleteToken(@PathVariable("id") Token id) {
        this.tokenRepository.delete(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
