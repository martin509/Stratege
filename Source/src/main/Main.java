
package main;

import java.awt.Color;
import main.unit.*;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.border.LineBorder;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

/**
 * Main game executable
 * @author Alexander Dingwall, Martin Rivard & Jacob Willemse
 * @since Dec 20, 2015
 *     _____  _                 _                      
 *    /  ___|| |               | |                     
 *    \ `--. | |_  _ __   __ _ | |_   ___   __ _   ___ 
 *     `--. \| __|| '__| / _` || __| / _ \ / _` | / _ \
 *    /\__/ /| |_ | |   | (_| || |_ |  __/| (_| ||  __/
 *    \____/  \__||_|    \__,_| \__| \___| \__, | \___|
 *                                          __/ |      
 *                                         |___/ 
 */
public class Main extends JFrame{
    
    // static variables
    public static List<Unit> units;
    public static Board board;
    
    // concrete units that must be added at startup
    public static Player player1;
    public static Player player2;
    public City city1;
    public City city2;
        
    // frames
    static Main frame;
    static StartScreen screen;
    static EndScreen loseScreen;
    
    // game buttons
    static JButton regenButton, endButton, saveButton, loadButton, turnButton;
    
    // generation variables
    private static double foliage, swamp, hill, scale;
    private final Random generator;
    private final int resource;
                
    /**
     * Main constructor for the program
     * @param screenType 1 is main game screen, 2 is starting menu screen, 3 is game over screen.
     */
    public Main(int screenType) {
        units = new ArrayList<>();
        generator = new Random();
        resource = 10;
        
        // init buttons
        initEndButton();
        initRegenButton();
        initTurnButton();
        SaveSystem.initSaveButton();
        SaveSystem.initLoadButton();
        
        // init players
        Player.resetPlayers();
        player1 = new Player("Blue");
        player2 = new Player("Red");
        player1.setTurn(true);
        
        // init start screen and board
        screen = new StartScreen();
        if (board == null){
            board = new Board(32, 32, 20, 20, 20);
        }
        initUnits();
        
        
       
        switch (screenType){
            case 1:
                // init main frame for the full-size board screen
                this.initFrame(board.getPanelWidth(), board.getPanelHeight());
                
                //add the board
                add(board, new AbsoluteConstraints(0,0, board.getPanelWidth(), board.getPanelHeight()));
                
                break;
            case 2:
                // init main frame for the smaller start screen
                this.initFrame(960, 512);
                
                //add start screen
                add(screen, new AbsoluteConstraints(0, 0, 960, 512));
                break;
            case 3:
                // init main frame for the smaller lose screen. This is for blue losing.
                this.initFrame(960, 512);
                
                loseScreen = new EndScreen(true);
                add(loseScreen, new AbsoluteConstraints(0, 0, 960, 512));
                break;
            case 4:
                // init main frame for the smaller lose screen
                this.initFrame(960, 512);
                
                loseScreen = new EndScreen(false);
                add(loseScreen, new AbsoluteConstraints(0, 0, 960, 512));
                break;
        }
                
        pack();
        
    }
    
    /**
     * Initiates frame properties
     * @author Martin Rivard
     */
    private void initFrame(int width, int height){
        Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(width,height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new AbsoluteLayout());
        this.setLocation(screen_size.width/2-getSize().width/2, screen_size.height/2-getSize().height/2);
    }
    
    /**
     * Initiates the two players and their cities. 
     * @author Alexander Dingwall
     */
    private void initUnits() {
        player1.units = new ArrayList();
        player1.resources = new HashSet();
        player2.units = new ArrayList();
        player2.resources = new HashSet();
        player1.money = 1000;
        player2.money = 1000;
        
        // init cities, generates randomly along opposite sides of the map 
        city1 = new City(generator.nextInt((board.getBoardWidth()-2) - 1) + 1, 0, player1);
        city2 = new City(generator.nextInt((board.getBoardWidth()-2) - 1) + 1, board.getBoardHeight()-1, player2);
        
        board.player1_info.setHealthBar(city1.health_bar_b);
        board.player2_info.setHealthBar(city2.health_bar_b);
    }
    
    /**
     * Generates oil rigs. Prevents the majority of resources from spawning on
     * the same side of the map
     * in half and generating each side separately.
     * @author Alexander Dingwall
     */
    private void generateResources() {
        int x, y; // coordinates of oil rig;
        int half; // amount of rigs in each half of the map.
        
        // if the number of resources given is odd, the extra resource tile is given to
        // player at the top of the map.
        if (resource % 2 == 0) {
            half = resource / 2;
        } else {
            half = resource / 2 + 1;
        }
        
        // generates resources for top half of the map
        for (int r=0; r<half; r++) {
            x = generator.nextInt(board.getBoardWidth()); 
            y = generator.nextInt((board.getBoardHeight() / 2 - 1) - 1) + 2; 
            
            Board.tiles[x][y].setResource(true);
        }
        
        // generates resources for bottom half of the map
        for (int r=half; r<resource; r++) {
            x = generator.nextInt(board.getBoardWidth()); 
            y = generator.nextInt((board.getBoardHeight() - 2) - (board.getBoardHeight() / 2)) + (board.getBoardHeight() / 2); 
            
            Board.tiles[x][y].setResource(true);
        }
        
        // ensures oil rigs are painted in front of the tile texture
        for (Tile[] _y : Board.tiles) {
            for (Tile t : _y) {
                t.repaint();
            }
        }
    }    
    
