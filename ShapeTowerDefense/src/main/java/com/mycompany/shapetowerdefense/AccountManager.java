/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.shapetowerdefense;

import java.io.*;
import java.util.Scanner;

public class AccountManager {
    private static final String ACCOUNT_FILE = "accounts.txt";
    
    public static boolean login(String username, String password) {
        // Loads previous data 
        try (Scanner scanner = new Scanner(new File(ACCOUNT_FILE))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].trim().equals(username) && parts[1].trim().equals(password)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            // File not found – no accounts exist yet.
            return false;
        }
        return false;
    }
    
    public static boolean createAccount(String username, String password) {
        if (accountExists(username)) {
            return false;
        }
        try (FileWriter fw = new FileWriter(ACCOUNT_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(username + "," + password);
            bw.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private static boolean accountExists(String username) {
        try (Scanner scanner = new Scanner(new File(ACCOUNT_FILE))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].trim().equals(username)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            // No accounts file exists yet.
            return false;
        }
        return false;
    }
}

