
package main.unit;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import main.*;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import java.net.URL;

/** 
 * Defines properties and methods for each unit
 * @author Alexander Dingwall
 * @since Dec 20, 2015
 */
abstract public class Unit implements UnitInterface {
    
    private static int num_units = 0;
    
    // holds unit definitions
    public static String[][] definitions;
        
    // location properties
    public int x;
    public int y;
    private int[] location;
    private Tile tile; 
    
    // description properties
    public String name;
    public String desc;
    public URL path;
    public int health;
    public int max_health;
    public int level;
    public int cost;
    public int upgrade_cost;
    public int unlock_level;
    public Player player;
    public boolean moved;
    
    // combat properties
    public int defence;
    public int offence;
    public int defence_with_bonus; // includes tile's defence bonus
    public int offence_with_bonus; // includes tile's offence bonus
    
    // texture properties
    public JLabel image; // unit's texture
    public JPanel health_bar_b; // displays unit health
    public JPanel health_bar_f; 
    public JPanel tile_health_bar_b; // displays unit health on tile
    public JPanel tile_health_bar_f;
    public JButton ability_button; // attack button
    public JButton pass_button; // skips turn
    public JButton upgrade_button; // levels up
    public JButton[] buttons; // ability buttons for each unit.
    public static int button_height = 30;
    
