package main;

import java.awt.Color;

/**
 * Generates terrain using OpenSimplexNoise
 * @author Martin Rivard
 * @since Jan 3, 2016
 */
public class TerrainGen {
    
    static OpenSimplexNoise simplex;
    
    public static Color swampColour = new Color(150, 150, 150);
    public static Color forestColour = new Color(0, 200, 0);
    public static Color plainColour = new Color(0, 250, 0);
    public static Color hillColour = new Color(160, 200, 160);
    
    /**
     * 
     * @param width x-size of the heightmap.
     * @param height y-size of the heightmap. Not to be confused with the amplitude of the values.
     * @param xScale 'zoom' factor of the heightmap.
     * @param yScale Amplitude of the heightmap.
     * @param threshold The threshold for the values.
     * @return map of values based on a simplex-generated height map. Values in the map above the threshold are true, the rest false.
     * @author Martin Rivard
     */
    public static boolean[][] simplexGen(int width, int height, double xScale, double yScale, double threshold){ //Simplex terrain generation. xScale is how much it's 'zoomed in', yScale is how much it's 'amplified' (by default it ranges from -1 to +1).
        simplex = new OpenSimplexNoise(System.currentTimeMillis()); //Make a new simplexnoise object with the system time as a seed.
        //This makes a map of simplex noise
        double[][] map = new double[width][height]; 
        boolean[][] out = new boolean[width][height];
        double seed = (Math.random()*1000);
        //System.out.println("Threshold: " + threshold + ", scale: " + yScale);
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
               map[x][y] = simplex.eval(((double)x)/xScale, ((double)y)/xScale, seed)*yScale;
                //System.out.println("x: " + x + " y: " + y  + " val: "+ map[x][y]); //Debug messages.
                if (map[x][y] > threshold){
                    out[x][y] = true;
                }else {
                    out[x][y] = false;
                }
            }
        }
        return out;
    }
    
    /**
     * Returns a map of randomly generated tiles
     * @param width Width of the board in tiles.
     * @param height Height of the board in tiles.
     * @param xScale 'zoom' factor, determines the regularity of the terrain.
     * @param swampSize Factor determining how much of the tiles will be swamps.
     * @param forestSize Factor determining how much of the tiles will be forests.
     * @param plainSize Factor determining how much of the tiles will be plains.
     * @param hillSize Factor determining how much of the tiles will be hills.
     * @param tileWidth Width of each tile.
     * @param tileHeight Height of each tile.
     * @return 2D array of Tiles representing a new terrain.
     * @author Martin Rivard
     */
    public static Tile[][] genTerrain(int width, int height, double xScale,  double swampSize, double forestSize, double plainSize, double hillSize, int tileWidth, int tileHeight){ //xScale is how much zoom-in you want, all the various sizes are for how much of each type of terrain you want (there has to be at least one not set to 0!)
        // Basically, this first splits a map into areas of swamp+forest and areas of plain+hill, then splits those again.
        
        Tile[][] out = new Tile[width][height];
        
        double totalRange = (forestSize + swampSize + plainSize + hillSize)/4;
        double swampForest = (double)(swampSize + forestSize);
        double plainHill = (double)(plainSize + hillSize);
        boolean[][] map1 = simplexGen(width, height, xScale, totalRange, (swampForest/plainHill) - 1); //Divides into swamp/forest and plain/hill
        boolean[][] map2 = simplexGen(width, height, xScale, swampForest/2, swampSize - 1); //Divides swamp/forest into swamp and forest
        boolean[][] map3 = simplexGen(width, height, xScale, plainHill/2, plainSize - 1); //Divides plain/hill into plain and hill
        
        //Debug values for calculating percentage of all that stuff
        int nForest = 0, nSwamp = 0, nHill = 0, nPlain = 0; 
        double total; 
        
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                if (map1[x][y] && !map2[x][y]){
                    out[x][y]  = new Tile("Swamp", x, y, tileWidth, tileHeight, swampColour, "A tract of wet, spongy land. \n-1 defence, -1 offence", "swamp.png", -1,-1,0);
                    nSwamp++;
                }else if (map1[x][y] && map2[x][y]){
                    out[x][y] = new Tile("Forest Tile", x, y, tileWidth, tileHeight, forestColour,"A thick growth of trees and brush. \n+2 defence, +0 offence.", "forest.png", 2,0,0);
                    nForest++;
                } else if (!map1[x][y] && map3[x][y]){
                    out[x][y] = new Tile("Hill Tile", x, y, tileWidth, tileHeight, hillColour, "A naturally raised area. \n+3 defence, +2 offence",  "grass.png", 3,2,0);
                    nHill++;
                } else{
                    out[x][y] = new Tile("Plains Tile",x, y, tileWidth, tileHeight, plainColour, "Broad area of relatively flat land. \n+0 defence, +0 offence", "grass.png", 0,0,0);
                    nPlain++;
                }
            }
        }
        total = nForest + nSwamp + nHill + nPlain; //Debug stuff
        //System.out.println("Swamp %: " + (int)((double)nSwamp*100/total));
        //System.out.println("Forest %: " + (int)((double)nForest*100/total));
        //System.out.println("Hill %: " + (int)((double)nHill*100/total));
        //System.out.println("Plain %: " + (int)((double)nPlain*100/total));
        
        return out;
    }

    /**
     * Calculates the parameters to pass to the generator
     * @param foliageBalance
     * @param swampBalance
     * @param hillBalance
     * @return
     * @author Martin Rivard
     */
    public static double[] calcTerrainParams(double foliageBalance, double swampBalance, double hillBalance){ //This takes the 3 terrain gen arguments and makes it into a set of params for the actual generator.
        //all parameters go from 0 to 10, where 5 is 1:1 ratio, 0 is 0:1, and 10 is 1:0
        //foliageBalance goes from swamp/forest(low) to hill/plain(high)
        //swampBalance goes from forest(low) to swamp(high)
        //hillBalance goes from plain(low) to hill(high)
        double[] out = new double[4];
        double swampForest, hillPlain;
        double avg;
        
        hillPlain = foliageBalance;
        swampForest = 10-foliageBalance;
        
        out[0] = swampBalance*swampForest/10;
        out[1] = (10-swampBalance)*swampForest/10;
        out[2] = (10-hillBalance)*hillPlain/10;
        out[3] = hillBalance*hillPlain/10;
        avg = (out[0] + out[1] + out[2] + out[3])/4;
        for (int n = 0; n < 4; n++){
            out[n] = out[n]/avg;
        }
//        System.out.println("input: " + foliageBalance + ", " + swampBalance + ", " + hillBalance);
//        System.out.println("swamp: " + out[0] + ", forest: " + out[1] + ", plain: "+  out[2] + ", hill: " + out[3]);
//        System.out.println("sum: " + (out[0] + out[1] + out[2] + out[3]));
        return out;
    }
            
}

