
package main;

import main.unit.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Defines properties and methods for each player. 
 * @author Alexander Dingwall
 * @since Dec 20, 2015
 */
public class Player {
    
    public static int num_players = 0;
    
    // object properties
    private String name;
    public List<Unit> units;
    public int units_moved;
    public Set<Tile> resources;
    public City city;
    public int money;
    
    public boolean turn;
    public boolean team; // true if team blue, false if team red
    
    /**
     * Main constructor for the class Player
     * @param name name of the player, will be set to default if empty
     * @author Alexander Dingwall
     */
    public Player(String name) {
        if (num_players == 2) {
            System.out.println("ERROR: >2 players created.");
            return;
        }
        
        num_players++;
        
        this.name = name.replaceAll("\\s+", "").equals("") ? "Player " + num_players : name;
        this.team = num_players == 1; // true if first player, false if not
        this.units = new ArrayList<>();
        this.units_moved = 0;
        this.turn = false;
        this.resources = new HashSet<>();
        this.money = 1000;
    }
    
    /**
     * Default constructor for the class Player. Sets name and other properties to default.
     * @author Alexander Dingwall
     */
    public Player() {
        this("");
    }
    
    /**
     * Constructor for the class Player, for player loading
     * @param name name of the player
     * @param defence total defence stat for each unit belonging to the player
     * @param offence total offence stat for each unit belonging to the player
     * @param resources total amount of resources
     * @param units an ArrayList including each unit belonging to the player
     * @param turn true if it is currently the players turn, false if not
     * @author Alexander Dingwall
     */
    private Player(String name, int money, List<Unit> units, boolean turn) {
        num_players++;
        this.name = name;
        this.units = units;
        this.turn = turn;
        this.money = money;
    }
    
    /**
     * Method to govern the switching of turns
     * @param p1 Player 1
     * @param p2 Player 2
     * @author Alexander Dingwall
     */
    public static void switchTurn(Player p1, Player p2) {
        if (p1.getTurn()) {
            p1.setTurn(false);
            p1.income();
            p2.setTurn(true);
            p2.units_moved = 0;
            p2.city.getTile().setFocusedTile();
            Main.board.println("It is now " + p2.getName() + "\'s turn!");
        } else if (p2.getTurn()) {
            p1.setTurn(true);
            p1.units_moved = 0;
            p2.setTurn(false);
            p2.income();
            Main.board.println("It is now " + p1.getName() + "\'s turn!");
        }
        Main.board.player1_info.repaint();
        Main.board.player2_info.repaint();
    }
    
    /**
     * Resets the number of players to zero
     * @author Martin Rivard
     */
    public static void resetPlayers(){
        num_players = 0;
    }
    
    /**
     * Method sets all units under the inputed player boolean moved variable to false 
     * @author Jacob Willemse
     */
    public void setMovedF(){
       int numUnits = this.units.size();
       
       for (int i = 0; i < numUnits; i ++){
           units.get(i).moved = false;
       }
   }
    
    /**
     * Converts List to array and calls sortUnitsByLocation(Unit[], int, int, true)
     * @author Alexander Dingwall
     */
    public void sortUnitsByLocation() {
        Unit[] a = (Unit[]) this.units.toArray(new Unit[0]);
        
        sortUnits(a, 0, a.length-1, true);
        
        this.units = new ArrayList<>();
        this.units.addAll(Arrays.asList(a));
    }
    
    /**
     * Converts List to array and calls sortUnitsByLocation(Unit[], int, int, false)
     * @author Alexander Dingwall
     */
    public void sortUnitsByType() {
        Unit[] a = (Unit[]) this.units.toArray(new Unit[0]);
        
        sortUnits(a, 0, a.length-1, false);
        
        this.units = new ArrayList<>();
        this.units.addAll(Arrays.asList(a));
    }
    
    /**
     * Sorts a list of units by location or by type.
     * @param a array of units to be sorted
     * @param left left side of list
     * @param right right side of list
     * @param type true to sort by location, false to sort by unit type
     * @author Alexander Dingwall
     */
    private static void sortUnits(Unit[] a, int left, int right, boolean type) {
        if (left >= right) {
            return;
        }
        int i = left;
        int j = right;
        
        // Get the pivot element from the middle of the list
        Unit pivot = a[((left + right) / 2)];
        
        // Divide into two list
        while (i < j) {
            
            // If the current value from the left list is smaller than the pivot
            while ((type ? Player.compareUnitsByLocation(a[i], pivot) : Player.compareUnitsByType(a[i], pivot)) == -1) {
                i++;
            }
            // If the current value from the right list is larger than the pivot
            while ((type ? Player.compareUnitsByLocation(a[j], pivot) : Player.compareUnitsByType(a[j], pivot)) == +1) {
                j--;
            }
            
            // If we have found a value in the left list which is larger then
            // the pivot element and if we have found a value in the right list
            // which is smaller then the pivot element then we exchange the
            // values. 
            if (i <= j) {
                Unit temp = a[i];
                a[i] = a[j];
                a[j] = temp; 
                
                // As we are done we can increase i and decrease j
                i++;
                j--;
            }
        }
        
        // Recursion
        sortUnits(a, left, j, type); // sort the left side of the list
        sortUnits(a, i, right, type); // sort the right side of the list
    }
    
