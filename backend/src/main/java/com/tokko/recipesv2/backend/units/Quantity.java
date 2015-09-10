package com.tokko.recipesv2.backend.units;

public class Quantity {

    private double quantity;
    private String unit;

    public Quantity(double quantity) {
        this.quantity = quantity;
    }

    public Quantity(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
}
