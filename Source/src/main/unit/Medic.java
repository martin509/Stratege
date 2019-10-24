
package main.unit;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;
import main.Board;
import main.Main;
import main.Player;
import main.Tile;


/**
 * Extends Unit class, defines medic unit. Player must be defined after instance is created. 
 * @author Alexander
 * @since Jan 3, 2016
 */
public class Medic extends Unit {
    
    private JButton heal_button;
 
    /**
     * Main constructor for the class InfantryUnit. Defines unit location
     * @param x starting x position for the unit, as an index in a two dimensional array of tiles 
     * @param y starting y position for the unit, as an index in a two dimensional array of tiles
     * @param player the player that the unit belongs to
     * @author Alexander Dingwall
     */
    public Medic(int x, int y, Player player) {
        super(x,y,player);
        
        this.name = definitions[2][0];
        this.desc = definitions[2][9];
        
        this.defence = Integer.parseInt(definitions[2][1]);
        this.offence = Integer.parseInt(definitions[2][2]);
        this.health = Integer.parseInt(definitions[2][3]);
        this.max_health = Integer.parseInt(definitions[2][3]);
        this.cost =         Integer.parseInt(definitions[2][10]);
        this.upgrade_cost = Integer.parseInt(definitions[2][11]);
        this.unlock_level = Integer.parseInt(definitions[2][12]);
        
        // if true, passes blue texture path, if false, passes red texture path
        this.path = Medic.class.getResource(this.player.team ? definitions[2][7] : definitions[2][8]);
        //URL url = Medic.class.getResource(this.player.team ? definitions[2][7] : definitions[2][8]);
        ImageIcon texture = new ImageIcon(this.path);
        image = new JLabel();
        image.setBounds(0,0, Main.board.getTileWidth(), Main.board.getTileHeight());
        image.setIcon(texture);
        
        setTile(Board.tiles[this.x][this.y]);
        
        // updates defence and offence values with the new tile bonus
        this.defence_with_bonus = (int) (this.defence * getTile().getDefenceBonus());
        this.offence_with_bonus = (int) (this.offence * getTile().getOffenceBonus());
       
        // define heal button for the medic unit
        this.heal_button = new JButton("Heal");
        this.heal_button.setSize(128,Unit.button_height);
        this.heal_button.setFont(new Font("Monospaced", Font.PLAIN, 12));
        this.heal_button.setBorder(new LineBorder(Color.GREEN, 1));
        this.heal_button.setBackground(Color.BLACK);
        this.heal_button.setForeground(Color.GREEN);
        
        this.heal_button.addActionListener((ActionEvent ae) -> {
            if (!moved) {
                Main.board.println("Medic started to heal!");
                Tile.attacker = getUnit();
            } else {
                Main.board.println("This unit's turn is over.");
            }
        });
        
        upgrade_button.setText("Upgrade ($" + this.upgrade_cost + ")");
        
        // update array reference
        buttons[3] = heal_button;
    }
    
    /**
     * Constructor including parameters for all properties
     * @param x x position
     * @param y y position
     * @param player
     * @param level level of the unit
     * @param health health of the unit
     * @param defence defence stat
     * @param offence offence stat
     * @author Alexander Dingwall
     */
    public Medic(int x, int y, Player player, int level, int health, int defence, int offence){
        this(x, y, player);
        this.level = level;
        this.health = health;
        this.defence = defence;
        this.offence = offence;
        this.defence_with_bonus = (int) (this.defence * getTile().getDefenceBonus());
        this.offence_with_bonus = (int) (this.offence * getTile().getOffenceBonus());
    }
    
    /**
     * Constructor including parameters for all properties
     * @param name name of the unit
     * @param x x position
     * @param y y position
     * @param level level of the unit
     * @param health health of the unit
     * @param defence defence stat
     * @param offence offence stat
     * @param player the player that the unit belongs 
     * @author Alexander Dingwall
     */
    private Medic(String name, String desc, int x, int y, int level, int health, int defence, int offence, Player player) {
        super(name, desc, x, y, level, health, defence, offence, player);
    }
    
    /**
     * Heals the given unit.
     * @param unit unit to be healed
     * @author Alexander Dingwall
     */
    public void heal(Unit unit) {
        if (Unit.getDistance(this, unit) == 1) {
            if (unit.health < unit.max_health) {
                unit.health += this.level; // heals +1 when level 1, +2 when level 2, +3 when level 3 
                
                // prints output message
                Main.board.println(this.name + " heals " + unit.name + " for " + this.level + " health.");
                
                // makes sure to not heal over the maximum health
                if (unit.health >= unit.max_health) {
                    unit.health = unit.max_health;
                    Main.board.println(unit.name + " is now at max health!");
                }
                
                // ends turn
                endTurn();
            } else {
                Main.board.println("That unit is already at max health!");
            }
        } 
        
    }
    
    /**
     * Increments level by one. 
     * @author Alexander Dingwall
     */
    @Override
    public void levelUp() {
        super.levelUp();
        this.defence += Integer.parseInt(definitions[2][5]);
        this.offence += Integer.parseInt(definitions[2][4]);
    }
    
    /**
     * Clone method for the class Infantry
     * @return an exact copy of the object
     * @throws java.lang.CloneNotSupportedException
     * @author Alexander Dingwall
     */
    @Override
    public Unit clone() throws CloneNotSupportedException { 
        return new Medic(this.name, this.desc, this.x, this.y, this.level, this.health, this.defence, this.offence, this.player);
    }
}
