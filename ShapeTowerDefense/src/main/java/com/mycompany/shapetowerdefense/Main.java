/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.shapetowerdefense;

import javax.swing.SwingUtilities;

public class Main {    
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
    // I dont think I need this function here but rather in data manager, also the instance of data manager shouldnt be made here must follow singleton rules
//    public static void afterLogin(String username) {
//        // load highest wave and characters
//        currentUsername = username;
//        String filePath = username + "_data.dat";
//        try {
//            dataManager = DataManager.loadFromFile(filePath);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            dataManager = new DataManager();
//        }
//        // Optionally close any previous windows here.
//        new LobbyWin().setVisible(true);
//    }
}
