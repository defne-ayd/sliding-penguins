package com.slidingpenguins.interfaces;

import com.slidingpenguins.model.Penguin;

/**
 * Interface representing a Hazard on the Icy Terrain.
 * Hazards can affect penguins upon collision.
 */
public interface IHazard extends ITerrainObject {
    
    /**
     * Handles the collision event when a penguin hits this hazard.
     * @param penguin The penguin colliding with the hazard.
     */
    void handleCollision(Penguin penguin);
}
