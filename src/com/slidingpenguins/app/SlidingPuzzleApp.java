package com.slidingpenguins.app;

import com.slidingpenguins.logic.IcyTerrain;

public class SlidingPuzzleApp {

    public static void main(String[] args) {
        //Main method only initializes IcyTerrain
        IcyTerrain game = new IcyTerrain();
        
        game.start();
    }
}