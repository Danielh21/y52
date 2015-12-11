/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y52;

import java.util.ArrayList;
import battleship.interfaces.Board;
import battleship.interfaces.Fleet;
import battleship.interfaces.Position;
import battleship.interfaces.Ship;
import battleship.implementations.*;
import java.util.Iterator;
/**
 *
 * @author Daniel
 */
public class Test {
    
    public static void main(String[] args) {
        RP rP = new RP();
        Coordinates[][] board = new Coordinates[10][10];
        Fleet fleet = FleetIni();
        rP.startMatch(1);
        rP.startRound(1);
//        for (int i = 0; i < 100; i++) {
//            Position n = rP.getFireCoordinates(fleet);
//            System.out.println("X: " + n.x + " Y: " + n.y);
//            
//        }
        rP.setBoard(board);
        rP.getFireCoordinates(fleet);
//        rP.editMap();
//        rP.updateGritMap();
//        String returnStatement = "";

//        System.out.println(board[7][2].x + "" + board[7][2].y);
//        for (int i = board.length-1; i > -1; i--) {
//
//            for (int j =0; j < board.length; j++) {
//                returnStatement = returnStatement + " " + board[j][i].x+","+board[j][i].y;
//            }
//            returnStatement = returnStatement + "\n";
//        }
        
//        System.out.println(returnStatement);
        
        
        rP.checkCoordinates(board[2][2]);
        
        String returnStatement = "";
        
        for (int i = board.length-1; i > -1; i--) {

            for (int j =0; j < board.length; j++) {
                returnStatement = returnStatement + " " + board[j][i].x+","+board[j][i].y;
            }
            returnStatement = returnStatement + "\n";
        }
        
        System.out.println(returnStatement);
}

    
    
    
    
    
    
    
    
    
    public static Fleet FleetIni() {
        Fleet fleet = new Fleet() {
            
            @Override
            public int getNumberOfShips() {
                return 2;
            }

            @Override
            public Ship getShip(int index) {
                return new Ship() {

                    @Override
                    public int size() {
                        return 2;
                    }
                };
            }

            @Override
            public Iterator<Ship> iterator() {
                TestHelp th = new TestHelp();
                return th;
            }
            
        };      return fleet;
    }
                }

