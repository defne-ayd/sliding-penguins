package com.slidingpenguins.enums;

/**
 * Represents the four cardinal directions of movement.
 * This enum helps calculate target positions on the 2D grid.
 * Grid Logic:
 * - Row increases going DOWN.
 * - Column increases going RIGHT.
 */
public enum Direction {
    UP(-1, 0), 
    DOWN(1, 0), 
    LEFT(0, -1), 
    RIGHT(0, 1);

    private int deltaRow;
    private int deltaColumn;

    private Direction(int deltaRow, int deltaColumn) {
        this.deltaRow = deltaRow;
        this.deltaColumn = deltaColumn;
    }

    public int getDeltaRow() {
        return deltaRow;
    }

    public int getDeltaColumn() {
        return deltaColumn;
    }

    @Override
    public String toString() {
        return "row " + getDeltaRow() + ", column " + getDeltaColumn();
    }
    
}