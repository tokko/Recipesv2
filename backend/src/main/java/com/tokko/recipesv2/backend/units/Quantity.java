package com.tokko.recipesv2.backend.units;

public class Quantity implements Comparable<Quantity> {

    private double quantity;

    public Quantity() {
    }

    public Quantity(Number quantity, Unit unit) {
        setQuantity(quantity, unit);
    }

    public Number getQuantity() {
        return quantity;
    }

    public void setQuantity(Number quantity, Unit unit) {
        this.quantity = unit.convertToBase(quantity).doubleValue();
    }

    public Quantity add(Quantity quantity) {
        this.quantity += quantity.getQuantity().doubleValue();
        return this;
    }

    @Override
    public int compareTo(Quantity o) {
        return (int) (quantity - o.getQuantity().doubleValue());
    }

    @Override
    public String toString() {
        Unit upScaled = new Grams().upscale();
        double newQuality = upScaled.convertToThis(quantity).doubleValue();
        return newQuality + upScaled.getSuffix();
    }
}
