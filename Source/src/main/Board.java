
package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import static main.Main.endButton;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import org.netbeans.lib.awtextra.AbsoluteConstraints;

/**
 * Creates a new JPanel and fills it with Tile objects
 * @author Alexander Dingwall & Martin Rivard
 * @since Jan 3, 2016
 */
public class Board extends JPanel {
           
    public static Tile[][] tiles;
    
    // dimensions of each tile (pixels)
    private final int tile_width; 
    private final int tile_height;
    
    // dimensions of the game board (# of tiles)
    private final int board_width;
    private final int board_height;
    
    // spacing around the outside of the board (pixels)
    private final int padding;
    
    // dimensions of the panel including side area (pixels)
    private final int panel_width;
    private final int panel_height;
    
    // dimensions of just the game board (pixels)
    private final int adjusted_width;
    private final int adjusted_height;
    
    // output area variables
    String output;
    JTextArea output_panel;
    JScrollPane output_pane;
    
    // info display panels
    public InfoPanel tile_info;
    public InfoPanel unit_info;
    public PlayerInfoPanel player1_info;
    public PlayerInfoPanel player2_info;
        
    /**
     * Constructor for the Board class. Creates a JPanel with Tile objects inside it.
     * @param tile_width width of each tile
     * @param tile_height height of each tile
     * @param board_width width of the board (number of tiles)
     * @param board_height height of the board (number of tiles)
     * @param padding space around the outside of the board
     * @author Alexander Dingwall
     */
    public Board(int tile_width, int tile_height, int board_width, int board_height, int padding) {
         
        // INIT VARIABLES
        this.tile_width = tile_width;
        this.tile_height = tile_height;
        this.board_width = board_width;
        this.board_height = board_height;
        this.padding = padding;
        
        this.panel_width = this.padding*2 + this.tile_width * this.board_width + 560;
        this.panel_height = this.padding*2 + this.tile_height * this.board_height;
        this.adjusted_width = this.panel_width - 560; //Width of just the board, excluding the side area.
        this.adjusted_height = this.panel_height; //Height of just the board, this variable's only here for posterity
                  
        // INIT PANEL
        setSize(this.panel_width, this.panel_height);
        setLayout(new AbsoluteLayout());
        setBackground(Color.BLACK);
        
        // ADD OUTPUT
        output = "";
        this.output_panel = new JTextArea(5,20);
        this.output_pane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.output_pane.setBorder(null);
        this.output_panel.setLineWrap(true);
        this.output_panel.setWrapStyleWord(true);
        this.output_panel.setLocation((this.adjusted_height)+1, (this.panel_height) - (this.panel_height / 3)+1);
        this.output_panel.setSize((this.panel_width - this.padding) - (this.adjusted_width) - 1, (this.panel_height / 3) - (this.padding) - 1);
        this.output_panel.setBackground(Color.WHITE);
        //this.output_panel.setForeground(Color.GREEN);
        this.output_panel.setFont(new Font("Monospaced", Font.PLAIN, 12));
        //this.output_panel.setText("");
        
        this.output_pane.setViewportView(this.output_panel);
        
        // GENERATE TILES
        regenBoard();
    }
    
    /**
     *
     * @param tile_width
     * @param tile_height
     * @param board_width
     * @param board_height
     * @param padding
     * @param tilesIn
     * @author Alexander Dingwall
     */
    public Board(int tile_width, int tile_height, int board_width, int board_height, int padding, Tile[][] tilesIn){
        // INIT VARIABLES
        this.tile_width = tile_width;
        this.tile_height = tile_height;
        this.board_width = board_width;
        this.board_height = board_height;
        this.padding = padding;
        
        this.panel_width = this.padding*2 + this.tile_width * this.board_width + 560;
        this.panel_height = this.padding*2 + this.tile_height * this.board_height;
        this.adjusted_width = this.panel_width - 560; //Width of just the board, excluding the side area.
        this.adjusted_height = this.panel_height; //Height of just the board, this variable's only here for posterity
          
        // INIT PANEL
        setSize(this.panel_width, this.panel_height);
        setLayout(new AbsoluteLayout());
        setBackground(Color.BLACK);
        
        tiles = new Tile[tilesIn.length][tilesIn[0].length];
        for (int x = 0; x < tilesIn.length; x++){
            for (int y = 0; y < tilesIn[x].length; y++){
                tiles[x][y] = tilesIn[x][y];
                add(tiles[x][y], new AbsoluteConstraints(x*this.tile_width + this.padding + 1, y*this.tile_height + this.padding + 1, tiles[x][y].getWidth()-1, tiles[x][y].getHeight()-1));
            }
        }
        //genUI();
    }
    
