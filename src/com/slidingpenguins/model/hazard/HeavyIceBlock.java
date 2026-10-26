package com.slidingpenguins.model.hazard;

import com.slidingpenguins.interfaces.IHazard;
import com.slidingpenguins.model.Penguin;
import com.slidingpenguins.model.Position;

/**
 * Hazard: HeavyIceBlock
 * -Cannot move.
 * -Stops anything that collides with it.
 * -Penalty: Colliding penguin loses their lightest food item.
 */
public class HeavyIceBlock implements IHazard {

    private Position position;

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
        return "HB";
    }

    @Override
    public boolean isSlidable() {
        return false;
    }

    @Override
    public boolean isCollectable() {
        return false;
    }

    @Override
    public void handleCollision(Penguin penguin) {
        // Penguin loses lightest food item.
        penguin.removeLightestFood();
        // The logic of "dropping" or removing the food from the game is handled 
        // by the Penguin class (it removes it from the list).
    }
}