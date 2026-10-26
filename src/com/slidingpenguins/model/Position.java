package com.slidingpenguins.model;

import com.slidingpenguins.enums.Direction;

/**
 * Immutable class representing a position on the 10×10 icy terrain grid.
 * Rows and columns are numbered 0–9.
 * Positions outside these bounds are used to represent "fallen into water".
 */
public final class Position {

    private final int row;
    private final int column;

    /** Size of the square grid */
    public static final int GRID_SIZE = 10;

    /**
     * Constructs a position. Allows creating positions outside the grid boundaries
     * to represent objects that have fallen into the water
     * @param row row index
     * @param column column index
     */
    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /** Copy constructor */
    public Position(Position other) {
        this.row = other.row;
        this.column = other.column;
    }

    /** Returns a new Position in the given direction (may be outside the grid) 
     * @param direction The direction of movement (UP, DOWN, LEFT, RIGHT).
     * @return A new Position object representing the target coordinate.
    */
    public Position getAdjacent(Direction direction) {
        return new Position(
            this.row + direction.getDeltaRow(),
            this.column + direction.getDeltaColumn()
        );
    }

    // Getters
    public int getRow() { 
        return row; 
    }

    public int getColumn() { 
        return column; 
    }

    // Validation 
    public static boolean isValid(int row, int col) {
        return row >= 0 && row < GRID_SIZE && col >= 0 && col < GRID_SIZE;
    }

    public boolean isOnGrid() {
        return isValid(row, column);
    }

    // Overrides
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        Position otherPosition = (Position) other;
        return row == otherPosition.row && column == otherPosition.column;
    }

    @Override
    public int hashCode() {
        return 31 * row + column;
    }

    @Override
    public String toString() {
        return "row: " + row + ", column: " + column;
    }
}