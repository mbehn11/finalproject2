/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.shapetowerdefense;

import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import org.json.JSONObject;

/**
 *
 * @author behnf
 */
public class BattleWin extends javax.swing.JFrame {

    DataManager dm = DataManager.getInstance();
    private ArrayList<ShapeCharacter> playerUnits = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private boolean battleStarted = false;
    private DefaultTableModel equippedListModel;
    private Timer gameTimer;
    private int enemiesPerWave = 5;
    private int enemiesSpawnedThisWave = 0;
    private int enemiesDefeatedThisWave = 0;
    private Timer waveTimer;
    private Timer spawnTimer;
    private int playerFood = 0;
    private int currentWave = dm.getHighestWave();

    /**
     * Creates new form BattleScreen
     */
    public BattleWin() {
        initComponents();

        startBattleButton.setVisible(true);
        equippedListModel = new DefaultTableModel(new String[]{"Equipped Characters"}, 0);
        equippedList.setModel(equippedListModel);
        loadEquippedUnits();
        gameTimer = new Timer(16, e -> updateGame());
        waveTimer = new Timer(1000, e -> updateWave());
        spawnTimer = new Timer(2000, e -> spawnEnemy());
        foodTimer.start();
    }

    private Timer foodTimer = new Timer(1000, e -> {
        if (battleStarted) {
            playerFood += 1 * dm.getFoodGenerationMultiplier(); // 1 food per second
            updateAllLabels();
        }
    });
    
    private void loadEquippedUnits() {
        equippedListModel.setRowCount(0); // Clear the table

        // Only show up to 3 equipped characters
        for (int i = 0; i < Math.min(3, dm.getEquippedUnits().size()); i++) {
            ShapeCharacter sc = dm.getEquippedUnits().get(i);
            equippedListModel.addRow(new Object[]{sc.getShapeType()});
        }

        // If we have less than 3, add empty slots
        for (int i = dm.getEquippedUnits().size(); i < 3; i++) {
            equippedListModel.addRow(new Object[]{"[Empty Slot]"});
        }
        updateButtonCosts();
    }

    private void updateAllLabels() {
        foodLabel.setText("🍎Food: " + playerFood);
        goldLabel.setText("💰Gold: " + dm.getGold());
        waveLabel.setText("👑Wave: " + dm.getHighestWave());
        updateButtonCosts();
    }

    private void updateButtonCosts() {
        // Get the currently equipped characters
        ShapeCharacter char1 = getShapeCharacter1();
        ShapeCharacter char2 = getShapeCharacter2();
        ShapeCharacter char3 = getShapeCharacter3();

        // Update button 1
        if (char1 != null) {
            spawnChar1Btn.setText("Spawn " + char1.getShapeType() + " (" + char1.getCost() + " food)💰");
            spawnChar1Btn.setEnabled(playerFood >= char1.getCost());
        } else {
            spawnChar1Btn.setText("No Character");
            spawnChar1Btn.setEnabled(false);
        }

        // Update button 2
        if (char2 != null) {
            spawnChar2Btn.setText("Spawn " + char2.getShapeType() + " (" + char2.getCost() + " food)💰");
            spawnChar2Btn.setEnabled(playerFood >= char2.getCost());
        } else {
            spawnChar2Btn.setText("No Character");
            spawnChar2Btn.setEnabled(false);
        }

        // Update button 3
        if (char3 != null) {
            spawnChar3Btn.setText("Spawn " + char3.getShapeType() + " (" + char3.getCost() + " food)💰");
            spawnChar3Btn.setEnabled(playerFood >= char3.getCost());
        } else {
            spawnChar3Btn.setText("No Character");
            spawnChar3Btn.setEnabled(false);
        }
    }

    private void startBattle() {
        // starts all the times and refresh functions
        battleStarted = true;
        startBattleButton.setEnabled(false);
        gameTimer.start();
        waveTimer.start();
        spawnTimer.start();
        dm.setHighestWave(currentWave);
        updateAllLabels();
        updateWave();
        displayTaunt();
    }

