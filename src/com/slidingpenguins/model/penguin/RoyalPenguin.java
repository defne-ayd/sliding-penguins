package com.slidingpenguins.model.penguin;

import com.slidingpenguins.model.Penguin;
import com.slidingpenguins.enums.Direction;

/**
 * Special Ability: Before sliding, can choose to safely move into an adjacent square
 * (only horizontally and vertically).
 * - It's possible to accidentally step out of the grid and fall into water.
 * - The ability can only be used once per game.
 * - This demonstrates inheritance and polymorphism through method overriding.
 */
public class RoyalPenguin extends Penguin {
    
    private boolean willPerformSingleStep;
    private Direction singleStepDirection;
    
    /**
     * Constructor for Royal Penguin.
     * @param name The penguin's identifier (e.g., "P1", "P2", "P3")
     * @throws IllegalArgumentException if name is null or empty
     */
    public RoyalPenguin(String name) {
        super(name);
        this.willPerformSingleStep = false;
        this.singleStepDirection = null;
    }
    
    @Override
    public String getTypeName() {
        return "Royal Penguin";
    }
    
    /**
     * Uses the Royal Penguin's special ability to move one square before sliding.
     * The penguin moves one square in the given direction, then will slide normally.
     * 
     * This method marks the action as used and sets internal flags for IcyTerrain
     * to process the single step before the normal slide.
     * 
     * @param direction The direction for the single-step movement (can be different from slide direction)
     * @return A message confirming the special action will be applied
     */
    @Override
    public String useSpecialAction(Direction direction) {
        if (hasUsedSpecialAction) {
            return null; // Already used
        }
        
        if (direction == null) {
            throw new IllegalArgumentException("Direction cannot be null for Royal Penguin's special action");
        }
        
        markSpecialActionUsed();
        this.willPerformSingleStep = true;
        this.singleStepDirection = direction;
        
        return "moves one square to the " + getDirectionName(direction) + ".";
    }
    
    /**
     * Checks if the Royal Penguin will perform a single step before sliding.
     * Used by IcyTerrain to determine if pre-slide movement is needed.
     * @return true if a single step should be performed
     */
    public boolean willPerformSingleStep() {
        return willPerformSingleStep;
    }
    
    /**
     * Gets the direction for the single step movement.
     * @return The direction for the single step, or null if not applicable
     */
    public Direction getSingleStepDirection() {
        return singleStepDirection;
    }
    
    /**
     * Resets the single step flags after the step has been performed.
     * Should be called by IcyTerrain after processing the single step.
     */
    public void resetSingleStep() {
        this.willPerformSingleStep = false;
        this.singleStepDirection = null;
    }
    
    /**
     * Helper method to convert Direction enum to readable text.
     * @param direction The direction to convert
     * @return A readable string (e.g., "RIGHT", "UPWARDS")
     */
    private String getDirectionName(Direction direction) {
        switch (direction) {
            case UP:
                return "UPWARDS";
            case DOWN:
                return "DOWNWARDS";
            case LEFT:
                return "LEFT";
            case RIGHT:
                return "RIGHT";
            default:
                return direction.name();
        }
    }
    
    @Override
    public String toString() {
        return super.toString();
    }
}
