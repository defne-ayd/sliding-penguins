package com.slidingpenguins.model.penguin;

import com.slidingpenguins.model.Penguin;
import com.slidingpenguins.enums.Direction;

/**
 * Special Ability: Before sliding, can prepare to jump over ONE hazard in their path.
 * - The ability is not used automatically and can be wasted if there are no hazards.
 * - Can only jump to an empty square. If the landing square is not empty, the jump fails.
 * - When the jump fails, the penguin collides with the hazard.
 * - Unlike other penguins, Rockhopper automatically uses its ability the first time
 *   it decides to move toward a hazard (for non-player penguins).
 * - This demonstrates inheritance and polymorphism through method overriding.
 */
public class RockhopperPenguin extends Penguin {
    
    private boolean isPreparedToJump;
    
    /**
     * Constructor for Rockhopper Penguin.
     * @param name The penguin's identifier (e.g., "P1", "P2", "P3")
     * @throws IllegalArgumentException if name is null or empty
     */
    public RockhopperPenguin(String name) {
        super(name);
        this.isPreparedToJump = false;
    }
    
    @Override
    public String getTypeName() {
        return "Rockhopper Penguin";
    }
    
    /**
     * Uses the Rockhopper Penguin's special ability to prepare for jumping over a hazard.
     * The penguin prepares to jump, and during sliding, if it encounters a hazard,
     * it will attempt to jump over it (if the landing square is empty).
     * 
     * This method marks the action as used and sets the prepared-to-jump flag.
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
        this.isPreparedToJump = true;
        
        return "prepares to jump over a hazard.";
    }
    
    /**
     * Checks if the Rockhopper Penguin is prepared to jump over a hazard.
     * Used by IcyTerrain during sliding to determine if a jump should be attempted.
     * @return true if the penguin is prepared to jump
     */
    public boolean isPreparedToJump() {
        return isPreparedToJump;
    }
    
    /**
     * Resets the prepared-to-jump flag after a jump attempt (successful or failed).
     * Should be called by IcyTerrain after processing the jump.
     */
    public void resetJumpPreparation() {
        this.isPreparedToJump = false;
    }
    
    /**
     * Marks that a jump was attempted (whether successful or not).
     * This ensures the ability is consumed even if the jump fails.
     */
    public void consumeJumpAttempt() {
        this.isPreparedToJump = false;
    }
    
    @Override
    public String toString() {
        return super.toString();
    }
}
