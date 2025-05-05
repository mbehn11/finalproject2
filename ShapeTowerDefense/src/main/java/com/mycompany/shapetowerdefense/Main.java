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
}
