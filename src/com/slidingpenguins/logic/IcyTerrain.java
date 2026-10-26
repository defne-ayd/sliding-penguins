package com.slidingpenguins.logic;

import com.slidingpenguins.enums.Direction;
import com.slidingpenguins.interfaces.IHazard;
import com.slidingpenguins.interfaces.ITerrainObject;
import com.slidingpenguins.model.Food;
import com.slidingpenguins.model.Penguin;
import com.slidingpenguins.model.Position;
import com.slidingpenguins.model.hazard.HeavyIceBlock;
import com.slidingpenguins.model.hazard.HoleInIce;
import com.slidingpenguins.model.hazard.LightIceBlock;
import com.slidingpenguins.model.hazard.SeaLion;
import com.slidingpenguins.model.penguin.EmperorPenguin;
import com.slidingpenguins.model.penguin.KingPenguin;
import com.slidingpenguins.model.penguin.RockhopperPenguin;
import com.slidingpenguins.model.penguin.RoyalPenguin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class IcyTerrain {

    private static final int GRID_SIZE = 10;
    private static final int TOTAL_TURNS = 4;
    private static final Random random = new Random();
    private static final Scanner scanner = new Scanner(System.in);

    // The Grid: A list of rows, where each row is a list of objects.
    private ArrayList<ArrayList<ITerrainObject>> grid;
    
    private List<Penguin> penguins;
    private List<IHazard> hazards;
    private List<Food> foods; // Keep track of foods for end game check if needed.

    private Penguin playerPenguin;

    public IcyTerrain() {
        initializeGrid();
        initializeGameObjects();
    }

    // Initialization

    private void initializeGrid() {
        grid = new ArrayList<>(GRID_SIZE);
        for (int i = 0; i < GRID_SIZE; i++) {
            ArrayList<ITerrainObject> row = new ArrayList<>(GRID_SIZE);
            for (int j = 0; j < GRID_SIZE; j++) {
                row.add(null);
            }
            grid.add(row);
        }
        System.out.println("Welcome to Sliding Penguins Puzzle Game App. An 10x10 icy terrain grid is being generated.");
    }

    private void initializeGameObjects() {
        // 1. Generate Penguins
        penguins = Penguin.generate3Penguins();
        
        // Place penguins on edges.
        for (Penguin p : penguins) {
            placeOnEdge(p);
        }

        // Assign one random penguin to player.
        playerPenguin = penguins.get(random.nextInt(penguins.size()));

        // 2. Generate Hazards
        hazards = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            IHazard hazard = generateRandomHazard();
            placeRandomly(hazard);
            hazards.add(hazard);
        }

        // 3. Generate Food
        foods = Food.generate20Foods();
        FoodPlacementHelper.placeFoodsRandomly(foods, grid);

        System.out.println("Penguins, Hazards, and Food items are also being generated. The initial icy terrain grid:\n");
        printGrid();
        
        // Print Penguin assignments.
        System.out.println("These are the penguins on the icy terrain:");
        for (Penguin p : penguins) {
            String suffix = (p == playerPenguin) ? " ---> YOUR PENGUIN" : "";
            System.out.println("- " + p.getName() + ": " + p.getTypeName() + suffix);
        }
    }

    private IHazard generateRandomHazard() {
        int type = random.nextInt(4); // Equal chance
        switch (type) {
            case 0: return new LightIceBlock();
            case 1: return new HeavyIceBlock();
            case 2: return new SeaLion();
            case 3: return new HoleInIce();
            default: return new HoleInIce();
        }
    }

    private void placeOnEdge(Penguin p) {
        Position pos;
        do {
            int edge = random.nextInt(4); // 0: Top, 1: Bottom, 2: Left, 3: Right
            int row = 0, col = 0;
            if (edge == 0) { row = 0; col = random.nextInt(GRID_SIZE); }
            else if (edge == 1) { row = GRID_SIZE - 1; col = random.nextInt(GRID_SIZE); }
            else if (edge == 2) { row = random.nextInt(GRID_SIZE); col = 0; }
            else { row = random.nextInt(GRID_SIZE); col = GRID_SIZE - 1; }
            
            pos = new Position(row, col);
        } while (getObjectAt(pos) != null); // Ensure it is empty

        p.setPosition(pos);
        updateGrid(p, pos);
    }

    private void placeRandomly(ITerrainObject obj) {
        Position pos;
        do {
            pos = new Position(random.nextInt(GRID_SIZE), random.nextInt(GRID_SIZE));
        } while (getObjectAt(pos) != null);
        
        obj.setPosition(pos);
        updateGrid(obj, pos);
    }

    // Game Loop

    public void start() {
        for (int turn = 1; turn <= TOTAL_TURNS; turn++) {
            // Turn order: P1, P2, P3 
            for (Penguin p : penguins) {
                // If penguin removed, skip
                if (p.isRemovedFromGame()) continue;

                System.out.println("*** Turn " + turn + " " + p.getName() + (p == playerPenguin ? " (Your Penguin)" : "") + ":");

                // Handle Stun
                if (p.shouldSkipTurn()) {
                    System.out.println(p.getName() + " is stunned and skips this turn.");
                    p.processTurnSkip();
                    continue;
                }

                if (p == playerPenguin) {
                    handlePlayerTurn(p);
                } else {
                    handleBotTurn(p);
                }
                
                System.out.println("New state of the grid:\n");
                printGrid();
            }
        }
        endGame();
    }

    private void handlePlayerTurn(Penguin p) {
        boolean usedSpecial = false;
        
        // Ask for Special Action
        if (!p.hasUsedSpecialAction()) {
            System.out.print("Will " + p.getName() + " use its special action? Answer with Y or N --> ");
            String input = getValidInput("^[yYnN]$");
            if (input.equalsIgnoreCase("Y")) {
                usedSpecial = true;
            } else {
                System.out.println(p.getName() + " does NOT use its special action.");
            }
        }

        // Ask for Direction
        System.out.print("Which direction will " + p.getName() + " move? Answer with U (Up), D (Down), L (Left), R (Right) --> ");
        String dirStr = getValidInput("^[uUdDlLrR]$");
        Direction direction = parseDirection(dirStr);

        // Execute Turn
        executeTurn(p, direction, usedSpecial);
    }

    private void handleBotTurn(Penguin p) {
        // AI Logic
        
        // 1. Determine target direction
        Direction bestDirection = chooseBotDirection(p);
        
        // 2. Decide Special Action
        boolean useSpecial = false;
        if (!p.hasUsedSpecialAction()) {
            if (p instanceof RockhopperPenguin) {
                // Rockhopper logic: If facing hazard, use it.
                if (isHazardInPath(p.getPosition(), bestDirection)) {
                    useSpecial = true;
                }
            } else {
                // 30% chance for others.
                if (random.nextInt(100) < 30) {
                    useSpecial = true;
                }
            }
        }
        
        if (useSpecial) {
            System.out.println(p.getName() + " chooses to USE its special action.");
        } else {
            System.out.println(p.getName() + " does NOT use its special action.");
        }
        
        System.out.println(p.getName() + " chooses to move " + directionToString(bestDirection) + ".");
        
        executeTurn(p, bestDirection, useSpecial);
    }
    
    // Simulation Logic

    private void executeTurn(Penguin p, Direction dir, boolean useSpecial) {
        // Apply Special Action Setup
        if (useSpecial) {
            // Special handling for Royal Penguin input (needs 2nd direction)
            if (p instanceof RoyalPenguin) {
                Direction stepDir = null;
                if (p == playerPenguin) {
                     System.out.print("Select direction for single step (U, D, L, R) --> ");
                     stepDir = parseDirection(getValidInput("^[uUdDlLrR]$"));
                } else {
                    // Bot Royal: Random safe step.
                    stepDir = getSafeRandomDirection(p.getPosition());
                }
                System.out.println(p.useSpecialAction(stepDir));
            } else {
                String msg = p.useSpecialAction(dir); // For Rockhopper, prepares jump. For others, just marks used.
                if(msg != null && !(p instanceof KingPenguin) && !(p instanceof EmperorPenguin)) {
                    // King/Emperor msg is shown after slide usually, or handled differently.
                    System.out.println(p.getName() + " " + msg);
                }
            }
        }

        // Phase 1: Royal Penguin Pre-Slide Step
        if (p instanceof RoyalPenguin && ((RoyalPenguin) p).willPerformSingleStep()) {
            Direction stepDir = ((RoyalPenguin) p).getSingleStepDirection();
            Position currentPos = p.getPosition();
            Position targetPos = currentPos.getAdjacent(stepDir);
            
            // Move one step.
            moveObject(p, currentPos, targetPos);
            
            // Check fall.
            if (!targetPos.isOnGrid()) {
                System.out.println(p.getName() + " fell into the water while using special action!");
                p.removeFromGame();
                ((RoyalPenguin) p).resetSingleStep();
                return; // Turn ends.
            }
            
            // Check immediate collision on single step.
            ITerrainObject obj = getObjectAt(targetPos);
            if (obj != null) {
                handleCollision(p, obj, targetPos); // Might stop or die.
                if (p.isRemovedFromGame()) return;
            }
            
            ((RoyalPenguin) p).resetSingleStep();
        }

        // Phase 2: Sliding
        simulateSlide(p, dir);
        
        // Reset per-turn flags.
        if (p instanceof RockhopperPenguin) {
            ((RockhopperPenguin) p).resetJumpPreparation();
        }
    }

    private void simulateSlide(Penguin mover, Direction dir) {
        Position current = mover.getPosition();
        // If mover removed during pre-steps, stop.
        if (!current.isOnGrid()) return;

        int steps = 0;
        boolean keepSliding = true;

        while (keepSliding) {
            Position next = current.getAdjacent(dir);
            steps++;

            // 1. Check Out of Bounds
            if (!next.isOnGrid()) {
                moveObject(mover, current, next); // Move to void.
                mover.removeFromGame();
                System.out.println(mover.getName() + " falls into the water!");
                return;
            }

            // 2. Check Collisions
            ITerrainObject obstacle = getObjectAt(next);
            
            // Special: Rockhopper Jump
            if (mover instanceof RockhopperPenguin && ((RockhopperPenguin) mover).isPreparedToJump() && obstacle instanceof IHazard) {
                // Attempt jump.
                Position jumpLanding = next.getAdjacent(dir);
                if (jumpLanding.isOnGrid() && getObjectAt(jumpLanding) == null) {
                    System.out.println(mover.getName() + " jumps over " + obstacle.getNotation() + " in its path.");
                    ((RockhopperPenguin) mover).consumeJumpAttempt(); // Used
                    // Land on jumpLanding.
                    moveObject(mover, current, jumpLanding);
                    current = jumpLanding;
                    continue; 
                } else {
                    // Jump failed.
                    System.out.println(mover.getName() + " failed to jump (landing blocked or OOB).");
                    ((RockhopperPenguin) mover).consumeJumpAttempt();
                    // Fall through to normal collision.
                }
            }

            if (obstacle != null) {
                // Collision Logic
                
                // Case A: Food
                if (obstacle instanceof Food) {
                    // Move to food square.
                    moveObject(mover, current, next);
                    mover.collectFood((Food) obstacle);
                    // Remove food from global list if needed, definitely from grid.
                    // Logic: Food is "eaten", so removed from grid inside.
                    System.out.println(mover.getName() + " takes the " + ((Food) obstacle).getNotation() + " on the ground. (Weight " + ((Food) obstacle).getWeight() + " units)");
                    return; // Stops at food.
                }

                // Case B: Hazard or Penguin
                handleCollision(mover, obstacle, next);
                
                // Post-Collision checks.
                if (mover.isRemovedFromGame()) return;
                
                // Interaction specific: SeaLion Bounce
                if (obstacle instanceof SeaLion) {
                     // Mover reverses direction.
                     dir = getOppositeDirection(dir);
                     System.out.println(mover.getName() + " bounces off SeaLion and slides " + directionToString(dir));
                     // SeaLion moves in original direction
                     processSecondarySlide((SeaLion)obstacle, getOppositeDirection(dir)); // original dir
                     current = mover.getPosition();
                     continue;
                }
                
                // Interaction: LightIceBlock
                if (obstacle instanceof LightIceBlock) {
                    // Penguin stops at 'current' (before collision).
                    // Block moves.
                    processSecondarySlide((LightIceBlock)obstacle, dir);
                    return; // Penguin turn ends (stunned).
                }

                // Interaction: HeavyIceBlock
                if (obstacle instanceof HeavyIceBlock) {
                    // Stops at 'current'.
                    return;
                }
                
                // Interaction: HoleInIce
                // Handled in handleCollision (removed).
                if (mover.isRemovedFromGame()) return;

                // Interaction: Another Penguin
                if (obstacle instanceof Penguin) {
                    // Mover stops at 'current'.
                    // Target penguin slides.
                    System.out.println(mover.getName() + " collides with " + ((Penguin)obstacle).getName() + ". Movement transfers.");
                    simulateSlide((Penguin)obstacle, dir);
                    return;
                }

                return; // Default stop for unhandled collision.
            }

            // 3. Move to empty square
            moveObject(mover, current, next);
            current = next;

            // 4. Special Stops (King/Emperor)
            if (mover instanceof EmperorPenguin && mover.hasUsedSpecialAction() && steps == 3) {
                 System.out.println(mover.getName() + " stops at the third square using its special action.");
                 keepSliding = false;
            }
            if (mover instanceof KingPenguin && mover.hasUsedSpecialAction() && steps == 5) {
                 System.out.println(mover.getName() + " stops at the fifth square using its special action.");
                 keepSliding = false;
            }
        }
    }

    // Secondary slide for objects pushed by penguins (SeaLion, LightIceBlock).
    private void processSecondarySlide(ITerrainObject obj, Direction dir) {
        if (!obj.isSlidable()) return;
        
        System.out.println(obj.getNotation() + " starts sliding " + directionToString(dir) + ".");
        
        Position current = obj.getPosition();
        boolean keepSliding = true;

        while (keepSliding) {
            Position next = current.getAdjacent(dir);
            
            if (!next.isOnGrid()) {
                moveObject(obj, current, next); // Remove from grid.
                System.out.println(obj.getNotation() + " falls into the water.");
                return;
            }

            ITerrainObject obstacle = getObjectAt(next);
            if (obstacle != null) {
                // Logic for sliding hazards hitting things.
                
                // Hazard vs Food: Remove food, continue.
                if (obstacle instanceof Food) {
                     // Food vanishes.
                     grid.get(next.getRow()).set(next.getColumn(), null); // Remove food.
                     // Move hazard.
                     moveObject(obj, current, next);
                     current = next;
                     continue;
                }
                
                // Hazard vs Penguin: Hazard stops, Penguin unaffected?
                if (obstacle instanceof Penguin) {
                    System.out.println(obj.getNotation() + " hits " + ((Penguin)obstacle).getName() + " and stops.");
                    return;
                }
                
                if (obj instanceof LightIceBlock && obstacle instanceof SeaLion) {
                    System.out.println("LB hits SeaLion. Energy transfers.");
                    processSecondarySlide(obstacle, dir); // SeaLion moves
                    return; // LightIceBlock stops
                }

                // Hazard vs Hole: Plugs hole
                if (obstacle instanceof HoleInIce) {
                    System.out.println(obj.getNotation() + " slides into HoleInIce and plugs it!");
                    // Remove obj from current.
                    grid.get(current.getRow()).set(current.getColumn(), null);
                    // Plug hole.
                    ((HoleInIce) obstacle).plug();
                    return;
                }
                
                // Hazard vs HeavyIceBlock: Stop
                if (obstacle instanceof HeavyIceBlock) {
                    return;
                }

                return; // Default stop.
            }
            
            // Move.
            moveObject(obj, current, next);
            current = next;
        }
    }

    private void handleCollision(Penguin mover, ITerrainObject obstacle, Position pos) {
        if (obstacle instanceof IHazard) {
            ((IHazard) obstacle).handleCollision(mover);
            if (obstacle instanceof HeavyIceBlock) {
                System.out.println(mover.getName() + " hit a HeavyIceBlock! Lost lightest food.");
            }
            if (obstacle instanceof HoleInIce && !((HoleInIce)obstacle).isPlugged()) {
                System.out.println(mover.getName() + " fell into a HoleInIce!");
            }
        }
    }

    // Helpers

    private void moveObject(ITerrainObject obj, Position from, Position to) {
        // Clear old pos if valid.
        if (from.isOnGrid()) {
            grid.get(from.getRow()).set(from.getColumn(), null);
        }
        
        // Set new pos if valid.
        if (to.isOnGrid()) {
            // Overwrite whatever is there.
            grid.get(to.getRow()).set(to.getColumn(), obj);
            obj.setPosition(to);
        } else {
            // Object effectively removed from grid array, though instance exists.
            // Handled by specific remove logic in caller.
        }
    }

    private ITerrainObject getObjectAt(Position p) {
        if (!p.isOnGrid()) return null;
        return grid.get(p.getRow()).get(p.getColumn());
    }
    
    private void updateGrid(ITerrainObject obj, Position p) {
        grid.get(p.getRow()).set(p.getColumn(), obj);
    }

    // AI Helpers

    private Direction chooseBotDirection(Penguin p) {
        // Priority: Food > Random Valid > Hazard (except Hole) > Water
        
        List<Direction> dirs = new ArrayList<>();
        Collections.addAll(dirs, Direction.values());
        Collections.shuffle(dirs); // Randomize check order.

        // 1. Check for immediate food
        for (Direction d : dirs) {
            ITerrainObject obj = getObjectAt(p.getPosition().getAdjacent(d));
            if (obj instanceof Food) return d;
        }

        // 2. Check for empty space or safe Move
        for (Direction d : dirs) {
            Position next = p.getPosition().getAdjacent(d);
            if (!next.isOnGrid()) continue; // Water
            ITerrainObject obj = getObjectAt(next);
            if (obj == null) return d;
        }
        
        // 3. Move to Hazard (except Hole)
        for (Direction d : dirs) {
            Position next = p.getPosition().getAdjacent(d);
            if (!next.isOnGrid()) continue;
            ITerrainObject obj = getObjectAt(next);
            if (obj instanceof IHazard && !(obj instanceof HoleInIce)) return d;
        }

        // 4. Force move (Water or Hole)
        return dirs.get(0);
    }
    
    private boolean isHazardInPath(Position start, Direction dir) {
        Position next = start.getAdjacent(dir);
        if(!next.isOnGrid()) return false;
        ITerrainObject obj = getObjectAt(next);
        return obj instanceof IHazard;
    }
    
    private Direction getSafeRandomDirection(Position pos) {
        // For Royal Penguin step.
        for (Direction d : Direction.values()) {
            Position next = pos.getAdjacent(d);
            if (next.isOnGrid() && getObjectAt(next) == null) return d;
        }
        return Direction.UP; // Fallback.
    }

    // I/O Helpers

    private String getValidInput(String regex) {
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.matches(regex)) return input;
            System.out.print("Invalid input. Try again: ");
        }
    }

    private Direction parseDirection(String s) {
        if (s.equalsIgnoreCase("U")) return Direction.UP;
        if (s.equalsIgnoreCase("D")) return Direction.DOWN;
        if (s.equalsIgnoreCase("L")) return Direction.LEFT;
        if (s.equalsIgnoreCase("R")) return Direction.RIGHT;
        return Direction.UP;
    }
    
    private Direction getOppositeDirection(Direction d) {
        switch(d) {
            case UP: return Direction.DOWN;
            case DOWN: return Direction.UP;
            case LEFT: return Direction.RIGHT;
            case RIGHT: return Direction.LEFT;
            default: return Direction.UP;
        }
    }
    
    private String directionToString(Direction d) {
        switch (d) {
            case UP: return "UPWARDS";
            case DOWN: return "DOWNWARDS";
            case LEFT: return "to the LEFT";
            case RIGHT: return "to the RIGHT";
            default: return "";
        }
    }

    private void printGrid() {
        // Cell width ~ 4 chars. "| Kr " or "| P1 ".
        
        for (int i = 0; i < GRID_SIZE; i++) {
            System.out.print("|"); // Start of row
            for (int j = 0; j < GRID_SIZE; j++) {
                ITerrainObject obj = grid.get(i).get(j);
                String str = (obj == null) ? "    " : String.format(" %-3s", obj.getNotation());
                System.out.print(str);
            }
            System.out.println("|"); // End of row.
        }
        System.out.println();
    }

    private void endGame() {
        System.out.println("GAME OVER");
        System.out.println("SCOREBOARD FOR THE PENGUINS *****");
        
        // Sort penguins by food weight.
        penguins.sort((p1, p2) -> Integer.compare(p2.getTotalFoodWeight(), p1.getTotalFoodWeight()));

        for (int i = 0; i < penguins.size(); i++) {
            Penguin p = penguins.get(i);
            String place = (i == 0) ? "1st" : (i == 1) ? "2nd" : "3rd";
            String suffix = (p == playerPenguin) ? " (Your Penguin)" : "";
            
            System.out.println(place + " place: " + p.getName() + suffix);
            
            System.out.print("---> Food items: ");
            List<Food> pf = p.getCollectedFood();
            for(int k=0; k<pf.size(); k++) {
                if(k>0) System.out.print(", ");
                System.out.print(pf.get(k).getNotation() + " (" + pf.get(k).getWeight() + " units)");
            }
            System.out.println();
            System.out.println("---> Total weight: " + p.getTotalFoodWeight() + " units");
        }
    }
}