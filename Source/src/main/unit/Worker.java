/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class Worker extends Unit {
    
    /**
     * Main constructor for the class InfantryUnit. Defines unit location
     * @param x starting x position for the unit, as an index in a two dimensional array of tiles 
     * @param y starting y position for the unit, as an index in a two dimensional array of tiles
     * @param player the player that the unit belongs to
     * @author Alexander Dingwall
     */
    public Worker(int x, int y, Player player) {
        super(x,y,player);
        
        this.name = definitions[4][0];
        this.desc = definitions[4][9];
        
        this.defence = Integer.parseInt(definitions[4][1]);
        this.offence = Integer.parseInt(definitions[4][2]);
        this.health = Integer.parseInt(definitions[4][3]);
        this.max_health = Integer.parseInt(definitions[4][3]);
        this.cost =         Integer.parseInt(definitions[4][10]);
        this.upgrade_cost = Integer.parseInt(definitions[4][11]);
        this.unlock_level = Integer.parseInt(definitions[4][12]);
        
        // if true, passes blue texture path, if false, passes red texture path
        this.path = Infantry.class.getResource(this.player.team ? definitions[4][7] : definitions[4][8]);
        //URL url = Infantry.class.getResource(this.player.team ? definitions[4][7] : definitions[4][8]);
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
    public Worker(int x, int y, Player player, int level, int health, int defence, int offence){
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
    private Worker(String name, String desc, int x, int y, int level, int health, int defence, int offence, Player player) {
        super(name, desc, x, y, level, health, defence, offence, player);
    }
    
    /**
     * Increments level by one. 
     * @author Alexander Dingwall
     */
    @Override
    public void levelUp() {
        super.levelUp();
        this.defence += Integer.parseInt(definitions[4][5]);
        this.offence += Integer.parseInt(definitions[4][4]);
    }
    
    /**
     * Clone method for the class Infantry
     * @return an exact copy of the object
     * @throws java.lang.CloneNotSupportedException
     * @author Alexander Dingwall
     */
    @Override
    public Unit clone() throws CloneNotSupportedException { 
        return new Worker(this.name, this.desc, this.x, this.y, this.level, this.health, this.defence, this.offence, this.player);
    }
}
