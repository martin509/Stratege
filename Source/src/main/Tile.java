
package main;

import main.unit.Unit;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import main.unit.Medic;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import java.net.URL;

/**
 * Defines properties and methods for each tile
 * @author Alexander Dingwall
 * @since Dec 20, 2015
 */
public class Tile extends JPanel {
        
    public static int num_tiles = 0;
    
    // static event variables
    public static Tile highlighted_tile; 
    public static Tile focused_tile;
    public static Unit focused_unit;
    public static Unit attacker;
    private static Tile start; // for line drawing
    
    // static resource variables
    private static final ImageIcon neutral_oilrig, red_oilrig, blue_oilrig;
    private static final URL neutral_url, red_url, blue_url;
    
    // location properties
    public int x;
    public int y;
    
    // dimension properties
    private final int width;
    private final int height;
    
    // description properties
    private final String name;
    private final String desc;
    private final String pathString;
    private final URL path;
    
    // texture properties
    private final Color colour;
    private JLabel resource_texture;
    private boolean is_resource;
    public JLabel tile_texture;
    private final ImageIcon image;
    
    // unit properties
    private final int defence_bonus;
    private final int offence_bonus;
    private int resource_bonus;
    private Unit unit;
            
    // initiates resource textures
    static {
        neutral_url = Tile.class.getResource("neutral_oilrig.png");
        neutral_oilrig = new ImageIcon(neutral_url);
        red_url = Tile.class.getResource("red_oilRig.png");
        red_oilrig = new ImageIcon(red_url);
        blue_url = Tile.class.getResource("blue_oilRig.png");
        blue_oilrig = new ImageIcon(blue_url);
        
        neutral_oilrig.setDescription("neutral_oilrig.png");
        red_oilrig.setDescription("red_oilRig.png");
        blue_oilrig.setDescription("blue_oilRig.png");
    }
    
    /**
     * 
     * @param name name of the tile
     * @param x x position (pxl)
     * @param y y position (pxl)
     * @param width width of the tile
     * @param height height of the tile
     * @param colour colour of the tile
     * @param desc short description of the tile
     * @param path path to the texture image
     * @param defence_bonus 
     * @param offence_bonus
     * @param resource_bonus
     * @author Alexander Dingwall
     */
    public Tile(String name, int x, int y, int width, int height, Color colour, String desc, String path, int defence_bonus, int offence_bonus, int resource_bonus) {
        num_tiles++;
        
        this.name = name;
        this.desc = desc;
        this.pathString = path;
        this.path = getClass().getResource(path);
        
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        
        this.colour = colour;
        this.image = new ImageIcon(this.path);
                
        this.defence_bonus = defence_bonus;
        this.offence_bonus = offence_bonus;
        this.resource_bonus = resource_bonus;
        
        this.setSize(this.width, this.height);
        this.setBackground(this.colour);
        
        tile_texture = new JLabel();
        tile_texture.setSize(this.width, this.height);
        tile_texture.setIcon(this.image);
        this.setLayout(new AbsoluteLayout());
        this.add(tile_texture, new AbsoluteConstraints(0,0));
        
        // initiates clicking functionality
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) { 
                setFocusedTile();
                
                // checks for attack
                if (attacker != null) {
                    // either attacks or heals 
                    if (!attacker.sameTeam(focused_unit))
                        attacker.attack(focused_unit);
                    else if (attacker.name.equals("Medic"))
                       ((Medic) attacker).heal(focused_unit);
                    
                    // resets attacker variable (ends attack)
                    attacker = null;
                }
                
                // sets start tile for line drawing
                setStart(focused_tile);
            }
            
            @Override
            public void mouseReleased(MouseEvent me) {  
                // if a unit is selected and dragged, move unit to new tile
                if (focused_unit != null && highlighted_tile.unit == null) {
                    focused_unit.move(highlighted_tile);
                    // updates focused_tile
                    focused_unit.getTile().setFocusedTile();
                }
                    
                // resets board
                Board.clearLines();
                start = null;
               
            }
            
