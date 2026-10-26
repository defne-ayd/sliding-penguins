package com.slidingpenguins.interfaces;

import com.slidingpenguins.model.Position;

/**
 * Common interface for all objects that exist on the Icy Terrain.
 * This includes Penguins, Food items, and Hazards.
 * Using this interface allows for polymorphism when managing the grid.
 */
public interface ITerrainObject {

    /**
     * Gets the current position of the object on the grid.
     * @return The Position object.
     */
    public Position getPosition();

    /**
     * Updates the position of the object.
     * @param position The new position.
     */
    public void setPosition(Position position);

    /**
     * Returns the 2-character string notation used in the console display.
     * @return A string like "P1", "Kr", "LB".
     */
    public String getNotation();

    /**
     * Determines if this object slides on ice.
     * Penguins and some hazards (LightIceBlock, SeaLion) slide.
     * Food, HeavyIceBlocks, and Holes do not.
     * @return true if the object slides.
     */
    public boolean isSlidable();

    /**
     * Determines if this object can be picked up by a penguin.
     * @return true for Food, false for others.
     */
    public boolean isCollectable();

}