    /**
     * Read from unit definitions file and store in the definitions array
     */
    static {
        try {
            InputStream in = Unit.class.getResourceAsStream("unit_definitions.txt");
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);
            
            definitions = new String[6][13];
            
            String line;
            for (int u=0; u<6; u++) {
                for (int l=0; l<13; l++) {
                    line = br.readLine();
                    if (line != null) {
                        definitions[u][l] = line.split("\\s+")[1];
                    }
                }
                definitions[u][9] = definitions[u][9].replaceAll("_", " ");
            }
        } catch(IOException ioe) {
            Main.board.println(ioe.getMessage());
        }
    }
    
    /**
     * Main constructor for the abstract class Unit. Defines unit location
     * @param x starting x position for the unit, as an index in a two dimensional array of tiles 
     * @param y starting y position for the unit, as an index in a two dimensional array of tiles
     * @param player the player that the unit belongs to
     * @author Alexander Dingwall
     */
    public Unit(int x, int y, Player player) {
        num_units++;
        
        // init properties
        Main.units.add(this); 
        
        this.player = player;
        this.player.units.add(this);
        
        this.x = x;
        this.y = y;
        this.location = new int[]{x,y};
        
        this.level = 1;      
                
        // init ability button
        ability_button = new JButton("Attack");
        Main.initButtonUI(ability_button, false, Color.GREEN);
        ability_button.setSize(128,Unit.button_height);
        
        ability_button.addActionListener((ActionEvent ae) -> {
            if (!moved) {
                Main.board.println(name + " started an attack!");
                Tile.attacker = getUnit();
            } else {
                Main.board.println("This unit's turn is over.");
            }
        });
        
        // init pass button
        pass_button = new JButton("Pass");
         Main.initButtonUI(pass_button, false, Color.GREEN);
        pass_button.setSize(128,Unit.button_height);
        
        pass_button.addActionListener((ActionEvent ae) -> {
            if (!moved) { 
                Main.board.println(name + " passed their turn.");
                endTurn();
                
                for (Unit u : this.player.units) {
                    if (!u.moved) {
                        u.tile.setFocusedTile();
                    }
                }
                
            } else {
                Main.board.println("This unit's turn is over");
            }
            
        });
        
        // init upgrade button
        upgrade_button = new JButton("Upgrade");
         Main.initButtonUI(upgrade_button, false, Color.GREEN);
        upgrade_button.setSize(128,Unit.button_height);
        
        upgrade_button.addActionListener((ActionEvent ae) -> {
            if (this.level == 3) {
                Main.board.println(this.name + " is already at the max level!");
            } else if (this.player.money < this.upgrade_cost) {
                Main.board.println("You do not have enough money to do that!");
            } else {
                this.levelUp();

                this.player.money -= this.upgrade_cost;

                if (this.player.team) 
                    Main.board.player1_info.repaint();
                else 
                    Main.board.player2_info.repaint();

                this.upgrade_cost *= 1.5;
                this.upgrade_button.setText("Upgrade ($" + this.upgrade_cost + ")");

                Main.board.println(this.name + " upgraded to level " + this.level + "!");
            }
        });
        
        // init array
        buttons = new JButton[6];
        
        // update array reference
        buttons[0] = ability_button;
        buttons[1] = pass_button;
        buttons[2] = upgrade_button;
        
        // init health bar
        health_bar_b = new JPanel();
        health_bar_b.setSize(85,7);
        health_bar_b.setBackground(Color.RED);
        health_bar_b.setLayout(new AbsoluteLayout());
        health_bar_b.setBorder(new LineBorder(Color.GREEN, 1));
             
        health_bar_f = new JPanel();
        health_bar_f.setSize(85,7);
        health_bar_f.setBackground(Color.GREEN);
        
        health_bar_b.add(health_bar_f, new AbsoluteConstraints(0,0, health_bar_f.getWidth(), health_bar_f.getHeight()));
        
        // init tile health bar
        tile_health_bar_b = new JPanel();
        tile_health_bar_b.setSize(Main.board.getTileWidth()-5, 3);
        tile_health_bar_b.setBackground(Color.RED);
        tile_health_bar_b.setLayout(new AbsoluteLayout());
             
        tile_health_bar_f = new JPanel();
        tile_health_bar_f.setSize(Main.board.getTileWidth()-5, 3);
        tile_health_bar_f.setBackground(Color.GREEN);
        
        tile_health_bar_b.add(tile_health_bar_f, new AbsoluteConstraints(0,0, tile_health_bar_f.getWidth(), tile_health_bar_f.getHeight()));
    }
    
    /**
     * Constructor including parameters for all properties
     * @param name name of the unit
     * @param desc
     * @param x x index, in a two dimensional array of tiles
     * @param y y index, in a two dimensional array of tiles
     * @param level the level of the unit
     * @param health health of the unit
     * @param defence defence stat
     * @param offence offence stat
     * @param player the player that the unit belongs 
     * @author Alexander Dingwall
     */
    public Unit(String name, String desc, int x, int y, int level, int health, int defence, int offence, Player player) {
        num_units++;
        Main.units.add(this); 
        
        this.name = name;
        this.desc = desc;
        this.level = level;
        this.x = x;
        this.y = y;
        this.location = new int[]{x,y};
        this.health = health;
        this.defence = defence;
        this.offence = offence;
        this.player = player;
    }
    
    /**
     * Calculates the distance between two units using Tile.getDistance()
     * @param u1 the first unit
     * @param u2 the second unit
     * @return the distance between the two given units 
     * @author Alexander Dingwall
     */
    public static int getDistance(Unit u1, Unit u2) {
        return Tile.getDistance(u1.tile, u2.tile);
    }
    
    /**
     * Calculate the amount of damage to deal to the given unit, then subtracts 
     * that damage from the given unit's health. Units must be within (int) max tile of
     * each other
     * @param unit the unit to attack
     * @param max the max distance a unit can attack from
     * @author Alexander Dingwall
     */
    @Override
    public void attack(Unit unit, int max) {
        if (this.equals(unit)) {
            Main.board.println("You cannot attack yourself!");
        } else if (unit == null) {
            Main.board.println("There is no one there to attack!");
        } else if (this.player.equals(unit.player)) {
            Main.board.println("You cannot attack your own team!");
        } else if (Unit.getDistance(this, unit) > max) {
            Main.board.println("That unit is too far away!");
        } else {
            
            Random rand = new Random();
            
            // calculates variables def_ratio = offence / defence (including bonuses)
            // percent variance will be a random number between -0.1 and +0.1 (-10% and +10%)
            double def_ratio =  ((double) (this.offence + this.tile.getOffenceBonus()) / (unit.defence + unit.tile.getDefenceBonus()));
            double percent_variance = (-10 + rand.nextInt(20)) / 100.0;
            
            // calculates damage: ( attack + (attack * percent_variance) )
            int damage = (int) (this.offence * def_ratio + (this.offence * def_ratio) * percent_variance);

            unit.doDamage(damage);
            
            // updates information panels
            Main.board.player1_info.updateHealthBar((int) (((double) Main.player1.city.health / Main.player1.city.max_health) * 85));
            Main.board.player2_info.updateHealthBar((int) (((double) Main.player2.city.health / Main.player2.city.max_health) * 85));
            
            unit.setInfoPanel();
            unit.updateTileHealthBar();
            
            // prints output message
            Main.board.println(this.player.getName() + "'s " + this.name + " dealt " + damage + " damage against " + (unit.name.equals(unit.player.getName() + "'s City") ? unit.name : (unit.player.getName() + "'s " + unit.name)));
        
            endTurn();
        }
    }
    
    /**
     * Attack method, called by most units. Max distance set to 1.
     * @param unit unit to be attacked
     * @author Alexander Dingwall
     */
    public void attack(Unit unit) {
        this.attack(unit, 1);
    }

    /**
     * Checks if move is valid, then changes the unit's location.
     * @param x new x position
     * @param y new y position
     * @return true if move was successful, false if not
     * @author Alexander Dingwall
     */
    @Override
    public boolean move(int x, int y) {
        // calculates new location and stores in array
        int[] new_location = new int[]{x, y};
        if (this.x == x && this.y == y) {
            return false;
        // checks new location for another unit
        } else if (Board.tiles[x][y].getUnit() != null) {
            Main.board.println("That location is already taken.");
            return false;
        // if new location is out of bounds
        } else if (new_location[0] < 0 || new_location[1] < 0 || new_location[0] > Main.board.getBoardWidth()-1 || new_location[1] > Main.board.getBoardHeight()-1) {
            Main.board.println("That location is out of bounds.");
            return false;
        // if not players turn
        } else if (!this.player.turn) {
            Main.board.println("It is not this player's turn.");
            return false;
        // if unit has already moved
        } else if (this.moved) {
            Main.board.println("This unit's turn is over!");
            return false;
        }
        
        // removes tile from resources list if necessary
        if (this.tile.isResource() && this.name.equals("Worker")) {
            this.player.resources.remove(this.tile);
            this.tile.repaint();
        }
        
        // sets unit property of current tile to null
        this.tile.setUnit(null);
        
        //updates location
        setLocation(new_location);
        setTile(Board.tiles[this.x][this.y]);
        endTurn();
        
        // updates defence and offence values with the new tile bonus
        this.defence_with_bonus = (int) (this.defence * this.tile.getDefenceBonus());
        this.offence_with_bonus = (int) (this.offence * this.tile.getOffenceBonus());
            
        Main.board.println("Moved \"" + this.name + "\" to " + x + ", " + y + '.');
        
        // adds tile to resource list if necessary
        if (this.tile.isResource() && this.name.equals("Worker")){
            Main.board.println(this.player.getName() + " took control of an oil rig!");
            this.player.resources.add(this.tile);
            this.tile.repaint();
        } 
        
        System.out.println("\n\n--");
        for (Tile t : this.player.resources) {
            System.out.println(t.getName());
        }
        System.out.println("--");
        
        return true;
    }
    
    /**
     * Updates tile and info panel health bars when attacked. Also checks
     * for a death
     * @param damage
     * @author Alexander Dingwall
     */
    public void doDamage(int damage) {
        this.health -= damage;
        
        this.setInfoPanel();
        Main.board.unit_info.updateHealthBar(this.health);
        this.updateTileHealthBar();
        
        // checks if defending unit died
        if (this.health <=0) {
            this.kill();
            this.tile.setFocusedTile();
        }
    }
    
    /**
     * Increments level by one and updates the stats
     * @author Alexander Dingwall
     */
    @Override
    public void levelUp() {
        this.level++;
        this.setInfoPanel();
    }
    
    /**
     * Updates the unit info panel with information for this tile
     * @author Alexander Dingwall
     */
    public void setInfoPanel() {
        Main.board.unit_info.unit = this;
        
        Main.board.unit_info.setTitle(this.name);
        Main.board.unit_info.setDescription(this.desc);
        Main.board.unit_info.setButtons(this.buttons);
        Main.board.unit_info.setHealthBar(this.health_bar_b);
        Main.board.unit_info.updateHealthBar((int) (((double) this.health / this.max_health) * 85));
    }
    
    /**
     * Move method called by units with ability to move more than 1 tile.
     * @param tile tile to be moved to
     * @param max max distance 
     * @author Alexander Dingwall
     */
    public void move(Tile tile, int max) {
        if (Tile.getDistance(this.tile, tile) > max) {
            Main.board.println("That tile is too far away!");
            return;
        }
        
        move(tile.x, tile.y);
    }
    
    /**
     * Move method called by normal units
     * @param tile tile to be moved to
     * @author Alexander Dingwall
     */
    public void move(Tile tile) {
        move(tile, 1);
    }
    
    /**
     * Removes the unit from unit lists and the unit's texture from the board
     * @author Alexander Dingwall
     */
    public void kill() {
        this.tile.remove(this.image);
        this.tile.remove(this.tile_health_bar_b);
        this.tile.setUnit(null);
        Main.units.remove(this);
        this.player.units.remove(this);
        
        // removes tile from resources list if necessary
        if (this.tile.isResource() && this.name.equals("Worker")) {
            this.player.resources.remove(this.tile);
            this.tile.repaint();
        }
        
        Main.board.println(this.player.getName() + "\'s " + this.name + " has died!");
    }
    
    /**
     * Checks if this unit and given unit are on the same team
     * @param unit unit to be tested
     * @return true players are the same, false if not
     * @author Alexander Dingwall
     */
    public boolean sameTeam(Unit unit) {
        if (unit == null) return false;
        return this.player.equals(unit.player);
    }
    
    /**
     * Disables button and changes texture
     * @param button
     * @author Alexander Dingwall
     */
    public void disableButton(JButton button) {
        button.setEnabled(false);
        button.setBorder(new LineBorder(Color.GRAY, 1));
    }
    
    /**
     * Enables button and changes texture
     * @param button
     * @author Alexander Dingwall
     */
    public void enableButton(JButton button) {
        button.setEnabled(true);
        button.setBorder(new LineBorder(Color.GREEN, 1));
    }
        
    
    /**
     * Calculates health percent and updates the tile health bar
     * @author Alexander Dingwall
     */
    public void updateTileHealthBar() {
        int health_meter = (int) (((double) this.health / this.max_health) * (Main.board.getTileWidth()-5));
        
        JPanel meter = (JPanel) this.tile_health_bar_b.getComponent(0);
        
        this.tile_health_bar_b.remove(meter);
        meter.setSize(health_meter, 3);
        this.tile_health_bar_b.add(meter, new AbsoluteConstraints(0,0, meter.getWidth(), meter.getHeight()));
        //this.tile_health_bar_b.revalidate();
        this.tile.repaint();
    }
    
    /**
     * Ends the unit's turn and updates textures
     * @author Alexander Dingwall
     */
    public void endTurn() {
        if (!this.moved) {
            this.moved = true;
            this.player.units_moved++;
            if (this.player.team) 
                Main.board.player1_info.repaint();
            else
                Main.board.player2_info.repaint();
        } else {
            Main.board.println("This unit's turn is over");
        }
    }
    
    /**
     * Static getter method for the number of units
     * @return the number of units
     * @author Alexander Dingwall
     */
    public static int getNumUnits() {
        return Unit.num_units;
    }
    
    /**
     * Returns the current instance
     * @return this
     * @author Alexander Dingwall
     */
    protected Unit getUnit() {
        return this;
    }
    
    /**
     * Getter method for the player property
     * @return the current player
     * @author Alexander Dingwall
     */
    public Player getPlayer() {
        return this.player;
    }
    
    /**
     * Getter method for the tile property
     * @return the tile that the unit is currently on
     * @author Alexander Dingwall
     */
    @Override
    public Tile getTile() {
        return this.tile;
    }
    
    /**
     * Setter method for the tile property
     * @param tile new tile 
     * @author Alexander Dingwall
     */
    @Override
    public void setTile(Tile tile) {
        this.tile = tile;
        tile.setUnit(this);
        
        // updates defence and offence values with the new tile bonus
        this.defence_with_bonus = (int) (this.defence * this.tile.getDefenceBonus());
        this.offence_with_bonus = (int) (this.offence * this.tile.getOffenceBonus());
    }
    
    /**
     * Getter method for the location property
     * @return the location as an array
     * @author Alexander Dingwall
     */
    @Override
    public int[] getLocation() {
        return this.location;
    }
    
    /**
     * Setter method for the location property
     * @param location new location as an array
     * @author Alexander Dingwall
     */
    @Override 
    public void setLocation(int[] location) {
        this.location = location;
        this.x = location[0];
        this.y = location[1];
    }
    
    /**
     * Setter method for the location property
     * @param x new x value
     * @param y new y value 
     * @author Alexander Dingwall
     */
    @Override
    public void setLocation(int x, int y) {
        this.location = new int[]{x,y};
        this.x = x;
        this.y = y;
    }
    
    /**
     * toString method for the abstract class Unit
     * @return information about the unit, including name, stats and location.
     * @author Martin Rivard
     */
    @Override
    public String toString() {
        return (this.name + "-" + this.x + "-" + this.y + "-" + this.health + "-" + this.defence + "-" + this.offence + "-" + this.level);
    }
    
    /**
     * Equals method for the abstract class Unit.
     * @param unit unit for the instance to be tested against
     * @return true if instance and given parameter are equal, false if not.
     * @author Alexander Dingwall
     */
    public boolean equals(Unit unit) {
        if (unit == null) {
            return false;
        } else {
            return this.name.equals(unit.name) &&
                    this.player == unit.player &&
                    this.level == unit.level &&
                    this.defence == unit.defence &&
                    this.offence == unit.offence &&
                    this.x == unit.x &&
                    this.y == unit.y &&
                    this.location == unit.location;
        }
    }
}