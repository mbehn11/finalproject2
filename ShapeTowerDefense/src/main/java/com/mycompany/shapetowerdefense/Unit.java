/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.shapetowerdefense;

import java.io.Serializable;
 
public abstract class Unit implements Serializable {
    private static final long serialVersionUID = 1L;
    protected int x, y;
    protected int health;
    protected int maxHealth;
    protected int damage;
    protected int speed;
    protected int width, height;
    protected String shapeType; // "Circle", "Square", "Rectangle"

    public Unit(int x, int y, int health, int damage, int speed, int width, int height, String shapeType) {
        this.x = x;
        this.y = y;
        this.health = health;
        this.maxHealth = health;
        this.damage = damage;
        this.speed = speed;
        this.width = width;
        this.height = height;
        this.shapeType = shapeType;
    }

    // Required for movement behavior in subclasses
    public abstract void moveForward();

    // Setters for position (used in spawn methods)
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }

    // Getters
    public String getShapeType() { return this.shapeType = shapeType; }
    public int getWidth() { return this.width = width; }
    public int getHeight() { return this.height = height; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getSpeed() { return speed; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getDamage() { return damage; }

    // Health management
    public void takeDamage(int amount) { health -= amount; }
    public boolean isAlive() { return health > 0; }
}
