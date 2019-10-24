
package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.MatteBorder;
import main.unit.*;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

/**
 * Display information for each player
 * @author Alexander Dingwall
 * @since Jan 16, 2016
 */
public class PlayerInfoPanel extends InfoPanel {

    JLabel money_icon;
    JLabel city_icon;
    JLabel levels_icon;
    JLabel units_icon;
    JLabel unitsleft_icon;
    
    JTextArea money_container;
    JTextArea city_container;
    JTextArea levels_container;
    JTextArea units_container;
    JTextArea unitsleft_container;
    
    JScrollPane units_scrollpane;
    JPanel units_display;
    List<Unit> units;
    
    Player player;
    
    /**
     * Initializes display variables for the player info panels
     * @param x
     * @param y
     * @param width
     * @param height
     * @param label
     * @param player
     * @author Alexander Dingwall
     */
    public PlayerInfoPanel(int x, int y, int width, int height, String label, Player player) {
        super(x, y, width, height, label);
        
        type = "Player";
        
        this.player = player;
        
        // sets location relative to label_container
        title_container = new JTextArea(this.player.getName());
        title_container.setFont(new Font("Monospaced", Font.PLAIN, 12));
        title_container.setBackground(Color.BLACK);
        title_container.setForeground(Color.GREEN);
        title_container.setLocation(5,label_container.getY() + label_container.getHeight());
        title_container.setSize((int) this.width - (int) ((double) this.width / 1.7), getStringDimensions(title_container.getText())[1]);
        add(title_container, new AbsoluteConstraints(title_container.getX(), title_container.getY(), title_container.getWidth(), title_container.getHeight()));
        
        // sets location relative to title_container
        money_icon = new JLabel();
        money_icon.setIcon(new ImageIcon(PlayerInfoPanel.class.getResource("money_icon.png")));
        money_icon.setLocation(5, title_container.getY() + title_container.getHeight()+5);
        money_icon.setSize(16,16);
        add(money_icon, new AbsoluteConstraints(money_icon.getX(), money_icon.getY(), money_icon.getWidth(), money_icon.getHeight()));
    
        // sets location relative to money_icon
        money_container = new JTextArea("Money: $" + this.player.money);
        money_container.setFont(new Font("Monospaced", Font.PLAIN, 12));
        money_container.setBackground(Color.BLACK);
        money_container.setForeground(Color.GREEN);
        money_container.setLocation(money_icon.getX() + money_icon.getWidth() + 5, money_icon.getY());
        money_container.setSize((int) this.width - (int) ((double) this.width / 1.6) - 10, getStringDimensions(money_container.getText())[1]);
        add(money_container, new AbsoluteConstraints(money_container.getX(), money_container.getY(), money_container.getWidth(), money_container.getHeight()));
    
        // sets location relative to money icon
        city_icon = new JLabel();
        city_icon.setIcon(new ImageIcon(PlayerInfoPanel.class.getResource(this.player.team ? "blue_city_16x16.png" : "red_city_16x16.png")));
        city_icon.setLocation(5, money_icon.getY() + money_icon.getHeight()+8);
        city_icon.setSize(16,16);
        add(city_icon, new AbsoluteConstraints(city_icon.getX(), city_icon.getY(), city_icon.getWidth(), city_icon.getHeight()));
    
        // sets location relative to city_icon
        health_bar = new JPanel();
        health_bar.setLocation(city_icon.getX() + city_icon.getWidth() + 5, city_icon.getY() + 5);
        health_bar.setSize(85,7);
        add(health_bar, new AbsoluteConstraints(health_bar.getX(), health_bar.getY(), health_bar.getWidth(), health_bar.getHeight()));
    
        // sets location relative to health_bar
        units_icon = new JLabel();
        units_icon.setIcon(new ImageIcon(PlayerInfoPanel.class.getResource("units_icon.png")));
        units_icon.setLocation(health_bar.getX() + health_bar.getWidth() + 18, title_container.getY());
        units_icon.setSize(16,16);
        add(units_icon, new AbsoluteConstraints(units_icon.getX(), units_icon.getY(), units_icon.getWidth(), units_icon.getHeight()));
    
        // sets location relative to units_icon
        units_container = new JTextArea("Units: " + this.player.units.size());
        units_container.setFont(new Font("Monospaced", Font.PLAIN, 12));
        units_container.setBackground(Color.BLACK);
        units_container.setForeground(Color.GREEN);
        units_container.setLocation(units_icon.getX() + units_icon.getWidth() + 8, units_icon.getY());
        units_container.setSize((int) this.width - (int) ((double) this.width / 1.6), getStringDimensions(money_container.getText())[1]);
        add(units_container, new AbsoluteConstraints(units_container.getX(), units_container.getY(), units_container.getWidth(), units_container.getHeight()));
            
        // sets location relative to units_icon
        unitsleft_icon = new JLabel();
        unitsleft_icon.setIcon(new ImageIcon(PlayerInfoPanel.class.getResource("units_icon.png")));
        unitsleft_icon.setLocation(units_icon.getX(), money_icon.getY()+2);
        unitsleft_icon.setSize(16,16);
        add(unitsleft_icon, new AbsoluteConstraints(unitsleft_icon.getX(), unitsleft_icon.getY(), unitsleft_icon.getWidth(), unitsleft_icon.getHeight()));
    
        // sets location relative to unitsleft_icon
        unitsleft_container = new JTextArea("Unit Turns: " + this.player.units.size());
        unitsleft_container.setFont(new Font("Monospaced", Font.PLAIN, 12));
        unitsleft_container.setBackground(Color.BLACK);
        unitsleft_container.setForeground(Color.GREEN);
        unitsleft_container.setLocation(unitsleft_icon.getX() + unitsleft_icon.getWidth() + 5, unitsleft_icon.getY());
        unitsleft_container.setSize((int) this.width - (int) ((double) this.width / 1.6), getStringDimensions(money_container.getText())[1]);
        add(unitsleft_container, new AbsoluteConstraints(unitsleft_container.getX(), unitsleft_container.getY(), unitsleft_container.getWidth(), unitsleft_container.getHeight()));
            
        // sets location relative to units_icon
        levels_icon = new JLabel();
        //URL levels_url = PlayerInfoPanel.class.getResource("levels_icon.png");
        levels_icon.setIcon(new ImageIcon(InfoPanel.class.getResource("levels_icon.png")));
        levels_icon.setLocation(units_icon.getX(), city_icon.getY());
        levels_icon.setSize(16,16);
        add(levels_icon, new AbsoluteConstraints(levels_icon.getX(), levels_icon.getY(), levels_icon.getWidth(), levels_icon.getHeight()));
    
        // sets location relative to levels_icon
        levels_container = new JTextArea("Level: 1");
        levels_container.setFont(new Font("Monospaced", Font.PLAIN, 12));
        levels_container.setBackground(Color.BLACK);
        levels_container.setForeground(Color.GREEN);
        levels_container.setLocation(levels_icon.getX() + levels_icon.getWidth() + 5, levels_icon.getY());
        levels_container.setSize((int) this.width - (int) ((double) this.width / 1.6), getStringDimensions(money_container.getText())[1]);
        add(levels_container, new AbsoluteConstraints(levels_container.getX(), levels_container.getY(), levels_container.getWidth(), levels_container.getHeight()));
    
        
        // sets location relative to city_icon
        units_display = new JPanel();
        units_display.setLayout(new AbsoluteLayout());
        units_scrollpane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        units_scrollpane.setBorder(new MatteBorder(1,0,0,0,Color.GREEN));
        units_scrollpane.getVerticalScrollBar().setUnitIncrement(36);
        units_display.setLocation(1, city_icon.getY() + city_icon.getHeight() + 10);
        units_display.setSize(this.width - 2, this.height - units_display.getY() - 1);
        units_display.setBackground(Color.BLACK);
       
        units_scrollpane.setViewportView(units_display);
    }
    