    /**
     * 
     * @param tilesIn
     * @author Martin Rivard
     */
    public void setTiles(Tile[][] tilesIn){
        //tiles = new Tile[tilesIn.length][tilesIn[0].length];
        //this = new Board(31, 31, 20, 20, 20);
        for (int x = 0; x < tilesIn.length; x++){
            for (int y = 0; y < tilesIn[x].length; y++){
                remove(tiles[x][y]);
                tiles[x][y] = tilesIn[x][y];
                add(tiles[x][y], new AbsoluteConstraints(x*this.tile_width + this.padding + 1, y*this.tile_height + this.padding + 1, tiles[x][y].getWidth(), tiles[x][y].getHeight()));
            }
        }
        repaint();
        revalidate();
    }
     
    /**
     * Adds green borders
     * @param g
     * @author Alexander Dingwall
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GREEN);
        
        // Draws the green lines between each tile
        for (int x=this.padding; x<=this.adjusted_width-this.padding; x+=this.tile_width) {
            g.drawLine(x, this.padding, x, this.panel_height-this.padding);
        }
        for (int y=this.padding; y<=this.panel_height-this.padding; y+=this.tile_height) {
            g.drawLine(this.padding, y, this.adjusted_width-this.padding, y);
        }
    }
    
    /**
     * Bresenham's line drawing algorithm, from <https://en.wikipedia.org/wiki/Bresenham%27s_line_algorithm>. 
     * Draws a line from the start tile to end tile by highlighting each tile in between. 
     * @param t1 start tile
     * @param t2 end tile
     * @author Alexander Dingwall
     */
    public static void drawLine(Tile t1, Tile t2) {
        int dx = t2.x - t1.x; // change in x position
        int dy = t2.y - t1.y; // change in y position
        double error = 0; 
        double d_error = Math.abs((double) dy / dx); // dx =/= 0, therefore no vertical lines
                
        int y = t1.y;
        if (d_error <= 1) { // if related acute angle is < 45 degrees
            if (dx < 0) { // line drawn towards left side of screen
                for (int x=t1.x; x>=t2.x; x--) {
                    Board.tiles[x][y].setColour(Color.red); // draws line
                    error += d_error;
                    while (error >= 0.5) {
                        Board.tiles[x][y].setColour(Color.red); // draws line
                        y += dy > 0 ? +1 : -1; // returns +1 if positive, -1 if negative
                        error -= 1.0;
                    }
                }
            } else if (dx > 0) { // line drawn towards right side of screen
                for (int x=t1.x; x<=t2.x; x++) {
                    Board.tiles[x][y].setColour(Color.red); // draws line
                    error += d_error;
                    while (error >= 0.5) {
                        Board.tiles[x][y].setColour(Color.red); // draws line
                        y += dy > 0 ? +1 : -1; // returns +1 if positive, -1 if negative
                        error -= 1.0;
                    }
                }
            }
        } else { // if related acute angle is > 45 degrees
            dx = t2.y - t1.y; // switch x and y values
            dy = t2.x - t1.x;
            
            d_error = Math.abs((double) dy / dx); // recalculate d_error
            y = t1.x;
            if (dx < 0) { // line drawn towards left side of screen
                for (int x=t1.y; x>=t2.y; x--) {
                    Board.tiles[y][x].setColour(Color.red); // draws line
                    error += d_error;
                    while (error >= 0.5) {
                        Board.tiles[y][x].setColour(Color.red); // draws line
                        y += dy > 0 ? +1 : -1; // returns +1 if positive, -1 if negative
                        error -= 1.0;
                    }
                }
            } else if (dx > 0) { // line drawn towards right side of screen
                for (int x=t1.y; x<=t2.y; x++) {
                    Board.tiles[y][x].setColour(Color.red); // draws line
                    error += d_error;
                    while (error >= 0.5) {
                        Board.tiles[y][x].setColour(Color.red); // draws line
                        y += dy > 0 ? +1 : -1; // returns +1 if positive, -1 if negative
                        error -= 1.0;
                    }
                }
            }
        }
    }
    
