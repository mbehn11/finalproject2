/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.shapetowerdefense;

/**
 *
 * @author behnf
 */


public abstract class Unit {
    protected int x, y;
    protected int health;
    protected int damage;
    protected int speed;
    protected int width, height;
    protected String shapeType; // "Circle", "Square", "Rectangle"

    public Unit(int x, int y, int health, int damage, int speed, int width, int height, String shapeType) {
        this.x = x;
        this.y = y;
        this.health = health;
        this.damage = damage;
        this.speed = speed;
        this.width = width;
        this.height = height;
        this.shapeType = shapeType;
    }

    public boolean isColliding(Unit other) {
        if (this.shapeType.equals("Circle") && other.shapeType.equals("Circle")) {
            return isCircleColliding(other);
        } else {
            return isRectangleColliding(other);
        }
    }

    private boolean isRectangleColliding(Unit other) {
        return x < other.x + other.width && x + width > other.x &&
               y < other.y + other.height && y + height > other.y;
    }

    private boolean isCircleColliding(Unit other) {
        int centerX1 = x + width / 2;
        int centerY1 = y + height / 2;
        int centerX2 = other.x + other.width / 2;
        int centerY2 = other.y + other.height / 2;
        int radius1 = width / 2;
        int radius2 = other.width / 2;
        
        int dx = centerX1 - centerX2;
        int dy = centerY1 - centerY2;
        int distanceSquared = dx * dx + dy * dy;
        int radiusSum = radius1 + radius2;

        return distanceSquared <= radiusSum * radiusSum;
    }

    public abstract void moveForward();

    public int getX() { return x; }
    public int getY() { return y; }
    public int getHealth() { return health; }
    public int getDamage() { return damage; } // Fixed mistake
    public void takeDamage(int amount) { health -= amount; }
    public boolean isAlive() { return health > 0; }
}
