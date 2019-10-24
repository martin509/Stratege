
package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import main.unit.Unit;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

/**
 * Organizes tile and unit informations and manages button display
 * @author Alexander Dingwall
 * @Since Jan 10, 2016
 */
public class InfoPanel extends JPanel {
    
    // location properties
    int x;
    int y;
    
    // dimension properties
    int width;
    int height;
    
    // description properties
    public String label;
    public String title;
    public String desc;
    protected String type;
    public Unit unit;
    public Tile tile;
    
    // display properties
    private JTextArea health_label;
    public JPanel health_bar;
    private JButton[] buttons;
    public JLabel label_container;
    public JTextArea title_container;
    public JTextArea desc_container;
    public JTextArea[] cost_containers;
    public JLabel defence_icon;
    public JLabel offence_icon;
    public JLabel level_icon;
    private JLabel resource_icon;
    public JTextArea defence_container;
    public JTextArea offence_container;
    public JTextArea level_container;
    private JLabel resource_container;
    
    //URL defence_url = InfoPanel.class.getResource("defence_icon.png");
    //URL offence_url = InfoPanel.class.getResource("attack_icon.png");
    //URL level_url = InfoPanel.class.getResource("levels_icon.png");
    //URL resource_url = InfoPanel.class.getResource("money_icon.png");
    
    /**
     * Initializes display variables for the panel
     * @param x
     * @param y
     * @param width
     * @param height
     * @param label
     * @author Alexander Dingwall
     */
    public InfoPanel(int x, int y, int width, int height, String label) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.label = label;
        this.title = "";
        this.desc = "";
        
        this.buttons = new JButton[6];
        
        setBackground(Color.BLACK);
        setLayout(new AbsoluteLayout());
        
