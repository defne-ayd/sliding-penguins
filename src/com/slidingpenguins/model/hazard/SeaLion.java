package com.slidingpenguins.model.hazard;

import com.slidingpenguins.enums.Direction;
import com.slidingpenguins.interfaces.IHazard;
import com.slidingpenguins.model.Penguin;
import com.slidingpenguins.model.Position;

/**
 * Hazard: SeaLion
 * -Slides on ice.
 * -Bounces penguins: Penguin starts sliding in opposite direction.
 * -SeaLion starts sliding in the penguin's initial direction.
 */
public class SeaLion implements IHazard {

    private Position position;
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
        return "SL";
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
        // The bounce logic involves changing directions, which is heavily tied to
        // the movement loop in IcyTerrain. This method marks the interaction.
    }

    public void setSlideDirection(Direction direction) {
        this.pendingSlideDirection = direction;
    }

    public Direction getPendingSlideDirection() {
        return pendingSlideDirection;
    }
}