    private void updateWave() {
        // rewards player and increase difficulty
        if(battleStarted) {
                gameLog2.setText("");
                gameLog.setText("Starting wave " + currentWave);
            }

        if (enemies.isEmpty() && enemiesSpawnedThisWave >= enemiesPerWave) {
            // Pause all timers
            foodTimer.stop();
            gameTimer.stop();
            waveTimer.stop();
            spawnTimer.stop();

            // Wave completed
            currentWave++;
            dm.setHighestWave(currentWave);
            enemiesPerWave = 5 + currentWave * 2;
            enemiesSpawnedThisWave = 0;
            enemiesDefeatedThisWave = 0;

            // Reward player
            dm.addGold(20 + currentWave);
            playerFood += currentWave + 10;
            updateAllLabels();
           
            // Restart timers after dialog is closed
            foodTimer.start();
            gameTimer.start();
            waveTimer.start();
            spawnTimer.start();
            displayTaunt();
        }
    }
    
    public void displayTaunt() {
        new Thread(() -> {
            try {
                URL url = new URL("https://www.yomama-jokes.com/api/v1/jokes/random/");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject json = new JSONObject(response.toString());
                String joke = json.getString("joke");

                SwingUtilities.invokeLater(() -> taunts.setText(joke));
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> taunts.setText("Failed to fetch taunt."));
                e.printStackTrace();
            }
        }).start();
    }

    private void spawnEnemy() {
        if (battleStarted && enemiesSpawnedThisWave < enemiesPerWave) {
            // Only spawn circles
            int health = 10 + currentWave;
            int damage = 5 + currentWave;
            int speed = 1;
            int size = 30; // Circle diameter

            Enemy enemy = new Enemy(
                    "Circle", // Only circles
                    getWidth() - size, // x position (right side)
                    getHeight() / 2,
                    health,
                    damage,
                    speed,
                    size,
                    size
            );

            enemies.add(enemy);
            enemiesSpawnedThisWave++;
        }
    }

    private ShapeCharacter getShapeCharacter1() {
        try {
            if (equippedListModel.getRowCount() > 0) {
                String charName = (String) equippedListModel.getValueAt(0, 0);
                ShapeCharacter character = dm.getCharacterByName(charName);
                return character;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error accessing slot 1: " + e.getMessage());
        }
        return null;
    }

    private ShapeCharacter getShapeCharacter2() {
        try {
            if (equippedListModel.getRowCount() > 1) {
                String charName = (String) equippedListModel.getValueAt(1, 0);
                ShapeCharacter character = dm.getCharacterByName(charName);
                return character;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error accessing slot 2: " + e.getMessage());
        }
        return null;
    }

    private ShapeCharacter getShapeCharacter3() {
        try {
            if (equippedListModel.getRowCount() > 2) {
                String charName = (String) equippedListModel.getValueAt(2, 0);
                ShapeCharacter character = dm.getCharacterByName(charName);
                return character;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error accessing slot 3: " + e.getMessage());
        }
        return null;
    }

    private void updateGame() {
        // Move units
        for (ShapeCharacter unit : playerUnits) {
            unit.moveForward();
        }

        for (Enemy unit : enemies) {
            unit.moveForward();
        }

        checkCollisions();

        // Check for game over (enemy reached left side)
        for (Enemy enemy : enemies) {
            if (enemy.getX() <= 0) {
                gameOver();
                break;
            }
        }

        // Check for wave completion
        updateWave();
        repaint();
    }

    private boolean collidesWith(Unit unit1, Unit unit2) {
        // checks if the X goes past each other
        return unit1.getX() < unit2.getX() + unit2.getWidth()
                && unit1.getX() + unit1.getWidth() > unit2.getX();
    }


    private void checkCollisions() {
        // goes through all of the players and enemies
        for (int i = 0; i < playerUnits.size(); i++) {
            ShapeCharacter player = playerUnits.get(i);
            for (int j = 0; j < enemies.size(); j++) {
                Enemy enemy = enemies.get(j);
                // checks if the player collides with the enemies
                if (collidesWith(player, enemy)) {
                    player.takeDamage(enemy.getDamage());
                    enemy.takeDamage(player.getDamage());

                    if (!enemy.isAlive()) {
                        enemies.remove(j);
                        j--;
                        dm.addGold(5 + currentWave); // when enemy dies give gold
                    }
                }
            }
            if (!player.isAlive()) {
                playerUnits.remove(i);
                i--;
            }
        }
    }

    private void gameOver() {
        // stops timers, updates user data
        gameTimer.stop();
        waveTimer.stop();
        spawnTimer.stop();
        foodTimer.stop();
        JOptionPane.showMessageDialog(this, "Game Over! Reached wave " + currentWave);
        dm.setHighestWave(currentWave);
        dispose(); // close BattleWin first
        // Refresh equipped units before showing lobby
        dm.refreshEquippedUnits();

        LobbyWin lobby = new LobbyWin();
        lobby.setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (battleStarted) {
            // Draw player units in middle left
            for (ShapeCharacter unit : playerUnits) {
                if (unit.isAlive()) {
                    g.setColor(Color.BLUE);

                    int x = unit.getX();
                    int y = unit.getY();
                    int w = unit.getWidth();
                    int h = unit.getHeight();
                    String shape = unit.getShapeType();

                    switch (shape) {
                        case "Circle":
                            g.fillOval(x, y, w, h);
                            break;
                        case "Square":
                        case "Rectangle":
                            g.fillRect(x, y, w, h);
                            break;
                        case "Pentagon":
                            drawRegularPolygon(g, 5, x + w / 2, y + h / 2, w / 2);
                            break;
                        case "Hexagon":
                            drawRegularPolygon(g, 6, x + w / 2, y + h / 2, w / 2);
                            break;
                        case "Heptagon":
                            drawRegularPolygon(g, 7, x + w / 2, y + h / 2, w / 2);
                            break;
                        case "Octagon":
                            drawRegularPolygon(g, 8, x + w / 2, y + h / 2, w / 2);
                            break;
                        case "Nonagon":
                            drawRegularPolygon(g, 9, x + w / 2, y + h / 2, w / 2);
                            break;
                        case "Decagon":
                            drawRegularPolygon(g, 10, x + w / 2, y + h / 2, w / 2);
                            break;
                        case "Star":
                            drawStar(g, x + w / 2, y + h / 2, w / 2, w / 4, 5);
                            break;
                    }

                    // Health bar
                    g.setColor(Color.GREEN);
                    int healthWidth = (int) ((double) unit.getHealth() / unit.getMaxHealth() * unit.getWidth());
                    g.fillRect(unit.getX(), unit.getY() - 10, healthWidth, 5);
                }
            }

            // Draw enemies (only circles) on right
            for (Enemy enemy : enemies) {
                if (enemy.isAlive()) {
                    g.setColor(Color.RED);
                    g.fillOval(enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight());

                    // Health bar
                    g.setColor(Color.GREEN);
                    int healthWidth = (int) ((double) enemy.getHealth() / enemy.getMaxHealth() * enemy.getWidth());
                    g.fillRect(enemy.getX(), enemy.getY() - 10, healthWidth, 5);
                }
            }
        }
    }

    // this is generated by chat
    private void drawRegularPolygon(Graphics g, int sides, int centerX, int centerY, int radius) {
        int[] xPoints = new int[sides];
        int[] yPoints = new int[sides];
        for (int i = 0; i < sides; i++) {
            double angle = 2 * Math.PI * i / sides - Math.PI / 2;
            xPoints[i] = centerX + (int) (radius * Math.cos(angle));
            yPoints[i] = centerY + (int) (radius * Math.sin(angle));
        }
        g.fillPolygon(xPoints, yPoints, sides);
    }

    // this is generated by chat
    private void drawStar(Graphics g, int centerX, int centerY, int outerRadius, int innerRadius, int points) {
        int[] xPoints = new int[points * 2];
        int[] yPoints = new int[points * 2];
        for (int i = 0; i < points * 2; i++) {
            double angle = Math.PI / points * i - Math.PI / 2;
            int radius = (i % 2 == 0) ? outerRadius : innerRadius;
            xPoints[i] = centerX + (int) (radius * Math.cos(angle));
            yPoints[i] = centerY + (int) (radius * Math.sin(angle));
        }
        g.fillPolygon(xPoints, yPoints, points * 2);
    }

    private void spawnCharacter(ShapeCharacter template) {
        // makes a template of a new character to spawn
        if (template != null && playerFood >= template.getCost()) {
            ShapeCharacter newUnit = new ShapeCharacter(
                    template.getShapeType(),
                    template.getRarity(),
                    0,
                    getHeight() / 2,
                    template.getHealth(),
                    template.getDamage(),
                    template.getSpeed(),
                    template.getWidth(),
                    template.getHeight(),
                    template.getCost()
            );

            playerUnits.add(newUnit);
            playerFood -= template.getCost();
            updateAllLabels();
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dataPanel = new javax.swing.JPanel();
        foodJPanel = new javax.swing.JPanel();
        foodLabel = new javax.swing.JLabel();
        waveJPanel = new javax.swing.JPanel();
        waveLabel = new javax.swing.JLabel();
        goldJPanel = new javax.swing.JPanel();
        goldLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        equippedList = new javax.swing.JTable();
        spawnChar1Btn = new javax.swing.JButton();
        startBattleButton = new javax.swing.JButton();
        spawnChar2Btn = new javax.swing.JButton();
        spawnChar3Btn = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        gameLog = new javax.swing.JLabel();
        gameLog2 = new javax.swing.JLabel();
        equippedListJlabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        taunts = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Battle");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        dataPanel.setBackground(new java.awt.Color(0, 102, 102));

        foodJPanel.setBackground(new java.awt.Color(204, 204, 255));

        foodLabel.setForeground(new java.awt.Color(0, 0, 255));
        foodLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        foodLabel.setText("🍎Food:");

        javax.swing.GroupLayout foodJPanelLayout = new javax.swing.GroupLayout(foodJPanel);
        foodJPanel.setLayout(foodJPanelLayout);
        foodJPanelLayout.setHorizontalGroup(
            foodJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(foodJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(foodLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                .addContainerGap())
        );
        foodJPanelLayout.setVerticalGroup(
            foodJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, foodJPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(foodLabel)
                .addContainerGap())
        );

        waveJPanel.setBackground(new java.awt.Color(204, 204, 255));

        waveLabel.setForeground(new java.awt.Color(255, 0, 51));
        waveLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        waveLabel.setText("👑Wave:");

        javax.swing.GroupLayout waveJPanelLayout = new javax.swing.GroupLayout(waveJPanel);
        waveJPanel.setLayout(waveJPanelLayout);
        waveJPanelLayout.setHorizontalGroup(
            waveJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(waveLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
        );
        waveJPanelLayout.setVerticalGroup(
            waveJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(waveJPanelLayout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(waveLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        goldJPanel.setBackground(new java.awt.Color(204, 204, 255));

        goldLabel.setForeground(new java.awt.Color(255, 255, 0));
        goldLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        goldLabel.setText("💰Gold:");

        javax.swing.GroupLayout goldJPanelLayout = new javax.swing.GroupLayout(goldJPanel);
        goldJPanel.setLayout(goldJPanelLayout);
        goldJPanelLayout.setHorizontalGroup(
            goldJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(goldJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(goldLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                .addContainerGap())
        );
        goldJPanelLayout.setVerticalGroup(
            goldJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(goldJPanelLayout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addComponent(goldLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout dataPanelLayout = new javax.swing.GroupLayout(dataPanel);
        dataPanel.setLayout(dataPanelLayout);
        dataPanelLayout.setHorizontalGroup(
            dataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(goldJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(waveJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(foodJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        dataPanelLayout.setVerticalGroup(
            dataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(goldJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(waveJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(foodJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        getContentPane().add(dataPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        equippedList.setBackground(new java.awt.Color(153, 204, 255));
        equippedList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Character1", "Character2", "Character3"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        equippedList.setAutoscrolls(false);
        equippedList.setEnabled(false);
        equippedList.setRowSelectionAllowed(false);
        jScrollPane1.setViewportView(equippedList);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(522, 276, 206, 89));

        spawnChar1Btn.setBackground(new java.awt.Color(255, 204, 153));
        spawnChar1Btn.setText("Spawn 1");
        spawnChar1Btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spawnChar1BtnActionPerformed(evt);
            }
        });
        getContentPane().add(spawnChar1Btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(114, 292, 402, -1));

        startBattleButton.setBackground(new java.awt.Color(0, 255, 0));
        startBattleButton.setText("Begin Battle➤");
        startBattleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startBattleButtonActionPerformed(evt);
            }
        });
        getContentPane().add(startBattleButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(249, 263, 129, -1));

        spawnChar2Btn.setBackground(new java.awt.Color(255, 204, 153));
        spawnChar2Btn.setText("Spawn 2");
        spawnChar2Btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spawnChar2BtnActionPerformed(evt);
            }
        });
        getContentPane().add(spawnChar2Btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(114, 317, 402, -1));

        spawnChar3Btn.setBackground(new java.awt.Color(255, 204, 153));
        spawnChar3Btn.setText("Spawn 3");
        spawnChar3Btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spawnChar3BtnActionPerformed(evt);
            }
        });
        getContentPane().add(spawnChar3Btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(114, 342, 402, -1));

        jButton1.setBackground(new java.awt.Color(255, 51, 51));
        jButton1.setText("⏪Go Back");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 292, 102, 73));

        gameLog.setBackground(new java.awt.Color(255, 51, 51));
        gameLog.setFont(new java.awt.Font("Snap ITC", 0, 12)); // NOI18N
        gameLog.setForeground(new java.awt.Color(255, 0, 0));
        gameLog.setText("Press Begin Battle to start!");
        getContentPane().add(gameLog, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 230, -1));

        gameLog2.setFont(new java.awt.Font("Snap ITC", 0, 12)); // NOI18N
        gameLog2.setForeground(new java.awt.Color(255, 51, 51));
        gameLog2.setText("Spawn Characters using your food to fend off enemies");
        getContentPane().add(gameLog2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, 480, -1));

        equippedListJlabel.setFont(new java.awt.Font("Snap ITC", 0, 18)); // NOI18N
        equippedListJlabel.setForeground(new java.awt.Color(255, 0, 255));
        equippedListJlabel.setText("Equipped List");
        getContentPane().add(equippedListJlabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 250, -1, -1));

        taunts.setEditable(false);
        taunts.setBackground(new java.awt.Color(0, 0, 0));
        taunts.setColumns(20);
        taunts.setFont(new java.awt.Font("Segoe UI Semibold", 0, 10)); // NOI18N
        taunts.setForeground(new java.awt.Color(255, 255, 255));
        taunts.setLineWrap(true);
        taunts.setRows(5);
        taunts.setWrapStyleWord(true);
        taunts.setAutoscrolls(false);
        taunts.setEnabled(false);
        jScrollPane2.setViewportView(taunts);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 110, 250, 50));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void startBattleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startBattleButtonActionPerformed
       startBattle();
    }//GEN-LAST:event_startBattleButtonActionPerformed

    private void spawnChar1BtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spawnChar1BtnActionPerformed
       spawnCharacter(getShapeCharacter1()); // get the price of the current character in slot one and update the button text to have (10) for the cost
    }//GEN-LAST:event_spawnChar1BtnActionPerformed

    private void spawnChar2BtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spawnChar2BtnActionPerformed
       spawnCharacter(getShapeCharacter2());  // get the price of the current character in slot one and update the button text to have (10) for the cost
    }//GEN-LAST:event_spawnChar2BtnActionPerformed

    private void spawnChar3BtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spawnChar3BtnActionPerformed
       spawnCharacter(getShapeCharacter3());  // get the price of the current character in slot one and update the button text to have (10) for the cost
    }//GEN-LAST:event_spawnChar3BtnActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int choice = JOptionPane.showConfirmDialog(
        this,
        "Exit to lobby? Current battle progress will be lost.",
        "Confirm Exit",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE
    );

    if (choice == JOptionPane.YES_OPTION) {  
            gameOver();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BattleWin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BattleWin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BattleWin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BattleWin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            }
        });
    }
public static class MyJpanel extends JPanel {
    
}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel dataPanel;
    private javax.swing.JTable equippedList;
    private javax.swing.JLabel equippedListJlabel;
    private javax.swing.JPanel foodJPanel;
    private javax.swing.JLabel foodLabel;
    private javax.swing.JLabel gameLog;
    private javax.swing.JLabel gameLog2;
    private javax.swing.JPanel goldJPanel;
    private javax.swing.JLabel goldLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton spawnChar1Btn;
    private javax.swing.JButton spawnChar2Btn;
    private javax.swing.JButton spawnChar3Btn;
    private javax.swing.JButton startBattleButton;
    private javax.swing.JTextArea taunts;
    private javax.swing.JPanel waveJPanel;
    private javax.swing.JLabel waveLabel;
    // End of variables declaration//GEN-END:variables
}
