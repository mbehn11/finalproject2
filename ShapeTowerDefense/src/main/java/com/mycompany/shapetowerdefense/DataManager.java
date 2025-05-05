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
// Main has access to DataManager which means every class can access anything
// TODO:::::::::::::::::::::::::
// possibly add variations of enemies that spawn
// background images
// sprites
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

    // Gold
    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    // Waves
    public int getHighestWave() {
        return highestWave;
    }

    public void setHighestWave(int wave) {
        this.highestWave = wave;
        saveToFile(currentFilePath);
    }

    // ShapeCharacter
    public ShapeCharacter getCharacterByName(String name) {
        for (ShapeCharacter sc : inventoryUnits) {
            if (sc.getShapeType().equals(name)) {
                return sc;
            }
        }
        return null;
    }
    
    // Username
    public String getUsername() { 
        return currentUsername;
    }

    public void setUsername(String username) {
        this.currentUsername = username;
    }

    // Saves the current DataManager state to a file using object serialization
    public void saveToFile(String filePath) {
        if (filePath == null) return;

        FileOutputStream fileOut = null;
        ObjectOutputStream out = null;
        try {
            // Create file output streams for writing the object
            fileOut = new FileOutputStream(filePath);
            out = new ObjectOutputStream(fileOut);

            // Serialize 'this' object and write it to file
            out.writeObject(this);
        } catch (IOException e) {
            // Show error dialog if saving fails
            JOptionPane.showMessageDialog(null, "Failed to save game", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Safely close streams
            try {
                if (out != null) out.close();
                if (fileOut != null) fileOut.close();
            } catch (IOException e) {
                System.err.println("Error closing streams: " + e.getMessage());
            }
        }
    }

    // Loads a DataManager object from a file
    private static DataManager loadFromFile(String filePath) {
        File file = new File(filePath);

        // Return null if file doesn't exist
        if (!file.exists() || file.length() == 0) {
            return null;
        }

        FileInputStream fileIn = null;
        ObjectInputStream in = null;
        try {
            // Create input streams to read object from file
            fileIn = new FileInputStream(file);
            in = new ObjectInputStream(fileIn);

            // Deserialize and return the DataManager object
            return (DataManager) in.readObject();
        } catch (Exception e) {
            // Return null if loading fails
            return null;
        } finally {
            // Safely close streams
            try {
                if (in != null) in.close();
                if (fileIn != null) fileIn.close();
            } catch (IOException e) {
                System.err.println("Error closing streams: " + e.getMessage());
            }
        }
    }

    // Logs in the user by loading or initializing their DataManager file
    public void login(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }

        // Construct the file path for this user
        currentFilePath = username + "_data.dat";
        File file = new File(currentFilePath);

        // If user has saved data, load it
        if (file.exists() && file.length() > 0) {
            DataManager loadedData = loadFromFile(currentFilePath);
            if (loadedData != null) {
                this.copyFrom(loadedData); // Copy values from loaded data
            }
        }

        // Set username, save current state, and open the lobby screen
        this.setUsername(username);
        this.saveToFile(currentFilePath);
        new LobbyWin().setVisible(true);
    }

    // Copies all relevant fields from another DataManager instance
    private void copyFrom(DataManager source) {
        this.gold = source.gold;
        this.highestWave = source.highestWave;
        this.equippedUnits = new ArrayList<>(source.equippedUnits);
        this.inventoryUnits = new ArrayList<>(source.inventoryUnits);
        this.foodGenerationMultiplier = source.foodGenerationMultiplier;
        this.UnitsCount = source.UnitsCount;
    }
    
    public void refreshEquippedUnits() {
        // Just reload from the save file, replacing current in-memory data
        if (currentFilePath != null) {
            DataManager loaded = loadFromFile(currentFilePath);
            if (loaded != null) {
                this.copyFrom(loaded);
            }
        }
    }

}