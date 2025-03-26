/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.shapetowerdefense;

/**
 *
 * @author behnf
 */

public class Enemy extends Unit {
    public Enemy(String shapeType, int x, int y, int health, int damage, int speed, int width, int height) {
        super(x, y, health, damage, speed, width, height, shapeType);
    }

    @Override
    public void moveForward() {
        x -= speed; // Moves left toward the player's base
    }
}


