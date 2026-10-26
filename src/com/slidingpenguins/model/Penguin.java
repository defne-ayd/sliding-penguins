package com.slidingpenguins.model;

import com.slidingpenguins.interfaces.ITerrainObject;
import com.slidingpenguins.enums.Direction;
import com.slidingpenguins.model.penguin.KingPenguin;
import com.slidingpenguins.model.penguin.EmperorPenguin;
import com.slidingpenguins.model.penguin.RoyalPenguin;
import com.slidingpenguins.model.penguin.RockhopperPenguin;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Abstract base class representing a Penguin on the Icy Terrain.
 * All penguins share common attributes (position, name, collected food, game status)
 * and behaviors (sliding, collecting food), but differ in their special abilities.
 * This class demonstrates inheritance, polymorphism, and the use of ArrayList for food storage.
 */
public abstract class Penguin implements ITerrainObject {
    
    protected String name;
    protected Position position;
    protected List<Food> collectedFood;
    protected boolean isRemovedFromGame;
    protected boolean hasUsedSpecialAction;
    protected int turnsToSkip;
    
    protected static final Random random = new Random();
    
    /**
     * Constructor for creating a new Penguin with a specific name.
     * @param name The penguin's identifier (e.g., "P1", "P2", "P3")
     * @throws IllegalArgumentException if name is null or empty
     */
    protected Penguin(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Penguin name cannot be null or empty");
        }
        
        this.name = name;
        this.collectedFood = new ArrayList<>();
        this.isRemovedFromGame = false;
        this.hasUsedSpecialAction = false;
        this.turnsToSkip = 0;
    }
    
    // ITerrainObject Implementation
    
    @Override
    public Position getPosition() {
        return position;
    }
    
    @Override
    public void setPosition(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        this.position = position;
    }
    
    @Override
    public String getNotation() {
        return name;
    }
    
    @Override
    public boolean isSlidable() {
        return true;
    }
    
    @Override
    public boolean isCollectable() {
        return false;
    }
    
    // Common Penguin Methods 
    
    /**
     * Adds a food item to the penguin's collected food list.
     * @param food The food item to collect
     * @throws IllegalArgumentException if food is null
     */
    public void collectFood(Food food) {
        if (food == null) {
            throw new IllegalArgumentException("Cannot collect null food");
        }
        collectedFood.add(food);
    }
    
    /**
     * Calculates the total weight of all collected food items.
     * @return The sum of weights of all food items
     */
    public int getTotalFoodWeight() {
        return collectedFood.stream()
                .mapToInt(Food::getWeight)
                .sum();
    }
    
    /**
     * Removes the lightest food item from the penguin's collection.
     * Used as a penalty when colliding with HeavyIceBlock.
     * @return The removed food item, or null if no food was collected
     */
    public Food removeLightestFood() {
        if (collectedFood.isEmpty()) {
            return null;
        }
        
        Food lightest = collectedFood.get(0);
        for (Food food : collectedFood) {
            if (food.getWeight() < lightest.getWeight()) {
                lightest = food;
            }
        }
        
        collectedFood.remove(lightest);
        return lightest;
    }
    
    /**
     * Marks this penguin as removed from the game (fallen into water/hole).
     * When removed, the penguin's remaining turns are skipped.
     */
    public void removeFromGame() {
        this.isRemovedFromGame = true;
    }
    
    /**
     * Checks if this penguin has been removed from the game.
     * @return true if the penguin has fallen into water or a hole
     */
    public boolean isRemovedFromGame() {
        return isRemovedFromGame;
    }
    
    /**
     * Stuns the penguin, causing them to skip their next turn.
     * Used when a penguin collides with a LightIceBlock.
     */
    public void stun() {
        turnsToSkip = 1;
    }
    
    /**
     * Checks if the penguin should skip their current turn.
     * @return true if the penguin has turns to skip
     */
    public boolean shouldSkipTurn() {
        return turnsToSkip > 0;
    }
    
    /**
     * Decrements the number of turns to skip.
     * Should be called at the start of each turn.
     */
    public void processTurnSkip() {
        if (turnsToSkip > 0) {
            turnsToSkip--;
        }
    }
    
    /**
     * Checks if the penguin has already used their special action.
     * @return true if the special action has been used
     */
    public boolean hasUsedSpecialAction() {
        return hasUsedSpecialAction;
    }
    
    /**
     * Marks the special action as used.
     */
    protected void markSpecialActionUsed() {
        this.hasUsedSpecialAction = true;
    }
    
    // Getters
    
    public String getName() {
        return name;
    }
    
    public List<Food> getCollectedFood() {
        // Return a defensive copy to prevent external modification
        return new ArrayList<>(collectedFood);
    }
    
    // Abstract Methods
    
    /**
     * Returns the type name of this penguin (e.g., "King Penguin", "Emperor Penguin").
     * Must be implemented by each concrete subclass.
     * @return The type name as a string
     */
    public abstract String getTypeName();
    
    /**
     * Executes the penguin's special ability.
     * Each penguin type has a unique special action that can be used once per game.
     * This method should handle the logic for the special action and mark it as used.
     * 
     * @param direction The direction the penguin intends to move
     * @return A message describing the action taken, or null if action wasn't applicable
     */
    public abstract String useSpecialAction(Direction direction);
    
    // Factory Method
    
    /**
     * Generates a random penguin from one of the four types with equal probability.
     * @param name The name to assign to the penguin (e.g., "P1", "P2", "P3")
     * @return A randomly generated penguin instance
     * @throws IllegalArgumentException if name is null or empty
     */
    public static Penguin generateRandomPenguin(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Penguin name cannot be null or empty");
        }
        
        int penguinType = random.nextInt(4);
        
        switch (penguinType) {
            case 0:
                return new KingPenguin(name);
            case 1:
                return new EmperorPenguin(name);
            case 2:
                return new RoyalPenguin(name);
            case 3:
                return new RockhopperPenguin(name);
            default:
                // This should never happen
                throw new IllegalStateException("Unexpected penguin type: " + penguinType);
        }
    }
    
    /**
     * Generates the initial set of 3 penguins with names P1, P2, P3.
     * Each penguin is randomly assigned a type.
     * @return A list containing 3 randomly generated penguins
     */
    public static List<Penguin> generate3Penguins() {
        List<Penguin> penguins = new ArrayList<>(3);
        
        for (int i = 1; i <= 3; i++) {
            penguins.add(generateRandomPenguin("P" + i));
        }
        
        return penguins;
    }
    
    // Utility Methods
    
    @Override
    public String toString() {
        return name + " (" + getTypeName() + ")";
    }
    
    /**
     * Returns a detailed string representation of the penguin's status.
     * Includes collected food items and their weights.
     * @return A formatted string with penguin details
     */
    public String getDetailedStatus() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" (").append(getTypeName()).append(")\n");
        sb.append("  Position: ").append(position != null ? position : "Not placed").append("\n");
        sb.append("  Removed: ").append(isRemovedFromGame).append("\n");
        sb.append("  Special Action Used: ").append(hasUsedSpecialAction).append("\n");
        sb.append("  Total Food Weight: ").append(getTotalFoodWeight()).append(" units\n");
        sb.append("  Collected Food: ");
        
        if (collectedFood.isEmpty()) {
            sb.append("None");
        } else {
            for (int i = 0; i < collectedFood.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(collectedFood.get(i));
            }
        }
        
        return sb.toString();
    }
}
