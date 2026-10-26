package com.slidingpenguins.model.hazard;

import com.slidingpenguins.enums.Direction;
import com.slidingpenguins.interfaces.IHazard;
import com.slidingpenguins.model.Penguin;
import com.slidingpenguins.model.Position;

/**
 * Hazard: LightIceBlock
 * -Slides on ice.
 * -Starts moving when hit by a penguin or sliding hazard.
 * -Stuns the colliding penguin(skips next turn).
 */
public class LightIceBlock implements IHazard {

    private Position position;
    // We store the direction of movement after collision to process it in IcyTerrain.
    private Direction pendingSlideDirection; 

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String getNotation() {
        return "LB";
    }

    @Override
    public boolean isSlidable() {
        return true;
    }

    @Override
    public boolean isCollectable() {
        return false;
    }

    @Override
    public void handleCollision(Penguin penguin) {
        // Penguin is temporarily stunned.
        penguin.stun();
    }
    
    /**
     * Sets the direction this block should start sliding towards.
     * @param direction The direction of the impact.
     */
    public void setSlideDirection(Direction direction) {
        this.pendingSlideDirection = direction;
    }
    
    public Direction getPendingSlideDirection() {
        return pendingSlideDirection;
    }
}