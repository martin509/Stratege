
package main.unit;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import main.Board;
import main.Main;
import main.Player;
import main.Tile;
import static main.unit.Unit.definitions;

/**
 * Extends Unit class, defines medic unit. Player must be defined after instance is created. 
 * @author Alexander
 * @since Jan 9, 2016
 */
public class Artillery extends Unit {

    /**
     * Main constructor for the class LightInfantryUnit. Defines unit location
     * @param x starting x position for the unit, as an index in a two dimensional array of tiles 
     * @param y starting y position for the unit, as an index in a two dimensional array of tiles
     * @param player the player that the unit belongs to
     * @author Alexander Dingwall
     */
    public Artillery(int x, int y, Player player) {
        super(x,y,player);
        
        this.name = definitions[5][0];
        this.desc = definitions[5][9];
        
        this.defence = Integer.parseInt(definitions[5][1]);
        this.offence = Integer.parseInt(definitions[5][2]);
        this.health = Integer.parseInt(definitions[5][3]);
        this.max_health = Integer.parseInt(definitions[5][3]);
        this.cost =         Integer.parseInt(definitions[5][10]);
        this.upgrade_cost = Integer.parseInt(definitions[5][11]);
        this.unlock_level = Integer.parseInt(definitions[5][12]);
        
        // if true, passes blue texture path, if false, passes red texture path
        this.path = Infantry.class.getResource(this.player.team ? definitions[5][7] : definitions[5][8]);
        //URL url = Infantry.class.getResource(this.player.team ? definitions[5][7] : definitions[5][8]);
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
     * @param player
     * @param level level of the unit
     * @param health health of the unit
     * @param defence defence stat
     * @param offence offence stat
     * @author Alexander Dingwall
     */
    public Artillery(int x, int y, Player player, int level, int health, int defence, int offence){
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
    private Artillery(String name, String desc, int x, int y, int level, int health, int defence, int offence, Player player) {
        super(name, desc, x, y, level, health, defence, offence, player);
    }
    
    /**
     * Calls Unit.attack, passes 2 as the max distance.
     * @param unit unit to be attacked
     * @author Alexander Dingwall
     */
    @Override
    public void attack(Unit unit) {
        this.attack(unit, 2);
    }
    
    /**
     * Calls Unit.move, passes 2 as the max distance.
     * @param tile tile to be moved to
     * @author Alexander Dingwall
     */
    @Override
    public void move(Tile tile) {
        this.move(tile, 2);
    }
    
    /**
     * Increments the level by one and increases the stats
     * @author Alexander Dingwall
     */
    @Override
    public void levelUp() {
        super.levelUp();
        this.defence += Integer.parseInt(definitions[5][5]);
        this.offence += Integer.parseInt(definitions[5][4]);
    }
    
    /**
     * Clone method for the class Infantry
     * @return an exact copy of the object
     * @throws java.lang.CloneNotSupportedException
     * @author Alexander Dingwall
     */
    @Override
    public Unit clone() throws CloneNotSupportedException { 
        return new Artillery(this.name, this.desc, this.x, this.y, this.level, this.health, this.defence, this.offence, this.player);
    }
    
}