            @Override
            public void mouseEntered(MouseEvent me) {
                // highlights tile
                setHighlightedTile();
                    
                // draws line
                if (start != null) {
                    Board.clearLines();
                    Board.drawLine(start, highlighted_tile);
                }
            }
        });
    } 
    
    /**
     * Calculates the distance between two tiles using Pythagorean's theorem
     * @param t1 first tile
     * @param t2 first tile 2
     * @return the distance between two values
     * @author Alexander Dingwall
     */
    public static int getDistance(Tile t1, Tile t2) {
        // distance formula : sqrt( (y2 - y1)^2 + (x2 - x1)^2 )
        return (int) Math.sqrt(Math.pow(t2.y - t1.y, 2) + Math.pow(t2.x - t1.x, 2));
    }
    
    /**
     * Passes tile information to the tile info panel
     * @author Alexander Dingwall
     */
    public void setInfoPanel() {
        Main.board.tile_info.tile = this;
        Main.board.tile_info.setTitle(getName());
        Main.board.tile_info.setDescription(getDescription());
        
        Main.board.tile_info.repaint();
    }
    
    /**
     * Sets tile to red until another tile is clicked and updates tile 
     * and unit info panels
     * @author Alexander Dingwall
     */
    public void setFocusedTile() {
        
        if (focused_tile != null) focused_tile.setColour(colour); // resets old tile's colour
        focused_tile = this; // updates reference
        focused_tile.setColour(Color.RED); // sets new tile's colour
        focused_tile.setInfoPanel(); // updates tile info panel
        
        // sets focused_unit
        if (focused_tile.unit != null) {
            focused_unit = focused_tile.unit;
            
            focused_unit.setInfoPanel();
            Main.board.unit_info.updateHealthBar((int) (((double) focused_unit.health / focused_unit.max_health) * 85));
           
        // sets focused_unit to null if the new focused_tile's unit is null
        } else {
            focused_unit = null;
            Main.board.unit_info.clear();
        }
    }
    
    /**
     * Resets previous highlighted tile colour and sets current to red
     * @author Alexander Dingwall
     */
    private void setHighlightedTile() {
        if (highlighted_tile != null && highlighted_tile != focused_tile) highlighted_tile.setColour(highlighted_tile.colour);
        highlighted_tile = this;
        highlighted_tile.setColour(Color.RED);
    }
    
    /**
     * Overrides JPanel.repaint(), ensures units are displayed over tile textures. Also 
     * updates oil rig texture when controlled.
     * @author Alexander Dingwall
     */
    @Override
    public void repaint() {
        super.repaint();
        if (this.is_resource) {
            // if a unit is on this tile
            if (this.unit != null){
                // if worker is on this tile
                if (this.unit.name.equals("Worker")) {
                    // resets texture
                    if (this.unit.player.team) {
                        resource_texture.setIcon(blue_oilrig);
                        setComponentZOrder(resource_texture, 1);
                        setComponentZOrder(this.unit.tile_health_bar_b, 0);
                    } else {
                        resource_texture.setIcon(red_oilrig);
                        setComponentZOrder(resource_texture, 1);
                        setComponentZOrder(this.unit.tile_health_bar_b, 0);
                    }
                }            
            // if no unit is on this tile
            } else if (this.unit == null) {
                // resets texture
                resource_texture.setIcon(neutral_oilrig);
                setComponentZOrder(resource_texture, 0);
            }
        }
    }
    
    /**
     * 
     * @param tile
     * @author Alexander Dingwall
     */
    public static void setStart(Tile tile) {
        start = tile;
    }
    
    /**
     * Getter method for the object
     * @return
     * @author Alexander Dingwall
     */
    public Tile getTile() {
        return this;
    }
    
    /**
     *
     * @return
     * @author Alexander Dingwall
     */
    public Unit getUnit() {
        return this.unit;
    }
    
    /** 
     * Checks to see if tile is empty, then adds the unit to the tile. 
     * @param unit unit to be added
     * @return true if successful (tile was empty), false if not
     * @author Alexander Dingwall
     */
    public boolean setUnit(Unit unit) {
        if (unit == null) {
            this.unit = null;
            return true;
        } else if (this.unit == null) {      
            unit.getTile().unit = null;
            this.add(unit.image, new AbsoluteConstraints(0,0));
            this.add(unit.tile_health_bar_b, new AbsoluteConstraints(2, Main.board.getTileHeight()-6, unit.tile_health_bar_b.getWidth(), unit.tile_health_bar_b.getHeight()));
            setComponentZOrder(tile_texture, 2);
            setComponentZOrder(unit.image, 1);
            this.unit = unit;
            this.unit.setLocation(new int[]{x,y});
            return true;
        } else {
            System.out.println("That tile is full.");
            return false;
        }
    }
    
    /**
     * Getter method for width property
     * @return the width of the tile
     * @author Alexander Dingwall
     */
    @Override
    public int getWidth() { 
        return this.width;
    }
    
    /**
     * Getter method for the height property
     * @return the height of the tile 
     * @author Alexander Dingwall
     */
    @Override
    public int getHeight() {
        return this.height;
    }
    
    /**
     * Getter method for the name of the tile
     * @return the name of the tile
     * @author Alexander Dingwall
     */
    @Override
    public  String getName() {
        return this.name;
    }
    
    /**
     * Getter method for the resource property
     * @return true if tile is a resource tile, false if not
     * @author Alexander Dingwall
     */
    public boolean isResource() {
        return this.is_resource;
    }
    
    /**
     * Setter method for the resource property
     * @param is_resource new resource value
     * @author Martin Rivard
     */
    public void setResource(boolean is_resource) {
        this.is_resource = is_resource;
        this.resource_bonus = 5;
        
        this.resource_texture = new JLabel();
        //ImageIcon r_icon = new ImageIcon(neutral_url);
        this.resource_texture.setBounds(0,0, this.width, this.height);
        this.resource_texture.setIcon(neutral_oilrig);
        this.add(resource_texture, new AbsoluteConstraints(0,0));
    }
    
    /**
     * For player resource capture.
     * @param player The player that is capturing.
     * @author Martin Rivard
     */
    public void setResource(Player player){ 
        
        if(Main.player1.resources.contains(this) && !player.team){
            Main.player1.resources.remove(this);
        }
        if(Main.player2.resources.contains(this) && player.team){
            Main.player2.resources.remove(this);
        }
    }
    
    /**
     * Getter method for the colour property
     * @return the background colour of the tile
     * @author Alexander Dingwall
     */
    public Color getColour() {
        return this.colour;
    }
    
    /**
     * Setter method for the colour property
     * @param colour new colour
     * @author Alexander Dingwall
     */
    public void setColour(Color colour) {
        setBackground(colour);
    }
    
    /**
     * Getter method for the desc property
     * @return the description of the tile.
     * @author Alexander Dingwall
     */
    public String getDescription() {
        return this.desc;
    }
    
    /**
     * Getter method for the image property
     * @return the texture of the tile
     * @author Alexander Dingwall
     */
    public ImageIcon getImage() {
        return this.image;
    }
    
    /**
     * Getter method for the path property
     * @return the path for the image of the tile
     * @author Alexander Dingwall
     */
    public URL getImagePath() {
        return this.path;
    }
    
    /**
     * Getter method for the defence_bonus property
     * @return the defence bonus for the tile
     * @author Alexander Dingwall
     */
    public int getDefenceBonus() {
        return this.defence_bonus;
    }
    
    /**
     * Getter method for the offence_bonus property
     * @return the offence bonus for the tile
     * @author Alexander Dingwall
     */
    public int getOffenceBonus() {
        return this.offence_bonus;
    }
    
    /**
     * Getter method for the resource_bonus property
     * @return the resource bonus for the tile
     * @author Alexander Dingwall
     */
    public int getResourceBonus() {
        return this.resource_bonus;
    }
    
    /**
     * toString method for the abstract class Tile
     * @return Information about the tile including name, position, size and texture
     * @author Alexander Dingwall
     */
    @Override
    public String toString() {
        return "[Name : \"" + getName() + "\"], [Position : (" + this.x + ", " + this.y + ")], [Size : (" + this.width + ", " + this.height + ")], [Bonuses : (" + this.defence_bonus + ", " + this.offence_bonus + ", " + this.resource_bonus + ")]";
    }
    
    /**
     * Clone method for the Tile class
     * @return new Tile object
     * @throws java.lang.CloneNotSupportedException
     * @author Alexander Dingwall
     */
    @Override
    public Tile clone() throws CloneNotSupportedException {
        return new Tile(this.name, this.x, this.y, this.width, this.height, this.colour, this.desc, this.pathString, this.defence_bonus, this.offence_bonus, this.resource_bonus);
    }
    
    /**
     * Equals method for the class Tile. Only tests for name, x position and y 
     * position because no tile should share the same location.
     * @param tile tile to be tested
     * @return true if tiles are equal, false if not
     * @author Alexander Dingwall
     */
    public boolean equals(Tile tile) {
        return this.name.
                equals(tile.getName()) &&
                this.x == tile.x &&
                this.y == tile.y;
    }
    
}
