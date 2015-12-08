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
import java.util.Comparator;
import java.util.PriorityQueue;

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
    private int shipsBeforeShot;
    private PriorityQueue<Coordinates> priorities;
    private Coordinates shot;
    public Coordinates[][] board = new Coordinates[10][10];
    
   
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
        for (Coordinates c : priorities) {
            System.out.println("Priorites:" + c.x + "," + c.y);
        }
        if(!priorities.isEmpty()){                                  //Will take the top from the priorities que.                    
            shot =priorities.poll();                                // And removes it. 
            int x = shot.x;
            int y = shot.y;
            System.out.println("Shoots at: " + x + "," + y);
            return new Position(x, y);
        }
        else{                                               // Will only excicure if their is nothing in the prioviteque.
        int randomX = rnd.nextInt(board.length);          // Finds a random number for the size of the array.
        int randomY = rnd.nextInt(board[1].length);
        shot = board[randomX][randomY];                      // Creates an instance.
        if(shot.getPre()==0){
            randomX = rnd.nextInt(board.length);
            randomY = rnd.nextInt(board[1].length);
            shot = board[randomX][randomY];
        }
         
                  for (Coordinates board[] : board) {               //runs through the array to check if if something has
                        for (Coordinates coor : board) {            // an higher %
                          if(coor.pre > shot.getPre()){
                              shot = coor;
                          }
                      }
            }

        
       int x = shot.x;
       int y = shot.y;
        System.out.println("Shoots Random at: " + x + "," + y);
        board[x][y].setPre(0);                             //sets the % to 0, because it's about to fire at the coordinat
                                                                  
        return new Position(x,y);
        }
       
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
        
        if(hit){
            System.out.println("We have a hit");
        
        if(shipsBeforeShot > enemyShips.getNumberOfShips()){ // Means that we hit and Wrecked 
           shipsBeforeShot= enemyShips.getNumberOfShips();
            
        }
        else{ // Means that we hit and did not Wreck
        prioritiseNabors(shot);
            
        }
        
        }
        
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
        Comparator<Coordinates> comparator = new Comparator<Coordinates>() {

            @Override
            public int compare(Coordinates o1, Coordinates o2) {
                if(o1.getPre() > o2.getPre()){
                    return 1;
                }
                else if (o1.getPre() < o2.getPre()){
                    return -1;
                }
                return 0;
            }
        };
        priorities = new PriorityQueue<>(100,comparator);
    }
    
    
    /**
     * Called at the beginning of each round.
     * @param round int the current round number.
     */
    @Override
    public void startRound(int round)
    {
        setBoard(board);
        shipsBeforeShot =5;
        priorities.clear();
        board[9][9].setPre(9);
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



    private void prioritiseNabors(Coordinates shot) {
        
        /*Easelier seen when drawing the board!
        left nabour = board[shot.x-1][shot.y]
        right nabour = board[shot.x+1][shot.y]
        under nabour = board[shot.x][shot.y-1]   
        above nabour = board[shot.x][shot.y+1]
        */
        
    
        if(board[shot.x-1][shot.y]!= null && board[shot.x-1][shot.y].getPre()!=0){
            priorities.add(board[shot.x-1][shot.y]);
            board[shot.x-1][shot.y].setPre(0);
        }
        
         if(board[shot.x+1][shot.y] != null && board[shot.x+1][shot.y].getPre()!=0){
            priorities.add(board[shot.x+1][shot.y]);
            board[shot.x+1][shot.y].setPre(0);
        }
         
          if(board[shot.x][shot.y-1] != null && board[shot.x][shot.y-1].getPre()!=0){
            priorities.add(board[shot.x][shot.y-1]);
            board[shot.x][shot.y-1].setPre(0);
        }
          
           if(board[shot.x][shot.y+1] != null && board[shot.x][shot.y+1].getPre()!=0){
            priorities.add(board[shot.x][shot.y+1]);
            board[shot.x][shot.y+1].setPre(0);
        }
    }
    
     public void setBoard(Coordinates[][] array) {

        for (int i = 0; i < array.length; i++) {

            for (int j = 0; j < array[i].length; j++) {
                 array[i][j] = new Coordinates(i, j, 1);
            }
            
        }
        
    }
     
     public void setGritShots(Coordinates[][] array){
         for (int i = 0; i < array.length; i++) {

            for (int j = 0; j < array[i].length; j++) {
                 if(i%2==0 && j%2!=0){
                     array[i][j].setPre(2);
                 }
            }
            
        }
     }
}
