/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.shapetowerdefense;

import javax.swing.SwingUtilities;

public class Main {
    public static String currentUsername;
    public static DataManager dataManager;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Open the splash screen
                new SplashScreen().setVisible(true);
            }
        });
    }
    
    // This method is called after a successful login or account creation.
    // It loads the user's data (or creates a new DataManager if none exists) and opens the Lobby.
    public static void afterLogin(String username) {
        currentUsername = username;
        String filePath = username + "_data.dat";
        try {
            dataManager = DataManager.loadFromFile(filePath);
        } catch (Exception ex) {
            ex.printStackTrace();
            dataManager = new DataManager();
        }
        // Optionally close any previous windows here.
        new LobbyWin().setVisible(true);
    }
}
