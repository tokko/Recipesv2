package com.tokko.recipesv2.backend.units;


public class Grams extends Unit {

    @Override
    public Number convertToBase(Number quantity) {
        return quantity;
    }

    @Override
    public Number convertToThis(Number quantity) {
        return quantity;
    }

    @Override
    public Unit upscale() {
        return new HectoGram();
    }

    @Override
    public String getSuffix() {
        return "g";
    }
}