    /**
     * initializes the default game button UI (black background, green outline, green monospaced text, lights up when moused over)
     * @param bigOrSmall determines if it's initializing UI for a big (like on the main menu) or small (like on game screen) button
     * @param button the button to initialize the UI of
     * @param color Color of the button.
     * @author Martin Rivard
     */
    public static void initButtonUI(JButton button, boolean bigOrSmall, Color color){
        if (bigOrSmall){
            button.setSize(128,32);
            button.setFont(new Font("Monospaced", Font.PLAIN, 24));
           
        }else{
            button.setSize(120,32);
            button.setFont(new Font("Monospaced", Font.PLAIN, 12));
          
        }
        button.setBorder(new LineBorder(color, 1));
        button.setBackground(Color.BLACK);
        button.setForeground(color);
        button.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent me) {
                if (button.isEnabled()){
                    button.setBackground(new Color(color.getRed()/2, color.getGreen()/2, color.getBlue()/2));
                }
            }
            @Override
            public void mouseExited(MouseEvent me){
                button.setBackground(Color.black);
            }
        });
    }
    
    /**
     * Initializes the button that goes back to the menu.
     * @author Martin Rivard
     */
    public static void initEndButton(){
        endButton = new JButton("Menu");
        
        initButtonUI(endButton, false, Color.GREEN);
        
        endButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                frame.removeAll();              
                frame.dispose();
                frame = new Main(2);
                frame.setVisible(true);
                frame.revalidate();
                
            }
        });
    }
    
    /**
     * Initializes the button that ends a players turn
     * @author Jacob Willemse
     */
    private void initTurnButton(){ 
        turnButton = new JButton();
        
        initButtonUI(turnButton, false, Color.YELLOW);
        
        turnButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                if (player1.turn){
                    board.println(player1.getName() + " has ended their turn.");
                }else {
                    board.println(player2.getName() + " has ended their turn.");
                }
               	player1.setMovedF();
                player2.setMovedF();
                Player.switchTurn(player1, player2);
                
                Main.board.unit_info.repaint();
            }
        });
        turnButton.setText("End Turn");
    }
    
    /**
     * Initiates the button that saves the map
     * @author Martin Rivard
     */
    public static void initRegenButton(){
        regenButton = new JButton("New Game");
        
        initButtonUI(regenButton, false, Color.GREEN);
        
        regenButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                board.regenBoard(foliage, swamp, hill, scale);
                units = new ArrayList<>();
                frame.initUnits();
                frame.generateResources();
                board.println("New Game.");
                frame.revalidate();
            }
        });
    }
    
    /**
     * revalidate() essentially 'refreshes' the game.
     * @author Martin Rivard
     */
    @Override
    public void revalidate(){ 
        // revalidates framed
        super.revalidate();
        super.repaint();
        screen.revalidate();
        
        // revalidates panel
        board.revalidate();
    }
    
    /**
     * This sets the game's parameters concretely from the slider screen for the actual game screen.
     * @author Martin Rivard
     */
    public static void setParams(){ 
        foliage = ((double)StartScreen.foliageSlider.getValue())/100;
        hill = ((double)StartScreen.hillSlider.getValue())/100;
        swamp = ((double)StartScreen.swampSlider.getValue())/100;
        scale = ((double)StartScreen.scaleSlider.getValue())/100;
    }

    /**
     * Getter method for the generation parameters
     * @return the generation parameters as an array of doubles
     * @author Martin Rivard
     */
    public static double[] getParams(){
        return new double[]{foliage, hill, swamp, scale};
    }
    
   /**
    * Goes to the game-over screen when it's lost.
    * @param team which team lost.
    * @author Martin Rivard
    */
    public static void goToEnd(boolean team){
        frame.removeAll();
        frame.dispose();
        if (team){
            frame = new Main(3);
        }else{
            frame = new Main(4);
        }
        frame.setVisible(true);
        frame.revalidate();
    }
    
    /**
     * Goes to the game screen from the menu.
     * @param type determines if the game will be loaded or generated from scratch
     * @author Martin Rivard
     */
    public static void goToGame(boolean type){
        setParams();
        frame.removeAll();
        frame.dispose();
        
        frame = new Main(1);
        frame.setVisible(true);
        board.regenBoard(foliage, swamp, hill, scale);
        
        if (type){
            SaveSystem.loadGame();
            //board.println("Loaded Game.");
        }else{
            frame.generateResources();
            frame.initUnits();
            board.println("New Game.");
            
            Main.player1.city.getTile().setFocusedTile();
        }
        
        board.player1_info.setHealthBar(player1.city.health_bar_b);
        board.player2_info.setHealthBar(player2.city.health_bar_b);
        frame.revalidate();
    }
    
    /**
     * Calls the constructor for Main()
     * @param args 
     */
    public static void main(String[] args) {
        frame = new Main(2);
        frame.setVisible(true);
        frame.revalidate();
    }
    
}