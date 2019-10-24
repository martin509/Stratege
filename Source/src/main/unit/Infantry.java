
package main.unit;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import main.Board;
import main.Main;
import main.Player;
import static main.unit.Unit.definitions;

/**
 * Extends Unit class, defines the light infantry unit. Player must be defined after instance is created. 
 * @author Alexander Dingwall
 * @since Dec 20, 2015
 */
public class Infantry extends Unit {

    /**
     * Main constructor for the class LightInfantryUnit. Defines unit location
     * @param x starting x position for the unit, as an index in a two dimensional array of tiles 
     * @param y starting y position for the unit, as an index in a two dimensional array of tiles
     * @param player the player that the unit belongs to
     * @author Alexander Dingwall
     */
    public Infantry(int x, int y, Player player) {
        super(x,y,player);
        
        this.name = definitions[1][0];
        this.desc = definitions[1][9];
        
        this.defence = Integer.parseInt(definitions[1][1]);
        this.offence = Integer.parseInt(definitions[1][2]);
        this.health = Integer.parseInt(definitions[1][3]);
        this.max_health = Integer.parseInt(definitions[1][3]);
        this.cost =         Integer.parseInt(definitions[1][10]);
        this.upgrade_cost = Integer.parseInt(definitions[1][11]);
        this.unlock_level = Integer.parseInt(definitions[1][12]);
        
        // if true, passes blue texture path, if false, passes red texture path
        this.path = Infantry.class.getResource(this.player.team ? definitions[1][7] : definitions[1][8]);
        //URL url = Infantry.class.getResource(this.player.team ? definitions[1][7] : definitions[1][8]);
        ImageIcon texture = new ImageIcon(this.path);
        image = new JLabel();
        image.setBounds(0,0, Main.board.getTileWidth(), Main.board.getTileHeight());
        image.setIcon(texture);
        
        setTile(Board.tiles[this.x][this.y]);
        // updates defence and offence values with the new tile bonus
        this.defence_with_bonus = (int) (this.defence * getTile().getDefenceBonus());
        this.offence_with_bonus = (int) (this.offence * getTile().getOffenceBonus());
        
        upgrade_button.setText("Upgrade ($" + this.upgrade_cost + ")");
        
    }
    
    /**
     * Constructor including parameters for all properties
     * @param x x position
     * @param y y position
     * @param level level of the unit
     * @param health health of the unit
     * @param defence defence stat
     * @param offence offence stat
     * @param player
     * @author Alexander Dingwall
     */
    public Infantry(int x, int y, Player player, int level, int health, int defence, int offence){
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
    private Infantry(String name, String desc, int x, int y, int level, int health, int defence, int offence, Player player) {
        super(name, desc, x, y, level, health, defence, offence, player);
    }
    
    
    
    /**
     * Increments the level by one and increases the stats
     * @author Alexander Dingwall
     */
    @Override
    public void levelUp() {
        super.levelUp();
        this.defence += Integer.parseInt(definitions[1][5]);
        this.offence += Integer.parseInt(definitions[1][4]);
        
    }
    
    /**
     * Clone method for the class Infantry
     * @return an exact copy of the object
     * @throws java.lang.CloneNotSupportedException
     * @author Alexander Dingwall
     */
    @Override
    public Unit clone() throws CloneNotSupportedException { 
        return new Infantry(this.name, this.desc, this.x, this.y, this.level, this.health, this.defence, this.offence, this.player);
    }
}
