/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y52;

import java.util.Random;
import java.util.ArrayList;
/**
 *
 * @author Daniel
 */
class Coordinates {
    
    public final int x;
    public final int y;
    public int pre;
    public final int upperBound=9;
    public final int lowerBound=0;
    Random gen = new Random();
    public Coordinates [][] board2;
            
    public Coordinates(int x, int y, int pre) {
        this.x = x;
        this.y = y;
        this.pre = pre;
    }

    public Coordinates() {
        x = gen.nextInt(10);
        y = gen.nextInt(10);
        pre=1;
    }

    public void setPre(int pre) {
        this.pre = pre;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPre() {
        return pre;
    }
    
    public  ArrayList<Coordinates> getNabours(Coordinates coor){
         ArrayList<Coordinates>  nabours = new ArrayList<>();
         
         //Coor = x=5, y=0.
         
         
         if(coor.x!=upperBound && RP.board[coor.x+1][coor.y].getPre()>0){
             RP.board[coor.x+1][coor.y].setPre(3);
             nabours.add(RP.board[coor.x+1][coor.y]);
             
         }
         if(coor.x!=lowerBound && RP.board[coor.x-1][coor.y].getPre()>0){
              nabours.add(RP.board[coor.x-1][coor.y]);
         }
         if(coor.y!=upperBound && RP.board[coor.x][coor.y+1].getPre()>0){
             RP.board[coor.x][coor.y+1].setPre(2);
             nabours.add(RP.board[coor.x][coor.y+1]);
             
         }
         if(coor.y!=lowerBound && RP.board[coor.x][coor.y-1].getPre()>0){
             nabours.add(RP.board[coor.x][coor.y-1]);
             
         }
         
         return nabours;
    }
    
}
