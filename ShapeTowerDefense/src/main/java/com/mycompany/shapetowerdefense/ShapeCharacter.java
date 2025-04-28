/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.shapetowerdefense;

import java.io.Serializable;
import java.util.ArrayList;

public class ShapeCharacter extends Unit implements Serializable {
    private static final long serialVersionUID = 1L; 
    private String rarity;
    private int cost;

    public ShapeCharacter(String shapeType, String rarity, int x, int y, int health, int damage, int speed, int width, int height, int cost) {
        super(x, y, health, damage, speed, width, height, shapeType);
        this.rarity = rarity;
        this.cost = cost;
    }

    @Override
    public void moveForward() {
        x += speed; // Moves right toward enemy
    }

    public String getName() { return shapeType; }
    public String getRarity() { return rarity; }
    public int getCost() { return cost; }

    public static ArrayList<ShapeCharacter> getAllShapes() { 
        ArrayList<ShapeCharacter> allShapes = new ArrayList<>();
        allShapes.add(new ShapeCharacter("Circle", "Common", 50, 300, 10, 5, 2, 30, 30, 2));
        allShapes.add(new ShapeCharacter("Square", "Common", 50, 300, 12, 6, 2, 30, 30, 3));

        allShapes.add(new ShapeCharacter("Rectangle", "Uncommon", 50, 300, 20, 10, 2, 40, 20, 6));
        allShapes.add(new ShapeCharacter("Pentagon", "Uncommon", 50, 300, 22, 12, 2, 30, 30, 7));

        allShapes.add(new ShapeCharacter("Hexagon", "Rare", 50, 300, 30, 15, 3, 30, 30, 10));
        allShapes.add(new ShapeCharacter("Heptagon", "Rare", 50, 300, 35, 18, 2, 35, 35, 12));

        allShapes.add(new ShapeCharacter("Octagon", "Epic", 50, 300, 45, 25, 3, 40, 40, 16));

        allShapes.add(new ShapeCharacter("Nonagon", "Legendary", 50, 300, 55, 30, 3, 45, 45, 20));
        allShapes.add(new ShapeCharacter("Decagon", "Legendary", 50, 300, 60, 35, 2, 50, 50, 22));

        allShapes.add(new ShapeCharacter("Star", "Mythic", 50, 300, 80, 50, 4, 50, 50, 30));
        return allShapes;
    }
}