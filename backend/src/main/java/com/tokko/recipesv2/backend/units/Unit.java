package com.tokko.recipesv2.backend.units;

public class Unit {
    public static final String G = "g";
    public static final String KG = "kg";
    public static final String ML = "krm";
    public static final String DL = "DL";
    public static final String TEASPOON = "tsk";
    public static final String TBLSPOON = "msk";

    private String unit;
    private String up;
    private String down;
    private double factor;

    public Unit(String unit, String up, String down, double factor) {
        this.unit = unit;
        this.up = up;
        this.down = down;
        this.factor = factor;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUp() {
        return up;
    }

    public void setUp(String up) {
        this.up = up;
    }

    public String getDown() {
        return down;
    }

    public void setDown(String down) {
        this.down = down;
    }

    public double getFactor() {
        return factor;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }
}
