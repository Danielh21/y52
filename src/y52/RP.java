/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package y52;

import battleship.interfaces.BattleshipsPlayer;
import battleship.interfaces.Fleet;
import battleship.interfaces.Position;
import battleship.interfaces.Board;
import battleship.interfaces.Ship;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Tobias
 */
public class RP implements BattleshipsPlayer
{
    private final static Random rnd = new Random();
    private int sizeX;
    private int sizeY;
    private Board myBoard;
    ArrayList<Coordinates> shootsAt = new ArrayList<>();
   
    public RP()
    {
        //hello
    }

   
    /**
     * The method called when its time for the AI to place ships on the board 
     * (at the beginning of each round).
     * 
     * The Ship object to be placed  MUST be taken from the Fleet given 
     * (do not create your own Ship objects!).
     * 
     * A ship is placed by calling the board.placeShip(..., Ship ship, ...) 
     * for each ship in the fleet (see board interface for details on placeShip()).
     * 
     * A player is not required to place all the ships. 
     * Ships placed outside the board or on top of each other are wrecked.
     * 
     * @param fleet Fleet all the ships that a player should place. 
     * @param board Board the board were the ships must be placed.
     */
    @Override
    public void placeShips(Fleet fleet, Board board)
    {
        
        // a round = one game of battleship
        // match = 1000 rounds vs an opponent
        myBoard = board; 
        sizeX = board.sizeX();
        sizeY = board.sizeY();
        
        Ship ship2= fleet.getShip(0); //length 2
        Ship ship301= fleet.getShip(1); //length 3
        Ship ship302= fleet.getShip(2); //length 3
        Ship ship4= fleet.getShip(3); //length 4
        Ship ship5= fleet.getShip(4); //length 5
        
        board.placeShip(new Position(9,8), ship2, true);
        board.placeShip(new Position(1,0), ship301, true);
        board.placeShip(new Position(2,0), ship302, true);
        board.placeShip(new Position(3,0), ship4, true);
        board.placeShip(new Position(4,0), ship5, true);
        
    }

    /**
     * Called every time the enemy has fired a shot.
     * 
     * The purpose of this method is to allow the AI to react to the 
     * enemy's incoming fire and place his/her ships differently next round.
     * 
     * @param pos Position of the enemy's shot 
     */
    @Override
    public void incoming(Position pos)
    {
        //Do nothing
    }

    
    /**
     * Called by the Game application to get the Position of your shot.
     *  hitFeedBack(...) is called right after this method.
     * 
     * @param enemyShips Fleet the enemy's ships. Compare this to the Fleet 
     * supplied in the hitFeedBack(...) method to see if you have sunk any ships.
     * 
     * @return Position of you next shot.
     */
    @Override
    public Position getFireCoordinates(Fleet enemyShips)
    {
        int randomShoot = rnd.nextInt(shootsAt.size());
        
        int x = shootsAt.get(randomShoot).x;
        int y = shootsAt.get(randomShoot).y;
        
        shootsAt.remove(randomShoot);
                                                                  
        return new Position(x,y);
        
        
//        for (Ship ship : enemyShips) {
//            ship.size();
//            //....
//        }
        // You need to create an method 
    }

    
    /**
     * Called right after getFireCoordinates(...) to let your AI know if you hit
     * something or not. 
     * 
     * Compare the number of ships in the enemyShips with that given in 
     * getFireCoordinates in order to see if you sunk a ship.
     * 
     * @param hit boolean is true if your last shot hit a ship. False otherwise.
     * @param enemyShips Fleet the enemy's ships.
     */
    @Override
    public void hitFeedBack(boolean hit, Fleet enemyShips)
    {
        //Do nothing
    }    

    
    /**
     * Called in the beginning of each match to inform about the number of 
     * rounds being played.
     * @param rounds int the number of rounds i a match
     */
    @Override
    public void startMatch(int rounds)
    {
        //Do nothing
    }
    
    
    /**
     * Called at the beginning of each round.
     * @param round int the current round number.
     */
    @Override
    public void startRound(int round)
    {
        //Do nothing
        setAllXY(shootsAt);
        
        
    }

    
    /**
     * Called at the end of each round to let you know if you won or lost.
     * Compare your points with the enemy's to see who won.
     * 
     * @param round int current round number.
     * @param points your points this round: 100 - number of shot used to sink 
     * all of the enemy's ships. 
     *
     * @param enemyPoints int enemy's points this round. 
     */
    @Override
    public void endRound(int round, int points, int enemyPoints)
    {
        //Do nothing
    }
    
    
    /**
     * Called at the end of a match (that usually last 1000 rounds) to let you 
     * know how many losses, victories and draws you scored.
     * 
     * @param won int the number of victories in this match.
     * @param lost int the number of losses in this match.
     * @param draw int the number of draws in this match.
     */
    @Override
    public void endMatch(int won, int lost, int draw)
    {
        //Do nothing
    }

    public void setAllXY(ArrayList<Coordinates> Contains) {
        for (int i = 0; i < 10; i++) {
            
            for (int j = 0; j < 10; j++) {
                Coordinates xy = new Coordinates(i, j, 100);
                Contains.add(xy);
                
            }
            
        }
    }
}
