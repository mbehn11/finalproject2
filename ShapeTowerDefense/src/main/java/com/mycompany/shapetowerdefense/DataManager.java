/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.shapetowerdefense;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

// Singleton Approach
// Main has access to DataManager which means every class can use Main.DataManager to access anything
public class DataManager implements Serializable {
    private static final long serialVersionUID = 1L;

    private int gold;
    private double foodGenerationMultiplier = 1.0;
    public int highestWave;
    private ArrayList<ShapeCharacter> equippedUnits;
    private ArrayList<ShapeCharacter> inventoryUnits; // Added inventory units
    private HashMap<String, ShapeCharacter> characterMap; // Added character map because Jlists/Tables are Strings

    public DataManager() {
        gold = 100;
        highestWave = 0;
        equippedUnits = new ArrayList<>();
        inventoryUnits = new ArrayList<>(); // Initialize inventory units
        characterMap = new HashMap<>(); // Initialize character map
    }

    // Unit methods
    public ArrayList<ShapeCharacter> getEquippedUnits() {
        return new ArrayList<>(equippedUnits);
    }

    public ArrayList<ShapeCharacter> getInventoryUnits() {
        return new ArrayList<>(inventoryUnits);
    }

    public void addShape(ShapeCharacter shape) {
        equippedUnits.add(shape);
        inventoryUnits.add(shape); // Add shape to inventory
        characterMap.put(shape.getName(), shape); // Add to map
    }

    public void setEquippedUnits(ArrayList<ShapeCharacter> units) {
        if (units.size() <= 3) {
            this.equippedUnits = new ArrayList<>(units);
        }
    }

    // Food Generation 
    
    public double getFoodGenerationMultiplier() {
        return foodGenerationMultiplier;
    }

    public void upgradeFoodGenerationMultiplier(double amount) {
        foodGenerationMultiplier += amount;
    }
    
    // Gold Methods
    public boolean spendGold(int amount) {
        if (gold >= amount) {
            gold -= amount;
            return true;
        }
        return false;
    }

    public void addGold(int amount) {
        gold += amount;
    }

    // Getters and setters
    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getHighestWave() {
        return highestWave;
    }

    public void setHighestWave(int wave) {
        this.highestWave = wave;
    }

    // Character map method
    public ShapeCharacter getCharacterByName(String name) {
        return characterMap.get(name);
    }

    // Save this DataManager to a file using serialization
    public void saveToFile(String filePath) throws IOException {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(filePath));
            out.writeObject(this);
        } finally {
            if (out != null) out.close();
        }
    }

    // Load a DataManager from file; if the file doesn't exist, return a new instance
    public static DataManager loadFromFile(String filePath) throws IOException, ClassNotFoundException {
        File f = new File(filePath);
        if (!f.exists()) {
            return new DataManager();
        }
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(filePath));
            return (DataManager) in.readObject();
        } finally {
            if (in != null) in.close();
        }
    }
}