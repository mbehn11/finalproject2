/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.shapetowerdefense;

/**
 *
 * @author behnf
 */

public class GameLoop extends Thread {
    private GamePanel gamePanel;
    private boolean running = true;
    
    public GameLoop(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void run() {
        while (running) {
            gamePanel.update();
            gamePanel.repaint();
            
            try {
                Thread.sleep(16); // ~60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void stopLoop() {
        running = false;
        gamePanel.setVisible(false);
    }
}

