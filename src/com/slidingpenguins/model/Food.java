package com.slidingpenguins.model;

import com.slidingpenguins.interfaces.ITerrainObject;
import com.slidingpenguins.enums.FoodType;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents a Food item on the Icy Terrain.
 * Food items are collectable objects with a specific type and weight.
 * There are 5 types of food and weights range from 1 to 5 units.
 */
public class Food implements ITerrainObject{
    
    private final FoodType type;
    private final int weight;
    private Position position;

    private static final Random random = new Random();

    /**
     * Constructs a new Food object with random properties.
     * 1. Randomly selects one of 5 FoodTypes.
     * 2. Randomly assigns a weight between 1 and 5.
     * 3. Type and Weight are independent.
     */
    public Food() {
        this.type = FoodType.values()[random.nextInt(FoodType.values().length)];
        this.weight = random.nextInt(5) + 1;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Returns the notation for the grid display
     */
    @Override
    public String getNotation() {
        return type.getNotation();
    }

    @Override
    public boolean isSlidable() {
        return false;
    }

    @Override
    public boolean isCollectable() {
        return true;
    }

    /** Used to calculate the winner at the end of the game.
     */
    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return type.name() + " (Weight=" + weight + " units)";
    }

    /**
     * Helper method to generate the initial set of 20 food items.
     * @return A list containing 20 randomly generated Food objects.
     */
    public static List<Food> generate20Foods() {
        List<Food> foods = new ArrayList<>(20);

        for(int i = 0; i < 20; i++) {
            foods.add(new Food());
        }

        return foods;
    }
}
