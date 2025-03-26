/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.shapetowerdefense;

import java.util.ArrayList;
import java.util.Arrays;

public class ShapeCharacter extends Unit {
    private String name;
    private String rarity;
    private int cost;

    public ShapeCharacter(String name, String rarity, String shapeType, int x, int y, int health, int damage, int speed, int width, int height, int cost) {
        super(x, y, health, damage, speed, width, height, shapeType);
        this.name = name;
        this.rarity = rarity;
        this.cost = cost;
    }

    @Override
    public void moveForward() {
        x += speed; // Moves right toward enemy
    }

    public String getName() { return name; }
    public String getRarity() { return rarity; }
    public int getCost() { return cost; }

    public static ArrayList<ShapeCharacter> getAllShapes() {
        // make this a shared thing between Unit
        return new ArrayList<>(Arrays.asList(
            new ShapeCharacter("Circle", "Common", "Circle", 50, 300, 100, 15, 2, 30, 30, 5),
            new ShapeCharacter("Square", "Uncommon", "Square", 50, 300, 80, 20, 3, 30, 30, 7),
            new ShapeCharacter("Rectangle", "Rare", "Rectangle", 50, 300, 60, 25, 2, 40, 20, 10)
        ));
    }
}
