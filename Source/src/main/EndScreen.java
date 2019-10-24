/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;


import java.awt.Color;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import org.netbeans.lib.awtextra.AbsoluteConstraints;

/**
 * Game-over screen.
 * @author Martin Rivard
 * @since January 21, 2016
 */

public class EndScreen extends JPanel {
    private static JLabel title;
    private static JButton endButton, newButton, loadButton;
    
    /**
     * Initiates end screen
     * @param player
     * @author Martin Rivard
     */
    public EndScreen(boolean player){
        if (player){
            title = new JLabel("Red Wins!", SwingConstants.CENTER);
        }else{
            title = new JLabel("Blue Wins!", SwingConstants.CENTER);
        }
        initLoseTitle(player);
        
        //Initialize panel.
        setSize(960, 512);
        setLayout(new AbsoluteLayout());
        setBackground(Color.BLACK);
        
        //Short initialization of various copies of buttons from elsewhere, specically the 'go to menu' button from the game screen, 'new game' button from the start menu, and the 'load game' button from likewise.
        endButton = Main.endButton;
        newButton = StartScreen.genButton;
        loadButton = StartScreen.loadButton;
        endButton.setFont(new Font("Monospaced", Font.PLAIN, 24));
        endButton.setText("Main Menu");
        newButton.setFont(new Font("Monospaced", Font.PLAIN, 24));
        newButton.setText("Rematch");
        loadButton.setFont(new Font("Monospaced", Font.PLAIN, 24));
        loadButton.setText("Load Savegame");
        
        add(title, new AbsoluteConstraints(256, 64, 448, 64));
        add(endButton, new AbsoluteConstraints(224, 192, 512, 64));
        add(newButton, new AbsoluteConstraints(224, 288, 512, 64));
        add(loadButton, new AbsoluteConstraints(224, 384, 512, 64));
    }
    
    /**
     * Initiates lose title
     * @param player 
     * @author Martin Rivard
     */
    private static void initLoseTitle(boolean player){
        if (player){
            title.setBorder(new LineBorder(Color.RED, 1));
            title.setForeground(Color.RED);
        }else {
            title.setBorder(new LineBorder(Color.BLUE, 1));
            title.setForeground(Color.BLUE);
        }
        title.setBackground(Color.BLACK);
        title.setFont(new Font("Monospaced", Font.PLAIN, 32));
    }
}
