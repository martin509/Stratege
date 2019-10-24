
package main;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import javax.swing.JButton;
import static main.Main.player1;
import static main.Main.player2;
import main.unit.*;
import javax.swing.filechooser.FileSystemView;

/**
 * Manages saving and loading the board
 * @author Martin Rivard
 */
public class SaveSystem {
        
    /**
     * Initializes Main.saveButton.
     * @author Martin Rivard
     */
    public static void initSaveButton(){
        Main.saveButton = new JButton("Save Game");
        
        Main.initButtonUI(Main.saveButton, false, Color.GREEN);
        
        Main.saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                saveGame();
                Main.board.println("Game saved.");
            }
        });
    }
    
    /**
     * Initializes Main.loadButton
     * @author Martin Rivard
     */
    public static void initLoadButton(){
        Main.loadButton = new JButton("Load Game");
        
        Main.initButtonUI(Main.loadButton, false, Color.GREEN);
        
        Main.loadButton.addMouseListener(new MouseAdapter() {            
            @Override
            public void mousePressed(MouseEvent me) {
                loadGame();
                Main.board.output_panel.setText("");
                Main.board.println("Game Loaded.");
                Main.board.tile_info.repaint();
                Main.board.unit_info.repaint();
                Main.board.player1_info.repaint();
                Main.board.player2_info.repaint();
                Main.player1.city.getTile().setFocusedTile();
            }
        });
    }
    
    /**
     * This takes lines of text read from a file and loads in a terrain.
     * @param map Lines of text read from a file that represent a saved map.
     * @author Martin Rivard
     */
    private static void loadBoard(String[] map){
        char[][] in = mapStringToCharArray(map);
        Tile[][] tiles = new Tile[in.length][in[0].length];
        for (int x = 0; x < in.length; x++){
            for (int y = 0; y < in[x].length; y++){
                switch (in[x][y]){
                    case 's' : tiles[x][y] = new Tile("Swamp", x, y, 31, 31, TerrainGen.swampColour, "A tract of wet, spongy land. \n-1 defence, -1 offence", "swamp.png", -1,-1,0);
                        break;
                    case 'f' : tiles[x][y] = new Tile("Forest Tile", x, y, 31, 31, TerrainGen.forestColour,"A thick growth of trees and brush. \n+2 defence, +0 offence.", "forest.png", 2,0,0);
                        break;
                    case 'p': tiles[x][y] = new Tile("Plains Tile",x, y, 31, 31, TerrainGen.plainColour, "Broad area of relatively flat land. \n+0 defence, +0 offence", "grass.png", 0,0,0);
                        break;
                    case 'h': tiles[x][y] = new Tile("Hill Tile", x, y, 31, 31, TerrainGen.hillColour, "A naturally raised area. \n+3 defence, +2 offence",  "grass.png", 3,2,0);
                        break;
                    case 'S' : tiles[x][y] = new Tile("Swamp", x, y, 31, 31, TerrainGen.swampColour, "A tract of wet, spongy land. \n-1 defence, -1 offence", "swamp.png", -1,-1,0);
                        tiles[x][y].setResource(true);
                        break;
                    case 'F' : tiles[x][y] = new Tile("Forest Tile", x, y, 31, 31, TerrainGen.forestColour,"A thick growth of trees and brush. \n+2 defence, +0 offence.", "forest.png", 2,0,0);
                        tiles[x][y].setResource(true);
                        break;
                    case 'P' : tiles[x][y] = new Tile("Plains Tile",x, y, 31, 31, TerrainGen.plainColour, "Broad area of relatively flat land. \n+0 defence, +0 offence", "grass.png", 0,0,0);
                        tiles[x][y].setResource(true);
                        break;
                    case 'H' : tiles[x][y] = new Tile("Hill Tile", x, y, 31, 31, TerrainGen.hillColour, "A naturally raised area. \n+3 defence, +2 offence",  "grass.png", 3,2,0);
                        tiles[x][y].setResource(true);
                        break;
                }
            }
        }
        Main.board.setTiles(tiles);
    }
    
    /**
     *
     * @param in the Board to be saved.
     * @return Lines of text representing the current terrain.
     * @author Martin Rivard
     */
    private static String[] saveBoard(){
        String[] out = new String[Board.tiles.length];
        for (int x = 0; x < Board.tiles.length; x++){
            out[x] = "";
            for (int y = 0; y < Board.tiles[x].length; y++){
                if (Board.tiles[x][y].isResource()){
                    if (Board.tiles[x][y].getName().equals("Swamp")){
                        out[x] += "S";
                    }else if (Board.tiles[x][y].getName().equals("Forest Tile")){
                        out[x] += "F";
                    }else if (Board.tiles[x][y].getName().equals("Plains Tile")){
                        out[x] += "P";
                    }else{
                        out[x] += "H";
                    }
                }else {
                    if (Board.tiles[x][y].getName().equals("Swamp")){
                        out[x] += "s";
                    }else if (Board.tiles[x][y].getName().equals("Forest Tile")){
                        out[x] += "f";
                    }else if (Board.tiles[x][y].getName().equals("Plains Tile")){
                        out[x] += "p";
                    }else{
                        out[x] += "h";
                    }
                }
                
                
            }
            
        }
        
        return out;
    }
    
    /**
     *
     * @return
     * @author Martin Rivard
     */
    private static String[] savePlayers(){
        ArrayList<String> buffer = new ArrayList();
        
        buffer.add("%");
        buffer.add(Main.player1.toString());
        String[] units1 = saveUnits(Main.player1);
        for (int t = 0; t < units1.length; t++){
            buffer.add(units1[t]);
        }
        buffer.add("%");
        buffer.add(Main.player2.toString());
        String[] units2 = saveUnits(Main.player2);
        for (int t = 0; t < units2.length; t++){
            buffer.add(units2[t]);
        }
        String[] out = new String[buffer.size()];
        buffer.toArray(out);
        return out;
    }
    
    /**
     * 
     * @param player The player whose units are being saved.
     * @return An array of Strings representing all the Units the player has.
     * @author Martin Rivard
     */
    private static String[] saveUnits(Player player){
        ArrayList<String> buffer = new ArrayList();
        Unit curUnit; //current unit
        
        for (int n = 0; n < player.getUnits().size(); n++){
            curUnit = player.getUnits().get(n);
            buffer.add(curUnit.toString());
        }
        String[] out = new String[buffer.size()];
        buffer.toArray(out);
        return out;
    }
    
    /**
     * 
     * @param split The lines of characters representing the map
     * @return A 2D array of characters fit to be easily read to a map.
     * @author Martin Rivard
     */
    private static char[][] mapStringToCharArray(String[] split){
        
        char[][] out = new char[split.length][split[0].length()];
        for (int x = 0; x < out.length; x++){
            for (int y = 0; y < out[x].length; y++){
                out[x][y] = split[x].toCharArray()[y];
            }
        }
        return out;
    }
    
    /**
     *
     * @author Martin Rivard
     */
    public static void saveGame(){
        writeFile(saveBoard(), savePlayers());
    }
    
    
    /**
     * 
     * @param map A String[] representing the current terrain.
     * @param players A String[] representing the two players and their units.
     * @author Martin Rivard
     */
    private static void writeFile(String[] map, String[] players){
        try {
            OutputStream fout= new FileOutputStream(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + File.separator + "save1.wad"); //.wad stands for "Where's All the Data?", as used by the original DOOM.
            OutputStream bout= new BufferedOutputStream(fout);
            OutputStreamWriter out = new OutputStreamWriter(bout);
            
            for(int n = 0; n < map.length; n++){
                out.write(map[n] + "\r\n");
            }
            //out.write("\r\n");
            for (int n = 0; n < players.length; n++){
                out.write(players[n] + "\r\n");
            }
            out.write("%");
            
            out.flush();
            out.close();
        }catch(IOException e){
                
        }
    }
    
    /**
     * 
     * @param player The player that is being loaded.
     * @param in The lines of text being read from. The first line details the player and their resources, every line after that details their units.
     * @author Martin Rivard
     */
    private static void readPlayer(Player player, String[] in){
        String[] splitPlayer = in[0].split("-");

        player.setTurn(Boolean.parseBoolean(splitPlayer[2]));
        player.money = Integer.parseInt(splitPlayer[1]);
        player.units = new ArrayList();
        player.units_moved = 0;
        Main.units = new ArrayList();
        if (splitPlayer.length > 3){
            for (int n = 3; n < splitPlayer.length; n++){
                String[] coords = splitPlayer[n].split("_");
                int x = Integer.parseInt(coords[0]);
                int y = Integer.parseInt(coords[1]);
                Board.tiles[x][y].setResource(player);   
            }
        }
        
        
        for (int n = 1; n < in.length; n++){
            readUnit(in[n].split("-"), player);
        } 
        
        
    }
    
    /**
     * 
     * @param unit
     * @param player 
     * @author Martin Rivard
     */
    private static void readUnit(String[] unit, Player player){
        String name = unit[0];
        int x = Integer.parseInt(unit[1]);
        int y = Integer.parseInt(unit[2]);
        int hp = Integer.parseInt(unit[3]);
        int def = Integer.parseInt(unit[4]);
        int off = Integer.parseInt(unit[5]);
        int level = Integer.parseInt(unit[6]);
        
        if (name.equals("Infantry")){
            Infantry infantry = new Infantry(x, y, player, level, hp, def, off);
        }else if (name.equals("Medic")){
            Medic medic = new Medic(x, y, player, level, hp, def, off);
        }else if (name.equals("Artillery")){
            Artillery artillery = new Artillery(x, y, player, level, hp, def, off);
        }else if (name.equals("Tank")){
            Tank tank = new Tank(x, y, player, level, hp, def, off);
        }else if (name.equals("Worker")){
            Worker worker = new Worker(x, y, player, level, hp, def, off);
        }else {
            City city = new City(x, y, player, level, hp, def, off);
        }
    }
    
    /**
     * Loads the game from file save1.wad.
     * @author Martin Rivard
     */
    public static void loadGame(){
        File test = new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + File.separator + "save1.wad");
        if (!test.exists()){
            Main.goToGame(false);
            Main.board.println("Save file does not exist.");
            return;
        }
        ArrayList<String> mapBuffer = new ArrayList();
        ArrayList<String> player1Buffer = new ArrayList();
        ArrayList<String> player2Buffer = new ArrayList();
        String currentLine = "";
        BufferedReader br = null;
        
        try{
            br = new BufferedReader(new FileReader(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + File.separator + "save1.wad"));
            currentLine = br.readLine();
                while (!currentLine.equals("%")){
                    mapBuffer.add(currentLine);
                    currentLine = br.readLine();
                }
                currentLine = br.readLine();
            
                while (!currentLine.equals("%")){
                    if (!currentLine.equals("%")){
                        player1Buffer.add(currentLine);  
                    }
                    currentLine = br.readLine();
                }
                currentLine = br.readLine();

                while (!currentLine.equals("%")){
                    if (!currentLine.equals("%")){
                        player2Buffer.add(currentLine);
                    }
                    currentLine = br.readLine();
                }
            
            
        }catch (IOException e){ //Various exception handling code.
             e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        Player.resetPlayers();
        
        String[] mapOut = new String[mapBuffer.size()];
        mapBuffer.toArray(mapOut);
        loadBoard(mapOut);
        
        String[] player1Out = new String[player1Buffer.size()]; //This puts the ArrayList of strings read from the file into an array and returns that array.
        player1Buffer.toArray(player1Out);
        readPlayer(Main.player1, player1Out);
        
        String[] player2Out = new String[player2Buffer.size()]; //This puts the ArrayList of strings read from the file into an array and returns that array.
        player2Buffer.toArray(player2Out);
        readPlayer(Main.player2, player2Out);
        
        for (Unit u : Main.units) {
            if (u.health < u.max_health) {
                u.updateTileHealthBar();
                if (u.name.equals(u.player.getName() + "\'s City")) {
                    if (u.player.team) {                        
                        u.health_bar_f.setSize((int) (((double) u.health / u.max_health) * 85), 7);
                        Main.board.player1_info.setHealthBar(u.health_bar_b);
                        Main.board.player1_info.repaint();
                    } else { 
                        u.health_bar_f.setSize((int) (((double) u.health / u.max_health) * 85), 7);
                        Main.board.player2_info.setHealthBar(u.health_bar_b);
                        Main.board.player2_info.repaint();
                    }
                }
            }
        }
        
        if (player1.turn) 
            player1.city.getTile().setFocusedTile();
        else 
            player2.city.getTile().setFocusedTile();
        Main.board.println("Loaded game.");
    }
    
}