    /**
     * Ensures buttons are displayed correctly.
     * @author Alexander Dingwall
     */
    @Override
    public void repaint() {
        super.repaint();
                
        if (this.getComponents().length > 0) {
            removeAll();
                    
            // updates display
            levels_container.setText("Level: " + this.player.city.level);
            money_container.setText("Money: $" + this.player.money);
            units_container.setText("Units: " + this.player.units.size());
            unitsleft_container.setText("Unit Turns: " + (this.player.units.size() - this.player.units_moved));
            updateUnitDisplay();
            
            // adds each component
            add(label_container, new AbsoluteConstraints(label_container.getX(), label_container.getY(), label_container.getWidth(), label_container.getHeight()));
            add(title_container, new AbsoluteConstraints(title_container.getX(), title_container.getY(), title_container.getWidth(), title_container.getHeight()));
            add(money_icon, new AbsoluteConstraints(money_icon.getX(), money_icon.getY(), money_icon.getWidth(), money_icon.getHeight()));
            add(money_container, new AbsoluteConstraints(money_container.getX(), money_container.getY(), money_container.getWidth(), money_container.getHeight()));
            add(city_icon, new AbsoluteConstraints(city_icon.getX(), city_icon.getY(), city_icon.getWidth(), city_icon.getHeight()));
            add(levels_icon, new AbsoluteConstraints(levels_icon.getX(), levels_icon.getY(), levels_icon.getWidth(), levels_icon.getHeight()));
            add(levels_container, new AbsoluteConstraints(levels_container.getX(), levels_container.getY(), levels_container.getWidth(), levels_container.getHeight()));
            add(units_icon, new AbsoluteConstraints(units_icon.getX(), units_icon.getY(), units_icon.getWidth(), units_icon.getHeight()));
            add(unitsleft_icon, new AbsoluteConstraints(unitsleft_icon.getX(), unitsleft_icon.getY(), unitsleft_icon.getWidth(), unitsleft_icon.getHeight()));
            add(units_container, new AbsoluteConstraints(units_container.getX(), units_container.getY(), units_container.getWidth(), units_container.getHeight()));
            add(unitsleft_container, new AbsoluteConstraints(unitsleft_container.getX(), unitsleft_container.getY(), unitsleft_container.getWidth(), unitsleft_container.getHeight()));
            add(units_scrollpane, new AbsoluteConstraints(units_display.getX(), units_display.getY(), units_display.getWidth(), units_display.getHeight()));
        
            health_bar.setLocation(city_icon.getX() + city_icon.getWidth() + 5, city_icon.getY() + 5);
            add(health_bar, new AbsoluteConstraints(health_bar.getX(), health_bar.getY(), health_bar.getWidth(), health_bar.getHeight()));
        
        }
    }
    
