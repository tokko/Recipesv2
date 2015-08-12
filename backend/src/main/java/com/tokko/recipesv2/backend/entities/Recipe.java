package com.tokko.recipesv2.backend.entities;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Recipe {
    @Id
    private Long id;
    private String title;
}
