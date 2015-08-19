package com.tokko.recipesv2.backend.units;

/**
 * Created by andre_000 on 2015-05-31.
 */
public class HectoGram extends Unit {
    @Override
    public Number convertToBase(Number quantity) {
        return quantity.doubleValue() * 100;
    }

    @Override
    public Number convertToThis(Number quantity) {
        return quantity.doubleValue() / 100;
    }

    @Override
    public Unit upscale() {
        return null;
    }

    @Override
    public String getSuffix() {
        return "hg";
    }
}
