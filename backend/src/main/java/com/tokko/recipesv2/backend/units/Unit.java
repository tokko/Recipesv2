package com.tokko.recipesv2.backend.units;

public class Unit {
    public static final String G = "g";
    public static final String KG = "kg";
    public static final String ML = "krm";
    public static final String DL = "DL";
    public static final String TEASPOON = "tsk";
    public static final String TBLSPOON = "msk";
    public static final String L = "l";

    private String unit;
    private String up;
    private String down;
    private double factorUp;
    private double factorDown;

    public Unit(String unit, String up, String down, double factorUp, double factorDown) {
        this.unit = unit;
        this.up = up;
        this.down = down;
        this.factorUp = factorUp;
        this.factorDown = factorDown;
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

    public double getFactorUp() {
        return factorUp;
    }

    public void setFactorUp(double factorUp) {
        this.factorUp = factorUp;
    }

    public double getFactorDown() {
        return factorDown;
    }

    public void setFactorDown(double factorDown) {
        this.factorDown = factorDown;
    }
}