    /**
     * Regenerates the board.
     * @author Martin Rivard
     */
    private void regenBoard(){ 
        regenBoard(
                (double) StartScreen.foliageSlider.getValue()/100 , 
                (double) StartScreen.swampSlider.getValue() / 100, 
                (double) StartScreen.hillSlider.getValue() / 100, 
                (double) StartScreen.scaleSlider.getValue() / 100); 
    }
    
    /**
     * Regenerates the board.
     * @param fol
     * @param swa
     * @param hil
     * @param scal
     * @author Martin Rivard
     */
    public void regenBoard(double fol, double swa, double hil, double scal){
        double[] genParams = TerrainGen.calcTerrainParams(fol, swa, hil);
        
        // GENERATE TILES
        tiles = TerrainGen.genTerrain(board_width, board_height, scal, genParams[0],genParams[1],genParams[2],genParams[3], 31, 31); //6.5 is the 'zoom' factor, set higher for a more monotonous landscape, and lower for a more random one.
        //The next 4 numbers are weighings for swamp, forest, plain and hill terrain. A relatively even landscape is made by 1,1,1,1. Experiment around, I feel like forest and plain should be more common than swamp/hill.
        //Note that plain+hill is weighed against swamp+forest. To ensure even weighing between those two subtypes of terrain, swamp+forest must equal plain+hill, hence the 0.9,1.1,1.1,0.9.
        removeAll(); //Clears old stuff from the board.
        
        for (int y=0; y<this.board_height; y++) {
            for (int x=0; x<this.board_width; x++) {
                add(tiles[x][y], new AbsoluteConstraints(x*this.tile_width + this.padding + 1, y*this.tile_height + this.padding + 1, tiles[x][y].getWidth(), tiles[x][y].getHeight()));
            }
        }
        
        int height = 190;
        
        // init info panels
        tile_info = new InfoPanel(this.adjusted_width, this.padding, (this.panel_width - this.adjusted_width - this.padding) / 2, height, "Tile Information", null, null);
        add(tile_info, new AbsoluteConstraints(tile_info.x, tile_info.y, tile_info.width, tile_info.height));
        unit_info = new InfoPanel(tile_info.x + tile_info.width - 1, tile_info.y, (this.panel_width - this.adjusted_width - this.padding) / 2, height, "Unit Information", null, null);
        add(unit_info, new AbsoluteConstraints(unit_info.x, unit_info.y, unit_info.width, unit_info.height));
        player1_info = new PlayerInfoPanel(this.adjusted_width, tile_info.y + tile_info.height - 1,(this.panel_width - this.adjusted_width - this.padding) / 2, height, "Player 1 Info", Main.player1);
        add(player1_info, new AbsoluteConstraints(player1_info.x, player1_info.y, player1_info.width, player1_info.height));
        player2_info = new PlayerInfoPanel(this.adjusted_width + player1_info.width - 1, player1_info.y,(this.panel_width - this.adjusted_width - this.padding) / 2, height, "Player 2 Info", Main.player2);
        add(player2_info, new AbsoluteConstraints(player2_info.x, player2_info.y, player2_info.width, player2_info.height));
        
        
        
        // init output panel
        this.output_panel.setLocation((this.adjusted_height)+1, player1_info.y + player1_info.height + this.padding);
        this.output_panel.setSize((this.panel_width - this.padding) - (this.adjusted_width) - 1, 190);
        this.output = "";
        add(this.output_pane, new AbsoluteConstraints(this.output_panel.getX(), this.output_panel.getY(), this.output_panel.getWidth(), this.output_panel.getHeight()));
        
        // init buttons
        Main.turnButton.setLocation(this.adjusted_width, output_panel.getY() + output_panel.getHeight() + this.padding);
        Main.loadButton.setLocation(Main.turnButton.getX() + Main.turnButton.getWidth() + this.padding, Main.turnButton.getY());
        Main.saveButton.setLocation(Main.loadButton.getX() + Main.loadButton.getWidth() + this.padding, Main.turnButton.getY());
        Main.endButton.setLocation(Main.saveButton.getX() + Main.saveButton.getWidth() + this.padding, Main.turnButton.getY());

        add(Main.turnButton, new AbsoluteConstraints(Main.turnButton.getX(), Main.turnButton.getY(), Main.turnButton.getWidth(), Main.turnButton.getHeight()));
        add(Main.endButton, new AbsoluteConstraints(endButton.getX(), Main.endButton.getY(), Main.endButton.getWidth(), Main.endButton.getHeight()));
        add(Main.saveButton, new AbsoluteConstraints(Main.saveButton.getX(), Main.saveButton.getY(), Main.saveButton.getWidth(), Main.saveButton.getHeight()));
        add(Main.loadButton, new AbsoluteConstraints(Main.loadButton.getX(), Main.loadButton.getY(), Main.loadButton.getWidth(), Main.loadButton.getHeight()));
        
        Player.num_players = 0;
        Tile.num_tiles = 0;
    } 
    
