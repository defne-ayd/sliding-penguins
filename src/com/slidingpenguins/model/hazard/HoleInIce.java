package com.slidingpenguins.model.hazard;

import com.slidingpenguins.interfaces.IHazard;
import com.slidingpenguins.model.Penguin;
import com.slidingpenguins.model.Position;

/**
 * Hazard: HoleInIce
 * -Cannot move.
 * -Objects falling into it are removed.
 * -Can be plugged by sliding hazards.
 */
public class HoleInIce implements IHazard {

    private Position position;
    private boolean isPlugged;

    public HoleInIce() {
        this.isPlugged = false;
    }

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
        return isPlugged ? "PH" : "HI";
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
        if (!isPlugged) {
            penguin.removeFromGame();
        }
    }

    // Plugs the hole. Used when a sliding object falls in.
    public void plug() {
        this.isPlugged = true;
    }

    public boolean isPlugged() {
        return isPlugged;
    }
}