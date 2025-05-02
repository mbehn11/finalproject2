/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.shapetowerdefense;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JOptionPane;

// Singleton Approach
// Main has access to DataManager which means every class can use Main.DataManager to access anything
// TODO:::::::::::::::::::::::::
// add highscore api // confused
// change drawing polygon code to my own
// fix message title
// fix screens titles
// fix so I cant type or resize anything
// fix the bug that constantly tells me I have no third character equipped
// make battleWin dialog not pop up
// weird code in shopWin

// change text color in shop to brighten game up 
// :::::::::::::::::::::::::::::
public class DataManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private static DataManager instance;
    private static String currentFilePath;
    private String currentUsername;
    private int gold;
    private double foodGenerationMultiplier = 1.0;
    public int highestWave;
    private ArrayList<ShapeCharacter> equippedUnits;
    private int UnitsCount = 0;
    private ArrayList<ShapeCharacter> inventoryUnits; 

    public DataManager() {
        gold = 100;
        highestWave = 0;
        equippedUnits = new ArrayList<>();
        inventoryUnits = new ArrayList<>(); 
    }
    
    // Singleton Accessor
    public static synchronized DataManager getInstance() {
        if (instance == null) {
            // If no file path set, create new instance
            if (currentFilePath == null) {
                instance = new DataManager();
            } else {
                // Try to load from file, fall back to new instance
                instance = loadFromFile(currentFilePath);
            }
        }
        return instance;
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
        inventoryUnits.add(shape);
        saveToFile(currentFilePath);
    }

    public void setEquippedUnits(ArrayList<ShapeCharacter> units) {
        if (units.size() <= 3) {
            this.equippedUnits = new ArrayList<>(units);
        }
        saveToFile(currentFilePath);
    }
    
    public int addUnitCount(int count) {
        return this.UnitsCount += count;
    }
    
    public int getUnitCount() {
        return this.UnitsCount;
    }

    // Food Generation 
    
    public double getFoodGenerationMultiplier() {
        return foodGenerationMultiplier;
    }

    public void upgradeFoodGenerationMultiplier(double amount) {
        foodGenerationMultiplier += amount;
        saveToFile(currentFilePath);
    }
    
    // Gold Methods
    public boolean spendGold(int amount) {
        if (gold >= amount) {
            gold -= amount;
            saveToFile(currentFilePath);
            return true;
        }
        return false;
    }

    public void addGold(int amount) {
        gold += amount;
        saveToFile(currentFilePath);
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
        saveToFile(currentFilePath);
    }

    public ShapeCharacter getCharacterByName(String name) {
        for (ShapeCharacter sc : inventoryUnits) {
            if (sc.getShapeType().equals(name)) {
                return sc;
            }
        }
        return null;
    }
    
    public String getUsername() { 
        return currentUsername;
    }

    public void setUsername(String username) {
        this.currentUsername = username;
    }

    public void saveToFile(String filePath) {
        if (filePath == null) return;

        FileOutputStream fileOut = null;
        ObjectOutputStream out = null;
        try {
            fileOut = new FileOutputStream(filePath);
            out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to save game", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (out != null) out.close();
                if (fileOut != null) fileOut.close();
            } catch (IOException e) {
                System.err.println("Error closing streams: " + e.getMessage());
            }
        }
    }

    private static DataManager loadFromFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) {
            return null;
        }

        FileInputStream fileIn = null;
        ObjectInputStream in = null;
        try {
            fileIn = new FileInputStream(file);
            in = new ObjectInputStream(fileIn);
            return (DataManager) in.readObject();
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (in != null) in.close();
                if (fileIn != null) fileIn.close();
            } catch (IOException e) {
                System.err.println("Error closing streams: " + e.getMessage());
            }
        }
    }



    public void login(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }

        currentFilePath = username + "_data.dat";
        File file = new File(currentFilePath);

        if (file.exists() && file.length() > 0) {
            DataManager loadedData = loadFromFile(currentFilePath);
            if (loadedData != null) {
                this.copyFrom(loadedData);
            }
        }

        this.setUsername(username);
        this.saveToFile(currentFilePath);
        new LobbyWin().setVisible(true);
    }

    private void copyFrom(DataManager source) {
        this.gold = source.gold;
        this.highestWave = source.highestWave;
        this.equippedUnits = new ArrayList<>(source.equippedUnits);
        this.inventoryUnits = new ArrayList<>(source.inventoryUnits);
        this.foodGenerationMultiplier = source.foodGenerationMultiplier;
        this.UnitsCount = source.UnitsCount;
    }
}