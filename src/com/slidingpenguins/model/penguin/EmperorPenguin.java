package com.slidingpenguins.model.penguin;

import com.slidingpenguins.model.Penguin;
import com.slidingpenguins.enums.Direction;

/**
 * Special Ability: When sliding, can choose to stop at the 3rd square they slide into.
 * - If the direction has fewer than 3 free squares, the ability is still considered used.
 * - The ability can only be used once per game.
 * - This demonstrates inheritance and polymorphism through method overriding.
 */
public class EmperorPenguin extends Penguin {
    
    private static final int STOP_AT_SQUARE = 3;
    
    /**
     * Constructor for Emperor Penguin.
     * @param name The penguin's identifier (e.g., "P1", "P2", "P3")
     * @throws IllegalArgumentException if name is null or empty
     */
    public EmperorPenguin(String name) {
        super(name);
    }
    
    @Override
    public String getTypeName() {
        return "Emperor Penguin";
    }
    
    /**
     * Uses the Emperor Penguin's special ability to stop at the 3rd square.
     * This method is called during the sliding process and marks the ability as used.
     * 
     * Note: The actual sliding logic is handled by IcyTerrain, which will check
     * if the penguin has used their special action and stop them at the appropriate square.
     * This method simply marks the action as used and returns a confirmation message.
     * 
     * @param direction The direction the penguin intends to slide (informational only)
     * @return A message confirming the special action will be applied
     */
    @Override
    public String useSpecialAction(Direction direction) {
        if (hasUsedSpecialAction) {
            return null; // Already used
        }
        
        markSpecialActionUsed();
        return "stops at the third square using its special action.";
    }
    
    /**
     * Returns the square number where this Emperor Penguin should stop when using special action.
     * Used by IcyTerrain during sliding simulation.
     * @return 3 (the 3rd square)
     */
    public int getStopAtSquare() {
        return STOP_AT_SQUARE;
    }
    
    @Override
    public String toString() {
        return super.toString();
    }
}
