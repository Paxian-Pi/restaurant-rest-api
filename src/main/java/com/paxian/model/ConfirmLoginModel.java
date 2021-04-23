package com.paxian.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "confirm-login-db")
public class ConfirmLoginModel {

    @Id
    private String id;
    private boolean confirmation;

    public ConfirmLoginModel(boolean confirmation) {
        this.confirmation = confirmation;
    }

    public ConfirmLoginModel() { }

    public String getId() {
        return id;
    }

    public boolean isConfirmation() {
        return confirmation;
    }
}
