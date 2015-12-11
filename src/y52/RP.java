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

/**
 *
 * @author Tobias
 */
public class RP implements BattleshipsPlayer {

    private final static Random rnd = new Random();
    private int shipsBeforeShot;
    private ArrayList<Coordinates> priorities;
    private ArrayList<Coordinates> heapMap;
    ArrayList<Coordinates> listOfGrits;
    private Coordinates shot;
    public static Coordinates[][] board;
    private int checkShips = 1;
    public static int[][] boardIntArray;
    public static int[][] placesHit;
    private int x;
    private int y;
    private int startShot = 0;
    Ship Lagerst = null;
    Ship Smallest = null;
    

    public RP() {
        //hello 
    }

    /**
     * The method called when its time for the AI to place ships on the board
     * (at the beginning of each round).
     *
     * The Ship object to be placed MUST be taken from the Fleet given (do not
     * create your own Ship objects!).
     *
     * A ship is placed by calling the board.placeShip(..., Ship ship, ...) for
     * each ship in the fleet (see board interface for details on placeShip()).
     *
     * A player is not required to place all the ships. Ships placed outside the
     * board or on top of each other are wrecked.
     *
     * @param fleet Fleet all the ships that a player should place.
     * @param board Board the board were the ships must be placed.
     */
    @Override
    public void placeShips(Fleet fleet, Board board) {

        // a round = one game of battleship
        // match = 1000 rounds vs an opponent
        boardIntArray = new int[10][10];
        Ship ship2 = fleet.getShip(0); //length 2
        Ship ship301 = fleet.getShip(1); //length 3
        Ship ship302 = fleet.getShip(2); //length 3
        Ship ship4 = fleet.getShip(3); //length 4
        Ship ship5 = fleet.getShip(4); //length 5

        findRandomPlace(ship2, board, "s1");
        findRandomPlace(ship301, board, "s2");
        findRandomPlace(ship302, board, "s3");
        findRandomPlace(ship4, board, "s4");
        findRandomPlace(ship5, board, "s5");
    }

    /**
     * Called every time the enemy has fired a shot.
     *
     * The purpose of this method is to allow the AI to react to the enemy's
     * incoming fire and place his/her ships differently next round.
     *
     * @param pos Position of the enemy's shot
     */
    @Override
    public void incoming(Position pos) {
        //Do nothing
    }

