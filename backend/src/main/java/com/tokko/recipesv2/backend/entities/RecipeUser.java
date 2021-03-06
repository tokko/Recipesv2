package com.tokko.recipesv2.backend.entities;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Entity
public class RecipeUser {
    @Id
    public String email;
    private List<String> registrationIds = new ArrayList<String>();

    public RecipeUser(String email) {
        this.email = email;
    }

    public RecipeUser() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRegistrationIds() {
        return registrationIds;
    }

    public void setRegistrationIds(List<String> registrationIds) {
        this.registrationIds = registrationIds;
    }
}
