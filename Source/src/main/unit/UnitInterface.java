
package main.unit;

import main.Tile;

/**
 * Defines methods that all entities must implement
 * @author Alexander Dingwall
 * @since Dec 20, 2015
 */
public interface UnitInterface {
    
    public void levelUp();
    public void attack(Unit unit, int max);
    public boolean move(int dx, int dy);
    
    public Tile getTile();
    public void setTile(Tile tile);
        
    public int[] getLocation();
    public void setLocation(int[] location);
    public void setLocation(int x, int y);
}
