
package main.unit;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import main.Board;
import main.Main;
import main.Player;
import static main.unit.Unit.definitions;

/**
 *
 * @author Alexander
 */
public class Tank extends Unit {
    
    /**
     * Main constructor for the class InfantryUnit. Defines unit location
     * @param x starting x position for the unit, as an index in a two dimensional array of tiles 
     * @param y starting y position for the unit, as an index in a two dimensional array of tiles
     * @param player the player that the unit belongs to
     * @author Alexander Dingwall
     */
    public Tank(int x, int y, Player player) {
        super(x,y,player);
        
        this.name = definitions[3][0];
        this.desc = definitions[3][9];
        
        this.defence = Integer.parseInt(definitions[3][1]);
        this.offence = Integer.parseInt(definitions[3][2]);
        this.health = Integer.parseInt(definitions[3][3]);
        this.max_health = Integer.parseInt(definitions[3][3]);
        this.cost =         Integer.parseInt(definitions[3][10]);
        this.upgrade_cost = Integer.parseInt(definitions[3][11]);
        this.unlock_level = Integer.parseInt(definitions[3][12]);
        
        // if true, passes blue texture path, if false, passes red texture path
        this.path = Infantry.class.getResource(this.player.team ? definitions[3][7] : definitions[3][8]);
        //URL url = Infantry.class.getResource(this.player.team ? definitions[3][7] : definitions[3][8]);
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
    public Tank(int x, int y, Player player, int level, int health, int defence, int offence){
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
    private Tank(String name, String desc, int x, int y, int level, int health, int defence, int offence, Player player) {
        super(name, desc, x, y, level, health, defence, offence, player);
    }
    
    /**
     * Heals the given unit.
     * @param unit unit to be healed
     * @return true if healing was successful (unit was in range), false if not
     * @author Alexander Dingwall
     */
    public boolean heal(Unit unit) {
        if (Unit.getDistance(this, unit) == 1) {
            unit.health += this.level; // heals +1 when level 1, +2 when level 2, +3 when level 3 
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Increments level by one and updates the stats
     * @author Alexander Dingwall
     */
    @Override
    public void levelUp() {
        super.levelUp();
        this.defence += Integer.parseInt(definitions[3][5]);
        this.offence += Integer.parseInt(definitions[3][4]);
    }
    
    /**
     * Clone method for the class Infantry
     * @return an exact copy of the object
     * @throws java.lang.CloneNotSupportedException
     * @author Alexander Dingwall
     */
    @Override
    public Unit clone() throws CloneNotSupportedException { 
        return new Tank(this.name, this.desc, this.x, this.y, this.level, this.health, this.defence, this.offence, this.player);
    }
    
}