    /**
     * Updates the unit display in the player's display panels
     * @author Alexander Dingwall
     */
    public void updateUnitDisplay() {
        units_scrollpane.getComponent(0).setSize(0,0); // solves a small graphical bug
        units_display.setLocation(1, city_icon.getY() + city_icon.getHeight() + 10);
        units_display.setSize(this.width - 2, this.height - units_display.getY() - 1);
        
        // sorts units by type, then by location
        this.player.sortUnitsByType();
        
        units = this.player.units;
        units_display.removeAll();
        // loops through player.units and creates a container of info for each one.
        for (int u=0; u<units.size(); u++) {
            Unit un = units.get(u);
            
            final JPanel container = new JPanel();
            container.setLayout(new AbsoluteLayout());
            container.setSize(this.width-1, 36);
            container.setLocation(0,(u)*container.getHeight());
            container.setBackground(Color.BLACK);

            // unit icon
            JLabel icon = new JLabel();
            //URL icon_url = PlayerInfoPanel.class.getResource(un.path);
            icon.setIcon(new ImageIcon(un.path));
            container.add(icon, new AbsoluteConstraints(2,2,32,32));

            // unit location
            JLabel location_label = new JLabel("Location: (" + un.x + ", " + un.y + ")");
            location_label.setFont(new Font("Monospaced", Font.PLAIN, 11));
            location_label.setForeground(Color.GREEN);
            container.add(location_label, new AbsoluteConstraints(36,2,126,19));
            
            // unit defence
            JLabel defence_label = new JLabel();
            //URL defence_url = PlayerInfoPanel.class.getResource("defence_icon.png");
            defence_label.setIcon(new ImageIcon(PlayerInfoPanel.class.getResource("defence_icon.png")));
            container.add(defence_label, new AbsoluteConstraints(36,18,16,16));
            
            JLabel def_container = new JLabel(Integer.toString(un.defence));
            def_container.setFont(new Font("Monospaced", Font.PLAIN, 11));
            def_container.setForeground(Color.GREEN);
            container.add(def_container, new AbsoluteConstraints(36 + 16 + 2,17,40,19));
            
            // unit offence
            JLabel offence_label = new JLabel();
            //URL offence_url = PlayerInfoPanel.class.getResource("attack_icon.png");
            offence_label.setIcon(new ImageIcon(PlayerInfoPanel.class.getResource("attack_icon.png")));
            container.add(offence_label, new AbsoluteConstraints(74,18,16,16));
            
            JLabel off_container = new JLabel(Integer.toString(un.offence));
            off_container.setFont(new Font("Monospaced", Font.PLAIN, 11));
            off_container.setForeground(Color.GREEN);
            container.add(off_container, new AbsoluteConstraints(74 + 16 + 2,17,40,19));
            
            // unit level 
            JLabel level_label = new JLabel();
            //URL level_url = PlayerInfoPanel.class.getResource("levels_icon.png");
            level_label.setIcon(new ImageIcon(PlayerInfoPanel.class.getResource("levels_icon.png")));
            container.add(level_label, new AbsoluteConstraints(112, 18, 16, 16));
            
            JLabel lev_container = new JLabel(Integer.toString(un.level));
            lev_container.setFont(new Font("Monospaced", Font.PLAIN, 11));
            lev_container.setForeground(Color.GREEN);
            container.add(lev_container, new AbsoluteConstraints(112 + 16 + 2,17,40,19));
            
            // unit moved
            JLabel moved_label = new JLabel("Moved: ");
            moved_label.setFont(new Font("Monospaced", Font.PLAIN, 11));
            moved_label.setForeground(Color.GREEN);
            container.add(moved_label, new AbsoluteConstraints(164,19,50,8));
            
            JLabel moved_icon = new JLabel();
            moved_icon.setIcon(new ImageIcon(PlayerInfoPanel.class.getResource(un.moved ? "check_mark.png" : "x_icon.png")));
            container.add(moved_icon, new AbsoluteConstraints(164+40 + 5,4,32,32));
            
            // add event listener, highlights and sets corresponding unit to the focused unit
            container.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent me) {
                    
                    for (Component c : units_display.getComponents()) {
                        ((JPanel) c).setBorder(null);
                    }

                    un.getTile().setFocusedTile();
                    container.setBorder(new MatteBorder(1,0,1,0,Color.yellow));
                }
            });

            units_display.add(container, new AbsoluteConstraints(container.getX(), container.getY(), container.getWidth(), container.getHeight()));
        }
    }
}