        label_container = new JLabel(this.label);
        label_container.setFont(new Font("Monospaced", Font.PLAIN, 12));
        label_container.setBackground(Color.BLACK);
        label_container.setForeground(Color.YELLOW);
        label_container.setLocation(5,3);
        label_container.setSize(this.width-6, getStringDimensions(label_container.getText())[1]);
        add(label_container, new AbsoluteConstraints(label_container.getX(), label_container.getY(), label_container.getWidth(), label_container.getHeight()));
    }
    
    /**
     * Separate constructor for non-player info panels
     * @param x
     * @param y
     * @param width
     * @param height
     * @param label
     * @param unit 
     * @param tile
     * @author Alexander Dingwall
     */
    public InfoPanel(int x, int y, int width, int height, String label, Unit unit, Tile tile) {
        this(x,y,width,height, label);
        
        type = "Normal";
        
        this.unit = unit;
        this.tile = tile;
        
        // sets location relative to label_container
        title_container = new JTextArea(this.title);
        title_container.setFont(new Font("Monospaced", Font.PLAIN, 12));
        title_container.setBackground(Color.BLACK);
        title_container.setForeground(Color.GREEN);
        title_container.setLocation(5,label_container.getY() + label_container.getHeight());
        title_container.setSize((int) this.width - (int) ((double) this.width / 1.7), getStringDimensions(title_container.getText())[1]);
        add(title_container, new AbsoluteConstraints(title_container.getX(), title_container.getY(), title_container.getWidth(), title_container.getHeight()));
        
        // sets location relative to title_container (below)
        desc_container = new JTextArea(this.desc);
        desc_container.setFont(new Font("Monospaced", Font.PLAIN, 12));
        desc_container.setBackground(Color.BLACK);
        desc_container.setForeground(Color.GREEN);
        desc_container.setLineWrap(true);
        desc_container.setWrapStyleWord(true);
        desc_container.setLocation(5, title_container.getY() + title_container.getHeight());
        desc_container.setSize(this.width-6, 36);
        add(desc_container, new AbsoluteConstraints(desc_container.getX(), desc_container.getY(),desc_container.getWidth(), desc_container.getHeight()));
        
        // sets location relative to title_container (to the right)
        health_bar = new JPanel();
        health_bar.setLocation(title_container.getX() + title_container.getWidth()+30, title_container.getY()+7);
        health_bar.setSize(85,7);
        health_bar.setBackground(Color.BLACK);
         
        // sets location relative to health_bar (to the left)
        health_label = new JTextArea("HP:");
        health_label.setFont(new Font("Monospaced", Font.PLAIN, 12));
        health_label.setBackground(Color.BLACK);
        health_label.setForeground(Color.GREEN);
        health_label.setSize(23, 23);
        health_label.setLocation(health_bar.getX() - health_label.getWidth(), title_container.getY());
        
        // sets location relative to desc_container
        defence_icon = new JLabel();
        //URL defence_url = InfoPanel.class.getResource("defence_icon.png");
        defence_icon.setIcon(new ImageIcon(InfoPanel.class.getResource("defence_icon.png")));
        defence_icon.setLocation(141, desc_container.getY() + desc_container.getHeight()+8);
        defence_icon.setSize(16,16);
        
        // sets location relative to defence_icon
        defence_container = new JTextArea();
        defence_container.setFont(new Font("Monospaced", Font.PLAIN, 12));
        defence_container.setBackground(Color.BLACK);
        defence_container.setForeground(Color.GREEN);
        defence_container.setSize(100, 19);
        defence_container.setLocation(defence_icon.getX() + defence_icon.getWidth() + 3, defence_icon.getY());
        
        // sets location relative to defence_icon
        offence_icon = new JLabel();
        //URL offence_url = InfoPanel.class.getResource("attack_icon.png");
        offence_icon.setIcon(new ImageIcon(InfoPanel.class.getResource("attack_icon.png")));
        offence_icon.setLocation(defence_icon.getX(), defence_icon.getY() + defence_icon.getHeight()+5);
        offence_icon.setSize(16,16);
        
        // sets location relative to offence_icon
        offence_container = new JTextArea();
        offence_container.setFont(new Font("Monospaced", Font.PLAIN, 12));
        offence_container.setBackground(Color.BLACK);
        offence_container.setForeground(Color.GREEN);
        offence_container.setSize(100, 19);
        offence_container.setLocation(offence_icon.getX() + offence_icon.getWidth() + 3, offence_icon.getY());
        
        // sets location relative to offence_icon
        level_icon = new JLabel();
        //URL level_url = InfoPanel.class.getResource("levels_icon.png");
        level_icon.setIcon(new ImageIcon(InfoPanel.class.getResource("levels_icon.png")));
        level_icon.setLocation(offence_icon.getX(), offence_icon.getY() + offence_icon.getHeight()+5);
        level_icon.setSize(16,16);
        
        // sets location relative to level_icon
        level_container = new JTextArea();
        level_container.setFont(new Font("Monospaced", Font.PLAIN, 12));
        level_container.setBackground(Color.BLACK);
        level_container.setForeground(Color.GREEN);
        level_container.setSize(100, 19);
        level_container.setLocation(level_icon.getX() + level_icon.getWidth() + 3, level_icon.getY());
        
        // sets location relative to offence_icon
        resource_icon = new JLabel();
        //URL resource_url = InfoPanel.class.getResource("money_icon.png");
        resource_icon.setIcon(new ImageIcon(InfoPanel.class.getResource("money_icon.png")));
        resource_icon.setSize(16,16);
        
        // sets location relative to resource_icon
        resource_container = new JLabel();
        resource_container.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resource_container.setBackground(Color.BLACK);
        resource_container.setForeground(Color.GREEN);
        resource_container.setSize(100, 19);
    }
    
    /**
     * Returns the width and height of a string in pixels, taking into account the font.
     * @param str
     * @return int[] = {width, height}
     * @author Alexander Dingwall
     */
    public int[] getStringDimensions(String str) {
        AffineTransform affinetransform = new AffineTransform();     
        FontRenderContext frc = new FontRenderContext(affinetransform,true,true);     
        Font font = new Font("Monospaced", Font.PLAIN, 12);
        
        return new int[]{(int)(font.getStringBounds(str, frc).getWidth()), (int)(font.getStringBounds(str, frc).getHeight())};
    }
    
    /**
     * Draws a green border around the edge of the panel.
     * @param g
     * @author Alexander Dingwall
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // draws green border
        g.setColor(Color.GREEN);
        g.drawRect(0, 0, this.width-1, this.height-1);
    }
    
    /**
     * Ensures buttons are displayed correctly.
     * @author Alexander Dingwall
     */
    @Override
    public void repaint() {
        super.repaint();
        if (this.type != null && !this.type.equals("Normal")) return; // stops PlayerInfoPanel.repaint from running this method as well
                
        if (this.getComponents().length > 0) {
            removeAll();
            
            add(label_container, new AbsoluteConstraints(label_container.getX(), label_container.getY(), label_container.getWidth(), label_container.getHeight()));
            add(title_container, new AbsoluteConstraints(title_container.getX(), title_container.getY(), title_container.getWidth(), title_container.getHeight()));
            add(desc_container, new AbsoluteConstraints(desc_container.getX(), desc_container.getY(),desc_container.getWidth(), desc_container.getHeight()));
            
            // unit info panel specific rendering
            if (this.unit != null) {
                // adds display information if panel should not be
                if (this.buttons[0] != null) {
                    defence_icon.setLocation(141, desc_container.getY() + desc_container.getHeight()+8);
                    defence_container.setLocation(defence_icon.getX() + defence_icon.getWidth() + 3, defence_icon.getY());
                    offence_icon.setLocation(defence_icon.getX(), defence_icon.getY() + defence_icon.getHeight()+5);
                    offence_container.setLocation(offence_icon.getX() + offence_icon.getWidth() + 3, offence_icon.getY());
                    defence_container.setText("Defence: " + Tile.focused_unit.defence);
                    offence_container.setText("Offence: " + Tile.focused_unit.offence);
                    level_container.setText("Level: " + Tile.focused_unit.level);
                    
                    add(defence_icon, new AbsoluteConstraints(defence_icon.getX(), defence_icon.getY(), defence_icon.getWidth(), defence_icon.getHeight()));
                    add(defence_container, new AbsoluteConstraints(defence_container.getX(), defence_container.getY(),defence_container.getWidth(), defence_container.getHeight()));
                    add(offence_icon, new AbsoluteConstraints(offence_icon.getX(), offence_icon.getY(),offence_icon.getWidth(), offence_icon.getHeight()));
                    add(offence_container, new AbsoluteConstraints(offence_container.getX(), offence_container.getY(),offence_container.getWidth(), offence_container.getHeight()));
                    add(level_icon, new AbsoluteConstraints(level_icon.getX(), level_icon.getY(),level_icon.getWidth(), level_icon.getHeight()));
                    add(level_container, new AbsoluteConstraints(level_container.getX(), level_container.getY(),level_container.getWidth(), level_container.getHeight()));

                    add(health_label, new AbsoluteConstraints(health_label.getX(), health_label.getY(), health_label.getWidth(), health_label.getHeight()));
                    if (health_bar != null) add(health_bar, new AbsoluteConstraints(title_container.getX() + title_container.getWidth() + 30, title_container.getY()+6, this.health_bar.getWidth(), this.health_bar.getHeight()));
                }

                // adds buttons, checking which ones should be enabled/disabled
                for (JButton b : this.buttons) {
                    if (b != null) {
                        if (this.buttons[0].getText().equals("Attack")) {
                            // disables buttons if not unit's player's turn
                            if (this.unit.player.turn) {
                                b.setBorder(new LineBorder(Color.GREEN, 1));
                                b.setEnabled(true);
                            } else {
                                b.setBorder(new LineBorder(Color.GRAY, 1));
                                b.setEnabled(false);
                            }
                        }
                        b.setBackground(Color.BLACK);
                        add(b, new AbsoluteConstraints(b.getX(), b.getY(), b.getWidth(), b.getHeight()));
                        setComponentZOrder(b, 0);
                    }
                }

                // displays create unit display by adding cost containers and removing other info
                for (int b=0; b<this.buttons.length; b++) {
                    if (buttons[0] != null && cost_containers != null && cost_containers[b] != null) { 
                        add(cost_containers[b], new AbsoluteConstraints(cost_containers[b].getX(), cost_containers[b].getY(), cost_containers[b].getWidth(), cost_containers[b].getHeight()));

                        // removes extra unit panel info
                        remove(defence_icon);
                        remove(defence_container);
                        remove(offence_icon);
                        remove(offence_container);
                        remove(level_icon);
                        remove(level_container);
                    }
                }
            }
            
            // tile info panel specific rendering
            if (this.tile != null) {
                defence_icon.setLocation(5, desc_container.getY() + desc_container.getHeight() + 5);
                defence_container.setLocation(defence_icon.getX() + defence_icon.getWidth() + 5, defence_icon.getY());
                offence_icon.setLocation(5, defence_icon.getY() + defence_icon.getHeight() + 5);
                offence_container.setLocation(offence_icon.getX() + offence_icon.getWidth() + 5, offence_icon.getY());
                resource_icon.setLocation(5, offence_icon.getY() + offence_icon.getHeight() + 5);
                resource_container.setLocation(resource_icon.getX() + resource_icon.getWidth() + 5, resource_icon.getY());
                
                defence_container.setText((this.tile.getDefenceBonus() >= 0 ? "+" + this.tile.getDefenceBonus() : this.tile.getDefenceBonus()) + " Defence");
                offence_container.setText((this.tile.getOffenceBonus() >= 0 ? "+" + this.tile.getOffenceBonus() : this.tile.getOffenceBonus()) + " Offence");
                resource_container.setText((this.tile.isResource() ? "+300" : "+0") + " Resources");
                
                add(defence_icon, new AbsoluteConstraints(defence_icon.getX(), defence_icon.getY(), defence_icon.getWidth(), defence_icon.getHeight()));
                add(defence_container, new AbsoluteConstraints(defence_container.getX(), defence_container.getY(),defence_container.getWidth(), defence_container.getHeight()));
                add(offence_icon, new AbsoluteConstraints(offence_icon.getX(), offence_icon.getY(),offence_icon.getWidth(), offence_icon.getHeight()));
                add(offence_container, new AbsoluteConstraints(offence_container.getX(), offence_container.getY(),offence_container.getWidth(), offence_container.getHeight()));
                add(resource_icon, new AbsoluteConstraints(resource_icon.getX(), resource_icon.getY(),resource_icon.getWidth(), resource_icon.getHeight()));                
                add(resource_container, new AbsoluteConstraints(resource_container.getX(), resource_container.getY(),resource_container.getWidth(), resource_container.getHeight()));                
            }
        }
    }
    
    /**
     * Changes and updates the title text
     * @param title
     * @author Alexander Dingwall
     */
    public void setTitle(String title) {
        this.title = title;
        title_container.setText(title);
        health_label.setText(title.equals("") ? "" : "HP:"); // sets HP label to empty when unit is not set (for tile info panel)
    }
    
    /**
     * Changes and updates the description text
     * @param desc
     * @author Alexander Dingwall
     */
    public void setDescription(String desc) {
        this.desc = desc;
        desc_container.setText(desc);
    }
    
    /**
     * Displays the button property for each unit. Also displays the six unit creation
     * buttons a little differently.
     * @param buttons
     * @author Alexander Dingwall
     */
    public void setButtons(JButton[] buttons) {
        // clears buttons when passed a null value
        if (buttons != null) {
            this.buttons = buttons;
        } else {
            this.buttons = null;
            return;
        }
        
        // true if main info panel menu, false if unit creation menu
        boolean button_type = buttons[0].getText().equals("Attack");
        cost_containers = new JTextArea[6];
        
        // sets location of button relative to the location of the button before it
        // if button is first in the array, sets location relative to desc_container
        for (int b=0; b<this.buttons.length; b++) {
            if (this.buttons[b] != null) {
                if (b >= 1 && b < 3) { // second and third button
                    
                    if (button_type) // if main unit screen
                        this.buttons[b].setLocation(5, this.buttons[b-1].getY() + this.buttons[b-1].getHeight()+8);
                    else // if unit creation screen
                        this.buttons[b].setLocation(9, this.buttons[b-1].getY() + this.buttons[b-1].getHeight()+17);
                
                } else if (b >= 3 && b < 6) { // last three buttons
                    
                    if (button_type) // if main unit screen, prints last button at the bottom
                        this.buttons[b].setLocation(this.buttons[1].getWidth() + 7, this.buttons[b-1].getY());
                    else // if unit creation screen, prints buttons normally
                        this.buttons[b].setLocation(this.buttons[1].getWidth() + 15, this.buttons[b-3].getY());
                
                } else { // first button
                    
                    if (button_type) // if main unit screen, prints normally
                        this.buttons[b].setLocation(5, desc_container.getY() + desc_container.getHeight() + 8);
                    else { // if unit creation screen, prints closer to the top to leave room for cost labels
                        this.buttons[b].setLocation(9, desc_container.getY() + desc_container.getHeight() - 8);
                    }
                }
                
                // if unit creation menu
                if (!button_type) {
                    Player player = this.unit.player;
                        
                    String cost = "";
                    boolean enabled = false;
                    // gets unit cost from unit_definitions.txt, determines whether unit is unlocked
                    // based on city level.
                    switch (buttons[b].getText()) {
                        case "Infantry":    
                            // gets cost
                            cost = Unit.definitions[1][10]; 
                            // checks if unlocked or not
                            if (player.city.level >= Integer.parseInt(Unit.definitions[1][12])) 
                                enabled = true;
                            break;
                        case "Medic":       
                            cost = Unit.definitions[2][10]; 
                            if (player.city.level >= Integer.parseInt(Unit.definitions[2][12]))
                                enabled = true;
                            break;
                        case "Tank":        
                            cost = Unit.definitions[3][10]; 
                            if (player.city.level >= Integer.parseInt(Unit.definitions[3][12]))
                                enabled = true;
                            break;
                        case "Worker":      
                            cost = Unit.definitions[4][10]; 
                            if (player.city.level >= Integer.parseInt(Unit.definitions[4][12]))
                                enabled = true;
                            break;
                        case "Artillery":   
                            cost = Unit.definitions[5][10];
                            if (player.city.level >= Integer.parseInt(Unit.definitions[5][12])) 
                                enabled = true;                       
                            break;
                        case "Back":        
                            cost = "0"; 
                            enabled = true;
                            break;
                    }
                        
                    // inits container
                    JTextArea cost_container = new JTextArea("Cost: $" + cost);
                    cost_container.setFont(new Font("Monospaced", Font.PLAIN, 10));
                    cost_container.setBackground(Color.BLACK);
                    cost_container.setForeground(Color.YELLOW);
                    cost_container.setLocation(this.buttons[b].getX() + 10, this.buttons[b].getY() + this.buttons[b].getHeight());
                    cost_container.setSize(this.buttons[b].getWidth(), getStringDimensions(cost_container.getText())[1]);
                    
                    // enables or disables buttons
                    if (!enabled) {
                        buttons[b].setEnabled(false);
                        buttons[b].setBorder(new LineBorder(Color.GRAY, 1));
                        cost_container.setForeground(Color.GRAY);
                    } else {
                        
                        buttons[b].setEnabled(true);
                        buttons[b].setBorder(new LineBorder(Color.GREEN, 1));
                        cost_container.setForeground(Color.YELLOW);
                    }
                    
                    // adds cost container
                    add(cost_container, new AbsoluteConstraints(cost_container.getX(), cost_container.getY(), cost_container.getWidth(), cost_container.getHeight()));
                    
                    // adds container to array for repainting
                    cost_containers[b] = cost_container;
                }
                
                // adds create button
                add(this.buttons[b], new AbsoluteConstraints(this.buttons[b].getX(), this.buttons[b].getY(), this.buttons[b].getWidth(), this.buttons[b].getHeight()));
            }
        }
        
        repaint();
    }
    
    /**
     * Creates a clone of the unit's health bar to display
     * @param health_bar
     * @author Alexander Dingwall
     */
    public void setHealthBar(JPanel health_bar) {
        // clears old health bar
        this.health_bar = new JPanel();
        
        // reference of the health bar's meter
        JPanel meter_ref = (JPanel) health_bar.getComponent(0);
        
        // clone of the health bar's meter
        JPanel meter = new JPanel();
        
        // copies health bar's properties
        this.health_bar.setLayout(new AbsoluteLayout());
        this.health_bar.setSize(health_bar.getWidth(), health_bar.getHeight());
        this.health_bar.setLocation(health_bar.getX(), health_bar.getY());
        this.health_bar.setBackground(health_bar.getBackground());
        this.health_bar.setBorder(health_bar.getBorder());
        
        // updates meter
        meter.setSize(meter_ref.getWidth(), meter_ref.getHeight());
        meter.setLocation(meter_ref.getX(), meter_ref.getY());
        meter.setBackground(meter_ref.getBackground());
        
        // adds meter
        this.health_bar.add(meter, new AbsoluteConstraints(0,0, meter.getWidth(), meter.getHeight()));
        
        repaint(); 
    }
    
    /**
     * Changes the size of the panel within the health_bar panel.
     * @param health
     * @author Alexander Dingwall
     */
    public void updateHealthBar(int health) {
        if (health_bar == null) return;
        
        // meter reference
        JPanel meter = (JPanel) health_bar.getComponent(0);
        
        // updates size
        health_bar.remove(meter);
        meter.setSize(health, 7);
        health_bar.add(meter, new AbsoluteConstraints(0,0, meter.getWidth(), meter.getHeight()));
        
        repaint();
    }
    
    /**
     * Clears each component from the info panel and only adds the label back.
     * @author Alexander Dingwall
     */ 
    public void clear() {
        
        Main.board.unit_info.setTitle("");
        Main.board.unit_info.setDescription("");
        
        for (JButton b : this.buttons) {
            if (b != null) remove(b);
        }
        
        remove(this.health_bar);
        this.buttons = new JButton[6];
       
        add(label_container, new AbsoluteConstraints(label_container.getX(), label_container.getY(), label_container.getWidth(), label_container.getHeight()));
        repaint();
    }
}
