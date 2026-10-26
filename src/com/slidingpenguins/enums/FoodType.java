package com.slidingpenguins.enums;

/**
 * Enumeration representing the 5 specific types of food.
 * Includes the string notation for grid display.
 */
public enum FoodType {
    KRILL("Kr"),
    CRUSTACEAN("Cr"),
    ANCHOVY("An"), 
    SQUID("Sq"), 
    MACKEREL("Ma");

    private String notation;

    private FoodType(String notation) {
        this.notation = notation;
    }

    /**
     * Gets the shorthand notation.
     * @return String notation (e.g., "Kr", "Cr").
     */
    public String getNotation() {
        return notation;
    }

    @Override
    public String toString() {
        return notation;
    }
}