    /**
     * Called by the Game application to get the Position of your shot.
     * hitFeedBack(...) is called right after this method.
     *
     * @param enemyShips Fleet the enemy's ships. Compare this to the Fleet
     * supplied in the hitFeedBack(...) method to see if you have sunk any
     * ships.
     *
     * @return Position of you next shot.
     */
    @Override
    public Position getFireCoordinates(Fleet enemyShips) {
        for (Coordinates c : priorities) {
            System.out.println("Priorites:" + c.x + "," + c.y);
        }
        if (!priorities.isEmpty()) {                                  //Will take the top from the priorities que.                    
            shot = priorities.get(0);
            int index=0;
            for (int i =0; i < priorities.size(); i++) {
                if(priorities.get(i).getPre() > shot.getPre()){
                    shot = priorities.get(i);
                    index=i;
                }
            }
            priorities.remove(index);                                                                    // And removes it. 
            int x = shot.x;
            int y = shot.y;
            board[x][y].setPre(0);
            System.out.println("Shoots nabours at: " + x + "," + y);
            return new Position(x, y);
        } else {                                               // Will only excicure if their is nothing in the prioviteque.
            int randomAtGit = rnd.nextInt(listOfGrits.size());
            shot = listOfGrits.get(randomAtGit);
            while (board[shot.x][shot.y].getPre() == 0) {                              // Makes sure that we don't shoot at something that is zero.
                listOfGrits.remove(randomAtGit);
                randomAtGit = rnd.nextInt(listOfGrits.size());
                shot = listOfGrits.get(randomAtGit);
            }
            listOfGrits.remove(randomAtGit);
            int x = shot.x;
            int y = shot.y;
            board[x][y].setPre(0);
            System.out.println("Shoots from grit at: " + x + "," + y);
            return new Position(x, y);
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
    public void hitFeedBack(boolean hit, Fleet enemyShips) {
        printOutHits();
        if (checkShips == 1) {
            shipsBeforeShot = enemyShips.getNumberOfShips();
            checkShips++;
        }
        if (hit) {
            System.out.println("We have a hit");
            placesHit[shot.x][shot.y]=1;
            
            
            if (shipsBeforeShot > enemyShips.getNumberOfShips()) { // Means that we hit and Wrecked 
                shipsBeforeShot = enemyShips.getNumberOfShips();
            getLargestAndSmallest(enemyShips);
            } 
            
            else { // Means that we hit and did not Wreck
                ArrayList<Coordinates> nabours = shot.getNabours(shot);
                for (Coordinates nabour : nabours) {
                    priorities.add(nabour);
                    if(placesHit[shot.x][shot.y-1]==1){
                    }
                }  
                checkNaboursV();
                checkNaboursH();
            }

        }else{
        placesHit[shot.x][shot.y]=-1;
            
        }
//            if(priorities.isEmpty()){
//                for (Coordinates[] array : board) {
//                    for (Coordinates coor : array) {
//                        
//                   if(board[coor.x][coor.y].getPre()!=0)
//                    checkCoordinates(coor); 
//                    }
//                }
                
//            }
    }

    public void getLargestAndSmallest(Fleet enemyShips) {
        for (Ship Ship : enemyShips) {
            if(Ship.size() > Lagerst.size()){
                Ship = Lagerst;
            }
            if(Ship.size() < Smallest.size()){
                Ship =Smallest;
            }
            
        }
    }


    /**
     * Called in the beginning of each match to inform about the number of
     * rounds being played.
     *
     * @param rounds int the number of rounds i a match
     */
    @Override
    public void startMatch(int rounds) {
        //Do nothing
    }

    /**
     * Called at the beginning of each round.
     *
     * @param round int the current round number.
     */
    @Override
    public void startRound(int round) {
        board = new Coordinates[10][10];
        placesHit = new int[10][10];
        priorities = new ArrayList<>();
        priorities.clear();
        setBoard(board);
        setGritShots(board);
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
    public void endRound(int round, int points, int enemyPoints) {
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
    public void endMatch(int won, int lost, int draw) {
        //Do nothing
    }

    public void findRandomPlace(Ship ship, Board board, String sNum) {

        Random gen = new Random();

        boolean horrOrVert = gen.nextBoolean(); //  True = Vertical (Increasing Y),  False Horrisontal(Increasing X)

        switch (sNum) {
            case "s1":
                this.x = rnd.nextInt(3) + 7;
                this.y = rnd.nextInt(5);
                break;
            case "s2":
                this.x = rnd.nextInt(3);
                this.y = rnd.nextInt(5) + 5;
                break;
            case "s3":
                this.x = rnd.nextInt(3);
                this.y = rnd.nextInt(5);
                break;
            case "s4":
                this.x = rnd.nextInt(3) + 7;
                this.y = rnd.nextInt(5) + 5;
                break;
            case "s5":
                this.x = rnd.nextInt(4) + 3;
                this.y = rnd.nextInt(10);
                break;
            default:
                System.out.println("Switch Case is not working!");
                break;
        }

        boolean placeable = checkKooridnat(x, y, horrOrVert, ship.size());

        if (!placeable) {
            findRandomPlace(ship, board, sNum);

        } else {
            if (horrOrVert) { // Place it Vertical (Increase Y)
                for (int i = 0; i < ship.size(); i++) {
                    boardIntArray[x][y + i] = ship.size();
                }
            } else { // Place it Horrisontal (Increase X)
                for (int i = 0; i < ship.size(); i++) {
                    boardIntArray[x + i][y] = ship.size();

                }
            }
            board.placeShip(new Position(x, y), ship, horrOrVert);

        }
    }

    private boolean checkKooridnat(int x, int y, boolean horrOrVert, int lengthOfShip) {

        if (horrOrVert) { // trying to place it vertical (incresing y)
            for (int i = 0; i < lengthOfShip; i++) {
                if (y + i > 9) {
                    return false;
                }
                if (boardIntArray[x][y + i] != 0) {
                    return false;
                }
            }
        } else { // trying to place it hoorisontal (incresing x)
            for (int i = 0; i < lengthOfShip; i++) {
                if (x + i > 9) {
                    return false;
                }
                if (boardIntArray[x + i][y] != 0) {
                    return false;
                }
            }
        }

        return true;
    }

    public void setBoard(Coordinates[][] array) {

        for (int i = 0; i < array.length; i++) {

            for (int j = 0; j < array[i].length; j++) {
                array[i][j] = new Coordinates(i, j, 1);
            }

        }

    }

    public void setGritShots(Coordinates[][] array) {
        listOfGrits = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {

            for (int j = 0; j < array[i].length; j++) {
                if (i % 2 == 0 && j % 2 == 1) {
                    listOfGrits.add(board[i][j]);
                    listOfGrits.add(board[i + 1][j - 1]);
                }
            }
        }        //Print out test
//        for (Coordinates coor : listOfGrits) {
//            System.out.println(coor.x + "," + coor.y);
//        }
//        System.out.println(listOfGrits.size());


    }
    /*
     Should be called after every shot
     */

    public void updateGritMap() {
        heapMap.clear();
        for (Coordinates coor : listOfGrits) {
            if (board[coor.x][coor.y].getPre() != 0) {

                heapMap.add(board[coor.x][coor.y]);
            }
        }

//        Test to Print out the map.
//        for (Coordinates coor : heapMap) {
//            System.out.println(coor.x + "," + coor.y + "  PRE: " + coor.getPre());
//        }
//        Coordinates d = heapMap.poll();
//        System.out.println(d.x + "," + d.y);
//        d = heapMap.poll();
//        System.out.println(d.x + "," + d.y);
    }
    /*
     Just a Testmethod
     */
//    public void editMap(){
//        board[1][4].setPre(0);
//        board[5][2].setPre(0);
//        board[8][5].setPre(98);
//        board[8][7].setPre(95);
//        board[4][5].setPre(92);
//        board[5][4].setPre(94);
//        board[7][0].setPre(100);
//    }

    public void checkCoordinates(Coordinates coor) {
           boolean firstNabourZero=true;
           boolean secondNabourZero=true;
           boolean thirdNabourZero=true;
           boolean fourthNabourZero=true;
           
           if(coor.x!=9){
             if(RP.board[coor.x+1][coor.y].getPre()!=0){
                 firstNabourZero=false;
             }  
           }
           
           if(coor.x!=0){
             if(RP.board[coor.x-1][coor.y].getPre()!=0){
                 secondNabourZero=false;
             }  
           }
           
           if(coor.y!=9){
             if(RP.board[coor.x][coor.y+1].getPre()!=0){
                 thirdNabourZero=false;
             }  
           }
           
           if(coor.y!=0){
             if(RP.board[coor.x][coor.y-1].getPre()!=0){
                 fourthNabourZero=false;
             }  
           }
           
           if(firstNabourZero && secondNabourZero && thirdNabourZero && fourthNabourZero){
//               System.out.println("Removed are: " + coor.x + "," + coor.y ); 
               board[coor.x][coor.y].setPre(0);
           }
        
            
    }

    private void printOutHits() {
        String returnStatement = "";
        
        for (int i = placesHit.length-1; i > -1; i--) {

            for (int j =0; j < placesHit.length; j++) {
                returnStatement = returnStatement +placesHit[j][i] + "\t";
            }
            returnStatement = returnStatement + "\n";
        }
        
        System.out.println(returnStatement);
    }

    private void checkNaboursV() {
         boolean above=true;
         boolean under=true;
         System.out.println("*************************************************");
         if(shot.y!=9 && placesHit[shot.x][shot.y+1] != 1 ){
             above=false;
         }
         
         if(shot.y!=0 && placesHit[shot.x][shot.y-1] != 1 ){
             under=false;
         }
        
        if(above || under){
            
        
        
        for (Coordinates coor : priorities) {
            if(coor.x == shot.x){
              coor.setPre(60);
                System.out.println(coor.x + "," + coor.y + " Is set to 60");
            }
        }
        }
    }
    
    private void checkNaboursH() {
         boolean right=true;
         boolean left=true;
         System.out.println("*************************************************");
         if(shot.x!=9 && placesHit[shot.x+1][shot.y] != 1 ){
             right=false;
         }
         
         if(shot.x!=0 && placesHit[shot.x-1][shot.y] != 1 ){
             left=false;
         }
        
        if(right || left){
            
        
        
        for (Coordinates coor : priorities) {
            if(coor.y == shot.y){
              coor.setPre(60);
                System.out.println(coor.x + "," + coor.y + " Is set to 60");
            }
        }
        }
    }
    
}
