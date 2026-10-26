# Grid-Based Penguin Simulation

A Java-based 2D grid simulation demonstrating advanced Object-Oriented Programming principles. The project simulates different species of penguins navigating an icy terrain to collect food while avoiding hazards.

## Key Features
* **Polymorphism & Inheritance:** Utilizes an abstract `Penguin` base class with concrete subclasses (`KingPenguin`, `EmperorPenguin`, `RockhopperPenguin`), each overriding the `useSpecialAction()` method for unique movement behaviors.
* **Interface-Driven Design:** Implements the `ITerrainObject` interface across all grid entities (Penguins, Food, Hazards) to allow unified coordinate tracking and collision detection.
* **Type Safety:** Uses `Enum` structures to strictly define cardinal movement directions (`Direction`) and collectible items (`FoodType`).
* **Dynamic Grid Management:** Manages spatial positioning, object states, and collision consequences within a simulated 10x10 matrix.

## Technologies
* Java (Inheritance, Polymorphism, Interfaces, Enums, Collections)