    /**
     * Compares units based on location
     * @param u1
     * @param u2
     * @return -1 if first unit is smaller, 0 if same, 1 if first unit is larger
     * @author Alexander Dingwall
     */
    public static int compareUnitsByLocation(Unit u1, Unit u2) {
        if (u1.y > u2.y) { // if first units y value is larger
            return +1;
        } else if (u1.y == u2.y) { // if y values are the same
            
            if (u1.x > u2.x) { // if first units x value is larger
                return +1;
            } else if (u1.x == u2.x) { // if x values are the same
                return 0; 
            } else { // if first units x value is smaller
                return -1;
            }
            
        } else { // if first units x valiue is smaller
            return -1;
        }
    }
    
    private void income(){
        this.money += (this.resources.size() + 1)*300;
    }
    
    /**
     * Compares units based on type
     * @param u1
     * @param u2
     * @return -1 if first unit is smaller, 0 if same, 1 if first unit is larger
     * @author Alexander Dingwall
     */
    public static int compareUnitsByType(Unit u1, Unit u2) {
        int u1_value;
        int u2_value;
        
        switch(u1.name) {
            case "Infantry": u1_value = 2;
                break;
            case "Worker": u1_value = 3;
                break;
            case "Medic": u1_value = 4;
                break;
            case "Tank": u1_value = 5;
                break;
            case "Artillery": u1_value = 6;
                break;
            default: u1_value = 1; // if city, since the names could be different
        }
        
        switch(u2.name) {
            case "Infantry": u2_value = 2;
                break;
            case "Worker":  u2_value = 3;
                break;
            case "Medic":  u2_value = 4;
                break;
            case "Tank":  u2_value = 5;
                break;
            case "Artillery":  u2_value = 6;
                break;
            default: u2_value = 1; // if city, since the names could be different
        }
        
        if (u1_value > u2_value) 
            return +1;
        else if (u1_value == u2_value) 
            return Player.compareUnitsByLocation(u1, u2); // if the type is the same, compare by location
        else
            return -1;
    }
    
    /**
     * Getter method for the name property
     * @return the name of the player
     * @author Alexander Dingwall
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Getter method for the city property
     * @return the city belonging to the player
     * @author Alexander Dingwall
     */
    public City getCity() {
        return this.city;
    }
    
    /**
     * Setter method for the city property
     * @param city new city
     * @author Alexander Dingwall
     */
    public void setCity(City city) {
        this.city = city;
    }

    /**
     * Getter method for the units property
     * @return the units of the player as an ArrayList
     * @author Alexander Dingwall
     */
    public List<Unit> getUnits() {
        return this.units;
    }
    
    /**
     * Getter method for the current turn
     * @return the current turn status
     * @author Alexander Dingwall
     */
    public boolean getTurn() {
        return this.turn;
    }

    /**
     * Setter method for the current turn
     * @param turn new turn status
     * @author Alexander Dingwall
     */
    public void setTurn(boolean turn) {
        this.turn = turn;
    }
    
    /**
     * toString method for the class Player
     * @return information about the player, including name, stats and the turn status.
     * @author Martin Rivard
     */
    @Override
    public String toString() {
        String out = (this.name + "-" + this.money + "-" + this.turn);
        if (this.resources.size() > 0){
            for (Tile t : this.resources){
                out += ("-" + t.x + "_" + t.y);
            }
        }
         return (out);
    }
    
    /**
     * Clone method for the class Player
     * @return an exact copy of the object
     * @throws java.lang.CloneNotSupportedException
     * @author Alexander Dingwall
     */
    @Override
    public Player clone() throws CloneNotSupportedException {
        return new Player(this.name, this.money, this.units, this.turn);
    }
    
    /**
     * Equals method for the class Player.
     * @param player unit for the instance to be tested against
     * @return true if instance and given parameter are equal, false if not.
     * @author Alexander Dingwall
     */
    public boolean equals(Player player) {
        return this.name.equals(player.getName()) &&
                this.city.equals(player.city) &&
                this.money == player.money &&
                this.units.equals(player.units) &&
                this.units.size() == player.units.size() &&
                this.turn == player.turn;
    }
}