    /**
     * Resets each tile background to the original colour.
     * @author Alexander Dingwall
     */
    public static void clearLines() {
        for (Tile[] y : tiles) {
            for (Tile t : y) {
                if (!t.equals(Tile.focused_tile)) 
                    t.setBackground(t.getColour());
                else 
                    t.repaint();
            }
        } 
    }
    
    /**
     * Prints text to the output panel
     * @param out
     * @author Alexander Dingwall
     */
    public void print(String out) {
        DateFormat df = new SimpleDateFormat("<HH:mm:ss> ("); // adds time stamp
        Date d = new Date(); // gets time
        
        // prints time + current player + out
        this.output += df.format(d) + (Main.player1.getTurn() ? Main.player1.getName() : Main.player2.getName()) + "): " + out;
        this.output_panel.setText(this.output);
        
        // sets scroll bar to bottom
        this.output_pane.getVerticalScrollBar().setValue(this.output_pane.getVerticalScrollBar().getMaximum());
    }
    
    /**
     * Prints text to the output panel
     * @param out
     * @author Alexander Dingwall
     */
    public void println(String out) {
        print(out + '\n');
    }
    
    /**
     * Getter method for the tile_width property
     * @return the tile width
     * @author Alexander Dingwall
     */
    public int getTileWidth() {
        return this.tile_width;
    }
    
    /**
     * Getter method for the tile_height property
     * @return the tile height
     * @author Alexander Dingwall
     */
    public int getTileHeight() {
        return this.tile_height;
    }
    
    /**
     * Getter method for the board_width property
     * @return the board width
     * @author Alexander Dingwall
     */
    public int getBoardWidth() {
        return this.board_width;
    }
    
    /**
     * Getter method for the board_height property
     * @return the board height
     * @author Alexander Dingwall
     */
    public int getBoardHeight() {
        return this.board_height;
    }
    
    /**
     * Getter method for the padding property
     * @return the padding
     * @author Alexander Dingwall
     */
    public int getPadding() {
        return this.padding;
    }
    
    /**
     * Getter method for the panel_width property
     * @return the panel width
     * @author Alexander Dingwall
     */
    public int getPanelWidth() {
        return this.panel_width;
    }
    
    /**
     * Getter method for the panel_height property
     * @return the panel height
     * @author Alexander Dingwall
     */
    public int getPanelHeight() {
        return this.panel_height;
    }
    
    /**
     * Getter method for the adjusted_width property
     * @return the adjusted width
     * @author Alexander Dingwall
     */
    public int getAdjustedWidth(){
        return this.adjusted_width;
    }
    
    /**
     * Getter method for the adjusted_height property
     * @return the adjusted height
     * @author Alexander Dingwall
     */
    public int getAdjustedHeight(){
        return this.adjusted_height;
    }
    
    /**
     * toString method for the class Board
     * @return information about the Board as a String
     * @author Alexander Dingwall
     */
    @Override
    public String toString() {
        return "[Tile Dimensions : (" + getTileWidth() + ", " + getTileHeight() + ")], [Board Dimensions : (" + getBoardWidth() + ", " + getBoardHeight() + ")], [Panel Dimensions : (" + getPanelWidth() + ", " + getPanelHeight() + ")], [Padding : " + getPadding() + "]";
    }
    
    /**
     * Clone method for the class Board
     * @return clone of the Board object. Tile map not included.
     * @throws java.lang.CloneNotSupportedException
     * @author Alexander Dingwall
     */
    @Override
    public Board clone() throws CloneNotSupportedException {
        return new Board(this.tile_width, this.tile_height, this.board_width, this.board_height, this.padding);
    }
    
    /**
     * Equals method for the Board class
     * @param board board to be tested
     * @return true if boards are equal, false if not
     * @author Alexander Dingwall
     */
    public boolean equals(Board board) {
        return this.tile_width == board.tile_width && 
                this.tile_height == board.tile_height &&
                this.board_width == board.board_width &&
                this.board_height == board.board_height &&
                this.padding == board.padding &&
                this.panel_width == board.panel_width &&
                this.panel_height == board.panel_height;
    }
}
