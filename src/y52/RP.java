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
public class RP implements BattleshipsPlayer {

    private final Random rnd = new Random();
    private int shipsBeforeShot;
    private ArrayList<Coordinates> priorities;
    ArrayList<Coordinates> listOfGrits;
    private Coordinates shot;
    public Coordinates[][] board;
    private int checkShips;
    public int[][] boardIntArray;
    public int[][] placesHit;
    private int x;
    private int y;
    private int shipPartsLeft;
    private ArrayList<Integer> ships;
    private int enemyShotCounter;
    private ArrayList<Position> ownShipPlacement;
    private ArrayList<Boolean> ownShipDirction;
    private boolean enemeyOver60shots;
    public int[][]posShipsLoc;
    public int ownShots;
    

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
        
        if(enemeyOver60shots){
            board.placeShip(ownShipPlacement.get(0), ship2, ownShipDirction.get(0));
            board.placeShip(ownShipPlacement.get(1), ship301, ownShipDirction.get(1));
            board.placeShip(ownShipPlacement.get(2), ship302, ownShipDirction.get(2));
            board.placeShip(ownShipPlacement.get(3), ship4, ownShipDirction.get(3));
            board.placeShip(ownShipPlacement.get(4), ship5, ownShipDirction.get(4));
        }else{

        findRandomPlace(ship2, board, "s1");
        findRandomPlace(ship301, board, "s2");
        findRandomPlace(ship302, board, "s3");
        findRandomPlace(ship4, board, "s4");
        findRandomPlace(ship5, board, "s5");
    }
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
        enemyShotCounter++; // is gonna be equal to the number of enemyShots
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
        printOutPossibleLocations();
        printOutHits();
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
            while (placesHit[shot.x][shot.y]!=0) {               // Makes sure to not shot twice
                System.out.println("Removing " + shot.x + "," + shot.y);
                priorities.remove(index);
                index = rnd.nextInt(priorities.size());
                shot = priorities.get(index);
            }
            priorities.remove(index);                                                                    // And removes it. 
            int x = shot.x;
            int y = shot.y;
            board[x][y].setPre(0);
            System.out.println("Shoots nabours at: " + x + "," + y);
            return new Position(x, y);
        } else if(ownShots <=20) {                                               // Will only excicure if their is nothing in the prioviteque.
            int randomAtGit = rnd.nextInt(listOfGrits.size());
            shot = listOfGrits.get(randomAtGit);
            while (placesHit[shot.x][shot.y]!=0) {                  // Makes sure that we don't shoot at something that is zero.
                System.out.println("Removing " + shot.x + "," + shot.y);        
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
        else{
            shot = getMostLikely();
            int x = shot.x;
            int y = shot.y;
            board[x][y].setPre(0);
            System.out.println("Shooting from posible Ship Location at " + shot.x + "," + shot.y);
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
    public void hitFeedBack(boolean hit, Fleet enemyShips) {
        posShipsLoc = new int[10][10];
        posibleLocationReset();
        ownShots++;
        if (checkShips == 1) {
            shipsBeforeShot = enemyShips.getNumberOfShips();
            shipPartsLeft=shipWrecked(enemyShips);
            System.out.println("Ship parts left set to: " + shipPartsLeft);
            addToShips(enemyShips);
            checkShips++;
        }
        if (hit) {
            System.out.println("We have a hit");
            placesHit[shot.x][shot.y]=1;
            
            
            if (shipsBeforeShot > enemyShips.getNumberOfShips()) { // Means that we hit and Wrecked 
                shipsBeforeShot = enemyShips.getNumberOfShips();
                System.out.println("We have a Wreck");
                int shipJustWreckedLength = shipWrecked(enemyShips);
                System.out.println("Ship Just Wrecked " + shipJustWreckedLength);
                findShipWrecked(shipJustWreckedLength);
                //removes from the list of ships
                removeFromShips(shipJustWreckedLength);
                boolean anyMore = anyMoreNaboursLeft();
                if(!anyMore){
                    System.out.println("Priorities Cleared!");
                    priorities.clear();
                }else{
                    System.out.println("Findes other nabours");
                    findOtherNabours();   
                }
                calculateShipLocationVertical();
                calculateShipLocationHorrisontal();
                if(enemyShips.getNumberOfShips()>1){
                calculateShipLocationHorrisontalSmallest();
                calculateShipLocationVerticalSmallest();
                }
            } 
            
            else { // Means that we hit and did not Wreck
                ArrayList<Position> nabours = shot.getNabours(shot);
                for (Position nabour : nabours) {
                    if(!(nabour.x >9 || nabour.y > 9 || nabour.x< 0 || nabour.y<0)){
                        if(board[nabour.x][nabour.y].getPre()!=0){
                            priorities.add(board[nabour.x][nabour.y]);
                        }
                    }
                }  
                checkNaboursV();
                checkNaboursH();
            }

        }else{
        placesHit[shot.x][shot.y]=-1;
        posShipsLoc[shot.x][shot.y]=-1;
        if(priorities.isEmpty()){
         calculateShipLocationHorrisontal();
         calculateShipLocationVertical();
         if(enemyShips.getNumberOfShips()>1){
         calculateShipLocationHorrisontalSmallest();
         calculateShipLocationVerticalSmallest();
         }
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
        enemeyOver60shots=false;
        ownShipPlacement = new ArrayList<>();
        ownShipDirction = new ArrayList<>();
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
        checkShips=1;
        shipPartsLeft=0;
        priorities = new ArrayList<>();
        priorities.clear();
        setBoard(board);
        setGritShots(board);
        enemyShotCounter=0;
        posShipsLoc= new int[10][10];
        ships = new ArrayList<>();
        ownShots=0;
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
        
        if(enemyShotCounter >= 60){
            enemeyOver60shots=true;
        }else{
            enemeyOver60shots=false;
            ownShipDirction.clear();
            ownShipPlacement.clear();
        }
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
            ownShipPlacement.add(new Position(x, y));
            ownShipDirction.add(horrOrVert);
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
             if(board[coor.x+1][coor.y].getPre()!=0){
                 firstNabourZero=false;
             }  
           }
           
           if(coor.x!=0){
             if(board[coor.x-1][coor.y].getPre()!=0){
                 secondNabourZero=false;
             }  
           }
           
           if(coor.y!=9){
             if(board[coor.x][coor.y+1].getPre()!=0){
                 thirdNabourZero=false;
             }  
           }
           
           if(coor.y!=0){
             if(board[coor.x][coor.y-1].getPre()!=0){
                 fourthNabourZero=false;
             }  
           }
           
           if(firstNabourZero && secondNabourZero && thirdNabourZero && fourthNabourZero){
               System.out.println("Removed are: " + coor.x + "," + coor.y ); 
               board[coor.x][coor.y].setPre(0);
           }
        
            
    }

    private void printOutHits() {
        String returnStatement = "Places Hit:\n";
        
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
         if(shot.y!=9 && placesHit[shot.x][shot.y+1] != 1 ){
             above=false;
         }
         
         if(shot.y!=0 && placesHit[shot.x][shot.y-1] != 1 ){
             under=false;
         }
         
         if(shot.y ==0){ // We Only need to check upstairs nabour 
             under=false;
         }
         
         if(shot.y == 9){ // We Only need to check downstairs nabour
             above=false;
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
         if(shot.x!=9 && placesHit[shot.x+1][shot.y] != 1 ){
             right=false;
         }
         
         if(shot.x!=0 && placesHit[shot.x-1][shot.y] != 1 ){
             left=false;
         }
        if(shot.x ==0){ // We Only need to check right nabour 
             left=false;
         }
         
         if(shot.x == 9){ // We Only need to check left nabour
            right=false;
             
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

    private int shipWrecked(Fleet enemyShips) {
        /*
        To begin with:
        enemyShips.getShip(0).size =2
        enemyShips.getShip(1).size =3
        enemyShips.getShip(2).size =3
        enemyShips.getShip(3).size =4
        enemyShips.getShip(4).size =5
        After the first shot, lastEnemyFleet is set to enemyShips!
        */    
        
        int count = 0;
        
        for (Ship Ship : enemyShips) {
            count = count + Ship.size();
        }
        
        if(shipPartsLeft -2 ==count  ){
            shipPartsLeft = count;
            return 2;
        }
        if(shipPartsLeft -3 ==count  ){
            shipPartsLeft = count;
            return 3;
        }
        if(shipPartsLeft -4 ==count  ){
            shipPartsLeft = count;
            return 4;
        }
        if(shipPartsLeft -5 ==count  ){
            shipPartsLeft = count;
            return 5;
        }
        
        
        
        return count; // Should only exicute the first time!
    }

    public void findShipWrecked(int shipJustWreckedLength ) {
        /*
        What We know:
        shot = last place we have just hit, and we know that we just hit. 
        shipJustWrecked = Is the length of the ship that we just Wrecked!
        placesHit will have 1's at all in a dircetion with the length of shipJustWrecked 
        */
        int dircetion=0;                      // 1 means right, 2 means left, 3 means up, 4 means down  
        
        if(shot.x + (shipJustWreckedLength-1) <=9){ // should check to the right!
        int count=0;
        for (int i = 0; i < shipJustWreckedLength; i++) {
         
            if(placesHit[shot.x+i][shot.y] ==1){
             count++;
         }  
                
        }
        if(count==shipJustWreckedLength){
            dircetion=1;
        }
        }
        
       if(shot.x - (shipJustWreckedLength-1) >=0){ // should check to the left!
        
        int count=0;
        for (int i = 0; i < shipJustWreckedLength; i++) {
            if(placesHit[shot.x-i][shot.y] ==1){
             count++;
         }  
                
        }
        if(count==shipJustWreckedLength){
            dircetion=2;    
        }
        }
        
       if(shot.y + (shipJustWreckedLength-1) <=9){ // should check to the up!
        
        int count=0;
        for (int i = 0; i < shipJustWreckedLength; i++) {
            if(placesHit[shot.x][shot.y+i] ==1){
             count++;
         }  
                
        }
        if(count==shipJustWreckedLength){
            dircetion=3;
            
        }
        }
       
       if(shot.y - (shipJustWreckedLength-1) >=0){ // should check to the below!
        
        int count=0;
        for (int i = 0; i < shipJustWreckedLength; i++) {
            if(placesHit[shot.x][shot.y-1] ==1){
             count++;
         }  
                
        }
        if(count==shipJustWreckedLength){
            dircetion=4;            
        }
        }
       
      if(dircetion==1){ // placeing -1 to the right
          System.out.println("Wrecked Ship to the right of the last shot");
          for (int i = 0; i < shipJustWreckedLength; i++) {
              placesHit[shot.x+i][shot.y]=-1;
              posShipsLoc[shot.x+i][shot.y]=-1;
          }
          
      } 
      if(dircetion==2){ // placeing -1 to the left
          System.out.println("Wrecked Ship to the left of the last shot");
          for (int i = 0; i < shipJustWreckedLength; i++) {
              placesHit[shot.x-i][shot.y]=-1;
              posShipsLoc[shot.x-i][shot.y]=-1;
          }
      }
      
      if(dircetion==3){ // placeing -1 to the up
          System.out.println("Wrecked Ship is up from the last shot");
          for (int i = 0; i < shipJustWreckedLength; i++) {
              placesHit[shot.x][shot.y+i]=-1;
              posShipsLoc[shot.x][shot.y+i]=-1;
          }
      }
      
      if(dircetion==4){ // placeing -1 to the down
          System.out.println("Wrecked Ship is down from the last shot");
          for (int i = 0; i < shipJustWreckedLength; i++) {
              placesHit[shot.x][shot.y-i]=-1;
              posShipsLoc[shot.x][shot.y-i]=-1;
          }
      }
    }

    private boolean anyMoreNaboursLeft() {
        for (int arrays[] : placesHit) {
            for (int num : arrays) {
                if(num==1){
                    return true;
                }
            }
        }
        return false;
    }
    
    private void removeFromShips(int shipJustWreckedLength) {
        for (int i = 0; i < ships.size(); i++) {
            if(ships.get(i)== shipJustWreckedLength){
                System.out.println("Removed are from Array Ships are: " + ships.get(i) );
                ships.remove(i);
                return;
            }
        }
    }

private void printOutPossibleLocations() {
        String returnStatement = "PossibleShip Locations \n";
        
        for (int i = posShipsLoc.length-1; i > -1; i--) {

            for (int j =0; j < posShipsLoc.length; j++) {
                returnStatement = returnStatement +posShipsLoc[j][i] + "\t";
            }
            returnStatement = returnStatement + "\n";
        }
        
        System.out.println(returnStatement);
    }

    public void calculateShipLocationHorrisontal() {
        int lengthOfLargest = getLagrestShip();

        for (int i = 0; i < posShipsLoc.length; i++) {
            for (int j = 0; j < posShipsLoc[i].length; j++) {
                {
                    int counter = 0;
                    if (posShipsLoc[i][j] != -1) {

                        if (i + lengthOfLargest - 1 <= 9) { // We have a coordinate that is not already hit,
                            // and won't go out of bounce of checking for the largest ship

                            for (int k = 1; k < lengthOfLargest; k++) {

                                if (posShipsLoc[i + k][j] == -1) {
                                    counter++;
                                }
                            }

                            for (int h = 0; h < lengthOfLargest; h++) {
                                if (counter == 0) {
                                    int last = posShipsLoc[i + h][j];
                                    posShipsLoc[i + h][j] = last + 1;
                                }
                            }

                        }

                    }
                }

            }
        }
    }
 
public void calculateShipLocationVertical() {
        int lengthOfLargest = getLagrestShip();
        System.out.println("Now Looking for " + lengthOfLargest);

        for (int i = 0; i < posShipsLoc.length; i++) {
            for (int j = 0; j < posShipsLoc[i].length; j++) {
                {
                    int counter = 0;
                    if (posShipsLoc[i][j] != -1) {

                        if (j + lengthOfLargest - 1 <= 9) { // We have a coordinate that is not already hit,
                            // and won't go out of bounce of checking for the largest ship

                            for (int k = 1; k < lengthOfLargest; k++) {

                                if (posShipsLoc[i][j+k] == -1) {
                                    counter++;
                                }
                            }

                            for (int h = 0; h < lengthOfLargest; h++) {
                                if (counter == 0) {
                                    int last = posShipsLoc[i][j + h ];
                                    posShipsLoc[i][j + h ] = last + 1;
                                }
                            }

                        }

                    }
                }

            }
        }
    }

public void calculateShipLocationHorrisontalSmallest() {
        int lengthOfSmallest = getSmallestShip();

        for (int i = 0; i < posShipsLoc.length; i++) {
            for (int j = 0; j < posShipsLoc[i].length; j++) {
                {
                    int counter = 0;
                    if (posShipsLoc[i][j] != -1) {

                        if (i + lengthOfSmallest - 1 <= 9) { // We have a coordinate that is not already hit,
                            // and won't go out of bounce of checking for the largest ship

                            for (int k = 1; k < lengthOfSmallest; k++) {

                                if (posShipsLoc[i + k][j] == -1) {
                                    counter++;
                                }
                            }

                            for (int h = 0; h < lengthOfSmallest; h++) {
                                if (counter == 0) {
                                    int last = posShipsLoc[i + h][j];
                                    posShipsLoc[i + h][j] = last + 1;
                                }
                            }

                        }

                    }
                }

            }
        }
    }
 
public void calculateShipLocationVerticalSmallest() {
        int lengthOfSmallest = getSmallestShip();
        System.out.println("Now Looking for " + lengthOfSmallest);

        for (int i = 0; i < posShipsLoc.length; i++) {
            for (int j = 0; j < posShipsLoc[i].length; j++) {
                {
                    int counter = 0;
                    if (posShipsLoc[i][j] != -1) {

                        if (j + lengthOfSmallest - 1 <= 9) { // We have a coordinate that is not already hit,
                            // and won't go out of bounce of checking for the largest ship

                            for (int k = 1; k < lengthOfSmallest; k++) {

                                if (posShipsLoc[i][j+k] == -1) {
                                    counter++;
                                }
                            }

                            for (int h = 0; h < lengthOfSmallest; h++) {
                                if (counter == 0) {
                                    int last = posShipsLoc[i][j + h ];
                                    posShipsLoc[i][j + h ] = last + 1;
                                }
                            }

                        }

                    }
                }

            }
        }
    }
 
 public int getLagrestShip(){
     int i =0;
     for (int shiplength : ships) {
         if(shiplength > i){
             i=shiplength;
         }
     }
     return i;
 }
 
 private int getSmallestShip() {
    int i =99;
     for (int shiplength : ships) {
         if(shiplength < i){
             i=shiplength;
         }
     }
     return i;
    }

    private void posibleLocationReset() {
        for (int i = 0; i < posShipsLoc.length; i++) {
            for (int j = 0; j < posShipsLoc[i].length; j++) {
                if(placesHit[i][j] == -1){
                    posShipsLoc[i][j]=-1;
                }
            }
        }
    }

    private Coordinates getMostLikely() {
        int largest = -1;
        ArrayList<Position> mostLikelyPositions = new ArrayList<>();
        for (int i = 0; i < posShipsLoc.length; i++) {
            for (int j = 0; j < posShipsLoc[i].length; j++) {
            if(posShipsLoc[i][j] > largest){ // clears the list, and adds the new number to the list
                largest = posShipsLoc[i][j];
                mostLikelyPositions.clear();
                mostLikelyPositions.add(new Position(i, j));
            }
            else if(posShipsLoc[i][j] == largest){ //adds to the list.
                mostLikelyPositions.add(new Position(i, j));
            }      
            
            
            }
            
            }// Now we will return a random from the list
        System.out.println("Chosing a random from the most likely. Chosing between: " + mostLikelyPositions.size() + " Positions, With "  + largest);
        for (Position pos : mostLikelyPositions) {
            System.out.println(pos.x + "," + pos.y);
        }
        Position randomFromLikelys = mostLikelyPositions.get(rnd.nextInt(mostLikelyPositions.size()));
        Coordinates mostLikely = new Coordinates(randomFromLikelys.x, randomFromLikelys.y, 1);
        return mostLikely;
    }

    private void findOtherNabours() {
        /* What we know:
        We have just wrecked and hit.
        Priovities are not empty, and will have all the nabours from the ship just wrecked,
        And the ones, from the ship we hit but did not wreck.
        The placeshit will have -1 on places we hit and wrecked and 1 ons on where we just hit.
        */
        
        ArrayList<Position> naboursToHits = new ArrayList<>();
        priorities.clear();
        System.out.println("Priovities cleared but added to, later");
        for (int i = 0; i < placesHit.length; i++) {
            for (int j = 0; j < placesHit[i].length; j++) {
                if(placesHit[i][j] == 1){ // we need to add to priovities
                 ArrayList<Position> temp = shot.getNabours(new Coordinates(i, j, 1));
                 
                    for (Position pos : temp) {
                        if(board[pos.x][pos.y].getPre() !=0){
                        naboursToHits.add(pos);
                        }
                    }
                    
                }
            }
            }
        for (Position pos : naboursToHits) {
            priorities.add(new Coordinates(pos.x, pos.y, 1));
            System.out.println("Added To Priovities " + pos.x + "," + pos.y);
        }
        
    }

    private void addToShips(Fleet enemyShips) {
        for (Ship ship : enemyShips) {
            ships.add(ship.size());
            System.out.println("Added to Ships is: " + ship.size());
        }
    }
    
}