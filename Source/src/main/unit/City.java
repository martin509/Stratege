
package main.unit;

import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import main.Board;
import main.Main;
import main.Player;
import main.Tile;
import static main.unit.Unit.definitions;

/**
 * Extends Unit class, defines City unit. City must be defined with 
 * the player as a parameter. 
 * @author Alexander
 * @since Dec 21, 2015
 */
public class City extends Unit {
    
    // unique ability button
    private JButton create_button;
    
    // create unit buttons
    private JButton artillery_button;
    private JButton infantry_button;
    private JButton medic_button;
    private JButton tank_button;
    private JButton worker_button;
    private JButton back_button;
    
    private JButton[] unit_buttons;
    
    
    
    /**
     * Main constructor for the class CityUnit. Defines unit location and player
     * @param x starting x position for the unit, as an index in a two dimensional array of tiles 
     * @param y starting y position for the unit, as an index in a two dimensional array of tiles
     * @param player the player that the city belongs to
     * @author Alexander Dingwall
     */
    public City(int x, int y, Player player) {
        super(x,y,player);
        
        this.player.setCity(this);
        this.name = this.player.getName() + "\'s " + definitions[0][0];
        this.desc = definitions[0][9];
        
        // if true, passes blue texture path, if false, passes red texture path
        this.defence = Integer.parseInt(definitions[0][1]);
        this.offence = Integer.parseInt(definitions[0][2]);
        this.health = Integer.parseInt(definitions[0][3]);
        this.max_health = Integer.parseInt(definitions[0][3]);
        this.cost =         Integer.parseInt(definitions[0][10]);
        this.upgrade_cost = Integer.parseInt(definitions[0][11]);
        this.unlock_level = Integer.parseInt(definitions[0][12]);
        
        this.path = City.class.getResource(this.player.team ? definitions[0][7] : definitions[0][8]);
        ImageIcon texture = new ImageIcon(this.path);
        image = new JLabel();
        image.setBounds(0,0, Main.board.getTileWidth(), Main.board.getTileHeight());
        image.setIcon(texture);
        
        setTile(Board.tiles[this.x][this.y]);
        
        // updates defence and offence values with the new tile bonus
        this.defence_with_bonus = (int) (this.defence * getTile().getDefenceBonus());
        this.offence_with_bonus = (int) (this.offence * getTile().getOffenceBonus());
        
        
        // INIT UNIT BUTTONS
        
        artillery_button = new JButton("Artillery");
        Main.initButtonUI(artillery_button, false, Color.GREEN);
        artillery_button.setSize(100,25);
        
        infantry_button = new JButton("Infantry");
        Main.initButtonUI(infantry_button, false, Color.GREEN);
        infantry_button.setSize(100,25);
        
        medic_button = new JButton("Medic");
        Main.initButtonUI(medic_button, false, Color.GREEN);
        medic_button.setSize(100,25);
               
        tank_button = new JButton("Tank");
        Main.initButtonUI(tank_button, false, Color.GREEN);
        tank_button.setSize(100,25);
        
        worker_button = new JButton("Worker");
        Main.initButtonUI(worker_button, false, Color.GREEN);
        worker_button.setSize(100,25);
        
        back_button = new JButton("Back");
         Main.initButtonUI(back_button, false, Color.GREEN);
        back_button.setSize(100,25);
        
        artillery_button.addActionListener((ActionEvent ae) -> {
            Tile t = getFirstEmptyTile();
            if (t == null) {
                Main.board.println("No empty tiles available. Try moving a unit away from your city.");
                return;
            } else if (this.player.money < Integer.parseInt(Unit.definitions[5][10])) {
                Main.board.println("You do not have enough money for that.");
                return;
            }
            
            // creates unit
            Artillery u = new Artillery(t.x, t.y, getPlayer());
            this.player.money -= Integer.parseInt(Unit.definitions[5][10]);
            
            this.getTile().setFocusedTile();
            
            // updates info panels
            if (this.player.team) 
                Main.board.player1_info.repaint();
            else 
                Main.board.player2_info.repaint();

            this.endTurn();
            
            Main.board.println(getPlayer().getName() + " created an Artillery unit!");
            Main.board.repaint();
        });
        
        infantry_button.addActionListener((ActionEvent ae) -> {
            Tile t = getFirstEmptyTile();
            if (t == null) {
                Main.board.println("No empty tiles available. Try moving a unit away from your city.");
                return;
            } else if (this.player.money < Integer.parseInt(Unit.definitions[1][10])) {
                Main.board.println("You do not have enough money for that.");
                return;
            }
            
            // creates unit
            Infantry u = new Infantry(t.x, t.y, getPlayer());
            this.player.money -= Integer.parseInt(Unit.definitions[1][10]);
                       
            this.getTile().setFocusedTile();
            
            // updates info panels
            if (this.player.team) 
                Main.board.player1_info.repaint();
            else 
                Main.board.player2_info.repaint();
            
            Main.board.println(getPlayer().getName() + " created an Infantry unit!");
            Main.board.repaint();
        });
        
        medic_button.addActionListener((ActionEvent ae) -> {
            Tile t = getFirstEmptyTile();
            if (t == null) {
                Main.board.println("No empty tiles available. Try moving a unit away from your city.");
                return;
            } else if (this.player.money < Integer.parseInt(Unit.definitions[2][10])) {
                Main.board.println("You do not have enough money for that.");
                return;
            }
            
            // creates unit
            Medic u = new Medic(t.x, t.y, getPlayer());
            this.player.money -= Integer.parseInt(Unit.definitions[2][10]);
            
            this.getTile().setFocusedTile();
            
            // updates info panels
            if (this.player.team) 
                Main.board.player1_info.repaint();
            else 
                Main.board.player2_info.repaint();

            Main.board.println(getPlayer().getName() + " created a Medic unit!");
            Main.board.repaint();
        });
        
        tank_button.addActionListener((ActionEvent ae) -> {
            Tile t = getFirstEmptyTile();
            if (t == null) {
                Main.board.println("No empty tiles available. Try moving a unit away from your city.");
                return;
            } else if (this.player.money < Integer.parseInt(Unit.definitions[3][10])) {
                Main.board.println("You do not have enough money for that.");
                return;
            }
            
            // creates unit
            Tank u = new Tank(t.x, t.y, getPlayer());
            this.player.money -= Integer.parseInt(Unit.definitions[3][10]);
            
            this.getTile().setFocusedTile();
            
            // updates info panels
            if (this.player.team) 
                Main.board.player1_info.repaint();
            else 
                Main.board.player2_info.repaint();

            Main.board.println(getPlayer().getName() + " created a Tank unit!");
            Main.board.repaint();
        });
        
        worker_button.addActionListener((ActionEvent ae) -> {
            Tile t = getFirstEmptyTile();
            if (t == null) {
                Main.board.println("No empty tiles available. Try moving a unit away from your city.");
                return;
            } else if (this.player.money < Integer.parseInt(Unit.definitions[4][10])) {
                Main.board.println("You do not have enough money for that.");
                return;
            }
            
            // creates unit
            Worker u = new Worker(t.x, t.y, getPlayer());
            this.player.money -= Integer.parseInt(Unit.definitions[4][10]);
           
            this.getTile().setFocusedTile();
            
            // updates info panels
            if (this.player.team) 
                Main.board.player1_info.repaint();
            else 
                Main.board.player2_info.repaint();

            Main.board.println(getPlayer().getName() + " created a Worker unit!");
            Main.board.repaint();
        });
        
        back_button.addActionListener((ActionEvent ae) -> {
            getUnit().setInfoPanel();
        });
        
        unit_buttons = new JButton[]{infantry_button, worker_button, medic_button, tank_button, artillery_button, back_button};
        
        // define create unit button for the city unit
        this.create_button = new JButton("Create Unit");
        Main.initButtonUI(create_button, false, Color.GREEN);
        this.create_button.setSize(128,Unit.button_height);
      
        this.create_button.addActionListener((ActionEvent ae) -> {
                moved = false;
            if (!moved) {
                Main.board.unit_info.setTitle("Create a Unit");
                Main.board.unit_info.setDescription("Click on a unit to create it!");
                Main.board.unit_info.setButtons(unit_buttons);
                Main.board.repaint();
            } else {
                Main.board.println("This unit's turn is over.");
            }
        });
        
        upgrade_button.setText("Upgrade ($" + this.upgrade_cost + ")");
        
         // update array reference
        buttons[3] = create_button;
        
        //System.out.println(this.defence);
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
    public City(int x, int y, Player player, int level, int health, int defence, int offence){
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
    private City(String name, String desc, int x, int y, int level, int health, int defence, int offence, Player player) {
         super(name, desc, x, y, level, health, defence, offence, player);
    }
    
    /**
     * Scans every direction around the city and returns the first empty tile
     * @return first empty Tile, null if every direction is full
     * @author Alexander Dingwall
     */
    private Tile getFirstEmptyTile() {
        int[][] directions_down = new int[][]{
            {-1,  0}, // West
            {-1,  1}, // South west
            { 0,  1}, // South
            { 1,  1}, // South east 
            { 1,  0}  // East                
        };
        int[][] directions_up = new int[][]{
            {-1,  0}, // West
            {-1, -1}, // North west
            { 0, -1}, // North
            { 1, -1}, // North east 
            { 1,  0}  // East                
        };
            
        // this.y == 0 returns true if city is at the top of the map, false if at the bottom
        int[][] directions = this.y == 0 ? directions_down : directions_up;
        for (int d=0; d<5; d++) {
            // if tile.unit is null 
            //     return tile
            if (Board.tiles[this.x + directions[d][0]][this.y + directions[d][1]].getUnit() == null) 
                return Board.tiles[this.x + directions[d][0]][this.y + directions[d][1]];
        }
        
        return null;
    }
        
    /**
     * Overwrites the abstract Unit classes move method. Takes away unit's ability to be moved.
     * @param x new x position
     * @param y new y position
     * @return false 
     * @author Alexander Dingwall
     */
    @Override
    public boolean move(int x, int y) {
        if (this.x != x && this.y != y) Main.board.println("Cities cannot be moved.");
        return false;
    }
    
    /**
     * Calls Unit.attack, passes 2 as the max distance.
     * @param unit the unit to attack
     * @author Alexander Dingwall
     */
    @Override
    public void attack(Unit unit) {
        this.attack(unit, 2);
    }
    
    /**
     * Increments the level by one and increases unit stats
     * @author Alexander Dingwall
     */
    @Override
    public void levelUp() {
        super.levelUp();
        this.defence += Integer.parseInt(definitions[0][5]);
        this.offence += Integer.parseInt(definitions[0][4]);
    }
    
    /**
     * Ends the game when a city is killed.
     * @author Alexander Dingwall
     */
    @Override
    public void kill(){
       Main.goToEnd(this.player.team);
    }
    
    /**
     * Clone method for the class City
     * @return an exact copy of the object
     * @throws java.lang.CloneNotSupportedException
     * @author Alexander Dingwall
     */
    @Override
    public Unit clone() throws CloneNotSupportedException {
        return new City(this.name, this.desc, this.x, this.y, this.level, this.health, this.defence, this.offence, this.player);
    }

}
