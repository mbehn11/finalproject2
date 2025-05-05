/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.shapetowerdefense;

import java.io.*;

public class AccountManager {  
    private static final String ACCOUNT_FILE = "accounts.txt";

    public static boolean login(String username, String password) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(ACCOUNT_FILE));
            String line;
            // Read each line in the accounts file
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                // Ensure the line has at least a username and password
                if (parts.length >= 2 && parts[0].trim().equals(username) && parts[1].trim().equals(password)) {
                    return true; // Match found
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading accounts: " + e.getMessage());
        } finally {
            // Ensure the file is closed even if an error occurs
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println("Error closing reader: " + e.getMessage());
                }
            }
        }
        return false; // No matching account found
    }

    public static boolean createAccount(String username, String password) {
        if (accountExists(username)) {
            return false;
        }

        BufferedWriter writer = null;
        try {
            // Open the file in append mode so we don't overwrite existing accounts
            writer = new BufferedWriter(new FileWriter(ACCOUNT_FILE, true));
            // Write the new account in the format: username,password
            writer.write(username + "," + password);
            writer.newLine(); // Move to the next line for future accounts
            return true; // Account creation successful
        } catch (IOException e) {
            System.out.println("Error saving account: " + e.getMessage());
        } finally {
            // Ensure the file is closed properly
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.out.println("Error closing writer: " + e.getMessage());
                }
            }
        }
        return false; // Account creation failed
    }

    private static boolean accountExists(String username) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(ACCOUNT_FILE));
            String line;
            // Read each line to check if the username is already taken
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].trim().equals(username)) {
                    return true; // Username exists
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading accounts: " + e.getMessage());
        } finally {
            // Close the reader even if an error occurs
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println("Error closing reader: " + e.getMessage());
                }
            }
        }
        return false; // Username not found
    }
}

