package com.slidingpenguins.logic;

import com.slidingpenguins.interfaces.ITerrainObject;
import com.slidingpenguins.model.Food;
import com.slidingpenguins.model.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Helper class to handle the random placement of Food items on the grid.
 * Ensures that food items are not placed on top of existing objects (like Hazards or Penguins).
 * Note: Food and Hazards cannot occupy the same square.
 */
public class FoodPlacementHelper {

    private static final Random random = new Random();
    private static final int GRID_SIZE = Position.GRID_SIZE;

    /**
     * Places the generated list of foods onto the grid at valid, empty locations.
     * @param foods The list of Food objects to place.
     * @param grid  The 10x10 grid representation (List of Lists).
     */
    public static void placeFoodsRandomly(List<Food> foods, ArrayList<ArrayList<ITerrainObject>> grid) {

        for(Food food: foods) {
            Position position;

            // Keep generating random positions until we find an empty spot (null)
            do {
                int row = random.nextInt(GRID_SIZE);
                int column = random.nextInt(GRID_SIZE);
                position = new Position(row, column);
            }
            while (grid.get(position.getRow()).get(position.getColumn()) != null);

            // Assign position to the food object and place it in the grid
            food.setPosition(position);
            grid.get(position.getRow()).set(position.getColumn(), food);
        }
    }
}
